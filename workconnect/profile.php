<?php
session_start();

// Check if user is logged in
if (!isset($_SESSION['user_id'])) {
    header("Location: login.php");
    exit();
}

$host = "localhost";
$dbname = "final_project_db";
$user = "root";
$password = "";

// Connect
$conn = new mysqli($host, $user, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$user_id = $_SESSION['user_id'];
$message = "";

// Handle form submission to update profile
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $fullname = trim($_POST['fullname']);
    $email = trim($_POST['email']);
    $phone = trim($_POST['phone']);
    $location = trim($_POST['location']);
    $gender = $_POST['gender'];
    $dob = $_POST['dob'];

    $profile_image_name = null;

    if (isset($_FILES['profile_image']) && $_FILES['profile_image']['error'] === UPLOAD_ERR_OK) {
        $fileTmpPath = $_FILES['profile_image']['tmp_name'];
        $fileName = $_FILES['profile_image']['name'];
        $fileNameCmps = explode(".", $fileName);
        $fileExtension = strtolower(end($fileNameCmps));

        $allowedfileExtensions = array('jpg', 'jpeg', 'png', 'gif');

        if (in_array($fileExtension, $allowedfileExtensions)) {
            $uploadFileDir = './uploads/';
            if (!is_dir($uploadFileDir)) {
                mkdir($uploadFileDir, 0755, true);
            }

            $newFileName = $user_id . '_' . time() . '.' . $fileExtension;
            $dest_path = $uploadFileDir . $newFileName;

            if (move_uploaded_file($fileTmpPath, $dest_path)) {
                $profile_image_name = $newFileName;
            } else {
                $message = "Error uploading image.";
            }
        } else {
            $message = "Invalid file type. Allowed: " . implode(", ", $allowedfileExtensions);
        }
    }

    if (empty($fullname) || empty($email)) {
        $message = "Full name and email are required.";
    } elseif (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $message = "Invalid email format.";
    } else {
        // Update user profile
        if ($profile_image_name) {
            $stmt = $conn->prepare("UPDATE users SET fullname=?, email=?, phone=?, location=?, gender=?, dob=?, profile_image=? WHERE id=?");
            $stmt->bind_param("sssssssi", $fullname, $email, $phone, $location, $gender, $dob, $profile_image_name, $user_id);
        } else {
            $stmt = $conn->prepare("UPDATE users SET fullname=?, email=?, phone=?, location=?, gender=?, dob=? WHERE id=?");
            $stmt->bind_param("ssssssi", $fullname, $email, $phone, $location, $gender, $dob, $user_id);
        }

        if ($stmt->execute()) {
            $message = "Profile updated successfully!";
        } else {
            $message = "Update failed: " . $stmt->error;
        }

        $stmt->close();
    }
}

// Fetch user info after update or initial load
$stmt = $conn->prepare("SELECT fullname, email, phone, location, gender, dob, role, profile_image FROM users WHERE id=?");
$stmt->bind_param("i", $user_id);
$stmt->execute();
$result = $stmt->get_result();
$user = $result->fetch_assoc();
$stmt->close();
$conn->close();
?>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Edit Profile - Student Career Path Finder</title>
<style>

body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background: #e9f0f4;
  margin: 0;
  padding: 20px;
}
/* addition */

.container {
  max-width: 600px;
  margin: auto;
  background: #ffffff;
  padding: 35px;
  border-radius: 12px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  color: #003366;
  margin-bottom: 25px;
}

form label {
  display: block;
  margin-top: 20px;
  font-weight: bold;
  color: #333;

}

form input[type="text"],
form input[type="email"],
form input[type="date"],
form input[type="file"],
form select {
  width: 100%;
  padding: 12px;
  margin-top: 6px;
  border-radius: 8px;
  border: 1px solid #ccc;
  font-size: 15px;
  transition: border 0.2s;
}

form input:focus,
form select:focus {
  border-color: #007bff;
  outline: none;
}

img.profile-preview {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: 50%;
  display: block;
  margin: 0 auto 15px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.btn-submit {
  margin-top: 30px;
  background-color: #007bff;
  color: white;
  padding: 14px;
  border: none;
  width: 100%;
  font-size: 17px;
  font-weight: bold;
  cursor: pointer;
  border-radius: 8px;
  transition: background-color 0.3s;
}

.btn-submit:hover {
  background-color: #0056b3;
}

.message {
  margin-top: 20px;
  padding: 14px;
  border-radius: 8px;
  font-weight: 600;
  text-align: center;
}

.success {
  background-color: #d4edda;
  color: #155724;
}

.error {
  background-color: #f8d7da;
  color: #721c24;
}

a.back-link {
  display: block;
  text-align: center;
  margin-top: 25px;
  color: #007bff;
  text-decoration: none;
  font-weight: 600;
}

a.back-link:hover {
  text-decoration: underline;
}

@media (max-width: 600px) {
  .container {
    padding: 25px 20px;
  }
}
</style>
</head>
<body>


<div class="container">
  <h2>Edit Profile</h2>

  <?php if ($message): ?>
    <div class="message <?= strpos($message, 'success') !== false ? 'success' : 'error' ?>">
      <?= htmlspecialchars($message) ?>
    </div>
  <?php endif; ?>

  <form method="POST" action="profile.php" enctype="multipart/form-data">
    <?php if (!empty($user['profile_image'])): ?>
      <img src="uploads/<?= htmlspecialchars($user['profile_image']) ?>" alt="Profile Image" style="width:120px; height:120px; border-radius:50%; object-fit:cover; margin-bottom: 15px;">
    <?php else: ?>
      <p>No profile image uploaded.</p>
    <?php endif; ?>

    <label for="fullname">Full Name:</label>
    <input type="text" id="fullname" name="fullname" value="<?= htmlspecialchars($user['fullname']) ?>" required>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" value="<?= htmlspecialchars($user['email']) ?>" required>

    <label for="phone">Phone Number:</label>
    <input type="text" id="phone" name="phone" value="<?= htmlspecialchars($user['phone']) ?>">

    <label for="location">Location:</label>
    <input type="text" id="location" name="location" value="<?= htmlspecialchars($user['location']) ?>">

    <label for="gender">Gender:</label>
    <select id="gender" name="gender">
      <option value="Male" <?= $user['gender'] === 'Male' ? 'selected' : '' ?>>Male</option>
      <option value="Female" <?= $user['gender'] === 'Female' ? 'selected' : '' ?>>Female</option>
      <option value="Other" <?= $user['gender'] === 'Other' ? 'selected' : '' ?>>Other</option>
    </select>

    <label for="dob">Date of Birth:</label>
    <input type="date" id="dob" name="dob" value="<?= htmlspecialchars($user['dob']) ?>">

    <label for="profile_image">Change Profile Image:</label>
    <input type="file" name="profile_image" id="profile_image" accept="image/*">

    <input type="submit" value="Update Profile" class="btn-submit">
  </form>

  <a href="<?= $user['role'] === 'student' ? 'student_dashboard.php' : 'employer_dashboard.php' ?>" class="back-link">&larr; Back to Dashboard</a>
</div>

</body>
</html>
