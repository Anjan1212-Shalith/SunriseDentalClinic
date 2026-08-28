package test;

import dao.DentistDAO;
import model.Dentist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Automated Unit Test Suite for Dentist & Treatment Lookups (Task C)
 * Covers: Doctor schedules, consultation fee lookups, treatment costs, and fallback defaults
 */
public class DentistAndTreatmentTest {

    private DentistDAO dentistDAO;

    @BeforeEach
    public void setUp() {
        dentistDAO = new DentistDAO();
    }

    @Test
    @DisplayName("TC-DOC-01: Retrieving all dentists should return populated list")
    public void testGetAllDentistsReturnsList() {
        List<Dentist> list = dentistDAO.getAllDentists();
        assertNotNull(list, "Dentist list should not be null");
        assertFalse(list.isEmpty(), "Dentist list should contain doctors from database");
    }

    @Test
    @DisplayName("TC-DOC-02: Finding doctor by valid name should return doctor details")
    public void testGetDentistByNameFound() {
        Dentist d = dentistDAO.getDentistByName("Dr. Priyanga Jayawardena");
        assertNotNull(d, "Should find Dr. Priyanga Jayawardena");
        assertEquals("Consultant Orthodontist", d.getSpecialization());
        assertTrue(d.getConsultationFee() > 0, "Consultation fee must be greater than zero");
    }

    @Test
    @DisplayName("TC-DOC-03: Searching non-existent doctor should return null")
    public void testGetDentistByNameNotFoundReturnsNull() {
        Dentist d = dentistDAO.getDentistByName("Dr. NonExistent FakeDoctor");
        assertNull(d, "Non-existent doctor search must return null");
    }

    @Test
    @DisplayName("TC-DOC-04: Retrieving all treatments with costs should return non-empty map")
    public void testGetAllTreatmentsWithCosts() {
        Map<String, Double> map = dentistDAO.getAllTreatmentsWithCosts();
        assertNotNull(map, "Treatments map should not be null");
        assertFalse(map.isEmpty(), "Treatments map should contain treatments");
    }

    @Test
    @DisplayName("TC-DOC-05: Non-existent treatment lookup should return default 0.00 cost")
    public void testUnknownTreatmentCostReturnsZero() {
        double cost = dentistDAO.getTreatmentCost("NonExistent Procedure 999");
        assertEquals(0.0, cost, 0.001, "Unknown treatment cost should safely return 0.00");
    }
}
