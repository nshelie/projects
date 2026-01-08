<?php
session_start();
$errors = $_SESSION['signup_errors'] ?? [];
$old = $_SESSION['signup_data'] ?? [];
$success = $_SESSION['signup_success'] ?? null;
unset($_SESSION['signup_errors'], $_SESSION['signup_data'], $_SESSION['signup_success']);
?>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Signup - WorkConnect</title>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" />
  <style>
    body {
      font-family: 'Poppins', sans-serif;
      background: linear-gradient(rgba(0,0,0,0.7), rgba(0,0,0,0.7)),
        url("../images/christopher-gower-m_HRfLhgABo-unsplash.jpg");
      background-size: cover;
      background-position: center;
      min-height: 100vh;
      margin: 0;
      display: flex;
      flex-direction: column;
      color: #fff;
    }
    /* //addition */
 header {
      background-color: rgba(0, 0, 0, 0.6);
      padding: 10px 2rem;
      display: flex;
      justify-content: space-between;
      align-items: center;
      color: white;
    }

    nav a {
      color: white;
      text-decoration: none;
      margin-left: 20px;
      /* font-weight: bold; */
    }

    nav a:hover {
     text-decoration:none;
      color: #00ffd5;
    }
/* end */
    .signup-box {
      /* background: rgba(255,255,255,0.95); */
      padding: 30px;
      margin: 60px auto;
      width: 100%;
      max-width: 500px;
      border-radius: 10px;
      box-shadow: 0 0 10px #000;
    }

    h2 {
      text-align: center;
      margin-bottom: 20px;
    }

    label {
      display: block;
      margin-bottom: 6px;
      /* font-weight: bold; */
      
    }

    .error {
      color: red;
      margin-top: -10px;
      margin-bottom: 10px;
      font-size: 0.9em;
    }

    .success {
      color: green;
      text-align: center;
      margin-bottom: 10px;
    }

    button {
      background-color: #007bff;
      color: #fff;
      padding: 12px;
      border: none;
      width: 100%;
      border-radius: 6px;
      font-weight: bold;
      cursor: pointer;
    }

    button:hover {
      background-color: #0056b3;
    }
      footer {
      background-color: rgba(0, 0, 0, 0.7);
      color: white;
      text-align: center;
      padding: 1rem;
    }
input[type="text"],
input[type="email"],
input[type="password"],
input[type="date"],
input[type="file"],
select {
  width: 100%;
  height: 42px;  /* Adjust height as you prefer */
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 16px;
  box-sizing: border-box;
  margin-bottom: 15px;    
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
     <div class="logo-container">
          <img src="images/logo3.png" alt="Zero Limits Logo" class="logo-img" />
          <span class="logo-text"
            ><span class="highlight">Work</span>Connect</span
          >
        </div>
    <nav>
      <a href="index.html">Home</a>
      <a href="signup.php">Signup</a>
      <a href="login.php">Signin</a>
    </nav>
  </header>

  <form class="signup-box" method="POST" action="process_signup.php">
    <h2>Create Account</h2>

    <?php if ($success): ?>
      <div class="success"><?= $success ?></div>
    <?php endif; ?>

    <label for="fullname">Full Name:</label>
    <input type="text" name="fullname" value="<?= htmlspecialchars($old['fullname'] ?? '') ?>">
    <?php if (isset($errors['fullname'])): ?><div class="error"><?= $errors['fullname'] ?></div><?php endif; ?>

    <label for="username">Username:</label>
    <input type="text" name="username" value="<?= htmlspecialchars($old['username'] ?? '') ?>">
    <?php if (isset($errors['username'])): ?><div class="error"><?= $errors['username'] ?></div><?php endif; ?>

    <label for="email">Email:</label>
    <input type="email" name="email" value="<?= htmlspecialchars($old['email'] ?? '') ?>">
    <?php if (isset($errors['email'])): ?><div class="error"><?= $errors['email'] ?></div><?php endif; ?>

    <label for="phone">Phone:</label>
    <input type="text" name="phone" value="<?= htmlspecialchars($old['phone'] ?? '') ?>">
    <?php if (isset($errors['phone'])): ?><div class="error"><?= $errors['phone'] ?></div><?php endif; ?>

    <label for="dob">Date of Birth:</label>
    <input type="date" name="dob" value="<?= htmlspecialchars($old['dob'] ?? '') ?>">
    <?php if (isset($errors['dob'])): ?><div class="error"><?= $errors['dob'] ?></div><?php endif; ?>

    <label for="gender">Gender:</label>
    <select name="gender">
      <option value="">-- Select Gender --</option>
      <option value="Male" <?= ($old['gender'] ?? '') === 'Male' ? 'selected' : '' ?>>Male</option>
      <option value="Female" <?= ($old['gender'] ?? '') === 'Female' ? 'selected' : '' ?>>Female</option>
      <option value="Other" <?= ($old['gender'] ?? '') === 'Other' ? 'selected' : '' ?>>Other</option>
    </select>
    <?php if (isset($errors['gender'])): ?><div class="error"><?= $errors['gender'] ?></div><?php endif; ?>

    <label for="location">Location:</label>
    <input type="text" name="location" value="<?= htmlspecialchars($old['location'] ?? '') ?>">
    <?php if (isset($errors['location'])): ?><div class="error"><?= $errors['location'] ?></div><?php endif; ?>

    <label for="role">Role:</label>
    <select name="role" id="role">
      <option value="">-- Select Role --</option>
      <option value="student" <?= ($old['role'] ?? '') === 'student' ? 'selected' : '' ?>>Opportunity seekers</option>
      <option value="employer" <?= ($old['role'] ?? '') === 'employer' ? 'selected' : '' ?>>Employer</option>
    </select>
    <?php if (isset($errors['role'])): ?><div class="error"><?= $errors['role'] ?></div><?php endif; ?>

    <!-- <label for="company_name">Company Name (if employer):</label>
    <input type="text" name="company_name" value="<?= htmlspecialchars($old['company_name'] ?? '') ?>">
    <?php if (isset($errors['company_name'])): ?><div class="error"><?= $errors['company_name'] ?></div><?php endif; ?> -->

      <div id="companyField" style="display: none;">
  <label for="company_name">Company Name (if employer):</label>
  <input type="text" name="company_name" value="<?= htmlspecialchars($old['company_name'] ?? '') ?>">
  <?php if (isset($errors['company_name'])): ?><div class="error"><?= $errors['company_name'] ?></div><?php endif; ?>
</div>

    <label for="password">Password:</label>
    <input type="password" name="password">
    <?php if (isset($errors['password'])): ?><div class="error"><?= $errors['password'] ?></div><?php endif; ?>

    <label for="confirm">Confirm Password:</label>
    <input type="password" name="confirm">
    <?php if (isset($errors['confirm'])): ?><div class="error"><?= $errors['confirm'] ?></div><?php endif; ?>

    <button type="submit">Register</button>
    <!-- <p>Dont have account?<a href="login.php" style="color: green; text-decoration:none; margin-left:5px; font-weight:bold"  >Signin<a></p> -->

  </form>

  <footer>&copy; 2025 WorkConnect. All rights reserved.</footer>
  <script>
  const roleSelect = document.getElementById('role');
  const companyField = document.getElementById('companyField');

  function toggleCompanyField() {
    if (roleSelect.value === 'employer') {
      companyField.style.display = 'block';
    } else {
      companyField.style.display = 'none';
    }
  }

  // Run on page load (in case "employer" is pre-selected)
  toggleCompanyField();

  // Run on change
  roleSelect.addEventListener('change', toggleCompanyField);
</script>

</body>
</html>
