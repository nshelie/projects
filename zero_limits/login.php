<?php
session_start();

$username = $password = "";
$username_error = $login_error = "";

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $conn = new mysqli("localhost", "root", "", "assignment_db");

    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    $username = trim($_POST["username"]);
    $password = $_POST["password"];

    $stmt = $conn->prepare("SELECT password FROM users WHERE username = ?");
    $stmt->bind_param("s", $username);
    $stmt->execute();
    $stmt->store_result();

    if ($stmt->num_rows === 1) {
        $stmt->bind_result($hashed_password);
        $stmt->fetch();

        if (password_verify($password, $hashed_password)) {
            $_SESSION["username"] = $username;
            header("Location: profile.php?username=" . urlencode($username));
            exit();
        } else {
            $login_error = "Invalid password. Please try again.";
        }
    } else {
        $login_error = "Username not found.";
    }

    $stmt->close();
    $conn->close();
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Login | Zero Limits</title>
  <link rel="stylesheet" href="style.css" />
  <style>
    .error {
      color: red;
      font-size: 0.9em;
      margin-top: 6px;
      text-align: center;
    }

    td input {
      width: 100%;
    }
  </style>
</head>
<body class="signup-body">

<!-- Top Bar -->
<div class="top-bar">
  <header>
    <img src="images/logo.png" alt="Zero Limits Logo" class="logo" />
    <div class="title-group">
      <h1>Zero <span class="highlight">Limits</span></h1>
      <p class="slogan">Access your account securely</p>
    </div>
  </header>
  <nav>
    <ul>
      <li><a href="index.html">Home</a></li>
      <li><a href="#">About us</a></li>
      <li><a href="#">Products</a></li>
      <li><a href="signup.php">Sign Up</a></li>
      <li><a href="login.php">Sign in</a></li>
     
    </ul>
  </nav>
</div>

<main>
  <section class="profile-section">
    <article class="profile-form">
      <h2>Login to Your Account</h2>

      <form action="login.php" method="POST">
        <table>
          <tr>
            <td ><label for="username" ><b>Username:</b></label></td>
            <td><input type="text" name="username" id="username" required value="<?= htmlspecialchars($username) ?>" /></td>
          </tr>
          <tr>
            <td><label for="password"><b>Password:</b></label></td>
            <td><input type="password" name="password" id="password" required /></td>
          </tr>
          <tr>
            <td colspan="2" style="text-align: center;">
              <input type="submit" value="Login" class="signup" />
              <?php if (!empty($login_error)): ?>
                <div class="error"><?= $login_error ?></div>
              <?php endif; ?>
            </td>
          </tr>
        </table>
      </form>
    </article>
  </section>
</main>

<footer>
  <p>&copy; 2025 Zero Limits. All rights reserved.</p>
  
</footer>

</body>
</html>
