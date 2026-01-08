<?php
session_start();
require 'db_conn.php';

if (!isset($_SESSION['user_id']) || $_SESSION['role'] !== 'student') {
    header("Location: login.php");
    exit();
}

$sender_id = $_SESSION['user_id'];
$receiver_id = isset($_GET['to_user_id']) ? (int)$_GET['to_user_id'] : 0;
$job_id = isset($_GET['job_id']) ? (int)$_GET['job_id'] : 0;

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $message = trim($_POST['message']);

    if (!empty($message)) {
        $stmt = $conn->prepare("INSERT INTO messages (sender_id, receiver_id, job_id, message) VALUES (?, ?, ?, ?)");
        $stmt->bind_param("iiis", $sender_id, $receiver_id, $job_id, $message);
        $stmt->execute();
        $success = "✅ Message sent successfully!";
    } else {
        $error = "❌ Message cannot be empty.";
    }
}
?>

<!DOCTYPE html>
<html>
<head>
    <title>Send Message</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f0f4f8;
            padding: 20px;
        }
        .container {
            width: 600px;
            margin: auto;
            background: white;
            padding: 25px;
            box-shadow: 0 0 10px rgba(0,0,0,0.15);
            border-radius: 10px;
        }
        textarea {
            width: 100%;
            padding: 10px;
            font-size: 16px;
        }
        button {
            padding: 10px 25px;
            background-color: #007BFF;
            color: white;
            border: none;
            margin-top: 10px;
            cursor: pointer;
            border-radius: 5px;
        }
        a {
            display: inline-block;
            margin-top: 20px;
            color: #007BFF;
            text-decoration: none;
        }
        .nav {
            margin-bottom: 20px;
        }
        .nav a {
            margin-right: 15px;
            font-weight: bold;
        }
        .message {
            margin-top: 10px;
            font-weight: bold;
        }
        .success { color: green; }
        .error { color: red; }
    </style>
</head>
<body>

<div class="container">
    <div class="nav">
        <a href="student_dashboard.php">← Back to Dashboard</a>
        <a href="logout.php" style="float: right;">Logout</a>
    </div>

    <h2>📩 Send Message to Employer</h2>

    <?php if (isset($success)) echo "<p class='message success'>$success</p>"; ?>
    <?php if (isset($error)) echo "<p class='message error'>$error</p>"; ?>

    <form method="post">
        <textarea name="message" rows="6" placeholder="Type your message..."></textarea><br>
        <button type="submit">Send Message</button>
    </form>
</div>

</body>
</html>
