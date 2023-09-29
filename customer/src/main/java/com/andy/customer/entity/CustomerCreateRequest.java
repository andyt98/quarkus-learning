package com.andy.customer.entity;

import com.andy.customer.control.ValidEmail;
import jakarta.validation.constraints.NotBlank;

public class CustomerCreateRequest {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @ValidEmail
    private String email;

    private CustomerType customerType;

    public CustomerCreateRequest() {
    }

    public CustomerCreateRequest(String username, String email, CustomerType customerType) {
        this.username = username;
        this.email = email;
        this.customerType = customerType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

}
