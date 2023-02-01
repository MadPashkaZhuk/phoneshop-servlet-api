package com.es.phoneshop.web;

import com.es.phoneshop.model.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.dao.ProductDao;
import com.es.phoneshop.model.entity.cart.Cart;
import com.es.phoneshop.model.entity.cart.CartService;
import com.es.phoneshop.model.entity.cart.DefaultCartService;
import com.es.phoneshop.model.entity.sortParams.SortField;
import com.es.phoneshop.model.entity.sortParams.SortOrder;
import com.es.phoneshop.model.exceptions.OutOfStockException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Optional;


public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("product", productDao.getProduct(parseProductId(request)));
        request.setAttribute("cart", cartService.getCart(request));
        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantity = request.getParameter("quantity");
        Long id = parseProductId(request);
        int parsedQuantity = 0;
        try {
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            parsedQuantity = format.parse(quantity).intValue();
        }
        catch (ParseException exception) {
            request.setAttribute("error", "Quantity is not a number");
            doGet(request, response);
            return;
        }

        if(parsedQuantity < 0) {
            request.setAttribute("error", "Quantity is negative");
            doGet(request, response);
            return;
        }

        if(parsedQuantity == 0) {
            request.setAttribute("error", "Quantity is zero");
            doGet(request, response);
            return;
        }

        Cart cart = cartService.getCart(request);
        try {
            cartService.add(cart, id, parsedQuantity);
        }
        catch (OutOfStockException exception) {
            request.setAttribute("error", "Out of stock, available: " + exception.getStockAvailable());
            doGet(request, response);
            return;
        }
        request.setAttribute("message", "Product added to cart");

        response.sendRedirect(request.getContextPath() + "/products/" + id + "?message=Product added to cart");
    }

    private Long parseProductId(HttpServletRequest request) {
        String productId = request.getPathInfo();
        return Long.valueOf(productId.substring(1));
    }
}
