<?php
session_start();

// DB config
$host = "localhost";
$dbname = "final_project_db";
$user = "root";
$password = "";

// Connect to DB
$conn = new mysqli($host, $user, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Initialize
$errors = [];
$data = [];

// Get and sanitize input
$email_or_username = trim($_POST['email'] ?? '');
$pass = $_POST['password'] ?? '';
$data['email'] = $email_or_username; // Used to re-fill email field on error

// Validate fields
if (empty($email_or_username)) {
    $errors['email'] = "Email or username is required.";
}
if (empty($pass)) {
    $errors['password'] = "Password is required.";
}

// Check database if no errors yet
if (empty($errors)) {
    $sql = "SELECT id, username, email, password, role, fullname, location FROM users WHERE username = ? OR email = ? LIMIT 1";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ss", $email_or_username, $email_or_username);
    $stmt->execute();
    $result = $stmt->get_result();

    // Check user found
    if ($result->num_rows === 1) {
        $user = $result->fetch_assoc();

        if (password_verify($pass, $user['password'])) {
            // Set session
            $_SESSION['user_id'] = $user['id'];
            $_SESSION['username'] = $user['username'];
            $_SESSION['fullname'] = $user['fullname'];
            $_SESSION['email'] = $user['email'];
            $_SESSION['location'] = $user['location'];
            $_SESSION['role'] = $user['role'];

            // Redirect by role
            if ($user['role'] === 'student') {
                header("Location: student_dashboard.php");
            } elseif ($user['role'] === 'employer') {
                header("Location: employer_dashboard.php");
            } else {
                header("Location: dashboard.php");
            }
            exit;
        } else {
            $errors['password'] = "Incorrect password";
        }
    } else {
        $errors['email'] = "Account not found with that email";
    }

    $stmt->close();
}

$conn->close();

// Redirect back with errors if any
if (!empty($errors)) {
    $_SESSION['login_errors'] = $errors;
    $_SESSION['login_data'] = $data;
    header("Location: login.php");
    exit;
}
