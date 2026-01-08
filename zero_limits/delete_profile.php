<?php
if (!isset($_GET['username'])) {
    header("Location: index.html");
    exit();
}

$username = $_GET['username'];

$conn = new mysqli("localhost", "root", "", "assignment_db");
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Delete user
$sql = "DELETE FROM users WHERE username = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $username);

if ($stmt->execute()) {
    // ✅ Successful deletion — redirect to goodbye page
    header("Location: goodbye.php");
    exit();
} else {
    echo "Failed to delete account. Please try again.";
}

$stmt->close();
$conn->close();
?>
