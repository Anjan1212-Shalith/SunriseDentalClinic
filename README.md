# 🦷 Sunrise Dental Clinic Management System

A robust, full-featured desktop clinic management application built with **Java Swing**, **Pure 3-Tier MVC + DAO Architecture**, **JDBC**, and **MySQL**, designed for **Sunrise Dental Clinic (Colombo Center)**.

---

## 🌟 Key Features

- **Staff Authentication & Role-Based Access Control (RBAC)**:
  - Secure login & registration for Administrators, Receptionists, and Dentists.
  - Dedicated **Staff Administration (`AdminStaffForm`)** module for user management.
- **Patient Records Management (CRUD)**:
  - Add, edit, delete, and search patient medical histories and contact information.
- **Appointment Scheduling with Double-Booking Prevention**:
  - Intelligent scheduling logic ensuring no dentist has conflicting time slots.
- **Billing & Invoice Generation**:
  - Computes treatment fees, dentist consultation charges, and custom discounts with stored procedure support (`sp_CalculateBill`).
  - Formatted receipt preview with direct printing and PDF export.
- **Distributed Services Layer**:
  - Embedded REST Web Service endpoint on port `8088` allowing external/branch appointment querying.
- **Academic Database Integrations**:
  - Stored Procedures (`sp_CalculateBill`).
  - Automated Audit Triggers (`trg_LogAppointmentAudit`, `trg_UpdateAppointmentAudit`).
- **Automated JUnit 5 Testing Suite (30 Test Cases)**:
  - Comprehensive automated unit test coverage across all Data Access Objects and business logic.

---

## 🏗 System Architecture

The project strictly follows a **3-Tier / MVC + Data Access Object (DAO) Pattern**:

```
SunriseDentalClinic
├── src/main/java
│   ├── db/          # JDBC Connection Manager (Singleton Pattern)
│   ├── model/       # Entity POJOs (User, Patient, Dentist, Appointment, Bill)
│   ├── dao/         # Data Access Objects with PreparedStatements & CallableStatements
│   ├── service/     # Lightweight REST Web Service (Distributed Architecture)
│   └── view/        # Java Swing Views (NetBeans Matisse GUI Builder Compatible)
├── src/test/java/   # Automated JUnit 5 Unit Test Suites (30 Test Cases)
└── clinic_db.sql    # Complete Database Schema, Triggers, and Stored Procedures
```

---

## 🧪 Automated Testing Suite (30 Test Cases)

The project includes **30 automated unit test cases** executed via JUnit 5 and Maven Surefire:

| Test Suite Class | Test Count | Key Scenarios Covered |
| :--- | :---: | :--- |
| **`UserDAOTest.java`** | **7** | Valid Admin login, Receptionist login, invalid passwords, unknown usernames, empty credentials, username existence checks. |
| **`PatientDAOTest.java`** | **6** | Patient record insertion, name searching, phone number lookup, patient counts, non-existent search handling, full list retrieval. |
| **`AppointmentDAOTest.java`** | **6** | Model data integrity, `APT-XXXX` ID format regex, Sri Lankan phone regex, email regex, double-booking detection, appointment list retrieval. |
| **`BillCalculatorTest.java`** | **6** | Treatment + consult fee sum, discount deduction, boundary discount flooring at 0.00, consultation-only billing, decimal precision, high-value implant calculations. |
| **`DentistAndTreatmentTest.java`**| **5** | Doctor list retrieval, doctor profile lookup by name, non-existent doctor fallback, treatment cost map, unknown treatment fallback cost. |
| **TOTAL** | **30** | **100% Automated & Passing** |

Execute the full automated test suite using Maven:
```bash
mvn clean test
```

---

## 🚀 Getting Started

### Prerequisites
- **JDK 8 or higher** (OpenJDK / Temurin / Oracle JDK)
- **Apache NetBeans IDE**
- **XAMPP / WAMP Server** (MySQL running on `localhost:3306`)

### Database Setup
1. Start your **XAMPP Control Panel** and ensure MySQL is running on port `3306`.
2. Open **phpMyAdmin** (`http://localhost/phpmyadmin`).
3. Import the [`clinic_db.sql`](clinic_db.sql) file.

### Running the Application
1. Open the project folder in **Apache NetBeans**.
2. Run `LoginForm.java` (or execute Maven `mvn clean test exec:java`).
3. **Default Credentials**:
   - **Administrator**: `admin` / `admin123`
   - **Receptionist**: `receptionist` / `staff123`
   - **Dentist**: `drpriyanga` / `doctor123`

---

## 📄 License & Academic Integrity
Developed as part of the **Advanced Programming (CIS6003)** module at **Cardiff Metropolitan University / ICBT Campus**.
