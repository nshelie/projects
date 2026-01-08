<?php
session_start();
if (!isset($_SESSION['user_id']) || $_SESSION['role'] !== 'employer') {
    header("Location: login.php");
    exit();
}

$employer_id = $_SESSION['user_id'];
$conn = new mysqli("localhost", "root", "", "final_project_db");
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Get all applications for employer's jobs
$sql = "
    SELECT ja.id AS application_id, u.fullname AS student_name, u.email, j.title, ja.status, ja.applied_at
    FROM job_applications ja
    JOIN jobs j ON ja.job_id = j.id
    JOIN users u ON ja.user_id = u.id
    WHERE j.employer_id = ?
    ORDER BY ja.applied_at DESC
";

$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $employer_id);
$stmt->execute();
$result = $stmt->get_result();
?>

<!DOCTYPE html>
<html>
<head><title>Manage Job Applications</title>
<style>
  table {
    border-collapse: collapse;
    width: 100%;
    max-width: 900px;
    margin: 20px auto;
    font-family: Arial, sans-serif;
  }
  th, td {
    padding: 10px 12px;
    border: 1px solid #ccc;
    text-align: left;
  }
  th {
    background-color: #004466;
    color: white;
  }
  button {
    padding: 6px 14px;
    margin: 2px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  button[name="action"][value="accept"] {
    background-color: #4CAF50;
    color: white;
  }
  button[name="action"][value="reject"] {
    background-color: #f44336;
    color: white;
  }
</style>

</head>
<body>
  <?php
if (isset($_SESSION['manage_msg'])) {
    echo '<p style="color: green;">' . htmlspecialchars($_SESSION['manage_msg']) . '</p>';
    unset($_SESSION['manage_msg']);
}
?>

<h2>Job Applications for Your Jobs</h2>

<table border="1" cellpadding="10" cellspacing="0">
    <thead>
        <tr>
            <th>Student Name</th>
            <th>Email</th>
            <th>Job Title</th>
            <th>Status</th>
            <th>Applied At</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
<?php while ($row = $result->fetch_assoc()): ?>
    <tr>
        <td><?= htmlspecialchars($row['student_name']) ?></td>
        <td><?= htmlspecialchars($row['email']) ?></td>
        <td><?= htmlspecialchars($row['title']) ?></td>
        <td><?= htmlspecialchars(ucfirst($row['status'])) ?></td>
        <td><?= htmlspecialchars($row['applied_at']) ?></td>
        <td>
            <?php if ($row['status'] === 'pending'): ?>
            <form method="POST" action="manage_application.php" style="display:inline;">
                <input type="hidden" name="application_id" value="<?= $row['application_id'] ?>" />
                <button type="submit" name="action" value="accept">Accept</button>
            </form>
            <form method="POST" action="manage_application.php" style="display:inline;">
                <input type="hidden" name="application_id" value="<?= $row['application_id'] ?>" />
                <button type="submit" name="action" value="reject">Reject</button>
            </form>
            <?php else: ?>
                No actions available
            <?php endif; ?>
        </td>
    </tr>
<?php endwhile; ?>
    </tbody>
</table>

</body>
</html>

<?php
$stmt->close();
$conn->close();
?>
