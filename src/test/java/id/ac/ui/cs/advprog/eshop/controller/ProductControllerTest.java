package id.ac.ui.cs.advprog.eshop.controller;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("test-id-123");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
    }

    // CREATE PAGE TESTS

    @Test
    void testCreateProductPage() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void testCreateProductPost_Success() throws Exception {
        when(service.create(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product/create")
                .param("productName", "Sampo Cap Bambang")
                .param("productQuantity", "100"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"))
                .andExpect(flash().attribute("success", "Produk berhasil dibuat!"));

        verify(service, times(1)).create(any(Product.class));
    }

    @Test
    void testCreateProductPost_NullName() throws Exception {
        mockMvc.perform(post("/product/create")
                .param("productQuantity", "100"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"))
                .andExpect(model().attribute("error", "Nama produk tidak boleh kosong!"));

        verify(service, never()).create(any(Product.class));
    }

    @Test
    void testCreateProductPost_EmptyName() throws Exception {
        mockMvc.perform(post("/product/create")
                .param("productName", "")
                .param("productQuantity", "100"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"))
                .andExpect(model().attribute("error", "Nama produk tidak boleh kosong!"));

        verify(service, never()).create(any(Product.class));
    }

    @Test
    void testCreateProductPost_NegativeQuantity() throws Exception {
        mockMvc.perform(post("/product/create")
                .param("productName", "Valid Name")
                .param("productQuantity", "-5"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"))
                .andExpect(model().attribute("error", "Jumlah produk tidak boleh negatif!"));

        verify(service, never()).create(any(Product.class));
    }

    // LIST PAGE TESTS

    @Test
    void testProductListPage() throws Exception {
        when(service.findAll()).thenReturn(Arrays.asList(product));

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    void testProductListPage_Empty() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"));
    }

    // EDIT PAGE TESTS

    @Test
    void testEditProductPage_ProductExists() throws Exception {
        when(service.findById("test-id-123")).thenReturn(product);

        mockMvc.perform(get("/product/edit/test-id-123"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void testEditProductPage_ProductNotFound() throws Exception {
        when(service.findById("non-existent-id")).thenReturn(null);

        mockMvc.perform(get("/product/edit/non-existent-id"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"))
                .andExpect(flash().attribute("error", "Produk tidak ditemukan!"));
    }

    @Test
    void testEditProductPost_Success() throws Exception {
        when(service.update(eq("test-id-123"), any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product/edit/test-id-123")
                .param("productName", "Updated Name")
                .param("productQuantity", "200"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"))
                .andExpect(flash().attribute("success", "Produk berhasil diupdate!"));

        verify(service, times(1)).update(eq("test-id-123"), any(Product.class));
    }

    @Test
    void testEditProductPost_EmptyName() throws Exception {
        when(service.findById("test-id-123")).thenReturn(product);

        mockMvc.perform(post("/product/edit/test-id-123")
                .param("productName", "")
                .param("productQuantity", "100"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"))
                .andExpect(model().attribute("error", "Nama produk tidak boleh kosong!"));

        verify(service, never()).update(any(), any());
    }

    @Test
    void testEditProductPost_NullName() throws Exception {
        when(service.findById("test-id-123")).thenReturn(product);

        mockMvc.perform(post("/product/edit/test-id-123")
                .param("productQuantity", "100"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"))
                .andExpect(model().attribute("error", "Nama produk tidak boleh kosong!"));

        verify(service, never()).update(any(), any());
    }

    @Test
    void testEditProductPost_NegativeQuantity() throws Exception {
        when(service.findById("test-id-123")).thenReturn(product);

        mockMvc.perform(post("/product/edit/test-id-123")
                .param("productName", "Valid Name")
                .param("productQuantity", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"))
                .andExpect(model().attribute("error", "Jumlah produk tidak boleh negatif!"));

        verify(service, never()).update(any(), any());
    }

    // DELETE TESTS

    @Test
    void testDeleteProduct_Success() throws Exception {
        when(service.deleteById("test-id-123")).thenReturn(product);

        mockMvc.perform(get("/product/delete/test-id-123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"))
                .andExpect(flash().attribute("success", "Produk berhasil dihapus!"));
    }

    @Test
    void testDeleteProduct_NotFound() throws Exception {
        when(service.deleteById("non-existent-id")).thenReturn(null);

        mockMvc.perform(get("/product/delete/non-existent-id"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"))
                .andExpect(flash().attribute("error", "Produk tidak ditemukan!"));
    }
}
