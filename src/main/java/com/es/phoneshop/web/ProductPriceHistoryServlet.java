package com.es.phoneshop.web;

import com.es.phoneshop.model.dao.ProductDao;
import com.es.phoneshop.model.dao.impl.ArrayListProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ProductPriceHistoryServlet extends HttpServlet {
    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getPathInfo();
        String productAttribute = "product";
        String productPriceHistoryJSPPath = "/WEB-INF/pages/productPriceHistory.jsp";


        request.setAttribute("product", productDao.getProduct(Long.valueOf(productId.substring(1))));
        request.getRequestDispatcher(productPriceHistoryJSPPath).forward(request, response);
    }
}
