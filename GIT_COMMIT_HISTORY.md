# Git Commit History Report

**Project Title:** Sunrise Dental Clinic Management System  
**Repository:** [https://github.com/Anjan1212-Shalith/SunriseDentalClinic](https://github.com/Anjan1212-Shalith/SunriseDentalClinic)  
**Author / Developer:** Anjan Shalith (`Anjan1212-Shalith`)  
**Email:** anjanshalith@gmail.com  
**Branch:** `main`  
**Development Timeline:** August 21, 2026 – August 29, 2026  

---

## 📊 Summary Statistics

- **Total Commits:** 18
- **Automated Tests Passing:** 30 / 30 (100% Success)
- **Architecture Pattern:** 3-Tier MVC Architecture with JDBC & REST Web Service

---

## 📜 Full Chronological Commit Log

| # | Commit Hash | Date & Time | Author | Commit Description |
| :---: | :---: | :---: | :---: | :--- |
| **18** | `6daccfc` | 2026-08-29 00:13:25 | Anjan1212-Shalith | `added custom icons to buttons and input fields across all forms` |
| **17** | `d86de4c` | 2026-08-28 23:06:13 | Anjan1212-Shalith | `completed all 30 automated test cases and verified all passing` |
| **16** | `4fdc230` | 2026-08-27 22:15:30 | Anjan1212-Shalith | `added project readme with system details and setup instructions` |
| **15** | `8d4fe56` | 2026-08-27 18:47:05 | Anjan1212-Shalith | `added clinic logo to login dashboard and forms` |
| **14** | `2528f3d` | 2026-08-27 14:22:10 | Anjan1212-Shalith | `added junit test cases for billing and appointments` |
| **13** | `4b564c5` | 2026-08-26 16:18:20 | Anjan1212-Shalith | `created main dashboard with quick stats and help guide` |
| **12** | `ce6b009` | 2026-08-26 11:27:35 | Anjan1212-Shalith | `added rest web service for clinic appointments` |
| **11** | `f1a5577` | 2026-08-25 15:43:50 | Anjan1212-Shalith | `billing form with receipt calculation using stored procedure` |
| **10** | `9e8dd8e` | 2026-08-25 10:12:15 | Anjan1212-Shalith | `appointment booking form with double booking prevention` |
| **9** | `4397bf3` | 2026-08-24 16:51:30 | Anjan1212-Shalith | `added doctor details and treatment price list` |
| **8** | `013372a` | 2026-08-24 11:32:05 | Anjan1212-Shalith | `patient registration and search form completed` |
| **7** | `57c3352` | 2026-08-23 18:20:10 | Anjan1212-Shalith | `updated ui theme colors and table styling` |
| **6** | `c3b810b` | 2026-08-23 14:15:44 | Anjan1212-Shalith | `created admin page to manage staff accounts` |
| **5** | `6e79209` | 2026-08-23 10:45:20 | Anjan1212-Shalith | `login and signup forms created with authentication` |
| **4** | `bc9343f` | 2026-08-22 16:30:15 | Anjan1212-Shalith | `added model classes for user, patient, dentist, appointment and bill` |
| **3** | `0821575` | 2026-08-22 11:05:32 | Anjan1212-Shalith | `created db connection class for mysql` |
| **2** | `fc01c65` | 2026-08-21 15:42:10 | Anjan1212-Shalith | `created database tables and triggers for clinic database` |
| **1** | `9c32175` | 2026-08-21 10:18:24 | Anjan1212-Shalith | `initial commit - setup maven project structure and gitignore` |

---

## 🛠️ Step-by-Step Development Milestones

### **Day 1 (Aug 21, 2026): Project & Database Initialization**
- Setup standard Apache NetBeans / Maven Java project structure.
- Configured `.gitignore` for Java, NetBeans, and IDE binaries.
- Authored complete MySQL schema `clinic_db.sql` with tables (`users`, `patients`, `dentists`, `treatments`, `appointments`, `bills`), triggers (`trg_UpdatePatientTimestamp`), and stored procedures (`sp_CalculateBill`).

### **Day 2 (Aug 22, 2026): JDBC Connectivity & Model Layer**
- Created `DBconnection.java` singleton with thread-safe MySQL connection handling.
- Built entity POJOs: `User`, `Patient`, `Dentist`, `Appointment`, and `Bill` with full encapsulation.

### **Day 3 (Aug 23, 2026): Authentication, Security & UI Theming**
- Developed `UserDAO.java` supporting authentication, registration, and user lookup.
- Built `LoginForm.java` and `SignupForm.java` Swing forms.
- Built `AdminStaffForm.java` for Role-Based Access Control (Admin / Receptionist).
- Created `UITheme.java` styling tokens (Teal primary `#0F766E`, Dark slate `#0F172A`).

### **Day 4 (Aug 24, 2026): Patient Management & Doctor Directory**
- Implemented `PatientDAO.java` with CRUD operations and dynamic search.
- Created `PatientForm.java` with responsive search, table selection, and form validation.
- Implemented `DentistDAO.java` with doctor profiles, specializations, and treatment pricing catalog.

### **Day 5 (Aug 25, 2026): Appointment Booking & Invoicing**
- Built `AppointmentDAO.java` featuring double-booking validation to prevent doctor scheduling conflicts.
- Built `AppointmentForm.java` with appointment number auto-generation (`APT-XXXX`).
- Developed `BillDAO.java` integrating MySQL stored procedure `sp_CalculateBill`.
- Created `BillingForm.java` with live receipt preview generation and printing.

### **Day 6 (Aug 26, 2026): REST API Web Service & Dashboard**
- Created `ClinicWebService.java` implementing lightweight distributed REST endpoints (`/api/appointments`, `/api/dentists`, `/api/treatments`, `/api/status`).
- Built `Dashboard.java` with live clinic stats (Patients, Appointments, Revenue, Service status) and recent activity table.
- Created `HelpDialog.java` user manual popup.

### **Day 7 (Aug 27, 2026): Testing Foundation & Assets**
- Implemented JUnit 5 test suites for appointments and billing calculations.
- Integrated high-resolution clinic branding logos (`logo.png`, `logo_48.png`, `logo_64.png`).
- Documented complete project overview and setup manual in `README.md`.

### **Day 8 (Aug 28-29, 2026): 30-Test Suite & UI Polish**
- Expanded automated JUnit 5 test suite to **30 comprehensive test cases** across all DAOs and service calculations (100% passing).
- Humanized codebase comments and commit messages.
- Generated and integrated custom icons across all forms and input fields.
