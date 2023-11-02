package com.andy.client;

import com.andy.shared.BookCreateUpdateRequest;
import com.andy.shared.BookDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/client/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class BookClientResource {

    @Inject
    private BookClient bookClient;
    
    @GET
    public List<BookDTO> getAllBooks() {
        return bookClient.getBooks();
    }

    @GET
    @Path("/{id}")
    public BookDTO getBookById(@PathParam("id") UUID id) {
        return bookClient.getBookById(id);
    }

    @POST
    public Response addBook(BookCreateUpdateRequest request) {
        bookClient.addBook(request);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") UUID id, BookCreateUpdateRequest request) {
        bookClient.updateBook(id, request);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") UUID id) {
        bookClient.deleteBook(id);
        return Response.ok().build();
    }
}
