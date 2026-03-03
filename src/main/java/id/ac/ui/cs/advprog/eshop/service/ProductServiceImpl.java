package id.ac.ui.cs.advprog.eshop.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.IProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private IProductRepository productRepository;

    @Override
    public Product create(Product product) {
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
        return productRepository.update(id, updatedProduct);
    }

    @Override
    public Product deleteById(String id) {
        return productRepository.deleteById(id);
    }

    @Override
    public boolean isProductValid(Product product) {
        return getValidationError(product) == null;
    }

    @Override
    public String getValidationError(Product product) {
        String name = product.getProductName();
        if (name == null || name.isBlank()) {
            return "Nama produk tidak boleh kosong!";
        }
        if (product.getProductQuantity() < 0) {
            return "Jumlah produk tidak boleh negatif!";
        }
        return null;
    }
}
