package id.ac.ui.cs.advprog.eshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import id.ac.ui.cs.advprog.eshop.exception.InvalidProductException;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/create")
    public String createProductPage(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "createProduct";
    }

    @PostMapping("/create")
    public String createProductPost(@ModelAttribute Product product, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            service.create(product);
        } catch (InvalidProductException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("product", product);
            return "createProduct";
        }
        redirectAttributes.addFlashAttribute("success", "Produk berhasil dibuat!");
        return "redirect:list";
    }

    @GetMapping("/list")
    public String productListPage(Model model) {
        List<Product> allProducts = service.findAll();
        model.addAttribute("products", allProducts);
        return "productList";
    }

    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        Product product = service.findById(id);
        if (product == null) {
            redirectAttributes.addFlashAttribute("error", "Produk tidak ditemukan!");
            return "redirect:/product/list";
        }
        model.addAttribute("product", product);
        return "editProduct";
    }

    @PostMapping("/edit/{id}")
    public String editProductPost(@PathVariable String id, @ModelAttribute Product product, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            service.update(id, product);
        } catch (InvalidProductException e) {
            model.addAttribute("error", e.getMessage());
            return "editProduct";
        }
        redirectAttributes.addFlashAttribute("success", "Produk berhasil diupdate!");
        return "redirect:/product/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable String id, RedirectAttributes redirectAttributes) {
        Product deleted = service.deleteById(id);
        if (deleted != null) {
            redirectAttributes.addFlashAttribute("success", "Produk berhasil dihapus!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Produk tidak ditemukan!");
        }
        return "redirect:/product/list";
    }

}