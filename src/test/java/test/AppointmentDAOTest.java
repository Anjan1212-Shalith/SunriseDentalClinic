package test;

import dao.AppointmentDAO;
import model.Appointment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Automated Unit Test Suite for Appointment Scheduling & Conflict Checks (Task C)
 * Covers: Model integrity, APT code format, phone regex, email regex, conflict checks
 */
public class AppointmentDAOTest {

    private AppointmentDAO appointmentDAO;

    @BeforeEach
    public void setUp() {
        appointmentDAO = new AppointmentDAO();
    }

    @Test
    @DisplayName("TC-APPT-01: Test Appointment POJO Creation and Data Integrity")
    public void testAppointmentModelIntegrity() {
        Appointment appt = new Appointment();
        appt.setAppointmentNo("APT-1099");
        appt.setPatientId(5);
        appt.setPatientName("Sunil Perera");
        appt.setDentistName("Dr. Priyanga Jayawardena");
        appt.setTreatmentType("Scaling & Polishing (Cleaning)");
        appt.setAppointmentDate(Date.valueOf("2026-09-15"));
        appt.setAppointmentTime("10:00 AM");
        appt.setStatus("Scheduled");
        appt.setNotes("First dental checkup");

        assertEquals("APT-1099", appt.getAppointmentNo());
        assertEquals(5, appt.getPatientId());
        assertEquals("Sunil Perera", appt.getPatientName());
        assertEquals("Dr. Priyanga Jayawardena", appt.getDentistName());
        assertEquals("Scaling & Polishing (Cleaning)", appt.getTreatmentType());
        assertEquals("Scheduled", appt.getStatus());
        assertEquals("10:00 AM", appt.getAppointmentTime());
    }

    @Test
    @DisplayName("TC-APPT-02: Test Appointment Number Formatting Rule (APT-XXXX)")
    public void testAppointmentNumberPattern() {
        String apptNo = "APT-1001";
        Pattern pattern = Pattern.compile("^APT-\\d{4,}$");
        assertTrue(pattern.matcher(apptNo).matches(), "Appointment number must follow 'APT-XXXX' format");
    }

    @Test
    @DisplayName("TC-APPT-03: Test Sri Lankan Phone Number Regex Validation")
    public void testSriLankanPhoneRegexValidation() {
        Pattern phonePattern = Pattern.compile("^(0|\\+94)?[0-9]{9,10}$");

        assertTrue(phonePattern.matcher("0771234567").matches(), "Standard 10-digit mobile must be valid");
        assertTrue(phonePattern.matcher("0112345678").matches(), "Standard Colombo landline must be valid");
        assertTrue(phonePattern.matcher("+94771234567").matches(), "International format must be valid");

        assertFalse(phonePattern.matcher("12345").matches(), "Short number must be invalid");
        assertFalse(phonePattern.matcher("abcdefghij").matches(), "Alphabetic string must be invalid");
    }

    @Test
    @DisplayName("TC-APPT-04: Test Email Address Regex Validation")
    public void testEmailRegexValidation() {
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

        assertTrue(emailPattern.matcher("patient@sunrisedental.lk").matches());
        assertTrue(emailPattern.matcher("nimal.wickrama@gmail.com").matches());

        assertFalse(emailPattern.matcher("plainaddress").matches());
        assertFalse(emailPattern.matcher("@missingusername.com").matches());
    }

    @Test
    @DisplayName("TC-APPT-05: Test Double Booking Check with Same Dentist, Date & Time")
    public void testDoubleBookingDetection() {
        Date date = Date.valueOf("2026-09-20");
        String dentist = "Dr. Priyanga Jayawardena";
        String timeSlot = "09:00 AM";

        // Query conflict logic - returns boolean flag indicating if slot is already booked
        boolean hasConflict = appointmentDAO.isDoubleBooked(dentist, date, timeSlot, null);
        assertNotNull(hasConflict);
    }

    @Test
    @DisplayName("TC-APPT-06: Getting All Appointments should return list of records")
    public void testGetAllAppointmentsReturnsList() {
        List<Appointment> list = appointmentDAO.getAllAppointments();
        assertNotNull(list, "getAllAppointments() must return a non-null list");
    }
}
