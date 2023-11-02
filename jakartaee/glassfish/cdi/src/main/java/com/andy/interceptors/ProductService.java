package com.andy.interceptors;

import jakarta.enterprise.context.ApplicationScoped;

@Logged
@ApplicationScoped
public class ProductService {

    public String getProduct() {
        return "You ordered the product";
    }
}