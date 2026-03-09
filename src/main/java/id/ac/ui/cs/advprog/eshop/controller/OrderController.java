package id.ac.ui.cs.advprog.eshop.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import id.ac.ui.cs.advprog.eshop.service.ProductService;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/create")
    public String createOrderPage(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        model.addAttribute("author", "");
        return "createOrder";
    }

    @PostMapping("/create")
    public String createOrderPost(@RequestParam String author, 
                                  @RequestParam List<String> selectedProducts,
                                  Model model) {
        try {
            List<Product> products = new ArrayList<>();
            for (String productId : selectedProducts) {
                Product product = productService.findById(productId);
                if (product != null) {
                    products.add(product);
                }
            }

            if (products.isEmpty()) {
                model.addAttribute("error", "Please select at least one product");
                model.addAttribute("products", productService.findAll());
                return "createOrder";
            }

            Order order = new Order(UUID.randomUUID().toString(), 
                    products, System.currentTimeMillis() / 1000, author);
            orderService.createOrder(order);
            
            return "redirect:/order/history";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Please select at least one product");
            model.addAttribute("products", productService.findAll());
            return "createOrder";
        }
    }

    @GetMapping("/history")
    public String orderHistoryPage(Model model) {
        return "orderHistory";
    }

    @PostMapping("/history")
    public String orderHistoryPost(@RequestParam String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        return "orderHistory";
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderPage(@PathVariable String orderId, Model model) {
        Order order = orderService.findById(orderId);
        if (order == null) {
            model.addAttribute("error", "Order not found");
            return "redirect:/order/history";
        }
        model.addAttribute("order", order);
        
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("method", "");
        model.addAttribute("paymentData", paymentData);
        return "payOrder";
    }

    @PostMapping("/pay/{orderId}")
    public String payOrderPost(@PathVariable String orderId,
                               @RequestParam String method,
                               @RequestParam(required = false) String voucherCode,
                               @RequestParam(required = false) String address,
                               @RequestParam(required = false) String deliveryFee,
                               Model model) {
        Order order = orderService.findById(orderId);
        if (order == null) {
            model.addAttribute("error", "Order not found");
            return "redirect:/order/history";
        }

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("method", method);
        
        if ("VOUCHER_CODE".equals(method) && voucherCode != null) {
            paymentData.put("voucherCode", voucherCode);
        } else if ("CASH_ON_DELIVERY".equals(method)) {
            paymentData.put("address", address != null ? address : "");
            paymentData.put("deliveryFee", deliveryFee != null ? deliveryFee : "");
        }

        Payment payment = paymentService.addPayment(order, method, paymentData);
        
        model.addAttribute("paymentId", payment.getId());
        model.addAttribute("orderId", orderId);
        model.addAttribute("status", payment.getStatus());
        
        return "paymentSuccess";
    }
}
