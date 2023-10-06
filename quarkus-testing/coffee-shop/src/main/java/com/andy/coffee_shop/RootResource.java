package com.andy.coffee_shop;

import com.andy.coffee_shop.orders.control.EntityBuilder;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static javax.ws.rs.core.HttpHeaders.ACCEPT;

@Path("/")
public class RootResource {

    @Context
    UriInfo uriInfo;

    @Context
    HttpServletRequest request;

    @Inject
    EntityBuilder entityBuilder;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    public Response redirect() {
        if (isHtmlMediaType(request))
            return Response.seeOther(URI.create("/index.html")).build();

        return Response.ok(entityBuilder.buildIndex(this.uriInfo)).build();
    }

    public static boolean isHtmlMediaType(HttpServletRequest request) {
        String acceptType = request.getHeader(ACCEPT);
        return acceptType != null && acceptType.contains(MediaType.TEXT_HTML);
    }

}
