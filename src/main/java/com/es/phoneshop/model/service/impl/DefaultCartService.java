package com.es.phoneshop.model.service.impl;

import com.es.phoneshop.model.dao.ProductDao;
import com.es.phoneshop.model.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.entity.Cart;
import com.es.phoneshop.model.entity.CartItem;
import com.es.phoneshop.model.entity.Product;
import com.es.phoneshop.model.exceptions.OutOfStockException;
import com.es.phoneshop.model.service.CartService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
    private ProductDao productDao;
    private static class SingletonHelper {
        private static final DefaultCartService INSTANCE = new DefaultCartService();
    }

    public static DefaultCartService getInstance() {
        return DefaultCartService.SingletonHelper.INSTANCE;
    }

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public Cart getCart(HttpServletRequest request) {
        synchronized (request.getSession()) {
            Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
            }
            return cart;
        }
    }

    @Override
    public synchronized void addProduct(Cart cart, Long productId, int quantity) throws OutOfStockException {
        if(quantity <= 0) {
            throw new OutOfStockException(null, quantity, 0);
        }
        Product product = productDao.getProduct(productId);
        CartItem newCartItem = new CartItem(product, quantity);
        Optional<CartItem> optionalCartItem = getCartItemByProduct(cart, product);
        if (!optionalCartItem.isPresent()) {
            if (product.getStock() < quantity) {
                throw new OutOfStockException(product, quantity, product.getStock());
            }
            cart.getItems().add(newCartItem);
            return;
        }
        CartItem oldCartItem = optionalCartItem.get();
        if(oldCartItem.getQuantity() + quantity > product.getStock()) {
            throw new OutOfStockException(product, quantity, product.getStock() - oldCartItem.getQuantity());
        }
        oldCartItem.setQuantity(oldCartItem.getQuantity() + newCartItem.getQuantity());
    }

    @Override
    public synchronized void update(Cart cart, Long productId, int quantity) throws OutOfStockException {
        if(quantity <= 0) {
            throw new OutOfStockException(null, quantity, 0);
        }
        Product product = productDao.getProduct(productId);
        CartItem newCartItem = new CartItem(product, quantity);
        Optional<CartItem> optionalCartItem = getCartItemByProduct(cart, product);

        if (!optionalCartItem.isPresent()) {
            if (product.getStock() < quantity) {
                throw new OutOfStockException(product, quantity, product.getStock());
            }
            cart.getItems().add(newCartItem);
            return;
        }
        CartItem oldCartItem = optionalCartItem.get();
        if(quantity > product.getStock()) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
        oldCartItem.setQuantity(quantity);
    }

    private Optional<CartItem> getCartItemByProduct(Cart cart, Product product) {
        return cart.getItems().stream()
                .filter(curProduct -> curProduct.getProduct().equals(product))
                .findAny();
    }


}
