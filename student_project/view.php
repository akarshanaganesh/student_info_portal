<?php
require 'db.php';

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

$fileStmt = $conn->prepare(
    "SELECT id, original_name, file_type, file_size, upload_date
     FROM student_files
     WHERE student_id = ?
     ORDER BY id DESC"
);

$fileStmt->bind_param("i", $id);
$fileStmt->execute();

$files = $fileStmt->get_result();

function escape($value) {
    return htmlspecialchars((string)$value, ENT_QUOTES, 'UTF-8');
}
?>

<!DOCTYPE html>
<html>
<head>
    <title>Student Profile</title>

    <style>
        :root {
            --bg: linear-gradient(135deg, #eef2ff 0%, #f8fafc 100%);
            --panel: rgba(255, 255, 255, 0.96);
            --primary: #4f46e5;
            --primary-dark: #3730a3;
            --accent: #8b5cf6;
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
            max-width: 760px;
            margin: 0 auto;
            background: var(--panel);
            padding: 30px;
            border-radius: 24px;
            box-shadow: var(--shadow);
            border: 1px solid rgba(148, 163, 184, 0.2);
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 12px;
            flex-wrap: wrap;
            margin-bottom: 20px;
        }

        h2 {
            margin: 0;
            color: var(--primary-dark);
            font-size: clamp(2rem, 3vw, 2.6rem);
            letter-spacing: -0.05em;
        }

        .badge {
            display: inline-flex;
            align-items: center;
            padding: 8px 12px;
            border-radius: 999px;
            background: rgba(79, 70, 229, 0.1);
            color: var(--primary);
            font-weight: 700;
            font-size: 0.8rem;
        }

        .profile-grid {
            display: grid;
            gap: 12px;
            margin-top: 16px;
        }

        .detail {
            display: flex;
            justify-content: space-between;
            gap: 18px;
            padding: 16px 18px;
            border: 1px solid var(--border);
            border-radius: 14px;
            background: rgba(99, 102, 241, 0.02);
        }

        .label {
            font-weight: 700;
            color: var(--muted);
        }

        .value {
            text-align: right;
            font-weight: 600;
            word-break: break-word;
        }

        h3 {
            color: var(--primary-dark);
            margin: 28px 0 16px;
            font-size: 1.3rem;
        }

        ul {
            list-style: none;
            padding: 0;
            margin: 0;
            display: grid;
            gap: 14px;
        }

        li {
            padding: 16px 18px;
            border: 1px solid var(--border);
            border-radius: 14px;
            background: white;
            box-shadow: 0 8px 24px rgba(15, 23, 42, 0.03);
            line-height: 1.7;
        }

        .file-name {
            display: block;
            color: var(--primary-dark);
            font-size: 1.05rem;
            margin-bottom: 6px;
        }

        .file-meta {
            color: var(--muted);
        }

        .file-link {
            display: inline-block;
            margin-top: 10px;
            color: var(--primary);
            font-weight: 700;
            text-decoration: none;
        }

        .file-link:hover {
            color: var(--primary-dark);
        }

        .actions {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            margin-top: 26px;
        }

        .primary-link,
        .secondary-link {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 12px 16px;
            border-radius: 12px;
            text-decoration: none;
            font-weight: 700;
            transition: transform 0.2s ease;
        }

        .primary-link {
            background: linear-gradient(135deg, var(--primary) 0%, var(--accent) 100%);
            color: white;
            box-shadow: 0 10px 24px rgba(79, 70, 229, 0.2);
        }

        .secondary-link {
            color: var(--primary-dark);
            background: rgba(79, 70, 229, 0.08);
        }

        .primary-link:hover,
        .secondary-link:hover {
            transform: translateY(-1px);
        }

        .empty-state {
            padding: 20px 18px;
            color: var(--muted);
            background: rgba(148, 163, 184, 0.08);
            border: 1px dashed var(--border);
            border-radius: 12px;
        }

        @media (max-width: 640px) {
            .container {
                padding: 22px 16px;
            }

            .detail {
                flex-direction: column;
                align-items: flex-start;
            }

            .value {
                text-align: left;
            }
        }
    </style>
</head>

<body>

<div class="container">

    <div class="header">
        <h2>Student Profile</h2>
        <span class="badge">Student Details</span>
    </div>

    <div class="profile-grid">
        <div class="detail">
            <span class="label">ID:</span>
            <span class="value"><?= (int)$student['id'] ?></span>
        </div>

        <div class="detail">
            <span class="label">Name:</span>
            <span class="value"><?= escape($student['name']) ?></span>
        </div>

        <div class="detail">
            <span class="label">Roll Number:</span>
            <span class="value"><?= escape($student['roll_number']) ?></span>
        </div>

        <div class="detail">
            <span class="label">Email:</span>
            <span class="value"><?= escape($student['email']) ?></span>
        </div>

        <div class="detail">
            <span class="label">Course:</span>
            <span class="value"><?= escape($student['course']) ?></span>
        </div>
    </div>

    <h3>Uploaded Documents</h3>

    <?php if ($files->num_rows > 0): ?>

        <ul>
            <?php while ($file = $files->fetch_assoc()): ?>

                <li>
                    <span class="file-name"><?= escape($file['original_name']) ?></span>
                    <span class="file-meta">Type: <?= escape($file['file_type']) ?></span><br>
                    <span class="file-meta">Size: <?= number_format($file['file_size'] / 1024, 2) ?> KB</span><br>
                    <span class="file-meta">Uploaded: <?= escape($file['upload_date']) ?></span><br>
                    <a class="file-link" href="download.php?id=<?= (int)$file['id'] ?>">
                        View / Download File
                    </a>
                </li>

            <?php endwhile; ?>
        </ul>

    <?php else: ?>

        <p class="empty-state">No documents uploaded.</p>

    <?php endif; ?>

    <div class="actions">
        <a class="primary-link" href="edit.php?id=<?= (int)$student['id'] ?>">Edit Student</a>
        <a class="secondary-link" href="index.php">Back to Student List</a>
    </div>

</div>

</body>
</html>

<?php
$fileStmt->close();
?>