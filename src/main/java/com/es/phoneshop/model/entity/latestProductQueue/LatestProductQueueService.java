package com.es.phoneshop.model.entity.latestProductQueue;

import com.es.phoneshop.model.entity.cart.Cart;
import com.es.phoneshop.model.exceptions.OutOfStockException;

import javax.servlet.http.HttpServletRequest;

public interface LatestProductQueueService {
    LatestProductQueue getLatestProductQueue(HttpServletRequest request);
    void add(LatestProductQueue productQueue, Long productId);
}