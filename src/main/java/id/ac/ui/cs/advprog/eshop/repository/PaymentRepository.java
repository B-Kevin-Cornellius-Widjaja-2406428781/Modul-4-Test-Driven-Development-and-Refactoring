package id.ac.ui.cs.advprog.eshop.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;

@Repository
public class PaymentRepository {

    private List<Payment> paymentData = new ArrayList<>();

    public Payment save(Payment payment) {
        // TODO: Implement save logic
        return null;
    }

    public Payment findById(String id) {
        // TODO: Implement findById logic
        return null;
    }

    public List<Payment> findAll() {
        // TODO: Implement findAll logic
        return null;
    }
}
