package id.ac.ui.cs.advprog.eshop.service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.advprog.eshop.exception.InvalidProductException;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productService;

    Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
    }

    // CREATE TESTS

    @Test
    void testCreate() {
        when(productRepository.create(product)).thenReturn(product);

        Product result = productService.create(product);

        assertNotNull(result);
        assertEquals("Sampo Cap Bambang", result.getProductName());
        verify(productRepository, times(1)).create(product);
    }

    @Test
    void testCreate_NullName() {
        Product invalidProduct = new Product();
        invalidProduct.setProductQuantity(10);

        assertThrows(InvalidProductException.class, () -> productService.create(invalidProduct));
        verify(productRepository, never()).create(any());
    }

    @Test
    void testCreate_EmptyName() {
        Product invalidProduct = new Product();
        invalidProduct.setProductName("");
        invalidProduct.setProductQuantity(10);

        assertThrows(InvalidProductException.class, () -> productService.create(invalidProduct));
        verify(productRepository, never()).create(any());
    }

    @Test
    void testCreate_NegativeQuantity() {
        Product invalidProduct = new Product();
        invalidProduct.setProductName("Valid Name");
        invalidProduct.setProductQuantity(-1);

        assertThrows(InvalidProductException.class, () -> productService.create(invalidProduct));
        verify(productRepository, never()).create(any());
    }

    // FIND ALL TESTS

    @Test
    void testFindAll() {
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        when(productRepository.findAll()).thenReturn(productList.iterator());

        List<Product> result = productService.findAll();

        assertEquals(1, result.size());
        assertEquals("Sampo Cap Bambang", result.get(0).getProductName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_Empty() {
        List<Product> emptyList = new ArrayList<>();
        when(productRepository.findAll()).thenReturn(emptyList.iterator());

        List<Product> result = productService.findAll();

        assertTrue(result.isEmpty());
    }

    // FIND BY ID TESTS

    @Test
    void testFindById_ProductExists() {
        when(productRepository.findById("eb558e9f-1c39-460e-8860-71af6af63bd6")).thenReturn(product);

        Product result = productService.findById("eb558e9f-1c39-460e-8860-71af6af63bd6");

        assertNotNull(result);
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", result.getProductId());
        assertEquals("Sampo Cap Bambang", result.getProductName());
        verify(productRepository, times(1)).findById("eb558e9f-1c39-460e-8860-71af6af63bd6");
    }

    @Test
    void testFindById_ProductNotFound() {
        when(productRepository.findById("non-existent-id")).thenReturn(null);

        Product result = productService.findById("non-existent-id");

        assertNull(result);
        verify(productRepository, times(1)).findById("non-existent-id");
    }

    // UPDATE/EDIT TESTS

    @Test
    void testUpdate_ProductExists() {
        Product updatedProduct = new Product();
        updatedProduct.setProductName("Sampo Cap Usep");
        updatedProduct.setProductQuantity(200);

        Product expectedResult = new Product();
        expectedResult.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        expectedResult.setProductName("Sampo Cap Usep");
        expectedResult.setProductQuantity(200);

        when(productRepository.update("eb558e9f-1c39-460e-8860-71af6af63bd6", updatedProduct))
                .thenReturn(expectedResult);

        Product result = productService.update("eb558e9f-1c39-460e-8860-71af6af63bd6", updatedProduct);

        assertNotNull(result);
        assertEquals("Sampo Cap Usep", result.getProductName());
        assertEquals(200, result.getProductQuantity());
        verify(productRepository, times(1)).update("eb558e9f-1c39-460e-8860-71af6af63bd6", updatedProduct);
    }

    @Test
    void testUpdate_ProductNotFound() {
        Product updatedProduct = new Product();
        updatedProduct.setProductName("Sampo Cap Usep");
        updatedProduct.setProductQuantity(200);

        when(productRepository.update("non-existent-id", updatedProduct)).thenReturn(null);

        Product result = productService.update("non-existent-id", updatedProduct);

        assertNull(result);
        verify(productRepository, times(1)).update("non-existent-id", updatedProduct);
    }

    @Test
    void testUpdate_NullName() {
        Product invalidProduct = new Product();
        invalidProduct.setProductQuantity(10);

        assertThrows(InvalidProductException.class,
                () -> productService.update("eb558e9f-1c39-460e-8860-71af6af63bd6", invalidProduct));
        verify(productRepository, never()).update(any(), any());
    }

    @Test
    void testUpdate_NegativeQuantity() {
        Product invalidProduct = new Product();
        invalidProduct.setProductName("Valid Name");
        invalidProduct.setProductQuantity(-5);

        assertThrows(InvalidProductException.class,
                () -> productService.update("eb558e9f-1c39-460e-8860-71af6af63bd6", invalidProduct));
        verify(productRepository, never()).update(any(), any());
    }

    // DELETE TESTS

    @Test
    void testDeleteById_ProductExists() {
        when(productRepository.deleteById("eb558e9f-1c39-460e-8860-71af6af63bd6")).thenReturn(product);

        Product deletedProduct = productService.deleteById("eb558e9f-1c39-460e-8860-71af6af63bd6");

        assertNotNull(deletedProduct);
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", deletedProduct.getProductId());
        verify(productRepository, times(1)).deleteById("eb558e9f-1c39-460e-8860-71af6af63bd6");
    }

    @Test
    void testDeleteById_ProductNotFound() {
        when(productRepository.deleteById("non-existent-id")).thenReturn(null);

        Product deletedProduct = productService.deleteById("non-existent-id");

        assertNull(deletedProduct);
        verify(productRepository, times(1)).deleteById("non-existent-id");
    }
}
