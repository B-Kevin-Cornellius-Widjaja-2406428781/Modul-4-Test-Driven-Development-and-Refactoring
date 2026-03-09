package id.ac.ui.cs.advprog.eshop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Payment;

class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
        paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testSaveCreate() {
        Payment payment = new Payment("pay-001", 
                PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
        
        Payment result = paymentRepository.save(payment);

        Payment findResult = paymentRepository.findById("pay-001");
        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getId(), findResult.getId());
        assertEquals(payment.getMethod(), findResult.getMethod());
        assertEquals(payment.getStatus(), findResult.getStatus());
    }

    @Test
    void testSaveUpdate() {
        Payment payment = new Payment("pay-001", 
                PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
        paymentRepository.save(payment);
        
        Payment newPayment = new Payment("pay-001", 
                PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
        newPayment.setStatus(PaymentStatus.SUCCESS.getValue());
        
        Payment result = paymentRepository.save(newPayment);

        Payment findResult = paymentRepository.findById("pay-001");
        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getId(), findResult.getId());
        assertEquals(PaymentStatus.SUCCESS.getValue(), findResult.getStatus());
    }

    @Test
    void testFindByIdIfIdFound() {
        Payment payment = new Payment("pay-001", 
                PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
        paymentRepository.save(payment);

        Payment findResult = paymentRepository.findById("pay-001");
        assertEquals("pay-001", findResult.getId());
        assertEquals(PaymentMethod.VOUCHER_CODE.getValue(), findResult.getMethod());
    }

    @Test
    void testFindByIdIfIdNotFound() {
        Payment payment = new Payment("pay-001", 
                PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
        paymentRepository.save(payment);

        Payment findResult = paymentRepository.findById("nonexistent");
        assertNull(findResult);
    }

    @Test
    void testFindAll() {
        Payment payment1 = new Payment("pay-001", 
                PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
        Payment payment2 = new Payment("pay-002", 
                PaymentMethod.CASH_ON_DELIVERY.getValue(), paymentData);
        
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        List<Payment> paymentList = paymentRepository.findAll();
        assertEquals(2, paymentList.size());
    }

    @Test
    void testFindAllIfEmpty() {
        List<Payment> paymentList = paymentRepository.findAll();
        assertTrue(paymentList.isEmpty());
    }
}
