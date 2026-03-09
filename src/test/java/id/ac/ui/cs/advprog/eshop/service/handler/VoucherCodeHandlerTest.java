package id.ac.ui.cs.advprog.eshop.service.handler;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VoucherCodeHandlerTest {

    private VoucherCodeHandler voucherCodeHandler;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        voucherCodeHandler = new VoucherCodeHandler();
        paymentData = new HashMap<>();
    }

    @Test
    void testValidateValidVoucherCode() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        assertTrue(voucherCodeHandler.validate(paymentData));
    }

    @Test
    void testValidateVoucherCodeTooShort() {
        paymentData.put("voucherCode", "ESHOP1234");

        assertFalse(voucherCodeHandler.validate(paymentData));
    }

    @Test
    void testValidateVoucherCodeTooLong() {
        paymentData.put("voucherCode", "ESHOP1234ABC56789");

        assertFalse(voucherCodeHandler.validate(paymentData));
    }

    @Test
    void testValidateVoucherCodeNotStartingWithESHOP() {
        paymentData.put("voucherCode", "XXXX1234ABC5678");

        assertFalse(voucherCodeHandler.validate(paymentData));
    }

    @Test
    void testValidateVoucherCodeNoNumbers() {
        paymentData.put("voucherCode", "ESHOPABCDEFGHIJ");

        assertFalse(voucherCodeHandler.validate(paymentData));
    }

    @Test
    void testValidateVoucherCodeNotEnoughNumbers() {
        paymentData.put("voucherCode", "ESHOP1234ABCD");

        assertFalse(voucherCodeHandler.validate(paymentData));
    }

    // Edge cases

    @Test
    void testValidateNullPaymentData() {
        assertFalse(voucherCodeHandler.validate(null));
    }

    @Test
    void testValidateNullVoucherCode() {
        paymentData.put("voucherCode", null);

        assertFalse(voucherCodeHandler.validate(paymentData));
    }

    @Test
    void testValidateMissingVoucherCode() {
        assertFalse(voucherCodeHandler.validate(paymentData));
    }

    // Initial status

    @Test
    void testGetInitialStatus() {
        assertEquals("PENDING", voucherCodeHandler.getInitialStatus());
    }
}
