package id.ac.ui.cs.advprog.eshop.repository;

import java.util.Iterator;

import id.ac.ui.cs.advprog.eshop.model.Product;

public interface IProductRepository {
    Product create(Product product);

    Iterator<Product> findAll();

    Product findById(String id);

    Product deleteById(String id);

    Product update(String id, Product updatedProduct);
}
