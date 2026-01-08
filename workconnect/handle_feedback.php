<?php
session_start();

if (!isset($_SESSION['role']) || $_SESSION['role'] !== 'employer') {
    die("Unauthorized access");
}

$conn = new mysqli("localhost", "root", "", "final_project_db");

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$submission_id = $_POST['submission_id'] ?? 0;
$feedback = $conn->real_escape_string(trim($_POST['feedback']));
$status = isset($_POST['mark_completed']) ? 'Completed' : 'Pending';

if (!$submission_id || $feedback === "") {
    die("Submission ID or Feedback is missing");
}

$stmt = $conn->prepare("UPDATE task_submissions SET feedback = ?, status = ? WHERE id = ?");
if (!$stmt) {
    die("Prepare failed: " . $conn->error);
}

$stmt->bind_param("ssi", $feedback, $status, $submission_id);
if ($stmt->execute()) {
    echo "Feedback submitted successfully. <a href='javascript:history.back()'>Go Back</a>";
} else {
    echo "Error: " . $stmt->error;
}

$stmt->close();
$conn->close();
?>
