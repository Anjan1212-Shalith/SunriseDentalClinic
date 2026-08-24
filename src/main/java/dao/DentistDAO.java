package dao;

import db.DBconnection;
import model.Dentist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Data Access Object for Dentist Profiles and Treatments
public class DentistDAO {

    // Get all dentists
    public List<Dentist> getAllDentists() {
        List<Dentist> list = new ArrayList<>();
        String sql = "SELECT dentist_id, name, specialization, consultation_fee, contact_no, available_days FROM dentists ORDER BY name ASC";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            if (con == null) return list;

            while (rs.next()) {
                Dentist d = new Dentist();
                d.setDentistId(rs.getInt("dentist_id"));
                d.setName(rs.getString("name"));
                d.setSpecialization(rs.getString("specialization"));
                d.setConsultationFee(rs.getDouble("consultation_fee"));
                d.setContactNo(rs.getString("contact_no"));
                d.setAvailableDays(rs.getString("available_days"));
                list.add(d);
            }
        } catch (SQLException e) {
            System.out.println("Get all dentists error: " + e.getMessage());
        }
        return list;
    }

    // Get dentist by name
    public Dentist getDentistByName(String name) {
        String sql = "SELECT dentist_id, name, specialization, consultation_fee, contact_no, available_days FROM dentists WHERE name = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            if (con == null) return null;

            pst.setString(1, name);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Dentist d = new Dentist();
                    d.setDentistId(rs.getInt("dentist_id"));
                    d.setName(rs.getString("name"));
                    d.setSpecialization(rs.getString("specialization"));
                    d.setConsultationFee(rs.getDouble("consultation_fee"));
                    d.setContactNo(rs.getString("contact_no"));
                    d.setAvailableDays(rs.getString("available_days"));
                    return d;
                }
            }
        } catch (SQLException e) {
            System.out.println("Get dentist by name error: " + e.getMessage());
        }
        return null;
    }

    // Get treatments list with standard pricing
    public Map<String, Double> getAllTreatmentsWithCosts() {
        Map<String, Double> map = new LinkedHashMap<>();
        String sql = "SELECT treatment_name, cost FROM treatments ORDER BY treatment_name ASC";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            if (con == null) return map;

            while (rs.next()) {
                map.put(rs.getString("treatment_name"), rs.getDouble("cost"));
            }
        } catch (SQLException e) {
            System.out.println("Get treatments error: " + e.getMessage());
        }
        return map;
    }

    // Get treatment standard fee
    public double getTreatmentCost(String treatmentName) {
        String sql = "SELECT cost FROM treatments WHERE treatment_name = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            if (con == null) return 0.0;

            pst.setString(1, treatmentName);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("cost");
                }
            }
        } catch (SQLException e) {
            System.out.println("Get treatment cost error: " + e.getMessage());
        }
        return 0.0;
    }
}
