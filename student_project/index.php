<?php
require 'db.php';

// Get all student records
$sql = "SELECT * FROM students ORDER BY id DESC";
$result = $conn->query($sql);
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Student Management System</title>

    <style>
        :root {
            --bg: linear-gradient(135deg, #eef2ff 0%, #f8fafc 100%);
            --panel: rgba(255, 255, 255, 0.9);
            --primary: #4f46e5;
            --primary-dark: #3730a3;
            --success: #16a34a;
            --success-dark: #166534;
            --danger: #dc2626;
            --danger-dark: #991b1b;
            --text: #0f172a;
            --muted: #475569;
            --border: #e2e8f0;
            --shadow: 0 18px 40px rgba(15, 23, 42, 0.12);
        }

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
            background: var(--bg);
            color: var(--text);
            padding: 40px 20px;
        }

        .container {
            max-width: 1180px;
            margin: 0 auto;
            background: var(--panel);
            padding: 30px;
            border-radius: 22px;
            box-shadow: var(--shadow);
            border: 1px solid rgba(148, 163, 184, 0.2);
        }

        .topbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 24px;
            gap: 16px;
            flex-wrap: wrap;
        }

        h2 {
            margin: 0;
            color: var(--primary-dark);
            font-size: clamp(1.8rem, 2vw, 2.4rem);
            letter-spacing: -0.04em;
        }

        .badge {
            display: inline-flex;
            align-items: center;
            padding: 8px 14px;
            border-radius: 999px;
            background: rgba(79, 70, 229, 0.1);
            color: var(--primary);
            font-weight: 700;
            font-size: 0.82rem;
        }

        .success {
            background: rgba(22, 163, 74, 0.1);
            color: var(--success-dark);
            padding: 14px 16px;
            text-align: center;
            border-radius: 12px;
            margin-bottom: 20px;
            border: 1px solid rgba(22, 163, 74, 0.2);
            font-weight: 600;
        }

        .toolbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 12px;
            margin-bottom: 24px;
            flex-wrap: wrap;
        }

        .add {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 12px 18px;
            background: linear-gradient(135deg, var(--success) 0%, #22c55e 100%);
            color: white;
            text-decoration: none;
            border-radius: 12px;
            font-weight: 700;
            box-shadow: 0 8px 20px rgba(34, 197, 94, 0.25);
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }

        .add:hover {
            transform: translateY(-1px);
            box-shadow: 0 12px 24px rgba(34, 197, 94, 0.35);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
            border-radius: 16px;
            overflow: hidden;
            box-shadow: 0 10px 30px rgba(15, 23, 42, 0.04);
        }

        th, td {
            padding: 16px 14px;
            border-bottom: 1px solid var(--border);
            text-align: left;
            vertical-align: middle;
        }

        th {
            background: linear-gradient(135deg, var(--primary) 0%, #6366f1 100%);
            color: white;
            font-size: 0.82rem;
            text-transform: uppercase;
            letter-spacing: 0.06em;
        }

        tr:nth-child(even) {
            background: rgba(99, 102, 241, 0.02);
        }

        tr:hover {
            background: rgba(79, 70, 229, 0.03);
        }

        .action {
            display: inline-block;
            margin: 3px;
            padding: 8px 12px;
            text-decoration: none;
            border-radius: 10px;
            font-weight: 600;
            font-size: 0.9rem;
            transition: all 0.2s ease;
        }

        .action:nth-of-type(1) {
            background: rgba(79, 70, 229, 0.08);
            color: var(--primary);
        }

        .action:nth-of-type(2) {
            background: rgba(234, 179, 8, 0.12);
            color: #a16207;
        }

        .action:nth-of-type(3) {
            background: rgba(220, 38, 38, 0.08);
            color: var(--danger);
        }

        .action:hover {
            filter: brightness(0.97);
        }

        .empty {
            text-align: center;
            color: var(--muted);
            padding: 28px 16px;
            font-weight: 600;
        }

        @media (max-width: 700px) {
            .container {
                padding: 22px 16px;
            }

            table {
                min-width: 760px;
            }

            .toolbar {
                align-items: stretch;
            }

            .add {
                width: 100%;
            }
        }
    </style>
</head>

<body>

<div class="container">

    <div class="topbar">
        <h2>Student Management System</h2>
        <span class="badge">Active Records</span>
    </div>

    <?php if (isset($_GET['success']) && $_GET['success'] == '1'): ?>

        <div class="success">
            Student added successfully!
        </div>

    <?php endif; ?>

    <div class="toolbar">
        <span></span>
        <a class="add" href="add_form.php">
            + Add New Student
        </a>
    </div>

    <table>

        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Roll Number</th>
            <th>Email</th>
            <th>Course</th>
            <th>Actions</th>
        </tr>

        <?php if ($result && $result->num_rows > 0): ?>

            <?php while ($row = $result->fetch_assoc()): ?>

                <tr>

                    <td>
                        <?= (int)$row['id'] ?>
                    </td>

                    <td>
                        <?= htmlspecialchars($row['name']) ?>
                    </td>

                    <td>
                        <?= htmlspecialchars($row['roll_number']) ?>
                    </td>

                    <td>
                        <?= htmlspecialchars($row['email']) ?>
                    </td>

                    <td>
                        <?= htmlspecialchars($row['course']) ?>
                    </td>

                    <td>
                        <a class="action"
                           href="view.php?id=<?= (int)$row['id'] ?>">
                            View
                        </a>

                        <a class="action"
                           href="edit.php?id=<?= (int)$row['id'] ?>">
                            Edit
                        </a>

                        <a class="action"
                           href="delete.php?id=<?= (int)$row['id'] ?>"
                           onclick="return confirm('Are you sure you want to delete this student?');">
                            Delete
                        </a>
                    </td>

                </tr>

            <?php endwhile; ?>

        <?php else: ?>

            <tr>
                <td colspan="6" class="empty">
                    No students registered yet.
                </td>
            </tr>

        <?php endif; ?>

    </table>

</div>

</body>
</html>