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

        String productJSPPath = "/WEB-INF/pages/product.jsp";
        LatestProductQueue queue = queueService.getLatestProductQueue(request);
        queueService.add(queue, parseProductId(request));
        request.getRequestDispatcher(productJSPPath).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantity = request.getParameter("quantity");

        String wrongQuantityExceptionMessage = "Quantity is not a number";
        String negativeQuantityExceptionMessage = "Quantity is negative";
        String zeroQuantityExceptionMessage = "Quantity is zero";
        String errorAttribute = "error";
        String messageAttribute = "message";

        Long id = parseProductId(request);
        int parsedQuantity = 0;
        try {
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            parsedQuantity = format.parse(quantity).intValue();
            if(!Integer.valueOf(parsedQuantity).toString().equals(quantity)) {
                throw new ParseException(wrongQuantityExceptionMessage, 0);
            }
        }
        catch (ParseException exception) {
            request.setAttribute(errorAttribute, wrongQuantityExceptionMessage);
            doGet(request, response);
            return;
        }

        if(parsedQuantity < 0) {
            request.setAttribute(errorAttribute, negativeQuantityExceptionMessage);
            doGet(request, response);
            return;
        }

        if(parsedQuantity == 0) {
            request.setAttribute(errorAttribute, zeroQuantityExceptionMessage);
            doGet(request, response);
            return;
        }

        Cart cart = cartService.getCart(request);
        String outOfStockMessage = "Out of stock, available: ";
        String successfulAddingMessage = "Product added to cart";
        String redirectPath = request.getContextPath() + "/products/" + id + "?message=Product added to cart";

        try {
            cartService.addProduct(cart, id, parsedQuantity);
        }
        catch (OutOfStockException exception) {
            request.setAttribute(errorAttribute, outOfStockMessage + exception.getStockAvailable());
            doGet(request, response);
            return;
        }
        request.setAttribute(messageAttribute, successfulAddingMessage);

        response.sendRedirect(redirectPath);
    }

    private Long parseProductId(HttpServletRequest request) {
        String productId = request.getPathInfo();
        return Long.valueOf(productId.substring(1));
    }
}
