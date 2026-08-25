package dao;

import db.DBconnection;
import model.Bill;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

// Data Access Object for Patient Billing and Invoicing
public class BillDAO {

    // Save and generate a new bill
    public boolean generateBill(Bill bill) {
        String sql = "INSERT INTO bills (appointment_no, patient_id, patient_name, treatment_fee, consultation_fee, discount, total_amount, payment_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            if (con == null) return false;

            pst.setString(1, bill.getAppointmentNo());
            pst.setInt(2, bill.getPatientId());
            pst.setString(3, bill.getPatientName());
            pst.setDouble(4, bill.getTreatmentFee());
            pst.setDouble(5, bill.getConsultationFee());
            pst.setDouble(6, bill.getDiscount());
            pst.setDouble(7, bill.getTotalAmount());
            pst.setString(8, bill.getPaymentStatus() != null ? bill.getPaymentStatus() : "Paid");

            int affected = pst.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        bill.setBillId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Generate bill error: " + e.getMessage());
        }
        return false;
    }

    // Call stored procedure sp_CalculateBill
    public double calculateBillProcedure(String appointmentNo, double discount) {
        String sql = "{CALL sp_CalculateBill(?, ?, ?)}";
        try (Connection con = DBconnection.getConnection();
             CallableStatement cstmt = con.prepareCall(sql)) {
            
            if (con == null) return 0.0;

            cstmt.setString(1, appointmentNo);
            cstmt.setDouble(2, discount);
            cstmt.registerOutParameter(3, Types.DECIMAL);

            cstmt.execute();
            return cstmt.getDouble(3);
        } catch (SQLException e) {
            System.out.println("Stored procedure bill calc error: " + e.getMessage());
        }
        return 0.0;
    }

    // Get all generated bills
    public List<Bill> getAllBills() {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT bill_id, appointment_no, patient_id, patient_name, treatment_fee, consultation_fee, discount, total_amount, payment_status, bill_date " +
                     "FROM bills ORDER BY bill_id DESC";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            if (con == null) return list;

            while (rs.next()) {
                list.add(extractBillFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Get all bills error: " + e.getMessage());
        }
        return list;
    }

    // Search bills by patient name or appointment number
    public List<Bill> searchBills(String keyword) {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT bill_id, appointment_no, patient_id, patient_name, treatment_fee, consultation_fee, discount, total_amount, payment_status, bill_date " +
                     "FROM bills WHERE appointment_no LIKE ? OR patient_name LIKE ? ORDER BY bill_id DESC";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            if (con == null) return list;

            String pattern = "%" + keyword + "%";
            pst.setString(1, pattern);
            pst.setString(2, pattern);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(extractBillFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Search bills error: " + e.getMessage());
        }
        return list;
    }

    // Calculate total clinic revenue
    public double getTotalRevenue() {
        String sql = "SELECT SUM(total_amount) FROM bills WHERE payment_status = 'Paid'";
        try (Connection con = DBconnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.out.println("Get revenue error: " + e.getMessage());
        }
        return 0.0;
    }

    private Bill extractBillFromResultSet(ResultSet rs) throws SQLException {
        Bill b = new Bill();
        b.setBillId(rs.getInt("bill_id"));
        b.setAppointmentNo(rs.getString("appointment_no"));
        b.setPatientId(rs.getInt("patient_id"));
        b.setPatientName(rs.getString("patient_name"));
        b.setTreatmentFee(rs.getDouble("treatment_fee"));
        b.setConsultationFee(rs.getDouble("consultation_fee"));
        b.setDiscount(rs.getDouble("discount"));
        b.setTotalAmount(rs.getDouble("total_amount"));
        b.setPaymentStatus(rs.getString("payment_status"));
        b.setPaymentDate(rs.getTimestamp("bill_date"));
        return b;
    }
}
