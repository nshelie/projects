<?php
session_start();
require 'db_conn.php'; // Adjust path if needed

if (!isset($_SESSION['user_id'])) {
    echo "Access denied.";
    exit;
}

if (!isset($_GET['to_user_id']) || !isset($_GET['job_id'])) {
    echo "Invalid request.";
    exit;
}

$to_user_id = $_GET['to_user_id'];
$job_id = $_GET['job_id'];
$from_user_id = $_SESSION['user_id']; // employer ID

$statusMessage = "";

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $message = trim($_POST['message']);

    if (!empty($message)) {
        $stmt = $conn->prepare("INSERT INTO messages (sender_id, receiver_id, job_id, message, sent_at) VALUES (?, ?, ?, ?, NOW())");
        $stmt->bind_param("iiis", $from_user_id, $to_user_id, $job_id, $message);
        if ($stmt->execute()) {
            $statusMessage = "<p class='success'>✅ Message sent successfully.</p>";
        } else {
            $statusMessage = "<p class='error'>❌ Failed to send message.</p>";
        }
    } else {
        $statusMessage = "<p class='warning'>⚠️ Message cannot be empty.</p>";
    }
}
?>

<!DOCTYPE html>
<html>
<head>
    <title>Reply to Student</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background-color: #f7f7f7;
        }
        h2 {
            color: #333;
        }
        form {
            background-color: #fff;
            padding: 20px;
            border: 1px solid #ddd;
            max-width: 600px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        textarea {
            width: 100%;
            padding: 12px;
            font-size: 16px;
            border-radius: 6px;
            border: 1px solid #ccc;
            resize: vertical;
        }
        button {
            padding: 10px 20px;
            background-color: #007bff;
            border: none;
            color: white;
            font-size: 16px;
            border-radius: 5px;
            cursor: pointer;
        }
        button:hover {
            background-color: #0056b3;
        }
        .status p {
            font-weight: bold;
        }
        .success { color: green; }
        .error { color: red; }
        .warning { color: #cc8800; }
        a {
            display: inline-block;
            margin-top: 20px;
            color: #007bff;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

    <h2>Reply to Student (User ID: <?= htmlspecialchars($to_user_id) ?>)</h2>

    <div class="status"><?= $statusMessage ?></div>

    <form method="POST">
        <label for="message"><strong>Your Message:</strong></label><br>
        <textarea name="message" rows="6" placeholder="Type your message here..." required></textarea><br><br>
        <button type="submit">Send Reply</button>
    </form>

    <a href="inbox.php">← Back to Inbox</a>

</body>
</html>
