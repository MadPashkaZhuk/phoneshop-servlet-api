package com.es.phoneshop.model.dao;

import com.es.phoneshop.model.entity.LatestProductQueue;
import com.es.phoneshop.model.entity.sortParams.SortField;
import com.es.phoneshop.model.entity.sortParams.SortOrder;
import com.es.phoneshop.model.exceptions.ProductNotFoundException;
import com.es.phoneshop.model.entity.Product;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id) throws ProductNotFoundException;
    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);
    LatestProductQueue getLatestProducts();
    void save(Product product);
    void delete(Long id);
    int getAmountOfProducts();
}
