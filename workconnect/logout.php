<?php
session_start();

// Clear all session data
$_SESSION = array();
session_destroy();

// Redirect to home page
header("Location: index.html?logout=success");
exit();
