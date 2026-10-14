<?php
require 'db.php';

function escape($value) {
    return htmlspecialchars((string)$value, ENT_QUOTES, 'UTF-8');
}

$id = filter_input(INPUT_GET, 'id', FILTER_VALIDATE_INT);

if (!$id || $id < 1) {
    exit("Invalid student ID.");
}

$stmt = $conn->prepare(
    "SELECT * FROM students WHERE id = ?"
);

$stmt->bind_param("i", $id);
$stmt->execute();

$result = $stmt->get_result();
$student = $result->fetch_assoc();
$stmt->close();

if (!$student) {
    exit("Student not found.");
}

$message = "";

if ($_SERVER['REQUEST_METHOD'] === 'POST') {

    $name = trim($_POST['name'] ?? '');
    $roll = trim($_POST['roll_number'] ?? '');
    $email = trim($_POST['email'] ?? '');
    $course = trim($_POST['course'] ?? '');

    if ($name === '' || $roll === '' ||
        $email === '' || $course === '') {

        $message = "Please fill in all fields.";

    } elseif (!filter_var($email, FILTER_VALIDATE_EMAIL)) {

        $message = "Enter a valid email address.";

    } else {

        $update = $conn->prepare(
            "UPDATE students
             SET name = ?, roll_number = ?,
                 email = ?, course = ?
             WHERE id = ?"
        );

        $update->bind_param(
            "ssssi",
            $name,
            $roll,
            $email,
            $course,
            $id
        );

        if ($update->execute()) {

            $update->close();

            header("Location: view.php?id=" . $id);
            exit;

        } elseif ($conn->errno === 1062) {

            $message = "This roll number already exists.";

        } else {

            $message = "Unable to update student details.";

        }

        if (isset($update)) {
            $update->close();
        }
    }

    $student['name'] = $name;
    $student['roll_number'] = $roll;
    $student['email'] = $email;
    $student['course'] = $course;
}
?>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Student</title>

    <style>
        :root {
            --bg: linear-gradient(135deg, #eef2ff 0%, #f8fafc 100%);
            --panel: rgba(255, 255, 255, 0.96);
            --primary: #4f46e5;
            --primary-dark: #3730a3;
            --text: #0f172a;
            --muted: #475569;
            --border: #e2e8f0;
            --danger: #dc2626;
            --shadow: 0 18px 40px rgba(15, 23, 42, 0.12);
        }

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 32px 16px;
            font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
            background: var(--bg);
            color: var(--text);
        }

        .container {
            width: 480px;
            max-width: 100%;
            margin: auto;
            background: var(--panel);
            padding: 30px 28px;
            border-radius: 24px;
            box-shadow: var(--shadow);
            border: 1px solid rgba(148, 163, 184, 0.2);
        }

        h2 {
            text-align: center;
            color: var(--primary-dark);
            margin: 0 0 24px;
            font-size: clamp(1.8rem, 3vw, 2.4rem);
            letter-spacing: -0.04em;
        }

        form {
            display: grid;
            gap: 12px;
        }

        label {
            display: block;
            font-weight: 700;
            color: var(--muted);
            margin-bottom: 6px;
        }

        input {
            width: 100%;
            padding: 12px 14px;
            border: 1px solid var(--border);
            border-radius: 12px;
            background: #fff;
            color: var(--text);
            font: inherit;
            transition: border-color 0.2s ease, box-shadow 0.2s ease;
        }

        input:focus {
            outline: none;
            border-color: rgba(79, 70, 229, 0.8);
            box-shadow: 0 0 0 4px rgba(79, 70, 229, 0.12);
        }

        button {
            width: 100%;
            padding: 14px 18px;
            background: linear-gradient(135deg, var(--primary) 0%, #6366f1 100%);
            color: white;
            border: none;
            border-radius: 12px;
            cursor: pointer;
            margin-top: 10px;
            font: inherit;
            font-weight: 700;
            box-shadow: 0 10px 24px rgba(79, 70, 229, 0.2);
        }

        .message {
            color: var(--danger);
            background: rgba(220, 38, 38, 0.08);
            padding: 12px 14px;
            border-radius: 12px;
            border: 1px solid rgba(220, 38, 38, 0.14);
            margin-bottom: 16px;
            font-weight: 600;
        }

        p {
            margin-top: 18px;
            text-align: center;
        }

        a {
            color: var(--primary);
            text-decoration: none;
            font-weight: 700;
        }
    </style>
</head>

<body>

<div class="container">

    <h2>Edit Student</h2>

    <?php if ($message !== ''): ?>
        <p class="message"><?= escape($message) ?></p>
    <?php endif; ?>

    <form method="POST">

        <label>Name</label>
        <input type="text"
               name="name"
               value="<?= escape($student['name']) ?>"
               required>

        <label>Roll Number</label>
        <input type="text"
               name="roll_number"
               value="<?= escape($student['roll_number']) ?>"
               required>

        <label>Email</label>
        <input type="email"
               name="email"
               value="<?= escape($student['email']) ?>"
               required>

        <label>Course</label>
        <input type="text"
               name="course"
               value="<?= escape($student['course']) ?>"
               required>

        <button type="submit">
            Update Student
        </button>

    </form>

    <p><a href="index.php">Back to Student List</a></p>

</div>

</body>
</html>