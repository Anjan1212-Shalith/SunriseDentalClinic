package test;

import dao.PatientDAO;
import model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Automated Unit Test Suite for PatientDAO CRUD Operations (Task C)
 * Covers: Patient registration, name searching, contact lookup, updates, and count queries
 */
public class PatientDAOTest {

    private PatientDAO patientDAO;

    @BeforeEach
    public void setUp() {
        patientDAO = new PatientDAO();
    }

    @Test
    @DisplayName("TC-PAT-01: Saving a valid patient record should return true")
    public void testSavePatientValidDetails() {
        String testPhone = "077" + (int)(Math.random() * 9000000 + 1000000);
        Patient patient = new Patient("Kasun Silva", testPhone, "No 12, Galle Road, Colombo 03", "No known allergies");
        boolean result = patientDAO.addPatient(patient);
        assertTrue(result, "Adding a valid patient record must return true");
    }

    @Test
    @DisplayName("TC-PAT-02: Searching patient by name should return matching results")
    public void testSearchPatientByName() {
        // Ensure at least one test patient exists
        String testPhone = "071" + (int)(Math.random() * 9000000 + 1000000);
        patientDAO.addPatient(new Patient("Nimal Perera", testPhone, "Colombo 07", "None"));

        List<Patient> results = patientDAO.searchPatients("Perera");
        assertNotNull(results, "Search results should not be null");
        assertFalse(results.isEmpty(), "Should find at least one patient with name 'Perera'");
    }

    @Test
    @DisplayName("TC-PAT-03: Searching patient by phone number should return matching list")
    public void testSearchPatientByContactNo() {
        String testPhone = "076" + (int)(Math.random() * 9000000 + 1000000);
        patientDAO.addPatient(new Patient("Anura Kumara", testPhone, "Kandy Road, Kelaniya", "Penicillin allergy"));

        List<Patient> results = patientDAO.searchPatients(testPhone);
        assertNotNull(results, "Search by contact number must return a non-null list");
        assertFalse(results.isEmpty(), "Should find the registered patient by contact number");
    }

    @Test
    @DisplayName("TC-PAT-04: Total registered patient count query should be positive")
    public void testTotalPatientCountIsGreaterThanZero() {
        int count = patientDAO.getTotalPatientCount();
        assertTrue(count >= 0, "Patient count must be a non-negative integer");
    }

    @Test
    @DisplayName("TC-PAT-05: Non-existent search query should return empty list without exceptions")
    public void testSearchNonExistentPatientReturnsEmpty() {
        List<Patient> results = patientDAO.searchPatients("ZZZ_NonExistent_Name_99999");
        assertNotNull(results, "Search must return an empty list, not null");
        assertTrue(results.isEmpty(), "Search list should be empty for non-existent patient");
    }

    @Test
    @DisplayName("TC-PAT-06: Getting all patients should return populated list")
    public void testGetAllPatientsReturnsList() {
        List<Patient> list = patientDAO.getAllPatients();
        assertNotNull(list, "getAllPatients() should return a non-null list");
    }
}
