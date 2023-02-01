package com.es.phoneshop.model.entity.latestProductQueue;

import com.es.phoneshop.model.dao.ProductDao;
import com.es.phoneshop.model.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.entity.product.Product;

import javax.servlet.http.HttpServletRequest;

public class DefaultLatestProductQueueService implements LatestProductQueueService {
    private static final String LATEST_PRODUCT_QUEUE_SESSION_ATTRIBUTE = DefaultLatestProductQueueService.class.getName() + ".queue";
    private ProductDao productDao;
    private static class SingletonHelper {
        private static final DefaultLatestProductQueueService INSTANCE = new DefaultLatestProductQueueService();
    }

    public static DefaultLatestProductQueueService getInstance() {
        return DefaultLatestProductQueueService.SingletonHelper.INSTANCE;
    }

    private DefaultLatestProductQueueService() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public LatestProductQueue getLatestProductQueue(HttpServletRequest request) {
        synchronized (request.getSession()) {
            LatestProductQueue queue = (LatestProductQueue) request.getSession().getAttribute(LATEST_PRODUCT_QUEUE_SESSION_ATTRIBUTE);
            if (queue == null) {
                request.getSession().setAttribute(LATEST_PRODUCT_QUEUE_SESSION_ATTRIBUTE, queue = new LatestProductQueue());
            }
            return queue;
        }
    }

    @Override
    public synchronized void add(LatestProductQueue queue, Long productId) {
        Product product = productDao.getProduct(productId);
        if(queue.getQueue().contains(product)) {
            return;
        }
        if (queue.getQueue().size() == 3) {
            queue.getQueue().remove();
        }
        queue.getQueue().add(product);
    }
}
