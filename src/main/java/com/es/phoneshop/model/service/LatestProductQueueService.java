package com.es.phoneshop.model.service;

import com.es.phoneshop.model.entity.LatestProductQueue;

import javax.servlet.http.HttpServletRequest;

public interface LatestProductQueueService {
    LatestProductQueue getLatestProductQueue(HttpServletRequest request);
    void add(LatestProductQueue productQueue, Long productId);
}