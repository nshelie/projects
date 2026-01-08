<?php
session_start();


if (!isset($_SESSION['user_id']) || $_SESSION['role'] !== 'student') {
    header("Location: login.php");
    exit();
}

// Show success/error messages from apply_job.php (if any)
$message_html = '';
if (isset($_SESSION['message'])) {
    $message_html = '<div class="message success">' . htmlspecialchars($_SESSION['message']) . '</div>';
    unset($_SESSION['message']);
}


// Connect to DB to fetch latest user profile image
$host = "localhost";
$dbname = "final_project_db";
$dbuser = "root";
$dbpass = "";

$conn = new mysqli($host, $dbuser, $dbpass, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$user_id = $_SESSION['user_id'];

$stmt = $conn->prepare("SELECT fullname, email, location, role, profile_image FROM users WHERE id = ?");
$stmt->bind_param("i", $user_id);
$stmt->execute();
$result = $stmt->get_result();
$user = $result->fetch_assoc();
$stmt->close();

// === FETCH NOTIFICATIONS HERE ===
$notif_stmt = $conn->prepare("SELECT id, message FROM notifications WHERE user_id = ? AND is_read = FALSE ORDER BY created_at DESC");
$notif_stmt->bind_param("i", $user_id);
$notif_stmt->execute();
$notif_result = $notif_stmt->get_result();

// Store notifications in an array for later display in HTML
$notifications = [];
while ($notif = $notif_result->fetch_assoc()) {
    $notifications[] = $notif;
}
$notif_stmt->close();

$conn->close();
?>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Student Dashboard - Student Career Path Finder</title>
<style>
  /* Reset & basics */
  * {
    box-sizing: border-box;
  }
body {
  margin: 0; 
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #f9fbfd;
  color: #333;
  

  /* ADD THIS: */
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

html, body {
  height: 100%;
  margin: 0;
}
 
  /* Header */
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
    transition: color 0.3s ease;
  }
  nav a:hover {
    color: #82c0ff;
  }

  /* Container for sidebar + main */
.container {
  display: flex;
  max-width: 1200px;
  margin: 40px auto;
  gap: 30px;
  padding: 0 20px;

  /* ADD THIS */
  flex: 1;
}

  /* Sidebar (Profile) */
  .sidebar {
    flex: 0 0 320px;
    background-color: white;
    border-radius: 10px;
    padding: 30px;
    box-shadow: 0 2px 15px rgba(0,0,0,0.1);
    position: sticky;
    top: 80px; /* below header */
    height: fit-content;
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
  display: block;
  margin-left: auto;
  margin-right: auto;
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
  .sidebar .role {
    font-style: italic;
    color: #888;
    margin-top: 15px;
  }
  .sidebar .edit-profile-btn {
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
  .sidebar .edit-profile-btn:hover {
    background-color: #0266c8;
  }

  /* Main Content */
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

  .main-section {
    margin-bottom: 40px;
  }

  .main-section h3 {
    font-size: 22px;
    margin-bottom: 15px;
    border-bottom: 2px solid #004466;
    padding-bottom: 5px;
    color: #004466;
  }

  /* Example job listings table */
  table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 15px;
  }
  th, td {
    padding: 12px 15px;
    border-bottom: 1px solid #ddd;
    text-align: left;
  }
  th {
    background-color: #004466;
    color: white;
  }
  tr:hover {
    background-color: #f1f9ff;
  }

  /* Buttons */
  .btn-primary {
    background-color: #004466;
    color: white;
    border: none;
    padding: 12px 25px;
    border-radius: 8px;
    cursor: pointer;
    font-weight: 600;
    transition: background-color 0.3s ease;
  }
  .btn-primary:hover {
    background-color: #0266c8;
  }

  /* Responsive */
  @media (max-width: 900px) {
    .container {
      flex-direction: column;
      margin: 20px;
    }
    .sidebar {
      position: relative;
      top: auto;
      margin-bottom: 30px;
    }
  }
  
  /* //footer */
 .footer {
  background: #111;
  color: #aaa;
  text-align: center;
  padding: 5px;
}

/* addition */
.message {
    padding: 15px;
    margin: 15px auto;
    max-width: 600px;
    border-radius: 8px;
    font-weight: bold;
    text-align: center;
}

.success {
    background-color: #d4edda;
    color: #155724;
    border: 1px solid #c3e6cb;
}

.error {
    background-color: #f8d7da;
    color: #721c24;
    border: 1px solid #f5c6cb;
}

  section h3 {
    font-size: 22px;
    margin-bottom: 15px;
    border-bottom: 2px solid #004466;
    padding-bottom: 5px;
    color: #004466;
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
    <a href="login.php">Login</a>
    <a href="logout.php">Logout</a>
  

  </nav>
</header>

<div class="container">
  <!-- Left Sidebar -->
  <aside class="sidebar">
    <?php if (!empty($user['profile_image'])): ?>
  <img src="uploads/<?= htmlspecialchars($user['profile_image']) ?>" alt="Profile Image" />
<?php else: ?>
  <img src="images/default-profile.png" alt="Default Profile Image" />
<?php endif; ?>

    <h2><?php echo htmlspecialchars($user['fullname']); ?></h2>
    
    <p class="role" style="font-weight:bold;" >
  <?php 
    $displayRole = ($user['role'] === 'student') ? 'Opportunity Seeker' : ucfirst($user['role']);
    echo htmlspecialchars($displayRole); 
  ?>
</p>
<p><strong>Email:</strong> <?php echo htmlspecialchars($user['email']); ?></p>
<p><strong>Location:</strong> <?php echo htmlspecialchars($user['location']); ?></p>
<!-- <p class="role"><?php echo htmlspecialchars(ucfirst($user['role'])); ?></p> -->
 

  
    <a href="profile.php" class="edit-profile-btn">Edit Profile</a>
  </aside>

  <!-- Right Main Content -->
  <div class="main-content">
  <h2>Opportunity Seeker Dashboard</h2>



  <!-- SECTION 2: Job Listings -->
  <section class="main-section" id="job-listings">
    <h3>Job Listings</h3>
    <p style="
  font-size: 16px;
  color: #555;
  margin-top: -10px;
  margin-bottom: 20px;
  background-color: #f4f8fb;
  padding: 10px 15px;
 font-weight:bold;
  border-radius: 4px;
">
  See latest opportunities from employers.
</p>
        <?php
    // Display the message here, if any
    echo $message_html;
?>
    <table>
      <thead><tr><th>Company</th><th>Position</th><th>Deadline</th><th>Action</th></tr></thead>
      <tbody>
<?php
$limit = 10;  // jobs per page
$page = isset($_GET['page']) ? max(1, intval($_GET['page'])) : 1;
$offset = ($page - 1) * $limit;

$conn = new mysqli("localhost", "root", "", "final_project_db");

// Get total job count for pagination links
$result_count = $conn->query("SELECT COUNT(*) AS total FROM jobs");
$total_jobs = $result_count->fetch_assoc()['total'];
$total_pages = ceil($total_jobs / $limit);

// Fetch jobs with limit and offset
$stmt = $conn->prepare("SELECT * FROM jobs ORDER BY created_at DESC LIMIT ? OFFSET ?");
$stmt->bind_param("ii", $limit, $offset);
$stmt->execute();
$jobs = $stmt->get_result();

while ($job = $jobs->fetch_assoc()): ?>
  <tr>
    <td><?= htmlspecialchars($job['company_name']) ?></td>
    <td><?= htmlspecialchars($job['title']) ?></td>
    <td><?= htmlspecialchars($job['deadline']) ?></td>
    <td>
      <form method="POST" action="apply_job.php" style="display:inline;">
        <input type="hidden" name="job_id" value="<?= $job['id'] ?>">
        <button type="submit" name="apply" class="btn-primary">Apply</button>
      </form>
    </td>
  </tr>
<?php endwhile; ?>
</tbody>

    </table>
    <div class="pagination" style="margin-top: 15px;">
<?php
// for ($p = 1; $p <= $total_pages; $p++) {
//     if ($p == $page) {
//         echo "<strong>$p</strong> ";
//     } else {
//         echo '<a href="?page=' . $p . '">' . $p . '</a> ';
//     }
//}
?>
</div>

  </section>
<!-- SECTION: My Applications -->
<section class="main-section" id="my-applications">
  <h3>My Job Applications</h3>
  <p style="
  font-size: 16px;
  color: #555;
  margin-top: -10px;
  margin-bottom: 20px;
  background-color: #f4f8fb;
  padding: 10px 15px;
 font-weight:bold;
  border-radius: 4px;
">Track the status of jobs you've applied to.</p>
  <?php
  $conn = new mysqli("localhost", "root", "", "final_project_db");
  $user_id = $_SESSION['user_id'];

 $stmt = $conn->prepare("
    SELECT j.title, j.company_name, j.employer_id, ja.job_id, ja.status, ja.applied_at
    FROM job_applications ja
    JOIN jobs j ON ja.job_id = j.id
    WHERE ja.user_id = ?
    ORDER BY ja.applied_at DESC
");

  $stmt->bind_param("i", $user_id);
  $stmt->execute();
  $result = $stmt->get_result();

  if ($result->num_rows > 0): ?>
    <table>
      <thead>
        <tr>
          <th>Job Title</th>
          <th>Company</th>
          <th>Status</th>
          <th>Applied At</th>
        </tr>
      </thead>
      <tbody>
        <?php while ($row = $result->fetch_assoc()): ?>
          <tr>
            <td><?= htmlspecialchars($row['title']) ?></td>
            <td><?= htmlspecialchars($row['company_name']) ?></td>
  <td>
  <?php
  $status = strtolower($row['status']);
  if ($status === 'accepted') {
      echo "<span style='color:green;font-weight:bold; '>Accepted</span>";

      // ✅ Show message button after acceptance
      echo "<br><a href='message.php?to_user_id=" . $row['employer_id'] . "&job_id=" . $row['job_id'] . "' class='btn-primary' style='margin-top:5px;display:inline-block; padding: 6px 12px; background-color: #004466; color: white; text-decoration: none; border-radius: 4px;'>Message Employer</a>";
  } elseif ($status === 'rejected') {
      echo "<span style='color:red;font-weight:bold;'>Rejected</span>";
  } else {
      echo "<span style='color:gray;'>Pending</span>";
  }
  ?>
</td>

            <td><?= htmlspecialchars($row['applied_at']) ?></td>
          </tr>
        <?php endwhile; ?>
      </tbody>
    </table>
  <?php else: ?>
    <p>You haven't applied to any jobs yet.</p>
  <?php endif;

  $stmt->close();
  ?>
</section>
<!-- <a href="inboxx.php" class="btn-primary">📥 View Inbox</a> -->
  <!-- SECTION 3: Task & Challenges -->
  <section class="main-section" id="tasks">
  <h3>Short-Term Gigs & Micro Projects</h3>
  <p style="background-color: #f0f8ff; padding: 12px; border-left: 4px solid #004466; font-size: 16px; line-height: 1.6; border-radius: 6px;">
  Apply for freelance-style tasks or challenges from employers. These may include design work, coding, writing, or marketing jobs. Submit a brief description of your skills and upload relevant files such as your CV or project samples.
</p>

<ol style="padding-left: 0; list-style-position: inside;">
<?php
$conn = new mysqli("localhost", "root", "", "final_project_db");
$tasks = $conn->query("SELECT * FROM tasks ORDER BY created_at DESC LIMIT 10");
while ($task = $tasks->fetch_assoc()): ?>
  <li style="background: #f9f9f9; padding: 15px; margin-bottom: 20px; border-radius: 6px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); display: flex; justify-content: space-between; align-items: center; list-style-type: decimal; font-size: 16px;">
    <div style="flex: 1;">
      <strong style="font-size: 18px;"><?= htmlspecialchars($task['title']) ?></strong>
    </div>
    <a href="task_details.php?id=<?= $task['id'] ?>" style="padding: 6px 12px; background-color: #004466; color: white; text-decoration: none; border-radius: 4px;">View</a>
  </li>
<?php endwhile; $conn->close(); ?>
</ol>

</section>
  <!-- SECTION 4: My Task Submissions & Feedback -->
<section class="main-section" id="task-feedback">
  <h3>My Task Submissions & Feedback</h3>

  <?php
  $conn = new mysqli("localhost", "root", "", "final_project_db");

  $stmt = $conn->prepare("
    SELECT ts.id, t.title AS task_title, ts.submission_file, ts.feedback, ts.status, ts.submitted_at
    FROM task_submissions ts
    JOIN tasks t ON ts.task_id = t.id
    WHERE ts.student_id = ?
    ORDER BY ts.submitted_at DESC
  ");

  $stmt->bind_param("i", $user_id);
  $stmt->execute();
  $result = $stmt->get_result();

  if ($result->num_rows > 0): ?>
    <table>
      <thead>
        <tr>
          <th>Task</th>
          <th>Submitted File</th>
          <th>Status</th>
          <th>Feedback</th>
          <th>Submitted At</th>
        </tr>
      </thead>
   <tbody>
  <?php while ($row = $result->fetch_assoc()): ?>
    <tr>
      <td><?= htmlspecialchars($row['task_title']) ?></td>
      <td>
        <?php if (!empty($row['submission_file'])): ?>
          <a href="uploads/<?= htmlspecialchars($row['submission_file']) ?>" target="_blank" style="padding: 6px 12px; background-color: #004466; color: white; text-decoration: none; border-radius: 4px;">View</a>
        <?php else: ?>
          No File
        <?php endif; ?>
      </td>
      <td><?= htmlspecialchars(ucfirst($row['status'])) ?></td>
      <td><?= !empty($row['feedback']) ? htmlspecialchars($row['feedback']) : '<em>No feedback yet</em>' ?></td>
      <td><?= htmlspecialchars($row['submitted_at']) ?></td>
    </tr>
  <?php endwhile; ?>
</tbody>

    </table>
  <?php else: ?>
    <p>You haven't submitted any tasks yet.</p>
  <?php endif;

  $stmt->close();
  $conn->close();
  ?>
</section>
<section>
  <h3>Messages</h3>
  <p>
  <a href="inboxx.php" class="btn-primary" style="display:inline-block; max-width: 200px; text-decoration: none;">
   📥 View Inbox
  </a>
</p>

</section>


</div>


 <!-- <footer class="footer">
      <p>&copy; 2025 WorkConnect | Post. Apply. Connect.</p>
    </footer> -->


</body>
</html>
