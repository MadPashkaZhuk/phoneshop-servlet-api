package com.es.phoneshop.web;

import com.es.phoneshop.model.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.dao.ProductDao;
import com.es.phoneshop.model.entity.Cart;
import com.es.phoneshop.model.service.CartService;
import com.es.phoneshop.model.service.impl.DefaultCartService;

import com.es.phoneshop.model.service.impl.DefaultLatestProductQueueService;
import com.es.phoneshop.model.service.LatestProductQueueService;
import com.es.phoneshop.model.exceptions.OutOfStockException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;


public class CartPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;
    private LatestProductQueueService queueService;

    private static final String CART_JSP = "/WEB-INF/pages/cart.jsp";
    private static final String outOfStockMessage = "Out of stock, available: ";
    private static final String successfulAddingMessage = "Product added to cart";
    private static final String wrongQuantityExceptionMessage = "Quantity is not a number";
    private static final String negativeOrZeroQuantityExceptionMessage = "Quantity is negative or zero";
    private static final String errorAttribute = "error";
    private static final String messageAttribute = "message";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        queueService = DefaultLatestProductQueueService.getInstance();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cart", cartService.getCart(request));
        request.getRequestDispatcher(CART_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");

        Map<Long, String> errors = new HashMap<>();
        for (int i = 0; i < productIds.length; i++) {
            Long productId = Long.valueOf(productIds[i]);

            int parsedQuantity = 0;
            try {
                parsedQuantity = parseQuantity(quantities[i], request);
                cartService.update(cartService.getCart(request), productId, parsedQuantity);
            }
            catch (ParseException | OutOfStockException exception) {
                handleError(errors, productId, exception);
            }
        }

        if(errors.isEmpty()) {
            String redirectPath = request.getContextPath() + "/cart?message=Cart updated successfully";
            response.sendRedirect(redirectPath);
        }
        else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }

    }

    private void handleError(Map<Long, String> errors, Long productId, Exception e) {
        if(e.getClass().equals(ParseException.class)) {
            errors.put(productId, wrongQuantityExceptionMessage);
        }
        else {
            if(((OutOfStockException) e).getStockRequested() <= 0) {
                errors.put(productId, negativeOrZeroQuantityExceptionMessage);
            }
            else {
                errors.put(productId, outOfStockMessage + ((OutOfStockException) e).getStockAvailable());
            }
        }
    }

    private Long parseProductId(HttpServletRequest request) {
        String productId = request.getPathInfo();
        return Long.valueOf(productId.substring(1));
    }

    private int parseQuantity(String quantity, HttpServletRequest request) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        int parsedQuantity = format.parse(quantity).intValue();
        if(!Integer.valueOf(parsedQuantity).toString().equals(quantity)) {
            throw new ParseException(wrongQuantityExceptionMessage, 0);
        }
        return parsedQuantity;
    }
}
