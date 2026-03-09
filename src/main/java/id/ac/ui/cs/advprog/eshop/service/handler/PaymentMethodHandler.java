package id.ac.ui.cs.advprog.eshop.service.handler;

import java.util.Map;

public interface PaymentMethodHandler {
    boolean validate(Map<String, String> paymentData);
    String getInitialStatus();
}
