package id.ac.ui.cs.advprog.eshop.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.eshop.exception.InvalidProductException;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.IProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private IProductRepository productRepository;

    private void validateProduct(Product product) {
        String name = product.getProductName();
        if (name == null || name.isBlank()) {
            throw new InvalidProductException("Nama produk tidak boleh kosong!");
        }
        if (product.getProductQuantity() < 0) {
            throw new InvalidProductException("Jumlah produk tidak boleh negatif!");
        }
    }

    @Override
    public Product create(Product product) {
        validateProduct(product);
        productRepository.create(product);
        return product;
    }

    @Override
    public List<Product> findAll() {
        Iterator<Product> productIterator = productRepository.findAll();
        List<Product> allProduct = new ArrayList<>();
        productIterator.forEachRemaining(allProduct::add);
        return allProduct;
    }

    @Override
    public Product findById(String id) {
        return productRepository.findById(id);
    }

    @Override
    public Product update(String id, Product updatedProduct) {
        validateProduct(updatedProduct);
        return productRepository.update(id, updatedProduct);
    }

    @Override
    public Product deleteById(String id) {
        return productRepository.deleteById(id);
    }
}
