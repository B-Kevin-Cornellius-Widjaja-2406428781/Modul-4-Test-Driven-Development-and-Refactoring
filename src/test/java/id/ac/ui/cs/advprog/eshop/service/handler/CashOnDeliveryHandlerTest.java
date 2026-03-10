package id.ac.ui.cs.advprog.eshop.service.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;

class CashOnDeliveryHandlerTest {

    private CashOnDeliveryHandler cashOnDeliveryHandler;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        cashOnDeliveryHandler = new CashOnDeliveryHandler();
        paymentData = new HashMap<>();
    }

    @Test
    void testValidateValidCashOnDelivery() {
        paymentData.put("address", "Jl. Margonda Raya No. 100");
        paymentData.put("deliveryFee", "15000");

        assertTrue(cashOnDeliveryHandler.validate(paymentData));
    }

    @Test
    void testValidateMissingAddress() {
        paymentData.put("deliveryFee", "15000");

        assertFalse(cashOnDeliveryHandler.validate(paymentData));
    }

    @Test
    void testValidateMissingDeliveryFee() {
        paymentData.put("address", "Jl. Margonda Raya No. 100");

        assertFalse(cashOnDeliveryHandler.validate(paymentData));
    }

    @Test
    void testValidateEmptyAddress() {
        paymentData.put("address", "");
        paymentData.put("deliveryFee", "15000");

        assertFalse(cashOnDeliveryHandler.validate(paymentData));
    }

    @Test
    void testValidateEmptyDeliveryFee() {
        paymentData.put("address", "Jl. Margonda Raya No. 100");
        paymentData.put("deliveryFee", "");

        assertFalse(cashOnDeliveryHandler.validate(paymentData));
    }

    @Test
    void testValidateNullPaymentData() {
        assertFalse(cashOnDeliveryHandler.validate(null));
    }

    @Test
    void testValidateBothNull() {
        paymentData.put("address", null);
        paymentData.put("deliveryFee", null);

        assertFalse(cashOnDeliveryHandler.validate(paymentData));
    }

    @Test
    void testGetInitialStatus() {
        assertEquals("PENDING", cashOnDeliveryHandler.getInitialStatus());
    }

    @Test
    void testGetPaymentMethod() {
        assertEquals(PaymentMethod.CASH_ON_DELIVERY, cashOnDeliveryHandler.getPaymentMethod());
    }
}
