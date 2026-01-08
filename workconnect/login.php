<?php
session_start();
$errors = $_SESSION['login_errors'] ?? [];
$old = $_SESSION['login_data'] ?? [];
unset($_SESSION['login_errors'], $_SESSION['login_data']);

// addition

?>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Login - WorkConnect</title>
  <style>
    body {
      margin: 0;
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background-image: linear-gradient(rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7)),
        url("../images/christopher-gower-m_HRfLhgABo-unsplash.jpg");
      background-size: cover;
      color: #fff;
    }

    header {
      background-color: rgba(0, 0, 0, 0.7);
      padding: 15px 30px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      flex-wrap: wrap;
    }

    .project-info {
      display: flex;
      flex-direction: column;
    }

    .project-name {
      font-size: 24px;
      font-weight: bold;
    }

    .slogan {
      font-size: 14px;
      color: #ccc;
    }

    nav a {
      margin-left: 20px;
      color: #fff;
      text-decoration: none;
      font-weight: 500;
    }

    nav a:hover {
      text-decoration:none;
      color: #00ffd5;
    }

    .login-container {
      max-width: 400px;
      margin: 80px auto;
      padding: 30px;
      border-radius: 10px;
      box-shadow: 0 0 10px #000;
    }

    .login-container h2 {
      text-align: center;
      margin-bottom: 25px;
    }

    label {
      display: block;
      margin-bottom: 8px;
      font-weight: 600;
    }

    input[type="text"],
    input[type="email"],
    input[type="password"],
    input[type="submit"]{
      width: 100%;
      padding: 10px;
      margin-bottom: 20px;
      border-radius: 5px;
      border: none;
      box-sizing:border-box;

    }

    .error-message {
      color: red;
      font-size: 15px;
      margin-bottom: 15px;
    }

    input[type="submit"] {
      width: 100%;
      padding: 12px;
      background-color: #28a745;
      border: none;
      color: white;
      font-size: 16px;
      font-weight: bold;
      border-radius: 5px;
      cursor: pointer;
    }

    input[type="submit"]:hover {
      background-color: #218838;
    }

    footer {
      background-color: rgba(0, 0, 0, 0.8);
      color: #fff;
      text-align: center;
      padding: 15px;
      position: fixed;
      width: 100%;
      bottom: 0;
    }

    @media (max-width: 600px) {
      header, .login-container {
        padding: 10px;
      }

      nav a {
        margin-left: 10px;
        font-size: 14px;
      }

      .project-name {
        font-size: 18px;
      }

      .slogan {
        font-size: 12px;
      }
    }
     .highlight {
        color: #ffcc00;
      }
      .logo-container {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo-img {
  height: 40px;
  width: auto;
  border-radius: 30px;
}

.logo-text {
  font-size: 26px;
  font-weight: bold;
  color: white;
}


  </style>
</head>
<body>

<header>
  <div class="project-info">
      <div class="logo-container">
          <img src="images/logo3.png" alt="Zero Limits Logo" class="logo-img" />
          <span class="logo-text"
            ><span class="highlight">Work</span>Connect</span
          >
        </div>
   
  </div>
  <nav>
    <a href="index.html">Home</a>
    <a href="signup.php">Signup</a>
    <a href="login.php">Signin</a>
  </nav>
</header>
<?php
  // Show success message from signup
  if (isset($_SESSION['signup_success'])) {
      echo "<div class='message' style='color:lightgreen;'>" . $_SESSION['signup_success'] . "</div>";
      unset($_SESSION['signup_success']);
  }
?>

<div class="login-container">
  <h2>Signin</h2>
  <form action="process_login.php" method="POST">
    <label for="email">Email:</label>
    <input type="email" name="email" id="email" value="<?= htmlspecialchars($old['email'] ?? '') ?>" required>
    <?php if (isset($errors['email'])): ?>
      <div class="error-message"> <?= $errors['email'] ?></div>
    <?php endif; ?>

    <label for="password">Password:</label>
    <input type="password" name="password" id="password" required>
    <?php if (isset($errors['password'])): ?>
      <div class="error-message"><?= $errors['password'] ?></div>
    <?php endif; ?>

    <input type="submit" value="Login">
    <p>Dont have account?<a href="signup.php" style="color: green; text-decoration:none; margin-left:5px; font-weight:bold"  >Signup<a></p>
  </form>
</div>

<footer>
  &copy; <?php echo date("Y"); ?> WorkConnect. All rights reserved.
</footer>

</body>
</html>
