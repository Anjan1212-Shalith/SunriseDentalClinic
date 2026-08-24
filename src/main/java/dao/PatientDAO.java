package dao;

import db.DBconnection;
import model.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Data Access Object for Patient management
public class PatientDAO {

    // Add a new patient record
    public boolean addPatient(Patient patient) {
        String sql = "INSERT INTO patients (name, contact_no, address, email, medical_history) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            if (con == null) return false;

            pst.setString(1, patient.getName());
            pst.setString(2, patient.getContactNo());
            pst.setString(3, patient.getAddress());
            pst.setString(4, patient.getEmail());
            pst.setString(5, patient.getMedicalHistory());

            int affected = pst.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        patient.setPatientId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Add patient error: " + e.getMessage());
        }
        return false;
    }

    // Update patient details
    public boolean updatePatient(Patient patient) {
        String sql = "UPDATE patients SET name = ?, contact_no = ?, address = ?, email = ?, medical_history = ? WHERE patient_id = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            if (con == null) return false;

            pst.setString(1, patient.getName());
            pst.setString(2, patient.getContactNo());
            pst.setString(3, patient.getAddress());
            pst.setString(4, patient.getEmail());
            pst.setString(5, patient.getMedicalHistory());
            pst.setInt(6, patient.getPatientId());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update patient error: " + e.getMessage());
        }
        return false;
    }

    // Delete patient by ID
    public boolean deletePatient(int patientId) {
        String sql = "DELETE FROM patients WHERE patient_id = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            if (con == null) return false;

            pst.setInt(1, patientId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Delete patient error: " + e.getMessage());
        }
        return false;
    }

    // Get patient details by ID
    public Patient getPatientById(int patientId) {
        String sql = "SELECT patient_id, name, contact_no, address, email, medical_history, created_at FROM patients WHERE patient_id = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            if (con == null) return null;

            pst.setInt(1, patientId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return extractPatientFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Get patient by id error: " + e.getMessage());
        }
        return null;
    }

    // Get list of all patients
    public List<Patient> getAllPatients() {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT patient_id, name, contact_no, address, email, medical_history, created_at FROM patients ORDER BY patient_id DESC";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            if (con == null) return list;

            while (rs.next()) {
                list.add(extractPatientFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Get all patients error: " + e.getMessage());
        }
        return list;
    }

    // Search patients by name or contact number
    public List<Patient> searchPatients(String keyword) {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT patient_id, name, contact_no, address, email, medical_history, created_at FROM patients " +
                     "WHERE name LIKE ? OR contact_no LIKE ? OR email LIKE ? OR address LIKE ? ORDER BY patient_id DESC";
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
                    list.add(extractPatientFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Search patients error: " + e.getMessage());
        }
        return list;
    }

    // Get total registered patient count
    public int getTotalPatientCount() {
        String sql = "SELECT COUNT(*) FROM patients";
        try (Connection con = DBconnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Get patient count error: " + e.getMessage());
        }
        return 0;
    }

    // Helper method to extract patient model from ResultSet
    private Patient extractPatientFromResultSet(ResultSet rs) throws SQLException {
        Patient p = new Patient();
        p.setPatientId(rs.getInt("patient_id"));
        p.setName(rs.getString("name"));
        p.setContactNo(rs.getString("contact_no"));
        p.setAddress(rs.getString("address"));
        p.setEmail(rs.getString("email"));
        p.setMedicalHistory(rs.getString("medical_history"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        return p;
    }
}
