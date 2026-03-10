package id.ac.ui.cs.advprog.eshop.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    private Payment payment;

    @BeforeEach
    void setUp() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        payment = new Payment("test-payment-id", "VOUCHER_CODE", paymentData);
        payment.setStatus("SUCCESS");
    }

    // DETAIL FORM TEST

    @Test
    void testPaymentDetailForm() throws Exception {
        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentDetailForm"))
                .andExpect(model().attributeExists("paymentId"));
    }

    // DETAIL PAGE TEST

    @Test
    void testPaymentDetail_Found() throws Exception {
        when(paymentService.getPayment("test-payment-id")).thenReturn(payment);

        mockMvc.perform(get("/payment/detail/test-payment-id"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentDetail"))
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void testPaymentDetail_NotFound() throws Exception {
        when(paymentService.getPayment("invalid-id")).thenReturn(null);

        mockMvc.perform(get("/payment/detail/invalid-id"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentDetailForm"))
                .andExpect(model().attribute("error", "Payment not found"));
    }

    // ADMIN LIST TEST

    @Test
    void testPaymentAdminList() throws Exception {
        when(paymentService.getAllPayments()).thenReturn(Arrays.asList(payment));

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminList"))
                .andExpect(model().attributeExists("payments"));
    }

    @Test
    void testPaymentAdminList_Empty() throws Exception {
        when(paymentService.getAllPayments()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminList"))
                .andExpect(model().attribute("payments", Collections.emptyList()));
    }

    // ADMIN DETAIL TEST

    @Test
    void testPaymentAdminDetail_Found() throws Exception {
        when(paymentService.getPayment("test-payment-id")).thenReturn(payment);

        mockMvc.perform(get("/payment/admin/detail/test-payment-id"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminDetail"))
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void testPaymentAdminDetail_NotFound() throws Exception {
        when(paymentService.getPayment("invalid-id")).thenReturn(null);

        mockMvc.perform(get("/payment/admin/detail/invalid-id"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/list"));
    }

    // SET STATUS TEST

    @Test
    void testSetPaymentStatus_Success() throws Exception {
        when(paymentService.getPayment("test-payment-id")).thenReturn(payment);
        when(paymentService.setStatus(any(Payment.class), eq("SUCCESS"))).thenReturn(payment);

        mockMvc.perform(post("/payment/admin/set-status/test-payment-id")
                .param("status", "SUCCESS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/detail/test-payment-id"));

        verify(paymentService, times(1)).setStatus(any(Payment.class), eq("SUCCESS"));
    }

    @Test
    void testSetPaymentStatus_Rejected() throws Exception {
        when(paymentService.getPayment("test-payment-id")).thenReturn(payment);
        when(paymentService.setStatus(any(Payment.class), eq("REJECTED"))).thenReturn(payment);

        mockMvc.perform(post("/payment/admin/set-status/test-payment-id")
                .param("status", "REJECTED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/detail/test-payment-id"));

        verify(paymentService, times(1)).setStatus(any(Payment.class), eq("REJECTED"));
    }

    @Test
    void testSetPaymentStatus_NotFound() throws Exception {
        when(paymentService.getPayment("invalid-id")).thenReturn(null);

        mockMvc.perform(post("/payment/admin/set-status/invalid-id")
                .param("status", "SUCCESS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/list"));
    }
}
