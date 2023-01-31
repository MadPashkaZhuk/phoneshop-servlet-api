package com.es.phoneshop.model.entity.priceHistory;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PriceHistory {
    LocalDate date;
    BigDecimal price;

    public PriceHistory(LocalDate date, BigDecimal price) {
        this.date = date;
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
