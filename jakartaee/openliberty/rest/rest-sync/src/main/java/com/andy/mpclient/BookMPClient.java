package com.andy.mpclient;


import com.andy.shared.BookCreateUpdateRequest;
import com.andy.shared.BookDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;
import java.util.UUID;

@RegisterRestClient(configKey = "bookClient")
@Path("/book")
public interface BookMPClient extends AutoCloseable {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<BookDTO> getBooks();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    BookDTO getBookById(@PathParam("id") UUID id);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Response addBook(BookCreateUpdateRequest request);

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateBook(@PathParam("id") UUID id, BookCreateUpdateRequest request);

    @DELETE
    @Path("/{id}")
    Response deleteBook(@PathParam("id") UUID id);
}