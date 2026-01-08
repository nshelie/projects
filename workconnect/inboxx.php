<?php
session_start();
require 'db_conn.php'; // Update path if needed

if (!isset($_SESSION['user_id']) || $_SESSION['role'] !== 'student') {
    header("Location: ../login.php");
    exit();
}

$student_id = $_SESSION['user_id'];

// Fetch messages sent TO this student
$sql = "
SELECT m.*, u.fullname AS sender_name
FROM messages m
JOIN users u ON m.sender_id = u.id
WHERE m.receiver_id = ?
ORDER BY m.sent_at DESC
";

$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $student_id);
$stmt->execute();
$result = $stmt->get_result();
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>📥 Student Inbox</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f7f7f7;
            margin: 40px;
            color: #333;
        }

        h2 {
            color: #005f73;
            margin-bottom: 25px;
        }

        ul {
            list-style: none;
            padding: 0;
        }

        li {
            background: #ffffff;
            border: 1px solid #ccc;
            padding: 15px;
            margin-bottom: 15px;
            border-radius: 8px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.05);
        }

        li strong {
            display: inline-block;
            width: 80px;
            color: #333;
        }

        .message {
            margin-top: 10px;
        }

        a.reply-btn {
            display: inline-block;
            margin-top: 10px;
            padding: 6px 12px;
            background-color: #007BFF;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }

        a.reply-btn:hover {
            background-color: #0056b3;
        }

        .no-messages {
            font-style: italic;
            color: #777;
        }
    </style>
</head>
<body>
    
    <nav style="margin-bottom: 30px;">
        <a href="student_dashboard.php" style="text-decoration: none; padding: 10px 15px; background-color: #28a745; color: white; border-radius: 5px;">🏠 Back to Dashboard</a>
    </nav>

    <h2>📥 Your Inbox</h2>

    <?php if ($result->num_rows > 0): ?>
        <ul>
        <?php while ($msg = $result->fetch_assoc()): ?>
            <li>
                <div><strong>From:</strong> <?= htmlspecialchars($msg['sender_name']) ?></div>
                <div class="message"><strong>Message:</strong> <?= nl2br(htmlspecialchars($msg['message'])) ?></div>
                <div><strong>Sent:</strong> <?= htmlspecialchars($msg['sent_at']) ?></div>
                <a href="replyy.php?message_id=<?= $msg['id'] ?>" class="reply-btn">Reply</a>
            </li>
        <?php endwhile; ?>
        </ul>
    <?php else: ?>
        <p class="no-messages">No messages yet.</p>
    <?php endif; ?>
</body>
</html>
