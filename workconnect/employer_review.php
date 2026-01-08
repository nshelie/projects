<?php
session_start();
if (!isset($_SESSION['role']) || $_SESSION['role'] !== 'employer') {
    header("Location: login.php");
    exit();
}

$conn = new mysqli("localhost", "root", "", "final_project_db");

$task_id = $_GET['id'] ?? 0;

// Get task info
$task = $conn->query("SELECT * FROM tasks WHERE id = $task_id")->fetch_assoc();
if (!$task) {
    echo "Task not found.";
    exit();
}

// Get submissions
$submissions = $conn->query("
    SELECT ts.*, s.fullname, s.email 
    FROM task_submissions ts
    JOIN users s ON ts.student_id = s.id
    WHERE ts.task_id = $task_id
");
?>

<!DOCTYPE html>
<html>
<head>
    <title>Review Submissions - <?= htmlspecialchars($task['title']) ?></title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f6f8;
        }
        .navbar {
            background-color: #2c3e50;
            padding: 15px;
            color: white;
            text-align: center;
        }
        .container {
            max-width: 900px;
            margin: 30px auto;
            padding: 20px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        h2 {
            text-align: center;
            margin-bottom: 25px;
            color: #34495e;
        }
        .submission-box {
            border: 1px solid #ddd;
            padding: 20px;
            margin-bottom: 20px;
            background-color: #fdfdfd;
            border-radius: 5px;
        }
        .submission-box h3 {
            margin-top: 0;
            color: #2c3e50;
        }
        .submission-box p {
            margin: 5px 0;
        }
        textarea {
            width: 100%;
            padding: 10px;
            margin-top: 10px;
            border-radius: 4px;
            border: 1px solid #ccc;
        }
        button {
            margin-top: 10px;
            padding: 10px 20px;
            background-color: #3498db;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        button:hover {
            background-color: #2980b9;
        }
        label {
            display: block;
            margin-top: 10px;
        }
        .no-submissions {
            text-align: center;
            color: #888;
        }
        a.back-link {
            display: inline-block;
            margin-bottom: 20px;
            color: #2980b9;
            text-decoration: none;
        }
        a.back-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="navbar">
    <h1>Employer Dashboard - Task Submissions</h1>
</div>

<div class="container">
    <a class="back-link" href="employer_dashboard.php">&larr; Back to Dashboard</a>
    <h2>Review Submissions for: <?= htmlspecialchars($task['title']) ?></h2>

    <?php if ($submissions->num_rows > 0): ?>
        <?php $count = 1; ?>
        <?php while ($row = $submissions->fetch_assoc()): ?>
            <div class="submission-box">
                <h3>#<?= $count ?> - <?= htmlspecialchars($row['fullname']) ?> (<?= htmlspecialchars($row['email']) ?>)</h3>
                <p><strong>Submission:</strong></p>
                <p><?= nl2br(htmlspecialchars($row['submission_text'])) ?></p>

                <?php if ($row['submission_file']): ?>
                    <p><strong>File:</strong> <a href="uploads/<?= htmlspecialchars($row['submission_file']) ?>" download style="background-color: #004466;
    color: white;
    border: none;
    padding: 5px 5px;
    border-radius: 8px;
    text-decoration:none;" >Download File</a></p>
                <?php endif; ?>

                <form method="POST" action="handle_feedback.php">
                    <input type="hidden" name="submission_id" value="<?= $row['id'] ?>">
                    <textarea name="feedback" rows="3" placeholder="Write your feedback here..." required></textarea>
                    <label><input type="checkbox" name="mark_completed"> Mark as Completed</label>
                    <button type="submit">Submit Feedback</button>
                </form>
            </div>
            <?php $count++; ?>
        <?php endwhile; ?>
    <?php else: ?>
        <p class="no-submissions">No submissions yet for this task.</p>
    <?php endif; ?>
</div>

</body>
</html>

<?php $conn->close(); ?>
