package id.ac.ui.cs.advprog.eshop.model;

import java.util.Map;

import lombok.Getter;

@Getter
public class Payment {
    String id;
    String method;
    String status;
    Map<String, String> paymentData;

    public Payment(String id, String method, Map<String, String> paymentData) {
        this.id = id;
        this.method = method;
        this.paymentData = paymentData;
        this.status = "REJECTED";
    }

    public void setStatus(String status) {
        if (!"SUCCESS".equals(status) && !"REJECTED".equals(status) && !"PENDING".equals(status)) {
            throw new IllegalArgumentException();
        }
        this.status = status;
    }
}
