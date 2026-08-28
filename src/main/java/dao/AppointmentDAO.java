package dao;

import db.DBconnection;
import model.Appointment;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Data Access Object for Appointment Scheduling
public class AppointmentDAO {

    // Create a new appointment
    public boolean createAppointment(Appointment appointment) {
        String sql = "INSERT INTO appointments (appointment_no, patient_id, patient_name, dentist_name, treatment_type, appointment_date, appointment_time, status, notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            if (con == null) return false;

            pst.setString(1, appointment.getAppointmentNo());
            pst.setInt(2, appointment.getPatientId());
            pst.setString(3, appointment.getPatientName());
            pst.setString(4, appointment.getDentistName());
            pst.setString(5, appointment.getTreatmentType());
            pst.setDate(6, appointment.getAppointmentDate());
            pst.setString(7, appointment.getAppointmentTime());
            pst.setString(8, appointment.getStatus() != null ? appointment.getStatus() : "Scheduled");
            pst.setString(9, appointment.getNotes());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Create appointment error: " + e.getMessage());
        }
        return false;
    }

    // Update appointment details
    public boolean updateAppointment(Appointment appointment) {
        String sql = "UPDATE appointments SET patient_id = ?, patient_name = ?, dentist_name = ?, treatment_type = ?, " +
                     "appointment_date = ?, appointment_time = ?, status = ?, notes = ? WHERE appointment_no = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            if (con == null) return false;

            pst.setInt(1, appointment.getPatientId());
            pst.setString(2, appointment.getPatientName());
            pst.setString(3, appointment.getDentistName());
            pst.setString(4, appointment.getTreatmentType());
            pst.setDate(5, appointment.getAppointmentDate());
            pst.setString(6, appointment.getAppointmentTime());
            pst.setString(7, appointment.getStatus());
            pst.setString(8, appointment.getNotes());
            pst.setString(9, appointment.getAppointmentNo());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update appointment error: " + e.getMessage());
        }
        return false;
    }

    // Delete appointment by number
    public boolean deleteAppointment(String appointmentNo) {
        String sql = "DELETE FROM appointments WHERE appointment_no = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            if (con == null) return false;

            pst.setString(1, appointmentNo);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Delete appointment error: " + e.getMessage());
        }
        return false;
    }

    // Find appointment by appointment number
    public Appointment getAppointmentByNo(String appointmentNo) {
        String sql = "SELECT appointment_no, patient_id, patient_name, dentist_name, treatment_type, appointment_date, appointment_time, status, notes, created_at " +
                     "FROM appointments WHERE appointment_no = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            if (con == null) return null;

            pst.setString(1, appointmentNo);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return extractAppointmentFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Get appointment error: " + e.getMessage());
        }
        return null;
    }

    // Get all scheduled appointments
    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT appointment_no, patient_id, patient_name, dentist_name, treatment_type, appointment_date, appointment_time, status, notes, created_at " +
                     "FROM appointments ORDER BY appointment_date DESC, appointment_time ASC";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            if (con == null) return list;

            while (rs.next()) {
                list.add(extractAppointmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Get all appointments error: " + e.getMessage());
        }
        return list;
    }

    // Search appointments by patient or doctor name
    public List<Appointment> searchAppointments(String keyword) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT appointment_no, patient_id, patient_name, dentist_name, treatment_type, appointment_date, appointment_time, status, notes, created_at " +
                     "FROM appointments WHERE appointment_no LIKE ? OR patient_name LIKE ? OR dentist_name LIKE ? OR treatment_type LIKE ? " +
                     "ORDER BY appointment_date DESC";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            if (con == null) return list;

            String pattern = "%" + keyword + "%";
            pst.setString(1, pattern);
            pst.setString(2, pattern);
            pst.setString(3, pattern);
            pst.setString(4, pattern);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(extractAppointmentFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Search appointments error: " + e.getMessage());
        }
        return list;
    }

    // Check if dentist is already booked for date and time slot
    public boolean isDoubleBooked(String dentistName, Date date, String time, String excludeApptNo) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE dentist_name = ? AND appointment_date = ? AND appointment_time = ? AND status != 'Cancelled'";
        if (excludeApptNo != null && !excludeApptNo.trim().isEmpty()) {
            sql += " AND appointment_no != ?";
        }
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            if (con == null) return false;

            pst.setString(1, dentistName);
            pst.setDate(2, date);
            pst.setString(3, time);
            if (excludeApptNo != null && !excludeApptNo.trim().isEmpty()) {
                pst.setString(4, excludeApptNo);
            }

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Double booking check error: " + e.getMessage());
        }
        return false;
    }

    // Generate next appointment number
    public String generateNextAppointmentNo() {
        String sql = "SELECT appointment_no FROM appointments ORDER BY created_at DESC LIMIT 1";
        try (Connection con = DBconnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                String lastNo = rs.getString("appointment_no");
                if (lastNo != null && lastNo.startsWith("APT-")) {
                    try {
                        int num = Integer.parseInt(lastNo.substring(4));
                        return String.format("APT-%04d", num + 1);
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (SQLException e) {
            System.out.println("Generate appointment number error: " + e.getMessage());
        }
        return "APT-1001";
    }

    // Get today's total appointment count
    public int getTodayAppointmentsCount() {
        String sql = "SELECT COUNT(*) FROM appointments WHERE appointment_date = CURDATE() AND status != 'Cancelled'";
        try (Connection con = DBconnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Today count error: " + e.getMessage());
        }
        return 0;
    }

    // Get all appointments assigned to a specific dentist
    public List<Appointment> getAppointmentsByDentist(String dentistName) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE dentist_name LIKE ? ORDER BY appointment_date DESC, appointment_time ASC";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            if (con == null) return list;
            pst.setString(1, "%" + dentistName + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(extractAppointmentFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Get dentist appointments error: " + e.getMessage());
        }
        return list;
    }

    // Save dentist consultation diagnosis and future recommended treatments
    public boolean saveDentistFeedback(String appointmentNo, String diagnosis, String recommendedTreatment, String followUpAdvice) {
        String sql = "UPDATE appointments SET diagnosis = ?, recommended_treatment = ?, follow_up_advice = ?, status = 'Completed' WHERE appointment_no = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            if (con == null) return false;
            pst.setString(1, diagnosis);
            pst.setString(2, recommendedTreatment);
            pst.setString(3, followUpAdvice);
            pst.setString(4, appointmentNo);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Save dentist feedback error: " + e.getMessage());
        }
        return false;
    }

    // Get latest doctor recommendation for patient (for staff booking suggestion)
    public Appointment getLatestRecommendationForPatient(int patientId) {
        String sql = "SELECT * FROM appointments WHERE patient_id = ? AND recommended_treatment IS NOT NULL AND recommended_treatment != '' ORDER BY appointment_date DESC, created_at DESC LIMIT 1";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            if (con == null) return null;
            pst.setInt(1, patientId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return extractAppointmentFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Get patient recommendation error: " + e.getMessage());
        }
        return null;
    }

    private Appointment extractAppointmentFromResultSet(ResultSet rs) throws SQLException {
        Appointment a = new Appointment();
        a.setAppointmentNo(rs.getString("appointment_no"));
        a.setPatientId(rs.getInt("patient_id"));
        a.setPatientName(rs.getString("patient_name"));
        a.setDentistName(rs.getString("dentist_name"));
        a.setTreatmentType(rs.getString("treatment_type"));
        a.setAppointmentDate(rs.getDate("appointment_date"));
        a.setAppointmentTime(rs.getString("appointment_time"));
        a.setStatus(rs.getString("status"));
        a.setNotes(rs.getString("notes"));
        try {
            a.setDiagnosis(rs.getString("diagnosis"));
            a.setRecommendedTreatment(rs.getString("recommended_treatment"));
            a.setFollowUpAdvice(rs.getString("follow_up_advice"));
        } catch (SQLException ignored) {}
        a.setCreatedAt(rs.getTimestamp("created_at"));
        return a;
    }
}
