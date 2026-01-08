<?php
session_start();

$host = "localhost";
$dbname = "assignment_db";
$user = "root";
$pass = "";

$conn = new mysqli($host, $user, $pass, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Get form data
$username = $_POST['username'];
$fullname = trim($_POST['fullname']);
$email = trim($_POST['email']);
$address = trim($_POST['address']);
$contact_number = trim($_POST['contact_number']);

// Handle optional image upload
$profile_image = null;
if (isset($_FILES['profile_image']) && $_FILES['profile_image']['error'] === UPLOAD_ERR_OK) {
    $tmpName = $_FILES['profile_image']['tmp_name'];
    $name = $_FILES['profile_image']['name'];
    $ext = strtolower(pathinfo($name, PATHINFO_EXTENSION));
    $allowed = ['jpg', 'jpeg', 'png', 'gif'];

    if (in_array($ext, $allowed)) {
        $newName = md5(time() . $name) . '.' . $ext;
        $uploadDir = "uploads/";

        if (!is_dir($uploadDir)) {
            mkdir($uploadDir, 0755, true);
        }

        $dest = $uploadDir . $newName;
        if (move_uploaded_file($tmpName, $dest)) {
            $profile_image = $newName;
        }
    }
}

// Build update query
if ($profile_image) {
    $sql = "UPDATE users SET fullname=?, email=?, address=?, contact_number=?, profile_image=? WHERE username=?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ssssss", $fullname, $email, $address, $contact_number, $profile_image, $username);
} else {
    $sql = "UPDATE users SET fullname=?, email=?, address=?, contact_number=? WHERE username=?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("sssss", $fullname, $email, $address, $contact_number, $username);
}

if ($stmt->execute()) {
    header("Location: profile.php?username=" . urlencode($username));
    exit();
} else {
    echo "Failed to update profile.";
}

$stmt->close();
$conn->close();
?>
