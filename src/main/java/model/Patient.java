package model;

import java.sql.Timestamp;

/**
 * Patient Entity POJO representing clinic patients
 */
public class Patient {
    private int patientId;
    private String name;
    private String contactNo;
    private String address;
    private String email;
    private String medicalHistory;
    private Timestamp createdAt;

    public Patient() {
    }

    public Patient(int patientId, String name, String contactNo, String address, String email, String medicalHistory) {
        this.patientId = patientId;
        this.name = name;
        this.contactNo = contactNo;
        this.address = address;
        this.email = email;
        this.medicalHistory = medicalHistory;
    }

    public Patient(int patientId, String name, String contactNo, String address, String medicalHistory) {
        this(patientId, name, contactNo, address, "", medicalHistory);
    }

    public Patient(String name, String contactNo, String address, String email, String medicalHistory) {
        this(0, name, contactNo, address, email, medicalHistory);
    }

    public Patient(String name, String contactNo, String address, String medicalHistory) {
        this(0, name, contactNo, address, "", medicalHistory);
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return name + " (" + contactNo + ")";
    }
}
