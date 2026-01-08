<?php
session_start();

if (!isset($_GET['username'])) {
    echo "No user specified.";
    exit();
}

$username = $_GET['username'];

$conn = new mysqli("localhost", "root", "", "assignment_db");
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
  <title>Update Profile | Zero Limits</title>
  <link rel="stylesheet" href="style.css" />
  <style>
    main {
      display: flex;
      justify-content: center;
      padding: 30px;
    }

    .update-container {
      background-color: rgba(255, 255, 255, 0.08);
      padding: 30px;
      border-radius: 10px;
      color: #fff;
      width: 100%;
      max-width: 600px;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.4);
    }

    .update-container h2 {
      text-align: center;
      color: #ffcc00;
      margin-bottom: 20px;
    }

    label {
      font-weight: bold;
      margin-bottom: 5px;
      display: block;
    }

    input[type="text"],
    input[type="email"],
    input[type="file"] {
      padding: 10px;
      width: 100%;
      margin-bottom: 15px;
      border: none;
      border-radius: 5px;
    }

    .btn {
      background-color: #ffcc00;
      color: black;
      border: none;
      padding: 10px 20px;
      border-radius: 5px;
      font-weight: bold;
      cursor: pointer;
    }

    .btn:hover {
      background-color: #e6b800;
    }

    .profile-img {
      width: 120px;
      height: 120px;
      border-radius: 50%;
      object-fit: cover;
      display: block;
      margin: 0 auto 15px;
      border: 3px solid #ffcc00;
    }

    nav ul {
      display: flex;
      justify-content: flex-end;
      gap: 15px;
      padding: 10px 30px;
    }

    nav ul li {
      list-style: none;
    }

    nav a {
      color: white;
      text-decoration: none;
      font-weight: bold;
      padding: 6px 12px;
      border-radius: 4px;
      transition: background-color 0.3s;
    }

    nav a:hover {
      background-color: rgba(255, 255, 255, 0.2);
    }

    header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 15px 30px;
    }

    .header-container {
      display: flex;
      align-items: center;
    }

    .logo {
      width: 60px;
      height: 60px;
      margin-right: 15px;
    }

    .title-group h1 {
      font-size: 2rem;
    }

    .highlight {
      color: #ffcc00;
    }

    .slogan {
      font-style: italic;
      font-size: 0.9rem;
    }

    footer {
      text-align: center;
      padding: 20px;
      background-color: #111;
      color: #ccc;
      font-size: 0.9rem;
    }
  </style>
</head>
<body>

  <!-- Header -->
  <header>
    <div class="header-container">
      <img src="images/logo.png" alt="Zero Limits Logo" class="logo" />
      <div class="title-group">
        <h1>Zero <span class="highlight">Limits</span></h1>
        <p class="slogan">Update Your Profile</p>
      </div>
    </div>
    <nav>
      <ul>
        <li><a href="index.html">Home</a></li>
        <li><a href="profile.php?username=<?= urlencode($username) ?>">Back to Profile</a></li>
        <li><a href="login.php">Logout</a></li>
      </ul>
    </nav>
  </header>

  <!-- Main Content -->
  <main>
    <div class="update-container">
      <h2>Edit Profile</h2>

      <img src="uploads/<?= htmlspecialchars($user['profile_image'] ?? 'default_avatar.png') ?>" class="profile-img" alt="Profile Image" />

      <form action="update_profile_process.php" method="POST" enctype="multipart/form-data">
        <input type="hidden" name="username" value="<?= htmlspecialchars($user['username']) ?>" />

        <label for="fullname">Full Name</label>
        <input type="text" name="fullname" value="<?= htmlspecialchars($user['fullname']) ?>" required />

        <label for="email">Email</label>
        <input type="email" name="email" value="<?= htmlspecialchars($user['email']) ?>" required />

        <label for="address">Shipping Address</label>
        <input type="text" name="address" value="<?= htmlspecialchars($user['address']) ?>" required />

        <label for="contact_number">Contact Number</label>
        <input type="text" name="contact_number" value="<?= htmlspecialchars($user['contact_number']) ?>" required />

        <label for="profile_image">Change Profile Image</label>
        <input type="file" name="profile_image" accept="image/*" />

        <button type="submit" class="btn">Save Changes</button>
      </form>
    </div>
  </main>

  <!-- Footer -->
  <footer>
    <p>&copy; 2025 Zero Limits. All rights reserved.</p>
  </footer>
</body>
</html>
