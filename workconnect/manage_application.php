<?php
session_start();
if (!isset($_SESSION['user_id']) || $_SESSION['role'] !== 'employer') {
    header("Location: login.php");
    exit();
}

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['application_id'], $_POST['action'])) {
    $application_id = intval($_POST['application_id']);
    $action = $_POST['action'];
    $allowed_actions = ['accept', 'reject'];

    if (!in_array($action, $allowed_actions)) {
        $_SESSION['manage_msg'] = "Invalid action.";
        header("Location: employer_dashboard.php");
        exit();
    }

    $conn = new mysqli("localhost", "root", "", "final_project_db");
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    // Verify the application belongs to the employer's job
    $stmt = $conn->prepare("
        SELECT ja.id FROM job_applications ja
        JOIN jobs j ON ja.job_id = j.id
        WHERE ja.id = ? AND j.employer_id = ?
    ");
    $stmt->bind_param("ii", $application_id, $_SESSION['user_id']);
    $stmt->execute();
    $stmt->store_result();

    if ($stmt->num_rows === 0) {
        $_SESSION['manage_msg'] = "Application not found or you don't have permission.";
        $stmt->close();
        $conn->close();
        header("Location: employer_dashboard.php");
        exit();
    }
    $stmt->close();

    // Update status
    $new_status = $action === 'accept' ? 'accepted' : 'rejected';
    $update_stmt = $conn->prepare("UPDATE job_applications SET status = ? WHERE id = ?");
    $update_stmt->bind_param("si", $new_status, $application_id);

    if ($update_stmt->execute()) {
    $_SESSION['manage_msg'] = "Application has been $new_status.";

    // Insert notification for student
    $get_student_stmt = $conn->prepare("SELECT user_id FROM job_applications WHERE id = ?");
    $get_student_stmt->bind_param("i", $application_id);
    $get_student_stmt->execute();
    $get_student_stmt->bind_result($student_user_id);
    $get_student_stmt->fetch();
    $get_student_stmt->close();

    $notif_msg = "Your application for job ID $application_id has been $new_status.";
    $notif_stmt = $conn->prepare("INSERT INTO notifications (user_id, message) VALUES (?, ?)");
    $notif_stmt->bind_param("is", $student_user_id, $notif_msg);
    $notif_stmt->execute();
    $notif_stmt->close();
} else {
    $_SESSION['manage_msg'] = "Failed to update application status.";
}


    $update_stmt->close();
    $conn->close();

    header("Location: employer_dashboard.php");
    exit();
} else {
    header("Location: employer_dashboard.php");
    exit();
}
