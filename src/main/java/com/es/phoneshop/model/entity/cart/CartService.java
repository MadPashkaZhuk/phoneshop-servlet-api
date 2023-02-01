package com.es.phoneshop.model.entity.cart;

import com.es.phoneshop.model.exceptions.OutOfStockException;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest request);
    void add(Cart cart, Long productId, int quantity) throws OutOfStockException;
}
