
🧑‍💼 Online Job Portal – Java GUI Project

A complete role-based Job Portal built using Java Swing (GUI), JDBC, and MySQL with a modular architecture following clean OOP principles.

🚀 Project Overview

This project is an Online Job Portal system where Admins, Employers, and Candidates interact through separate dashboards.

✔ Candidates can view jobs, apply, and track their applications
✔ Employers can post jobs and manage applicants (optional module)
✔ Admins can approve jobs, manage users, and control system settings

The project strictly follows Java OOP Principles, MVC pattern, and JDBC database operations.

🎯 Features (Role-Based)
👨‍💼 Admin

Manage all users (edit, delete, change role)

Approve/Reject job postings

Manage system settings

View pending jobs & admin controls

🏢 Employer

Post new job openings

View and manage posted jobs

Review candidate applications
(If you want, I can generate this module fully too)

👨‍🎓 Candidate (Job Seeker)

Search and view available job listings

Apply to jobs with resume text

Track application history

Manage profile

🛠 Tech Stack Used

Java (Swing GUI)

JDBC (MySQL connectivity)

MySQL Database

OOP principles

DAO Design Pattern

VS Code + Extensions

📁 Project Structure
src/
 └── com.onlinejobportal
       ├── ui/         → All GUI Frames (Login, Signup, Dashboards)
       ├── dao/        → JDBC & SQL Operations (UserDAO, JobDAO, ApplicationDAO)
       ├── model/      → POJO classes (User, Job, Application)
       ├── util/       → DBUtil.java (MySQL Connection Handler)
       └── Main.java   → Entry Point
