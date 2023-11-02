package com.andy.api;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@ApplicationScoped
public class BookDAO {
    private final List<Book> books = new CopyOnWriteArrayList<>();

    public BookDAO() {
    }

    @PostConstruct
    public void init() {
        // Creating and adding initial books
        books.add(new Book("Book One", "Author One"));
        books.add(new Book("Book Two", "Author Two"));
        books.add(new Book("Book Three", "Author Three"));
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public Optional<Book> getBookById(UUID id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst();
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public void updateBook(Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(updatedBook.getId())) {
                books.set(i, updatedBook);
                return;
            }
        }
    }

    public void deleteBookById(UUID id) {
        books.removeIf(book -> book.getId().equals(id));
    }


}
