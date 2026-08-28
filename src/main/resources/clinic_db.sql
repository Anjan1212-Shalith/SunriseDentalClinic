-- ==========================================================
-- SUNRISE DENTAL CLINIC MANAGEMENT SYSTEM
-- Database Schema & Seed Data Script for WAMP Server / phpMyAdmin
-- Database: clinic_db
-- Host: localhost:3306
-- ==========================================================

CREATE DATABASE IF NOT EXISTS clinic_db;
USE clinic_db;

-- 1. Table: users
DROP TABLE IF EXISTS bills;
DROP TABLE IF EXISTS appointment_audit_log;
DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS treatments;
DROP TABLE IF EXISTS dentists;
DROP TABLE IF EXISTS patients;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL DEFAULT 'Receptionist',
    contact_no VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Table: patients
CREATE TABLE patients (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact_no VARCHAR(20) NOT NULL,
    address VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    medical_history TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Table: dentists
CREATE TABLE dentists (
    dentist_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    consultation_fee DECIMAL(10,2) NOT NULL DEFAULT 2000.00,
    contact_no VARCHAR(20),
    available_days VARCHAR(100) DEFAULT 'Monday - Friday'
);

-- 4. Table: treatments
CREATE TABLE treatments (
    treatment_id INT AUTO_INCREMENT PRIMARY KEY,
    treatment_name VARCHAR(100) NOT NULL UNIQUE,
    cost DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    description TEXT
);

-- 5. Table: appointments
CREATE TABLE appointments (
    appointment_no VARCHAR(20) PRIMARY KEY,
    patient_id INT NOT NULL,
    patient_name VARCHAR(100) NOT NULL,
    dentist_name VARCHAR(100) NOT NULL,
    treatment_type VARCHAR(100) NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time VARCHAR(20) NOT NULL,
    status VARCHAR(30) DEFAULT 'Scheduled',
    notes TEXT,
    diagnosis TEXT,
    recommended_treatment VARCHAR(150),
    follow_up_advice VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE
);

-- 6. Table: bills
CREATE TABLE bills (
    bill_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_no VARCHAR(20) NOT NULL,
    patient_id INT NOT NULL,
    patient_name VARCHAR(100) NOT NULL,
    treatment_fee DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    consultation_fee DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    discount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    payment_status VARCHAR(20) DEFAULT 'Paid',
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_no) REFERENCES appointments(appointment_no) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE
);

-- 7. Table: appointment_audit_log (For Academic Requirement - Audit Trail)
CREATE TABLE appointment_audit_log (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_no VARCHAR(20),
    action_type VARCHAR(50) NOT NULL,
    action_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    details TEXT
);

-- ==========================================================
-- ADVANCED DATABASE FEATURES: STORED PROCEDURES & TRIGGERS
-- ==========================================================

-- Stored Procedure: sp_CalculateBill
DROP PROCEDURE IF EXISTS sp_CalculateBill;
DELIMITER //
CREATE PROCEDURE sp_CalculateBill(
    IN p_treatment_fee DECIMAL(10,2),
    IN p_consultation_fee DECIMAL(10,2),
    IN p_discount_percent DECIMAL(5,2),
    OUT p_subtotal DECIMAL(10,2),
    OUT p_discount_amount DECIMAL(10,2),
    OUT p_total_amount DECIMAL(10,2)
)
BEGIN
    SET p_subtotal = p_treatment_fee + p_consultation_fee;
    SET p_discount_amount = (p_subtotal * p_discount_percent) / 100.00;
    SET p_total_amount = p_subtotal - p_discount_amount;
END //
DELIMITER ;

-- Trigger 1: trg_LogAppointmentAudit (Fires after appointment insertion)
DROP TRIGGER IF EXISTS trg_LogAppointmentAudit;
DELIMITER //
CREATE TRIGGER trg_LogAppointmentAudit
AFTER INSERT ON appointments
FOR EACH ROW
BEGIN
    INSERT INTO appointment_audit_log (appointment_no, action_type, details)
    VALUES (
        NEW.appointment_no, 
        'APPOINTMENT_SCHEDULED', 
        CONCAT('Patient: ', NEW.patient_name, ' | Dentist: ', NEW.dentist_name, ' | Date: ', NEW.appointment_date, ' ', NEW.appointment_time)
    );
END //
DELIMITER ;

-- Trigger 2: trg_UpdateAppointmentAudit (Fires after appointment update)
DROP TRIGGER IF EXISTS trg_UpdateAppointmentAudit;
DELIMITER //
CREATE TRIGGER trg_UpdateAppointmentAudit
AFTER UPDATE ON appointments
FOR EACH ROW
BEGIN
    INSERT INTO appointment_audit_log (appointment_no, action_type, details)
    VALUES (
        NEW.appointment_no, 
        'APPOINTMENT_UPDATED', 
        CONCAT('Status: ', OLD.status, ' -> ', NEW.status, ' | Treatment: ', NEW.treatment_type)
    );
END //
DELIMITER ;

-- ==========================================================
-- SEED DATA INSERTIONS
-- ==========================================================

-- Insert Users
INSERT INTO users (username, password, full_name, role, contact_no) VALUES
('admin', 'admin123', 'System Administrator', 'Admin', '0771234567'),
('receptionist', 'staff123', 'Kavindi Perera', 'Receptionist', '0719876543'),
('drpriyanga', 'doctor123', 'Dr. Priyanga Jayawardena', 'Dentist', '0761122334');

-- Insert Dentists
INSERT INTO dentists (name, specialization, consultation_fee, contact_no, available_days) VALUES
('Dr. Priyanga Jayawardena', 'Consultant Orthodontist', 2500.00, '0772345678', 'Monday, Wednesday, Friday'),
('Dr. Samantha Silva', 'Dental Surgeon & Implantologist', 3000.00, '0713456789', 'Tuesday, Thursday, Saturday'),
('Dr. Kasun Fernando', 'General Dental Practitioner', 1800.00, '0754567890', 'Monday to Saturday'),
('Dr. Anura Perera', 'Pediatric Dental Specialist', 2200.00, '0765678901', 'Wednesday, Saturday, Sunday');

-- Insert Treatments
INSERT INTO treatments (treatment_name, cost, description) VALUES
('Dental Consultation & Examination', 1500.00, 'Comprehensive oral examination and treatment planning'),
('Scaling & Polishing (Cleaning)', 3500.00, 'Full mouth ultrasonic plaque and tartar removal with polishing'),
('Tooth Extraction (Simple)', 4000.00, 'Standard painless extraction under local anesthesia'),
('Surgical Tooth Extraction (Impacted)', 12000.00, 'Surgical wisdom tooth removal and suturing'),
('Dental Composite Filling', 4500.00, 'Tooth-colored light-cured composite resin restoration'),
('Root Canal Treatment (RCT)', 18000.00, 'Complete endodontic therapy for infected pulp canal'),
('Teeth Whitening (Bleaching)', 15000.00, 'In-office LED laser accelerated teeth brightening'),
('Ceramic Dental Crown', 22000.00, 'High quality porcelain/zirconia crown restoration'),
('Braces Adjustment & Orthodontics', 8500.00, 'Routine archwire adjustment, bracket review and tightening');

-- Insert Sample Patients
INSERT INTO patients (name, contact_no, address, email, medical_history) VALUES
('Nimal Wickramasinghe', '0773456789', 'No 45, Galle Road, Colombo 03', 'nimal.w@gmail.com', 'Penicillin allergy, mild hypertension'),
('Sanduni Rajapaksa', '0714567890', '12/A, Kandy Road, Kiribathgoda', 'sanduni.r@yahoo.com', 'None'),
('Mohamed Rizwan', '0765678901', '78, Main Street, Dehiwala', 'm.rizwan@outlook.com', 'Type 2 Diabetes'),
('Chathuri Dilrukshi', '0726789012', '104, High Level Road, Nugegoda', 'chathuri.d@gmail.com', 'Asthma');

-- Insert Sample Appointments
INSERT INTO appointments (appointment_no, patient_id, patient_name, dentist_name, treatment_type, appointment_date, appointment_time, status, notes) VALUES
('APT-1001', 1, 'Nimal Wickramasinghe', 'Dr. Priyanga Jayawardena', 'Scaling & Polishing (Cleaning)', '2026-08-20', '09:00 AM', 'Confirmed', 'Routine annual cleaning'),
('APT-1002', 2, 'Sanduni Rajapaksa', 'Dr. Samantha Silva', 'Dental Composite Filling', '2026-08-20', '10:30 AM', 'Confirmed', 'Upper right molar sensitivity'),
('APT-1003', 3, 'Mohamed Rizwan', 'Dr. Kasun Fernando', 'Root Canal Treatment (RCT)', '2026-08-21', '02:00 PM', 'Scheduled', 'Severe pain on lower left premolar');

-- Insert Sample Bills
INSERT INTO bills (appointment_no, patient_id, patient_name, treatment_fee, consultation_fee, discount, total_amount, payment_status) VALUES
('APT-1001', 1, 'Nimal Wickramasinghe', 3500.00, 2500.00, 500.00, 5500.00, 'Paid'),
('APT-1002', 2, 'Sanduni Rajapaksa', 4500.00, 3000.00, 0.00, 7500.00, 'Paid');
