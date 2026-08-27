package test;

import model.Bill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Automated Unit Test Suite for Billing & Calculation Logic (Task C)
 * Tests TDD requirements, edge cases, boundary values, and currency calculations.
 */
public class BillCalculatorTest {

    private Bill testBill;

    @BeforeEach
    public void setUp() {
        testBill = new Bill();
    }

    @Test
    @DisplayName("TC-BILL-01: Standard Bill Calculation (Treatment + Consultation - Discount)")
    public void testStandardBillCalculation() {
        testBill.setTreatmentFee(3500.00);      // Cleaning
        testBill.setConsultationFee(2500.00);   // Dr. Priyanga
        testBill.setDiscount(500.00);          // Promotional discount
        
        testBill.calculateTotal();

        // Subtotal = 6000.00, Discount = 500.00 -> Net Total = 5500.00
        assertEquals(5500.00, testBill.getTotalAmount(), 0.001, "Standard total calculation should equal 5500.00");
    }

    @Test
    @DisplayName("TC-BILL-02: Zero Discount Boundary Condition")
    public void testZeroDiscountCalculation() {
        testBill.setTreatmentFee(18000.00);     // Root Canal Treatment
        testBill.setConsultationFee(3000.00);    // Specialist Fee
        testBill.setDiscount(0.00);             // No discount
        
        testBill.calculateTotal();

        assertEquals(21000.00, testBill.getTotalAmount(), 0.001, "Total with zero discount should be exactly sum of fees");
    }

    @Test
    @DisplayName("TC-BILL-03: High-Discount Boundary (Total should not drop below 0)")
    public void testExcessDiscountBoundary() {
        testBill.setTreatmentFee(4000.00);
        testBill.setConsultationFee(1500.00);
        testBill.setDiscount(10000.00); // Discount exceeds total fees
        
        testBill.calculateTotal();

        assertEquals(0.00, testBill.getTotalAmount(), 0.001, "Total amount payable cannot be negative");
    }

    @Test
    @DisplayName("TC-BILL-04: Consultation Only (Treatment Fee = 0.00)")
    public void testConsultationOnlyCalculation() {
        testBill.setTreatmentFee(0.00);
        testBill.setConsultationFee(2500.00);
        testBill.setDiscount(0.00);
        
        testBill.calculateTotal();

        assertEquals(2500.00, testBill.getTotalAmount(), 0.001);
    }

    @Test
    @DisplayName("TC-BILL-05: Floating-Point Precision for LKR Currency Decimals")
    public void testDecimalPrecision() {
        testBill.setTreatmentFee(4500.75);
        testBill.setConsultationFee(2200.50);
        testBill.setDiscount(250.25);
        
        testBill.calculateTotal();

        // 4500.75 + 2200.50 = 6701.25 - 250.25 = 6451.00
        assertEquals(6451.00, testBill.getTotalAmount(), 0.001);
    }

    @Test
    @DisplayName("TC-BILL-06: High Value Dental Implant Bill Calculation")
    public void testHighValueSurgeryBillCalculation() {
        testBill.setTreatmentFee(45000.00);
        testBill.setConsultationFee(3000.00);
        testBill.setDiscount(5000.00);

        testBill.calculateTotal();

        // 45000 + 3000 = 48000 - 5000 = 43000.00
        assertEquals(43000.00, testBill.getTotalAmount(), 0.001, "High value surgery bill calculation should equal 43000.00");
    }
}
