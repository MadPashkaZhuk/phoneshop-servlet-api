package com.es.phoneshop.web;

import com.es.phoneshop.model.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.dao.ProductDao;
import com.es.phoneshop.model.entity.sortParams.SortField;
import com.es.phoneshop.model.entity.sortParams.SortOrder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


public class ProductListPageServlet extends HttpServlet {
    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");
        request.setAttribute("products", productDao.findProducts(query,
                Optional.ofNullable(sortField).map(field -> SortField.valueOf(field.toUpperCase())).orElse(null),
                Optional.ofNullable(sortOrder).map(order -> SortOrder.valueOf(order.toUpperCase())).orElse(null)));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }
}
