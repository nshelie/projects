<?php
session_start();
require 'db_conn.php';

if (!isset($_SESSION['user_id']) || $_SESSION['role'] !== 'employer') {
    echo "Access denied.";
    exit;
}

$employer_id = $_SESSION['user_id'];

$sql = "SELECT m.*, u.fullname AS student_name, j.title AS job_title
        FROM messages m
        JOIN users u ON m.sender_id = u.id AND u.role = 'student'
        JOIN jobs j ON m.job_id = j.id
        WHERE m.receiver_id = ?
        ORDER BY m.sent_at DESC";

$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $employer_id);
$stmt->execute();
$result = $stmt->get_result();
?>

<!DOCTYPE html>
<html>
<head>
    <title>Inbox</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #eef1f5;
            padding: 20px;
        }
        .container {
            width: 95%;
            max-width: 1000px;
            margin: auto;
            background: white;
            padding: 25px;
            box-shadow: 0 0 10px rgba(0,0,0,0.15);
            border-radius: 10px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }
        th, td {
            padding: 12px;
            border: 1px solid #ccc;
        }
        th {
            background-color: #007BFF;
            color: white;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
        .nav {
            margin-bottom: 20px;
        }
        .nav a {
            margin-right: 15px;
            font-weight: bold;
            text-decoration: none;
            color: #007BFF;
        }
        .action-link {
            background-color: #28a745;
            color: white;
            padding: 6px 12px;
            text-decoration: none;
            border-radius: 5px;
        }
        .action-link:hover {
            background-color: #218838;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="nav">
        <a href="employer_dashboard.php">← Back to Dashboard</a>
        <a href="logout.php" style="float: right;">Logout</a>
    </div>

    <h2>📥 Employer Inbox</h2>

    <?php if ($result && $result->num_rows > 0): ?>
        <table>
            <tr>
                <th>From (Opportunity seeker)</th>
                <th>Job</th>
                <th>Message</th>
                <th>Date</th>
                <th>Action</th>
            </tr>
            <?php while($row = $result->fetch_assoc()): ?>
                <tr>
                    <td><?= htmlspecialchars($row['student_name']) ?></td>
                    <td><?= htmlspecialchars($row['job_title']) ?></td>
                    <td><?= htmlspecialchars(substr($row['message'], 0, 50)) ?>...</td>
                    <td><?= $row['sent_at'] ?></td>
                    <td>
                        <a class="action-link" href="reply.php?to_user_id=<?= $row['sender_id'] ?>&job_id=<?= $row['job_id'] ?>">Reply</a>
                    </td>
                </tr>
            <?php endwhile; ?>
        </table>
    <?php else: ?>
        <p>No messages yet.</p>
    <?php endif; ?>
</div>

</body>
</html>
