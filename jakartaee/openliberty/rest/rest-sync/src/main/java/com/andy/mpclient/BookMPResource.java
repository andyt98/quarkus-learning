package com.andy.mpclient;

import com.andy.shared.BookCreateUpdateRequest;
import com.andy.shared.BookDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.UUID;

@Path("/mpclient/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookMPResource {

    @Inject
    @RestClient
    private BookMPClient bookMPClient;

    @GET
    public List<BookDTO> getAllBooks() {
        return bookMPClient.getBooks();
    }

    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") UUID id) {
        BookDTO book = bookMPClient.getBookById(id);
        if (book != null) {
            return Response.ok(book).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    public Response addBook(BookCreateUpdateRequest request) {
        Response response = bookMPClient.addBook(request);
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            return Response.status(Response.Status.CREATED).build();
        } else {
            return Response.serverError().build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") UUID id, BookCreateUpdateRequest request) {
        Response response = bookMPClient.updateBook(id, request);
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") UUID id) {
        Response response = bookMPClient.deleteBook(id);
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
