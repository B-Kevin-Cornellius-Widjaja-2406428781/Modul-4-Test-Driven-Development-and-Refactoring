package id.ac.ui.cs.advprog.eshop.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        // TODO: Implement addPayment logic
        return null;
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        // TODO: Implement setStatus logic
        return null;
    }

    @Override
    public Payment getPayment(String paymentId) {
        // TODO: Implement getPayment logic
        return null;
    }

    @Override
    public List<Payment> getAllPayments() {
        // TODO: Implement getAllPayments logic
        return null;
    }
}
