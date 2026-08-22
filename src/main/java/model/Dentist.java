package model;

/**
 * Dentist Entity POJO representing clinic dental surgeons and specialists
 */
public class Dentist {
    private int dentistId;
    private String name;
    private String specialization;
    private double consultationFee;
    private String contactNo;
    private String availableDays;

    public Dentist() {
    }

    public Dentist(int dentistId, String name, String specialization, double consultationFee, String contactNo, String availableDays) {
        this.dentistId = dentistId;
        this.name = name;
        this.specialization = specialization;
        this.consultationFee = consultationFee;
        this.contactNo = contactNo;
        this.availableDays = availableDays;
    }

    public Dentist(String name, String specialization, double consultationFee, String contactNo, String availableDays) {
        this.name = name;
        this.specialization = specialization;
        this.consultationFee = consultationFee;
        this.contactNo = contactNo;
        this.availableDays = availableDays;
    }

    public int getDentistId() {
        return dentistId;
    }

    public void setDentistId(int dentistId) {
        this.dentistId = dentistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(String availableDays) {
        this.availableDays = availableDays;
    }

    @Override
    public String toString() {
        return name + " - " + specialization + " (LKR " + String.format("%.2f", consultationFee) + ")";
    }
}
