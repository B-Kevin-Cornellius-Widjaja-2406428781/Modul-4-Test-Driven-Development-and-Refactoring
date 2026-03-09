package id.ac.ui.cs.advprog.eshop.service.handler;

import java.util.Map;

import org.springframework.stereotype.Component;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;

@Component
public class VoucherCodeHandler implements PaymentMethodHandler {

    @Override
    public boolean validate(Map<String, String> paymentData) {
        if (paymentData == null) {
            return false;
        }

        String voucherCode = paymentData.get("voucherCode");
        if (voucherCode == null) {
            return false;
        }

        // Rule 1: Must be 16 characters long
        if (voucherCode.length() != 16) {
            return false;
        }

        // Rule 2: Must start with "ESHOP"
        if (!voucherCode.startsWith("ESHOP")) {
            return false;
        }

        // Rule 3: Must contain 8 numerical characters
        int numericCount = 0;
        for (char c : voucherCode.toCharArray()) {
            if (Character.isDigit(c)) {
                numericCount++;
            }
        }
        return numericCount == 8;
    }

    @Override
    public String getInitialStatus() {
        // Voucher code: validated -> SUCCESS if valid
        return PaymentStatus.SUCCESS.getValue();
    }

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.VOUCHER_CODE;
    }
}
