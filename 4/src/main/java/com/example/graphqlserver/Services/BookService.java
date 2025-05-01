package com.example.graphqlserver.Services;

import com.example.graphqlserver.Entities.Book;
import com.example.graphqlserver.Repositories.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book getById(int id) {
        return bookRepository.findById(id).orElse(null);
    }
}
