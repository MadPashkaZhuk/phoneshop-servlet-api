package com.es.phoneshop.web;

import com.es.phoneshop.model.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.dao.ProductDao;
import com.es.phoneshop.model.entity.Cart;
import com.es.phoneshop.model.service.CartService;
import com.es.phoneshop.model.service.impl.DefaultCartService;

import com.es.phoneshop.model.service.impl.DefaultLatestProductQueueService;
import com.es.phoneshop.model.entity.LatestProductQueue;
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


public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;
    private LatestProductQueueService queueService;

    private static final String PRODUCT_JSP = "/WEB-INF/pages/product.jsp";
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
        request.setAttribute("product", productDao.getProduct(parseProductId(request)));
        request.setAttribute("cart", cartService.getCart(request));
        request.setAttribute("latestProducts", queueService.getLatestProductQueue(request).getQueue());

        LatestProductQueue queue = queueService.getLatestProductQueue(request);
        queueService.add(queue, parseProductId(request));
        request.getRequestDispatcher(PRODUCT_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantity = request.getParameter("quantity");
        Long id = parseProductId(request);
        int parsedQuantity = 0;
        try {
            parsedQuantity = parseQuantity(quantity, request);
        }
        catch (ParseException exception) {
            handleError(request, response, exception);
            return;
        }

        Cart cart = cartService.getCart(request);
        String redirectPath = request.getContextPath() + "/products/" + id + "?message=Product added to cart";

        try {
            cartService.addProduct(cart, id, parsedQuantity);
        }
        catch (OutOfStockException exception) {
            handleError(request, response, exception);
            return;
        }
        request.setAttribute(messageAttribute, successfulAddingMessage);

        response.sendRedirect(redirectPath);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, Exception e) throws ServletException, IOException {
        if(e.getClass().equals(ParseException.class)) {
            request.setAttribute(errorAttribute, wrongQuantityExceptionMessage);
        }
        else {
            if(((OutOfStockException) e).getStockRequested() <= 0) {
                request.setAttribute(errorAttribute, negativeOrZeroQuantityExceptionMessage);
            }
            else {
                request.setAttribute(errorAttribute, outOfStockMessage + ((OutOfStockException) e).getStockAvailable());
            }
        }
        doGet(request, response);
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
