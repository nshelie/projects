<?php
session_start();
if (!isset($_SESSION['role']) || $_SESSION['role'] !== 'student') {
    header("Location: login.php");
    exit();
}

$conn = new mysqli("localhost", "root", "", "final_project_db");

$task_id = $_GET['id'] ?? 0;
$student_id = $_SESSION['user_id'];
$errors = [];
$success = '';

// Fetch task details
$task_result = $conn->prepare("SELECT * FROM tasks WHERE id = ?");
$task_result->bind_param("i", $task_id);
$task_result->execute();
$task = $task_result->get_result()->fetch_assoc();
$task_result->close();

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $submission_text = $conn->real_escape_string(trim($_POST['submission_text']));
    $file_name = '';

    if (!empty($_FILES['submission_file']['name'])) {
        $upload_dir = "uploads/";
        $original_name = basename($_FILES["submission_file"]["name"]);
        $ext = pathinfo($original_name, PATHINFO_EXTENSION);
        $file_name = uniqid('submission_', true) . '.' . $ext;
        $target_file = $upload_dir . $file_name;

        if (!move_uploaded_file($_FILES["submission_file"]["tmp_name"], $target_file)) {
            $errors[] = "Failed to upload the file.";
            $file_name = '';
        }
    }

    // Insert submission
    $stmt = $conn->prepare("INSERT INTO task_submissions (task_id, student_id, submission_text, submission_file) VALUES (?, ?, ?, ?)");
    $stmt->bind_param("iiss", $task_id, $student_id, $submission_text, $file_name);

    if ($stmt->execute()) {
        $success = "✅ Your task has been submitted!";
    } else {
        $errors[] = "Submission failed: " . $conn->error;
    }
    $stmt->close();
}

$conn->close();
?>

<!DOCTYPE html>
<html>
<head>
    <title>Gig Details & Submission</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f4f6f9;
            margin: 0;
            padding: 0;
        }

        nav {
            background-color: #2c3e50;
            color: white;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        nav h2 {
            margin: 0;
            font-size: 20px;
        }

        nav a {
            color: white;
            text-decoration: none;
            margin-left: 20px;
        }

        nav a:hover {
            text-decoration: underline;
        }

        .container {
            padding: 30px;
            max-width: 800px;
            margin: auto;
        }

        h2 {
            color: #2c3e50;
        }

        p {
            color: #34495e;
        }

        form {
            background-color: #ffffff;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            margin-top: 20px;
        }

        label {
            font-weight: bold;
            margin-top: 15px;
            display: block;
        }

        textarea {
            width: 100%;
            padding: 10px;
            border-radius: 5px;
            border: 1px solid #ccc;
            resize: vertical;
        }

        input[type="file"] {
            margin-top: 10px;
        }

        button {
            background-color: #3498db;
            color: white;
            padding: 10px 20px;
            border-radius: 5px;
            border: none;
            margin-top: 20px;
            cursor: pointer;
        }

        button:hover {
            background-color: #2980b9;
        }

        .success {
            background-color: #d4edda;
            color: #155724;
            padding: 10px 15px;
            border-radius: 5px;
            margin-top: 15px;
        }

        .error {
            background-color: #f8d7da;
            color: #721c24;
            padding: 10px 15px;
            border-radius: 5px;
            margin-top: 15px;
        }

        .back-link {
            display: inline-block;
            margin-top: 20px;
            /* color: #3498db; */
            text-decoration: none;
            background-color: #3498db;
            color: white;
            padding: 10px 20px;
            border-radius: 5px;
            border: none;
        }

        .back-link:hover {
            text-decoration: none;
            background-color: #03568dff;
        }
    </style>
</head>
<body>
    <nav>
        <h2>Student Portal</h2>
        <div>
            <a href="student_dashboard.php">Dashboard</a>
            <a href="logout.php">Logout</a>
        </div>
    </nav>

    <div class="container">
        <h2>Gig: <?= htmlspecialchars($task['title']) ?></h2>
        <p><strong>Description:</strong> <?= nl2br(htmlspecialchars($task['description'])) ?></p>

        <?php if ($success): ?>
            <div class="success"><?= $success ?></div>
        <?php endif; ?>

        <?php if ($errors): ?>
            <div class="error">
                <ul>
                    <?php foreach ($errors as $e): ?>
                        <li><?= $e ?></li>
                    <?php endforeach; ?>
                </ul>
            </div>
        <?php endif; ?>

        <form method="POST" enctype="multipart/form-data">
            <label>Describe your solution or work (optional):</label>
            <textarea name="submission_text" rows="5"></textarea>

            <label>Upload your deliverables (ZIP, DOCX, PDF, etc):</label>
            <input type="file" name="submission_file" accept=".zip,.pdf,.doc,.docx,.txt">

            <button type="submit">Submit Your Work</button>
        </form>

        <a href="student_dashboard.php" class="back-link">← Back to Dashboard</a>
    </div>
</body>
</html>
