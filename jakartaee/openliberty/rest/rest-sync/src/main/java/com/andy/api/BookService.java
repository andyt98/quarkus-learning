package com.andy.api;

import com.andy.shared.BookCreateUpdateRequest;
import com.andy.shared.BookDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class BookService {

    @Inject
    private BookDAO bookDAO;

    @Inject
    private BookDTOMapper bookDTOMapper;

    public List<BookDTO> getAllBooks() {
        return bookDAO.getAllBooks().stream()
                .map(bookDTOMapper)
                .collect(Collectors.toList());
    }

    public Optional<BookDTO> getBookById(UUID id) {
        return bookDAO.getBookById(id)
                .map(bookDTOMapper);
    }

    public void addBook(BookCreateUpdateRequest request) {
        Book newBook = new Book(request.title(), request.author());
        bookDAO.addBook(newBook);
    }

    public void updateBook(UUID id, BookCreateUpdateRequest request) {
        Optional<Book> existingBook = bookDAO.getBookById(id);
        existingBook.ifPresent(book -> {
            Book updatedBook = new Book(id, request.title(), request.author());
            bookDAO.updateBook(updatedBook);
        });
    }

    public void deleteBook(UUID id) {
        bookDAO.deleteBookById(id);
    }
}
