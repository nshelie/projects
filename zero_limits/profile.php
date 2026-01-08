<?php
session_start();

if (!isset($_GET['username'])) {
    echo "No user specified.";
    exit();
}

$username = $_GET['username'];

$host = "localhost";
$dbname = "assignment_db";
$user = "root";
$pass = "";

$conn = new mysqli($host, $user, $pass, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$sql = "SELECT * FROM users WHERE username = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $username);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 1) {
    $user = $result->fetch_assoc();
} else {
    echo "User not found.";
    exit();
}

$stmt->close();
$conn->close();
?>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Profile | Zero Limits</title>
  <link rel="stylesheet" href="style.css" />
  <style>
    .profile-wrapper {
      padding: 30px;
      max-width: 800px;
      margin: 40px auto;
      background-color: rgba(255, 255, 255, 0.08);
      border-radius: 10px;
      color: #fff;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.4);
    }

    .profile-wrapper h2 {
      text-align: center;
      color: #ffcc00;
      margin-bottom: 20px;
    }

    .profile-img {
      width: 120px;
      height: 120px;
      border-radius: 50%;
      object-fit: cover;
      display: block;
      margin: 0 auto 15px;
      border: 2px solid #ffcc00;
    }

    .profile-info {
      margin-top: 20px;
    }

    .profile-info p {
      margin: 10px 0;
    }

    .profile-actions {
      margin-top: 30px;
      text-align: center;
    }

    .btn {
      background: #1976d2;
      color: white;
      padding: 10px 20px;
      text-decoration: none;
      border-radius: 5px;
      margin: 5px;
      display: inline-block;
    }

    .btn.danger {
      background: #c62828;
    }

    .btn.logout {
      background-color: #444;
    }

    nav ul {
      list-style: none;
      display: flex;
      gap: 20px;
      justify-content: center;
      padding: 10px 0;
    }

    nav a {
      color: white;
      text-decoration: none;
      font-weight: bold;
      padding: 8px 14px;
      border-radius: 5px;
      transition: background-color 0.3s;
    }

    nav a:hover {
      background-color: rgba(255, 255, 255, 0.2);
    }
  </style>
</head>
<body>

<header>
  <div class="top-bar">
    <header>
      <img src="images/logo.png" alt="Logo" class="logo" />
      <div class="title-group">
        <h1>Zero <span class="highlight">Limits</span></h1>
        <p class="slogan">Your Account Profile</p>
      </div>
    </header>
    <nav>
      <ul>
        <li><a href="index.html">Home</a></li>
        <li><a href="#">About us</a></li>
        <li><a href="#">Products</a></li>
        <li><a href="signup.php">Sign Up</a></li>
        <li><a href="login.php">Sign in</a></li>
      
        <li><a href="logout.php" class="btn logout">Logout</a></li>
      </ul>
    </nav>
  </div>
</header>

<main>
  <section class="profile-wrapper">
    <img 
      src="uploads/<?php echo htmlspecialchars($user['profile_image'] ?? 'default_avatar.png'); ?>" 
      alt="Profile Image" 
      class="profile-img" 
    />

    <h2>Welcome, <?php echo htmlspecialchars($user['fullname']); ?>!</h2>
    <div class="profile-info">
      <p><strong>Username:</strong> <?php echo htmlspecialchars($user['username']); ?></p>
      <p><strong>Email:</strong> <?php echo htmlspecialchars($user['email']); ?></p>
      <p><strong>Address:</strong> <?php echo htmlspecialchars($user['address']); ?></p>
      <p><strong>Contact:</strong> <?php echo htmlspecialchars($user['contact_number']); ?></p>
      <p><strong>Account Created:</strong> <?php echo htmlspecialchars($user['created_at'] ?? 'N/A'); ?></p>
    </div>

    <div class="profile-actions">
      <a href="update_profile.php?username=<?php echo urlencode($user['username']); ?>" class="btn">Update Profile</a>
      <a href="delete_profile.php?username=<?php echo urlencode($user['username']); ?>" class="btn danger" onclick="return confirm('Are you sure you want to delete this account?');">Delete Profile</a>
    </div>
  </section>
</main>

<footer>
  <p style="text-align: center;">&copy; 2025 Zero Limits. All rights reserved.</p>
</footer>

</body>
</html>
