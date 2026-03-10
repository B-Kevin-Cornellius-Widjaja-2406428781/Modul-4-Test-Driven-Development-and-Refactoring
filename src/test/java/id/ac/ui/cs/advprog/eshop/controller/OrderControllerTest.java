package id.ac.ui.cs.advprog.eshop.controller;

import java.util.Arrays;
import java.util.Collections;

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

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import id.ac.ui.cs.advprog.eshop.service.ProductService;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private PaymentService paymentService;

    private Product product;
    private Order order;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("test-product-id");
        product.setProductName("Test Product");
        product.setProductQuantity(10);

        order = new Order("test-order-id", Arrays.asList(product), System.currentTimeMillis() / 1000, "Test User",
                "WAITING_PAYMENT");
    }

    // CREATE PAGE TEST

    @Test
    void testCreateOrderPage() throws Exception {
        when(productService.findAll()).thenReturn(Arrays.asList(product));

        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createOrder"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("author"));
    }

    @Test
    void testCreateOrderPost_Success() throws Exception {
        when(productService.findById(any())).thenReturn(product);
        when(orderService.createOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/order/create")
                .param("author", "Test User")
                .param("selectedProducts", "test-product-id"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/history"));

        verify(orderService, times(1)).createOrder(any(Order.class));
    }

    @Test
    void testCreateOrderPost_NoProductSelected() throws Exception {
        when(productService.findAll()).thenReturn(Arrays.asList(product));

        mockMvc.perform(post("/order/create")
                .param("author", "Test User")
                .param("selectedProducts", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("createOrder"));
    }

    @Test
    void testCreateOrderPost_ProductNotFound() throws Exception {
        // When productService.findById returns null for selected product
        when(productService.findById("nonexistent")).thenReturn(null);
        when(productService.findAll()).thenReturn(Arrays.asList(product));

        mockMvc.perform(post("/order/create")
                .param("author", "Test User")
                .param("selectedProducts", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(view().name("createOrder"))
                .andExpect(model().attributeExists("error"));
    }

    // HISTORY PAGE TEST

    @Test
    void testOrderHistoryPage() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderHistory"));
    }

    @Test
    void testOrderHistoryPost() throws Exception {
        when(orderService.findAllByAuthor("Test User")).thenReturn(Arrays.asList(order));

        mockMvc.perform(post("/order/history")
                .param("author", "Test User"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderHistory"))
                .andExpect(model().attributeExists("orders"));
    }

    @Test
    void testOrderHistoryPost_EmptyResult() throws Exception {
        when(orderService.findAllByAuthor("Unknown")).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/order/history")
                .param("author", "Unknown"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderHistory"));
    }

    // PAY PAGE TEST

    @Test
    void testPayOrderPage_Success() throws Exception {
        when(orderService.findById("test-order-id")).thenReturn(order);

        mockMvc.perform(get("/order/pay/test-order-id"))
                .andExpect(status().isOk())
                .andExpect(view().name("payOrder"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    void testPayOrderPage_OrderNotFound() throws Exception {
        when(orderService.findById("invalid-id")).thenReturn(null);

        mockMvc.perform(get("/order/pay/invalid-id"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/history"));
    }

    @Test
    void testPayOrderPost_VoucherCode_Success() throws Exception {
        when(orderService.findById("test-order-id")).thenReturn(order);

        Payment payment = new Payment("test-order-id", "VOUCHER_CODE", java.util.Collections.emptyMap());
        payment.setStatus("SUCCESS");
        when(paymentService.addPayment(any(Order.class), eq("VOUCHER_CODE"), any())).thenReturn(payment);

        mockMvc.perform(post("/order/pay/test-order-id")
                .param("method", "VOUCHER_CODE")
                .param("voucherCode", "ESHOP1234ABC5678"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentSuccess"))
                .andExpect(model().attributeExists("paymentId"))
                .andExpect(model().attributeExists("status"));
    }

    @Test
    void testPayOrderPost_VoucherCode_Invalid() throws Exception {
        when(orderService.findById("test-order-id")).thenReturn(order);

        Payment payment = new Payment("test-order-id", "VOUCHER_CODE", java.util.Collections.emptyMap());
        payment.setStatus("REJECTED");
        when(paymentService.addPayment(any(Order.class), eq("VOUCHER_CODE"), any())).thenReturn(payment);

        mockMvc.perform(post("/order/pay/test-order-id")
                .param("method", "VOUCHER_CODE")
                .param("voucherCode", "INVALID"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentSuccess"))
                .andExpect(model().attribute("status", "REJECTED"));
    }

    @Test
    void testPayOrderPost_CashOnDelivery_Success() throws Exception {
        when(orderService.findById("test-order-id")).thenReturn(order);

        Payment payment = new Payment("test-order-id", "CASH_ON_DELIVERY", java.util.Collections.emptyMap());
        payment.setStatus("PENDING");
        when(paymentService.addPayment(any(Order.class), eq("CASH_ON_DELIVERY"), any())).thenReturn(payment);

        mockMvc.perform(post("/order/pay/test-order-id")
                .param("method", "CASH_ON_DELIVERY")
                .param("address", "Jl. Test No. 123")
                .param("deliveryFee", "15000"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentSuccess"))
                .andExpect(model().attribute("status", "PENDING"));
    }

    @Test
    void testPayOrderPost_OrderNotFound() throws Exception {
        when(orderService.findById("invalid-id")).thenReturn(null);

        mockMvc.perform(post("/order/pay/invalid-id")
                .param("method", "VOUCHER_CODE")
                .param("voucherCode", "ESHOP1234ABC5678"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/history"));
    }

    @Test
    void testPayOrderPost_VoucherCode_NullVoucherCode() throws Exception {
        // Test when voucherCode is null for VOUCHER_CODE method
        when(orderService.findById("test-order-id")).thenReturn(order);

        Payment payment = new Payment("test-order-id", "VOUCHER_CODE", java.util.Collections.emptyMap());
        payment.setStatus("SUCCESS");
        when(paymentService.addPayment(any(Order.class), eq("VOUCHER_CODE"), any())).thenReturn(payment);

        mockMvc.perform(post("/order/pay/test-order-id")
                .param("method", "VOUCHER_CODE"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentSuccess"));
    }

    @Test
    void testPayOrderPost_CashOnDelivery_NullAddress() throws Exception {
        // Test when address is null for COD method
        when(orderService.findById("test-order-id")).thenReturn(order);

        Payment payment = new Payment("test-order-id", "CASH_ON_DELIVERY", java.util.Collections.emptyMap());
        payment.setStatus("PENDING");
        when(paymentService.addPayment(any(Order.class), eq("CASH_ON_DELIVERY"), any())).thenReturn(payment);

        mockMvc.perform(post("/order/pay/test-order-id")
                .param("method", "CASH_ON_DELIVERY")
                .param("deliveryFee", "15000"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentSuccess"));
    }

    @Test
    void testPayOrderPost_CashOnDelivery_NullDeliveryFee() throws Exception {
        // Test when deliveryFee is null for COD method
        when(orderService.findById("test-order-id")).thenReturn(order);

        Payment payment = new Payment("test-order-id", "CASH_ON_DELIVERY", java.util.Collections.emptyMap());
        payment.setStatus("PENDING");
        when(paymentService.addPayment(any(Order.class), eq("CASH_ON_DELIVERY"), any())).thenReturn(payment);

        mockMvc.perform(post("/order/pay/test-order-id")
                .param("method", "CASH_ON_DELIVERY")
                .param("address", "Jl. Test No. 123"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentSuccess"));
    }
}
