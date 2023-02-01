package com.es.phoneshop.model.entity.latestProductQueue;

import com.es.phoneshop.model.entity.product.Product;

import java.util.ArrayDeque;
import java.util.Queue;

public class LatestProductQueue {
    private Queue<Product> latestProductQueue;

    public LatestProductQueue() {
        this.latestProductQueue = new ArrayDeque<>();
    }

    public Queue<Product> getQueue() {
        return latestProductQueue;
    }
}
