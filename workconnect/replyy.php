<?php
session_start();
require 'db_conn.php';

if (!isset($_SESSION['user_id']) || $_SESSION['role'] !== 'student') {
    die("Unauthorized.");
}

if (!isset($_GET['message_id'])) {
    die("Invalid request.");
}

$message_id = $_GET['message_id'];
$student_id = $_SESSION['user_id'];

// Get original message to find the employer
$stmt = $conn->prepare("SELECT sender_id, job_id FROM messages WHERE id = ? AND receiver_id = ?");
$stmt->bind_param("ii", $message_id, $student_id);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 0) {
    die("Message not found.");
}

$message = $result->fetch_assoc();
$employer_id = $message['sender_id'];
$job_id = $message['job_id'];

$feedback = "";

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $reply = trim($_POST['message']);
    if (!empty($reply)) {
        $insert = $conn->prepare("INSERT INTO messages (sender_id, receiver_id, job_id, message) VALUES (?, ?, ?, ?)");
        $insert->bind_param("iiis", $student_id, $employer_id, $job_id, $reply);
        if ($insert->execute()) {
            $feedback = "<p style='color: green;'>✅ Reply sent successfully.</p>";
        } else {
            $feedback = "<p style='color: red;'>❌ Failed to send reply.</p>";
        }
    } else {
        $feedback = "<p style='color: red;'>⚠️ Message cannot be empty.</p>";
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Reply to Message</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f7f7f7;
            margin: 40px;
            color: #333;
        }

        h2 {
            color: #005f73;
        }

        form {
            background-color: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
            width: 400px;
        }

        textarea {
            width: 100%;
            padding: 10px;
            resize: vertical;
            font-size: 14px;
            border-radius: 5px;
            border: 1px solid #ccc;
        }

        button {
            margin-top: 15px;
            padding: 10px 20px;
            background-color: #007BFF;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        button:hover {
            background-color: #0056b3;
        }

        .nav-link {
            margin-bottom: 20px;
            display: inline-block;
            text-decoration: none;
            padding: 8px 15px;
            background-color: #28a745;
            color: white;
            border-radius: 5px;
        }

        .nav-link:hover {
            background-color: #218838;
        }
  

    </style>
</head>
<body>

    <a href="inboxx.php" class="nav-link">📨 Back to Inbox</a>
    <h2>Reply to Employer</h2>

    <?= $feedback ?>

    <form method="POST">
        <label for="message"><strong>Your Reply:</strong></label><br>
        <textarea name="message" rows="5" placeholder="Write your reply..." required></textarea><br>
        <button type="submit">Send Reply</button>
    </form>

</body>
</html>
