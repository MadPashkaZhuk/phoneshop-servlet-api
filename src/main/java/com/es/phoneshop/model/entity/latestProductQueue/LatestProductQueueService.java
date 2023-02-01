package com.es.phoneshop.model.entity.latestProductQueue;

import javax.servlet.http.HttpServletRequest;

public interface LatestProductQueueService {
    LatestProductQueue getLatestProductQueue(HttpServletRequest request);
    void add(LatestProductQueue productQueue, Long productId);
}