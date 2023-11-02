package com.andy.client;

import com.andy.shared.BookCreateUpdateRequest;
import com.andy.shared.BookDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class BookClient {

    private final Client client = ClientBuilder.newClient();
    private final String BASE_URL = "http://localhost:9080/rest/api/books";

    public List<BookDTO> getBooks() {
        WebTarget target = client.target(BASE_URL);
        return target.request(MediaType.APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    public BookDTO getBookById(UUID id) {
        WebTarget target = client.target(BASE_URL).path(id.toString());
        return target.request(MediaType.APPLICATION_JSON)
                .get(BookDTO.class);
    }

    public void addBook(BookCreateUpdateRequest request) {
        WebTarget target = client.target(BASE_URL);
        Response response = target.request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));

        handleResponse(response);
    }

    public void updateBook(UUID id, BookCreateUpdateRequest request) {
        WebTarget target = client.target(BASE_URL).path(id.toString());
        Response response = target.request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(request, MediaType.APPLICATION_JSON));

        handleResponse(response);
    }

    public void deleteBook(UUID id) {
        WebTarget target = client.target(BASE_URL).path(id.toString());
        Response response = target.request().delete();

        handleResponse(response);
    }

    private void handleResponse(Response response) {
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw new RuntimeException("Request failed with status: " + response.getStatus());
        }
    }
}
