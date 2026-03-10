package id.ac.ui.cs.advprog.eshop.service.handler;

import java.util.Map;

import org.springframework.stereotype.Component;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;

@Component
public class CashOnDeliveryHandler implements PaymentMethodHandler {

    @Override
    public boolean validate(Map<String, String> paymentData) {
        if (paymentData == null) {
            return false;
        }

        String address = paymentData.get("address");
        String deliveryFee = paymentData.get("deliveryFee");

        // Both must be present and not empty
        return address != null && !address.isEmpty()
                && deliveryFee != null && !deliveryFee.isEmpty();
    }

    @Override
    public String getInitialStatus() {
        return PaymentStatus.PENDING.getValue();
    }

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.CASH_ON_DELIVERY;
    }
}
