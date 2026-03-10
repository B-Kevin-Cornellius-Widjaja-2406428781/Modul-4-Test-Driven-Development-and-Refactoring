package id.ac.ui.cs.advprog.eshop.service.handler;

import java.util.Map;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;

public interface PaymentMethodHandler {
    boolean validate(Map<String, String> paymentData);
    String getInitialStatus();
    PaymentMethod getPaymentMethod();
}
