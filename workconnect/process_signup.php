<?php
session_start();
$host = "localhost";
$dbname = "final_project_db";
$user = "root";
$password = "";

$conn = new mysqli($host, $user, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$errors = [];
$data = [];

// Collect and sanitize form data
$fields = ['fullname', 'username', 'email', 'password', 'confirm', 'phone', 'dob', 'gender', 'location', 'role', 'company_name'];
foreach ($fields as $f) {
    $data[$f] = isset($_POST[$f]) ? trim($_POST[$f]) : '';
}

extract($data);

// Validate required fields
if (empty($fullname)) $errors['fullname'] = "Full name is required.";
if (empty($username)) $errors['username'] = "Username is required.";
if (empty($email)) $errors['email'] = "Email is required.";
if (empty($phone)) $errors['phone'] = "Phone number is required.";
if (empty($dob)) $errors['dob'] = "Date of birth is required.";
if (empty($gender)) $errors['gender'] = "Gender is required.";
if (empty($location)) $errors['location'] = "Location is required.";
if (empty($role)) $errors['role'] = "Role is required.";

if (empty($password)) {
    $errors['password'] = "Password is required.";
} elseif (strlen($password) < 6) {
    $errors['password'] = "Password must be at least 6 characters.";
}

if ($password !== $confirm) {
    $errors['confirm'] = "Passwords do not match.";
}

if ($role === 'employer' && empty($company_name)) {
    $errors['company_name'] = "Company name is required for employers.";
}

// Check for duplicate username/email
$check = $conn->prepare("SELECT id FROM users WHERE email = ? OR username = ?");
$check->bind_param("ss", $email, $username);
$check->execute();
$check->store_result();
if ($check->num_rows > 0) {
    $errors['email'] = "Email or username already exists.";
}
$check->close();

if (!empty($errors)) {
    $_SESSION['signup_errors'] = $errors;
    $_SESSION['signup_data'] = $data;
    header("Location: signup.php");
    exit;
}

// Hash password
$hashed_password = password_hash($password, PASSWORD_DEFAULT);

// Insert into DB
$stmt = $conn->prepare("INSERT INTO users (fullname, username, email, password, phone, dob, gender, location, role, company_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
$stmt->bind_param("ssssssssss", $fullname, $username, $email, $hashed_password, $phone, $dob, $gender, $location, $role, $company_name);
$stmt->execute();
$stmt->close();
$conn->close();

// Set success message and redirect
$_SESSION['signup_success'] = "✅ Registration successful! You can now log in.";
header("Location: login.php");
exit;
