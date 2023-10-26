package com.andy.domain.service;

import com.andy.domain.entity.Router;
import com.andy.domain.specification.CIDRSpecification;
import com.andy.domain.specification.NetworkAmountSpecification;
import com.andy.domain.specification.NetworkAvailabilitySpecification;
import com.andy.domain.specification.RouterTypeSpecification;
import com.andy.domain.value_objects.Network;

public class NetworkOperation {

    public static Router createNewNetwork(Router router, Network network) {
        var availabilitySpec = new NetworkAvailabilitySpecification(network.address(), network.name(), network.cidr());
        var cidrSpec = new CIDRSpecification();
        var routerTypeSpec = new RouterTypeSpecification();
        var amountSpec = new NetworkAmountSpecification();

        if (cidrSpec.isSatisfiedBy(network.cidr()))
            throw new IllegalArgumentException("CIDR is below " + CIDRSpecification.MINIMUM_ALLOWED_CIDR);

        if (!availabilitySpec.isSatisfiedBy(router))
            throw new IllegalArgumentException("Address already exist");

        if (amountSpec.and(routerTypeSpec).isSatisfiedBy(router)) {
            Network newNetwork = router.createNetwork(network.address(), network.name(), network.cidr());
            router.addNetworkToSwitch(newNetwork);
        }
        return router;
    }
}
