package com.andy;


import io.quarkus.vertx.web.RouteFilter;
import io.vertx.ext.web.RoutingContext;

public class CustomHeaderFilterWithVertx {

    @RouteFilter(100)
    void myFilter(RoutingContext rc) {
        rc.response().putHeader("X-My-Custom-Header", "hello world from Vert.x");
        rc.next();
    }
}