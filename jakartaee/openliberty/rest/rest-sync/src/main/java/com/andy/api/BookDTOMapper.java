package com.andy.api;

import com.andy.shared.BookDTO;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.function.Function;

@ApplicationScoped
public class BookDTOMapper implements Function<Book, BookDTO> {


    @Override
    public BookDTO apply(Book book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor()
        );
    }
}
