package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        this.paymentData = new HashMap<>();
    }

    @Test
    void testCreatePayment() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment("pay-001", "VOUCHER_CODE", paymentData);

        assertEquals("pay-001", payment.getId());
        assertEquals("VOUCHER_CODE", payment.getMethod());
        assertEquals(paymentData, payment.getPaymentData());
    }

    @Test
    void testCreatePaymentDefaultStatus() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment("pay-001", "VOUCHER_CODE", paymentData);

        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testSetStatusToSuccess() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment("pay-001", "VOUCHER_CODE", paymentData);
        payment.setStatus("SUCCESS");

        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testSetStatusToInvalidStatus() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment("pay-001", "VOUCHER_CODE", paymentData);

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setStatus("MEOW");
        });
    }

    @Test
    void testSetStatusToPending() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment("pay-001", "VOUCHER_CODE", paymentData);
        payment.setStatus("PENDING");

        assertEquals("PENDING", payment.getStatus());
    }
}
