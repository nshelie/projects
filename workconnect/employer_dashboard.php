
<?php
session_start();

// Redirect if not logged in or not an employer
if (!isset($_SESSION['role']) || $_SESSION['role'] !== 'employer') {
    header("Location: login.php");
    exit();
}

$host = "localhost";
$dbname = "final_project_db";
$dbuser = "root";
$dbpass = "";

$conn = new mysqli($host, $dbuser, $dbpass, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$user_id = $_SESSION['user_id'] ?? null;

// Fetch employer profile info
$stmt = $conn->prepare("SELECT fullname, email, company_name, location, profile_image FROM users WHERE id = ?");
$stmt->bind_param("i", $user_id);
$stmt->execute();
$result = $stmt->get_result();
$user = $result->fetch_assoc();
$stmt->close();

$errors = [];
$success_msg = '';

// Handle job or task form submission
if ($_SERVER['REQUEST_METHOD'] === 'POST') {

    // JOB UPLOAD
    if (isset($_POST['upload_job'])) {
        $company = trim($_POST['company']);
        $title = trim($_POST['title']);
        $description = trim($_POST['description']);
        $deadline = trim($_POST['deadline']);
        $created_at = date("Y-m-d H:i:s");

        if (!$company || !$title || !$deadline) {
            $errors[] = "Company, Job Title, and Deadline are required.";
        } elseif (!DateTime::createFromFormat('Y-m-d', $deadline)) {
            $errors[] = "Invalid deadline format. Use YYYY-MM-DD.";
        } else {
            $job_stmt = $conn->prepare("INSERT INTO jobs (employer_id, company_name, title, description, deadline) VALUES (?, ?, ?, ?, ?)");
            $job_stmt->bind_param("issss", $user_id, $company, $title, $description, $deadline);

            if ($job_stmt->execute()) {
                $success_msg = "Job posted successfully.";
            } else {
                $errors[] = "Failed to post job: " . $conn->error;
            }
            $job_stmt->close();
        }
    }

    // TASK UPLOAD
    elseif (isset($_POST['upload_task'])) {
        $title = trim($_POST['task_title']);
        $description = trim($_POST['task_description']);
        $deadline = trim($_POST['task_deadline']);
        $created_at = date("Y-m-d H:i:s");

        if (!$title) {
            $errors[] = "Task title is required.";
        } elseif (!empty($deadline) && !DateTime::createFromFormat('Y-m-d', $deadline)) {
            $errors[] = "Invalid task deadline format. Use YYYY-MM-DD.";
        } else {
            $task_stmt = $conn->prepare("INSERT INTO tasks (employer_id, title, description, created_at, deadline) VALUES (?, ?, ?, ?, ?)");
            $task_stmt->bind_param("issss", $user_id, $title, $description, $created_at, $deadline);

            if ($task_stmt->execute()) {
                $success_msg = "Task uploaded successfully.";
            } else {
                $errors[] = "Failed to upload task: " . $conn->error;
            }
            $task_stmt->close();
        }
    }
}

$conn->close();
?>


<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Employer Dashboard - Student Career Path Finder</title>
<style>
  /* Basic Reset */
  * {
    box-sizing: border-box;
  }
  body {
    margin: 0; 
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    background-color: #f9fbfd;
    color: #333;
  }
  header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #004466;
    padding: 15px 30px;
    color: white;
    position: sticky;
    top: 0;
    z-index: 100;
  }
  .project-name {
    font-size: 24px;
    font-weight: bold;
  }
  nav a {
    color: white;
    margin-left: 20px;
    text-decoration: none;
    font-weight: 600;
  }
  nav a:hover {
    color: #82c0ff;
  }
  .container {
    display: flex;
    max-width: 1200px;
    margin: 40px auto;
    gap: 30px;
    padding: 0 20px;
  }
  .sidebar {
    flex: 0 0 320px;
    background-color: white;
    border-radius: 10px;
    padding: 30px;
    box-shadow: 0 2px 15px rgba(0,0,0,0.1);
    position: sticky;
    top: 80px;
    height: fit-content;
    text-align: center;
  }
  .sidebar img {
    width: 180px;
    height: 180px;
    object-fit: cover;
    border-radius: 50%;
    margin-bottom: 25px;
    border: 5px solid #2c3e50;
    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    background: radial-gradient(circle at center, #6fa8dc 0%, #2c3e50 80%);
    padding: 5px;
    display: inline-block;
  }
  .sidebar h2 {
    margin: 0 0 15px;
    font-weight: 700;
    font-size: 28px;
    color: #004466;
  }
  .sidebar p {
    font-size: 16px;
    margin: 8px 0;
  }
  .sidebar .btn-primary {
    margin-top: 25px;
    display: block;
    text-align: center;
    background-color: #004466;
    color: white;
    padding: 12px 0;
    border-radius: 8px;
    font-weight: 600;
    text-decoration: none;
    transition: background-color 0.3s ease;
  }
  .sidebar .btn-primary:hover {
    background-color: #0266c8;
  }
  .main-content {
    flex: 1;
    background-color: white;
    border-radius: 10px;
    padding: 30px;
    box-shadow: 0 2px 15px rgba(0,0,0,0.1);
  }
  .main-content h2 {
    font-size: 28px;
    margin-bottom: 25px;
    color: #004466;
  }
  section {
    margin-bottom: 40px;
  }
  section h3 {
    font-size: 22px;
    margin-bottom: 15px;
    border-bottom: 2px solid #004466;
    padding-bottom: 5px;
    color: #004466;
  }
  label {
    display: block;
    margin: 12px 0 5px;
    font-weight: 600;
  }
  input[type="text"], input[type="date"], textarea {
    width: 100%;
    padding: 10px 12px;
    font-size: 16px;
    border-radius: 6px;
    border: 1px solid #ccc;
  }
  textarea {
    min-height: 80px;
    resize: vertical;
  }
  .btn-primary {
    background-color: #004466;
    color: white;
    border: none;
    padding: 12px 25px;
    border-radius: 8px;
    cursor: pointer;
    font-weight: 600;
    transition: background-color 0.3s ease;
    margin-top: 15px;
  }
  .btn-primary:hover {
    background-color: #0266c8;
  }
  .message {
    padding: 15px;
    margin-bottom: 20px;
    border-radius: 8px;
  }
  .error {
    background-color: #fdd;
    color: #a00;
  }
  .success {
    background-color: #dfd;
    color: #080;
  }
  @media (max-width: 900px) {
    .container {
      flex-direction: column;
      margin: 20px;
    }
    .sidebar {
      position: relative;
      top: auto;
      margin-bottom: 30px;
      text-align: left;
    }
  }

  /* footer */

  .footer {
  background: #111;
  color: #aaa;
  text-align: center;
  padding: 5px;
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
    <a href="logout.php">Logout</a>
  </nav>
</header>

<div class="container">
  <aside class="sidebar">
    

    <?php if (!empty($user['profile_image'])): ?>
      <img src="uploads/<?= htmlspecialchars($user['profile_image']) ?>" alt="Profile Image" />
    <?php else: ?>
      <img src="images/default-profile.png" alt="Default Profile Image" />
    <?php endif; ?>
    <h2><?= htmlspecialchars($user['fullname']) ?></h2>
    <p><strong>Email:</strong> <?= htmlspecialchars($user['email']) ?></p>
    <p><strong>Company:</strong> <?= htmlspecialchars($user['company_name']) ?></p>
    <p><strong>Location:</strong> <?= htmlspecialchars($user['location']) ?></p>
    <a href="profile.php" class="btn-primary">Edit Profile</a>
  </aside>

  <div class="main-content">
    <h2>Employer Dashboard</h2>
<?php
if (isset($_SESSION['manage_msg'])) {
    echo '<div class="message success">' . htmlspecialchars($_SESSION['manage_msg']) . '</div>';
    unset($_SESSION['manage_msg']);
}
?>

    <?php if (!empty($errors)): ?>
      <div class="message error">
        <ul>
          <?php foreach ($errors as $error): ?>
            <li><?= htmlspecialchars($error) ?></li>
          <?php endforeach; ?>
        </ul>
      </div>
    <?php endif; ?>

    <?php if ($success_msg): ?>
      <div class="message success"><?= htmlspecialchars($success_msg) ?></div>
    <?php endif; ?>

    <section>
      <h3>Post a New Job</h3>
      <form method="POST" action="">
        <label for="company">Company Name *</label>
        <input type="text" id="company" name="company" required value="<?= htmlspecialchars($user['company_name']) ?>" />

        <label for="title">Job Title *</label>
        <input type="text" id="title" name="title" required />

        <label for="description">Job Description</label>
        <textarea id="description" name="description"></textarea>

        <label for="deadline">Application Deadline *</label>
        <input type="date" id="deadline" name="deadline" required />

        <button type="submit" name="upload_job" class="btn-primary">Upload Job</button>
      </form>
    </section>

    <section> 
  <h3>Post a Short-Term Gig / Micro Project</h3>
<!-- <p>Use this form to post freelance-style gigs, part-time projects, or skill-based challenges. Ideal for short tasks like designing a flyer, writing content, or building a feature. job seekers can apply by submitting a short description and uploading their CV or samples.</p>
  -->
<p style="
  background-color: #f0f4f8;
  border-left: 5px solid #3498db;
  padding: 15px;
  border-radius: 8px;
  font-size: 16px;
  color: #333;
  line-height: 1.6;
  max-width: 800px;
">
  Use this form to post freelance-style gigs, part-time projects, or skill-based challenges.
  Ideal for short tasks like designing a flyer, writing content, or building a feature.
  Job seekers can apply by submitting a short description and uploading their CV or samples.
</p>

  <form method="POST" action="">
    <label for="task_title">Task Title *</label>
    <input type="text" id="task_title" name="task_title" required />

    <label for="task_description">Task Description</label>
    <textarea id="task_description" name="task_description"></textarea>

    <label for="task_deadline">Deadline (optional)</label>
    <input type="date" id="task_deadline" name="task_deadline" />

    <button type="submit" name="upload_task" class="btn-primary">Upload Gig</button>

  </form>
</section>

    <section>
  <h3>Job Applications</h3>
  <?php
  $conn = new mysqli("localhost", "root", "", "final_project_db");

  $employer_id = $_SESSION['user_id'];
  $sql = "
      SELECT ja.id AS application_id, u.fullname, j.title, ja.status, ja.applied_at
      FROM job_applications ja
      JOIN users u ON ja.user_id = u.id
      JOIN jobs j ON ja.job_id = j.id
      WHERE j.employer_id = ?
      ORDER BY ja.applied_at DESC
  ";
  $stmt = $conn->prepare($sql);
  $stmt->bind_param("i", $employer_id);
  $stmt->execute();
  $result = $stmt->get_result();
  ?>
  <table border="1" cellpadding="8" cellspacing="0">
    <thead>
      <tr>
        <th>Student Name</th>
        <th>Job Title</th>
        <th>Status</th>
        <th>Applied At</th>
        <th>Action</th>
      </tr>
    </thead>
    <tbody>
      <?php while ($row = $result->fetch_assoc()): ?>
        <tr>
          <td><?= htmlspecialchars($row['fullname']) ?></td>
          <td><?= htmlspecialchars($row['title']) ?></td>
          <td><?= htmlspecialchars($row['status']) ?></td>
          <td><?= htmlspecialchars($row['applied_at']) ?></td>
          <td>
            <?php if ($row['status'] === 'pending'): ?>
              <!-- <form method="POST" action="manage_application.php" style="display:inline;">
                <input type="hidden" name="application_id" value="<?= $row['application_id'] ?>">
                <input type="hidden" name="action" value="accept">
                <button type="submit">Accept</button>
              </form>
              <form method="POST" action="manage_application.php" style="display:inline;">
                <input type="hidden" name="application_id" value="<?= $row['application_id'] ?>">
                <input type="hidden" name="action" value="reject">
                <button type="submit">Reject</button>
              </form> -->
              <form method="POST" action="manage_application.php" style="display:inline;">
  <input type="hidden" name="application_id" value="<?= $row['application_id'] ?>">
  <input type="hidden" name="action" value="accept">
  <button type="submit" style="background-color: #004466; color: white; border: none; padding: 6px 12px; border-radius: 4px; cursor: pointer;">
    Accept
  </button>
</form>

<form method="POST" action="manage_application.php" style="display:inline;">
  <input type="hidden" name="application_id" value="<?= $row['application_id'] ?>">
  <input type="hidden" name="action" value="reject">
  <button type="submit" style="background-color: #e74c3c; color: white; border: none; padding: 6px 12px; border-radius: 4px; cursor: pointer;">
    Reject
  </button>
</form>

            <?php else: ?>
              <em><?= ucfirst($row['status']) ?></em>
            <?php endif; ?>
          </td>
        </tr>
      <?php endwhile; ?>
    </tbody>
  </table>
</section>
<section>
  <h3>Task Submissions</h3>
  <?php
  $conn = new mysqli("localhost", "root", "", "final_project_db");

  $employer_id = $_SESSION['user_id'];

  $task_result = $conn->query("SELECT * FROM tasks WHERE employer_id = $employer_id ORDER BY created_at DESC");

  if ($task_result->num_rows > 0): ?>
    <table border="1" cellpadding="8" cellspacing="0">
      <thead>
        <tr>
          <th>Task Title</th>
          <th>Posted At</th>
          <th>Deadline</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <?php while ($task = $task_result->fetch_assoc()): ?>
          <tr>
            <td><?= htmlspecialchars($task['title']) ?></td>
            <td><?= htmlspecialchars($task['created_at']) ?></td>
            <td><?= htmlspecialchars($task['deadline']) ?></td>
            <td><a href="employer_review.php?id=<?= $task['id'] ?>" class="btn-primary" style="display:inline-block; max-width: 200px; text-decoration: none;" >Review Submissions</a></td>
          </tr>
        <?php endwhile; ?>
      </tbody>
    </table>
  <?php else: ?>
    <p>No tasks posted yet.</p>
  <?php endif; ?>
</section>
<section>
  <h3>Messages</h3>
  <p>
  <a href="inbox.php" class="btn-primary" style="display:inline-block; max-width: 200px; text-decoration: none;">
    Go to Inbox
  </a>
</p>

</section>

  </div>
</div>

 <footer class="footer">
      <p>&copy; 2025 WorkConnect | Post. Apply. Connect.</p>
    </footer>

</body>
</html>
