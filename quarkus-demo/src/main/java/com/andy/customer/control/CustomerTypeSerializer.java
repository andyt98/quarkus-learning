package com.andy.customer.control;

import com.andy.customer.entity.CustomerType;
import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;


public class CustomerTypeSerializer implements JsonbSerializer<CustomerType> {

    @Override
    public void serialize(CustomerType customerType, JsonGenerator generator, SerializationContext ctx) {
        generator.writeStartObject();
        generator.write("name", customerType.name());
        generator.write("description", getCustomerTypeDescription(customerType));
        generator.writeEnd();
    }

    private String getCustomerTypeDescription(CustomerType customerType) {
        return switch (customerType) {
            case REGULAR -> "Regular Customer - no extra benefits";
            case PREMIUM -> "Premium Customer - 10% discount";
            case VIP -> "VIP Customer - 20% discount";
        };
    }
}
