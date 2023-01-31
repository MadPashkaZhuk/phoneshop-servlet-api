package com.es.phoneshop.web.listeners;

import com.es.phoneshop.model.dao.ProductDao;
import com.es.phoneshop.model.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.entity.priceHistory.PriceHistory;
import com.es.phoneshop.model.entity.product.Product;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class ProductDemodataServletContextListener implements ServletContextListener {
    private ProductDao productDao;

    public ProductDemodataServletContextListener() {
        this.productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        boolean insertDemoData = Boolean.parseBoolean(servletContextEvent.getServletContext().getInitParameter("insertDemoData"));
        if(insertDemoData) {
            getSampleProducts().forEach(product ->
                    productDao.save(product)
            );
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private List<Product> getSampleProducts(){
        List<Product> products = new ArrayList<>();
        Currency usd = Currency.getInstance("USD");

        ArrayList<PriceHistory> priceHistoryExample1 = new ArrayList<>();
        ArrayList<PriceHistory> priceHistoryExample2 = new ArrayList<>();
        ArrayList<PriceHistory> priceHistoryExample3 = new ArrayList<>();

        priceHistoryExample1.add(new PriceHistory(LocalDate.of(2016,1,8), new BigDecimal(1250)));
        priceHistoryExample1.add(new PriceHistory(LocalDate.of(2020,3,13), new BigDecimal(1100)));
        priceHistoryExample1.add(new PriceHistory(LocalDate.of(2022,8,26), new BigDecimal(650)));

        priceHistoryExample2.add(new PriceHistory(LocalDate.of(2008,3,8), new BigDecimal(432)));
        priceHistoryExample2.add(new PriceHistory(LocalDate.of(2009,3,23), new BigDecimal(400)));
        priceHistoryExample2.add(new PriceHistory(LocalDate.of(2010,4,12), new BigDecimal(234)));

        priceHistoryExample3.add(new PriceHistory(LocalDate.of(2014,1,4), new BigDecimal(1070)));
        priceHistoryExample3.add(new PriceHistory(LocalDate.of(2015,2,5), new BigDecimal(900)));
        priceHistoryExample3.add(new PriceHistory(LocalDate.of(2017,3,6), new BigDecimal(700)));



        products.add(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", priceHistoryExample1));
        products.add(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg", priceHistoryExample2));
        products.add(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg", priceHistoryExample3));
        products.add(new Product("iphone", "Apple iPhone", new BigDecimal(200), usd, 10,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg", priceHistoryExample2));
        products.add(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg", priceHistoryExample1));
        products.add(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg", priceHistoryExample3));
        products.add(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg", priceHistoryExample1));
        products.add(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg", priceHistoryExample2));
        products.add(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg", priceHistoryExample1));
        products.add(new Product("palmp", "Palm Pixi", new BigDecimal(170), usd, 30,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg", priceHistoryExample3));
        products.add(new Product("simc56", "Siemens C56", new BigDecimal(70), usd, 20,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg", priceHistoryExample2));
        products.add(new Product("simc61", "Siemens C61", new BigDecimal(80), usd, 30,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg", priceHistoryExample1));
        products.add(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg", priceHistoryExample3));

        return products;
    }
}
