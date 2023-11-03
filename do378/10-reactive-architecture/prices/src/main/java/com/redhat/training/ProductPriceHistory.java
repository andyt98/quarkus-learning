package com.redhat.training;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductPriceHistory {

    @JsonProperty("product_id")
    private Long productId;

    private List<Price> prices;

    public ProductPriceHistory() {
    }

    public ProductPriceHistory( Long productId, List<Price> prices ) {
        this.productId = productId;
        this.prices = prices;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }
}
