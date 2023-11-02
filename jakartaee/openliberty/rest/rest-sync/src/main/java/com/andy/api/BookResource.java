package com.andy.api;

import com.andy.shared.BookCreateUpdateRequest;
import com.andy.shared.BookDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.util.List;
import java.util.UUID;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    @Inject
    private BookService bookService;

    @GET
    @Operation(summary = "Get all books", description = "Retrieves a list of all available books.")
    @APIResponse(
            responseCode = "200",
            description = "List of books",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = BookDTO.class, type = SchemaType.ARRAY))
    )
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieves a book based on its ID.")
    @APIResponse(
            responseCode = "200",
            description = "Book found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = BookDTO.class))
    )
    @APIResponse(
            responseCode = "404",
            description = "Book not found"
    )
    public Response getBookById(
            @Parameter(description = "ID of the book to retrieve", required = true)
            @PathParam("id") UUID id) {
        return bookService.getBookById(id)
                .map(bookDTO -> Response.ok(bookDTO).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Operation(summary = "Add a new book", description = "Adds a new book to the collection.")
    @APIResponse(
            responseCode = "201",
            description = "Book added"
    )
    public Response addBook(BookCreateUpdateRequest request) {
        bookService.addBook(request);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update book by ID", description = "Updates an existing book based on its ID.")
    @APIResponse(
            responseCode = "200",
            description = "Book updated"
    )
    @APIResponse(
            responseCode = "404",
            description = "Book not found"
    )
    public Response updateBook(
            @Parameter(description = "ID of the book to update", required = true)
            @PathParam("id") UUID id,
            BookCreateUpdateRequest request) {
        bookService.updateBook(id, request);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete book by ID", description = "Deletes a book based on its ID.")
    @APIResponse(
            responseCode = "200",
            description = "Book deleted"
    )
    @APIResponse(
            responseCode = "404",
            description = "Book not found"
    )
    public Response deleteBook(
            @Parameter(description = "ID of the book to delete", required = true)
            @PathParam("id") UUID id) {
        bookService.deleteBook(id);
        return Response.ok().build();
    }

}
