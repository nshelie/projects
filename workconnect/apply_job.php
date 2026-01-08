<?php
session_start();

if (!isset($_SESSION['user_id']) || $_SESSION['role'] !== 'student') {
    header("Location: login.php");
    exit();
}
// Show any message from apply_job.php
if (isset($_SESSION['message'])) {
    echo '<div class="message success" style="padding: 10px; margin-bottom: 15px; background-color: #d4edda; color: #155724; border-radius: 5px;">' 
         . htmlspecialchars($_SESSION['message']) . '</div>';
    unset($_SESSION['message']);
}

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['apply'])) {
    $job_id = intval($_POST['job_id']);
    $user_id = $_SESSION['user_id'];

    // DB connection
    $conn = new mysqli("localhost", "root", "", "final_project_db");
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    // Check if already applied
    $stmt = $conn->prepare("SELECT id FROM job_applications WHERE job_id = ? AND user_id = ?");
    $stmt->bind_param("ii", $job_id, $user_id);
    $stmt->execute();
    $stmt->store_result();

    if ($stmt->num_rows > 0) {
        // Already applied
        $stmt->close();
        $conn->close();
        $_SESSION['message'] = "You have already applied for this job.";
        header("Location: student_dashboard.php");
        exit();
    }
    $stmt->close();

    // Insert new application
    $stmt = $conn->prepare("INSERT INTO job_applications (job_id, user_id) VALUES (?, ?)");
    $stmt->bind_param("ii", $job_id, $user_id);

    if ($stmt->execute()) {
        $_SESSION['message'] = "Application submitted successfully.";
    } else {
        $_SESSION['message'] = "Failed to submit application. Please try again.";
    }

    $stmt->close();
    $conn->close();

    header("Location: student_dashboard.php");
    exit();
} else {
    header("Location: student_dashboard.php");
    exit();
}
?>
