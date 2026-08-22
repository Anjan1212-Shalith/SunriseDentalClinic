package model;

import java.sql.Timestamp;

/**
 * Bill Entity POJO representing billing receipts and payment records
 */
public class Bill {
    private int billId;
    private String appointmentNo;
    private int patientId;
    private String patientName;
    private double treatmentFee;
    private double consultationFee;
    private double discount;
    private double totalAmount;
    private String paymentStatus;
    private Timestamp paymentDate;

    public Bill() {
    }

    public Bill(int billId, String appointmentNo, int patientId, String patientName,
                double treatmentFee, double consultationFee, double discount,
                double totalAmount, String paymentStatus) {
        this.billId = billId;
        this.appointmentNo = appointmentNo;
        this.patientId = patientId;
        this.patientName = patientName;
        this.treatmentFee = treatmentFee;
        this.consultationFee = consultationFee;
        this.discount = discount;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
    }

    public Bill(String appointmentNo, int patientId, String patientName,
                double treatmentFee, double consultationFee, double discount,
                double totalAmount, String paymentStatus) {
        this.appointmentNo = appointmentNo;
        this.patientId = patientId;
        this.patientName = patientName;
        this.treatmentFee = treatmentFee;
        this.consultationFee = consultationFee;
        this.discount = discount;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getAppointmentNo() {
        return appointmentNo;
    }

    public void setAppointmentNo(String appointmentNo) {
        this.appointmentNo = appointmentNo;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public double getTreatmentFee() {
        return treatmentFee;
    }

    public void setTreatmentFee(double treatmentFee) {
        this.treatmentFee = treatmentFee;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * Helper method to calculate total amount based on treatment fee, consultation fee, and discount
     */
    public void calculateTotal() {
        double subtotal = this.treatmentFee + this.consultationFee;
        this.totalAmount = Math.max(0, subtotal - this.discount);
    }

    @Override
    public String toString() {
        return "Bill #" + billId + " - " + patientName + " (Total: LKR " + String.format("%.2f", totalAmount) + ")";
    }
}
