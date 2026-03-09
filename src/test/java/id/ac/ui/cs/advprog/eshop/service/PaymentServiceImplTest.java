package id.ac.ui.cs.advprog.eshop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

        @InjectMocks
        PaymentServiceImpl paymentService;

        @Mock
        PaymentRepository paymentRepository;

        private Order order;
        private Map<String, String> paymentData;

        @BeforeEach
        void setUp() {
                List<Product> products = new ArrayList<>();
                Product product = new Product();
                product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
                product.setProductName("Sampo Cap Bambang");
                product.setProductQuantity(2);
                products.add(product);

                order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                                products, 1708560000L, "Safira Sudrajat");

                paymentData = new HashMap<>();
                paymentData.put("voucherCode", "ESHOP1234ABC5678");
        }

        @Test
        void testAddPayment() {
                Payment payment = new Payment("pay-001",
                                PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
                doReturn(payment).when(paymentRepository).save(any(Payment.class));

                Payment result = paymentService.addPayment(order,
                                PaymentMethod.VOUCHER_CODE.getValue(), paymentData);

                verify(paymentRepository, times(1)).save(any(Payment.class));
                assertNotNull(result);
        }

        @Test
        void testSetStatusToSuccess() {
                Payment payment = new Payment("pay-001",
                                PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
                payment.setStatus(PaymentStatus.PENDING.getValue());

                Payment updatedPayment = new Payment("pay-001",
                                PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
                updatedPayment.setStatus(PaymentStatus.SUCCESS.getValue());

                doReturn(payment).when(paymentRepository).save(any(Payment.class));

                Payment result = paymentService.setStatus(payment,
                                PaymentStatus.SUCCESS.getValue());

                verify(paymentRepository, times(1)).save(any(Payment.class));
                assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        }

        @Test
        void testSetStatusToRejected() {
                Payment payment = new Payment("pay-001",
                                PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
                payment.setStatus(PaymentStatus.PENDING.getValue());

                Payment updatedPayment = new Payment("pay-001",
                                PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
                updatedPayment.setStatus(PaymentStatus.REJECTED.getValue());

                doReturn(payment).when(paymentRepository).save(any(Payment.class));

                Payment result = paymentService.setStatus(payment,
                                PaymentStatus.REJECTED.getValue());

                verify(paymentRepository, times(1)).save(any(Payment.class));
                assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        }

        @Test
        void testSetStatusToInvalidStatus() {
                Payment payment = new Payment("pay-001",
                                PaymentMethod.VOUCHER_CODE.getValue(), paymentData);

                assertThrows(IllegalArgumentException.class,
                                () -> paymentService.setStatus(payment, "MEOW"));
        }

        @Test
        void testGetPaymentIfIdFound() {
                Payment payment = new Payment("pay-001",
                                PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
                doReturn(payment).when(paymentRepository).findById("pay-001");

                Payment result = paymentService.getPayment("pay-001");
                assertEquals("pay-001", result.getId());
        }

        @Test
        void testGetPaymentIfIdNotFound() {
                doReturn(null).when(paymentRepository).findById("nonexistent");

                Payment result = paymentService.getPayment("nonexistent");
                assertNull(result);
        }

        @Test
        void testGetAllPayments() {
                List<Payment> payments = new ArrayList<>();
                payments.add(new Payment("pay-001",
                                PaymentMethod.VOUCHER_CODE.getValue(), paymentData));
                payments.add(new Payment("pay-002",
                                PaymentMethod.CASH_ON_DELIVERY.getValue(), paymentData));

                doReturn(payments).when(paymentRepository).findAll();

                List<Payment> results = paymentService.getAllPayments();
                assertEquals(2, results.size());
        }
}
