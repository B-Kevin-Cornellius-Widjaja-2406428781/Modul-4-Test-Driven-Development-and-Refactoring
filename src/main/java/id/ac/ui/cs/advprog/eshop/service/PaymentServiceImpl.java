package id.ac.ui.cs.advprog.eshop.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import id.ac.ui.cs.advprog.eshop.service.handler.PaymentMethodHandler;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private List<PaymentMethodHandler> handlers;

    private Map<PaymentMethod, PaymentMethodHandler> handlerMap = new HashMap<>();

    @Autowired
    public void initHandlerMap() {
        for (PaymentMethodHandler handler : handlers) {
            handlerMap.put(handler.getPaymentMethod(), handler);
        }
    }

    // For testing purposes
    public void setHandlerMap(Map<PaymentMethod, PaymentMethodHandler> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        String paymentId = order.getId();
        Payment payment = new Payment(paymentId, method, paymentData);

        // Find handler for this payment method
        PaymentMethod paymentMethod = PaymentMethod.valueOf(method);
        PaymentMethodHandler handler = handlerMap.get(paymentMethod);

        if (handler != null) {
            if (handler.validate(paymentData)) {
                payment.setStatus(handler.getInitialStatus());
            } else {
                payment.setStatus(PaymentStatus.REJECTED.getValue());
            }
        }

        Payment savedPayment = paymentRepository.save(payment);

        // Update Order status based on Payment status
        updateOrderStatus(savedPayment);

        return savedPayment;
    }

    private void updateOrderStatus(Payment payment) {
        Order order = orderRepository.findById(payment.getId());
        if (order != null) {
            String orderStatus;
            if (PaymentStatus.SUCCESS.getValue().equals(payment.getStatus())) {
                orderStatus = OrderStatus.SUCCESS.getValue();
            } else if (PaymentStatus.REJECTED.getValue().equals(payment.getStatus())) {
                orderStatus = OrderStatus.FAILED.getValue();
            } else {
                orderStatus = order.getStatus();
            }
            Order updatedOrder = new Order(order.getId(), order.getProducts(),
                    order.getOrderTime(), order.getAuthor(), orderStatus);
            orderRepository.save(updatedOrder);
        }
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        Payment savedPayment = paymentRepository.save(payment);

        // Update Order status based on Payment status
        updateOrderStatus(savedPayment);

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
