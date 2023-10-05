package com.andy.customer.control;

import com.andy.customer.entity.Customer;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.inject.Inject;

import java.util.concurrent.locks.LockSupport;

@ApplicationScoped
public class CustomerListener {

    @Inject
    MeterRegistry meterRegistry;

    public void onCreatedCustomer(@Observes(during = TransactionPhase.AFTER_COMPLETION)
                                  @Created Customer customer) {
        String type = customer.getCustomerType().name().toLowerCase();
        meterRegistry.counter("customers.created.total", "type", type).increment();
        System.out.println("Created customer: " + customer);
    }

//    public void onCreatedCustomerAsync(@Observes @Created Customer customer){
//        LockSupport.parkNanos(2_000_000_000L);
//        System.out.println("Created customer async: " + customer);
//    }

}
