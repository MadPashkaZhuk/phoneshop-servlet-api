package com.es.phoneshop.model.entity;

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
