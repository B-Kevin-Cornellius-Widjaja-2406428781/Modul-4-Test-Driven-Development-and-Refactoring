package id.ac.ui.cs.advprog.eshop.model;

import java.util.Map;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import lombok.Getter;

@Getter
public class Payment {
    String id;
    String method;
    String status;
    Map<String, String> paymentData;

    public Payment(String id, String method, Map<String, String> paymentData) {
        if (!PaymentMethod.contains(method)) {
            throw new IllegalArgumentException();
        }
        this.id = id;
        this.method = method;
        this.paymentData = paymentData;

        // Validate voucher code for VOUCHER_CODE payment method
        if (PaymentMethod.VOUCHER_CODE.getValue().equals(method)) {
            this.status = validateVoucherCode(paymentData)
                    ? PaymentStatus.SUCCESS.getValue()
                    : PaymentStatus.REJECTED.getValue();
        } else {
            this.status = PaymentStatus.PENDING.getValue();
        }
    }

    private boolean validateVoucherCode(Map<String, String> paymentData) {
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

    public void setStatus(String status) {
        if (!PaymentStatus.contains(status)) {
            throw new IllegalArgumentException();
        }
        this.status = status;
    }
}
