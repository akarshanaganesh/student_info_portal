document.getElementById("registerForm").addEventListener("submit", function(event) {
    event.preventDefault();

    // Get input values
    let name = document.getElementById("name").value.trim();
    let roll = document.getElementById("roll").value.trim();
    let email = document.getElementById("email").value.trim();
    let course = document.getElementById("course").value;
    let password = document.getElementById("password").value;
    let file = document.getElementById("file").value;

    // Clear old messages
    document.querySelectorAll(".error").forEach(function(error) {
        error.textContent = "";
    });
    document.getElementById("success").textContent = "";

    let valid = true;

    if (name === "") {
        document.getElementById("nameError").textContent = "Please enter your name.";
        valid = false;
    }

    if (roll === "") {
        document.getElementById("rollError").textContent = "Please enter your roll number.";
        valid = false;
    }

    let emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (email === "") {
        document.getElementById("emailError").textContent = "Please enter your email.";
        valid = false;
    } else if (!emailPattern.test(email)) {
        document.getElementById("emailError").textContent = "Please enter a valid email.";
        valid = false;
    }

    if (course === "") {
        document.getElementById("courseError").textContent = "Please select a course.";
        valid = false;
    }

    let passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;
    if (!passwordPattern.test(password)) {
        document.getElementById("passwordError").textContent =
            "Use 8 characters with uppercase, lowercase and a number.";
        valid = false;
    }

    if (file === "") {
        document.getElementById("fileError").textContent = "Please upload a document.";
        valid = false;
    }

    if (valid) {
        document.getElementById("success").textContent =
            "Registration form validated successfully!";
        document.getElementById("registerForm").reset();
    }
});
