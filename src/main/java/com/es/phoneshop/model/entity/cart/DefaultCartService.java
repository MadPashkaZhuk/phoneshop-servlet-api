package com.es.phoneshop.model.entity.cart;

import com.es.phoneshop.model.dao.ProductDao;
import com.es.phoneshop.model.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.entity.product.Product;
import com.es.phoneshop.model.exceptions.OutOfStockException;

import javax.servlet.http.HttpServletRequest;

public class DefaultCartService implements CartService{
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
    public synchronized void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);
        if (product.getStock() < quantity) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
        CartItem newCartItem = new CartItem(product, quantity);
        if (!cart.getItems().contains(newCartItem)) {
            cart.getItems().add(newCartItem);
            return;
        }
        CartItem oldCartItem = cart.getItems().stream()
                .filter(cartItem -> cartItem.equals(newCartItem))
                .findAny().get(); // always present
        if(oldCartItem.getQuantity() + quantity > product.getStock()) {
            throw new OutOfStockException(product, quantity, product.getStock() - oldCartItem.getQuantity());
        }
        oldCartItem.addToQuantity(newCartItem.getQuantity());
    }
}
