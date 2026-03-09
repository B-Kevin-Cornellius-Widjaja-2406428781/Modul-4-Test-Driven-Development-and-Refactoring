package id.ac.ui.cs.advprog.eshop.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        String paymentId = order.getId();
        Payment payment = new Payment(paymentId, method, paymentData);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        Payment savedPayment = paymentRepository.save(payment);

        // Update Order status based on Payment status
        Order order = orderRepository.findById(payment.getId());
        if (order != null) {
            String orderStatus;
            if (PaymentStatus.SUCCESS.getValue().equals(status)) {
                orderStatus = OrderStatus.SUCCESS.getValue();
            } else if (PaymentStatus.REJECTED.getValue().equals(status)) {
                orderStatus = OrderStatus.FAILED.getValue();
            } else {
                orderStatus = order.getStatus();
            }
            
            Order updatedOrder = new Order(order.getId(), order.getProducts(), 
                    order.getOrderTime(), order.getAuthor(), orderStatus);
            orderRepository.save(updatedOrder);
        }

        return savedPayment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
