package com.example.graphqlserver;

import com.example.graphqlserver.Entities.Author;
import com.example.graphqlserver.Entities.Book;
import com.example.graphqlserver.Services.AuthorService;
import com.example.graphqlserver.Services.BookService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;

    public BookController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @QueryMapping
    public ResponseType<Book> bookById(@Argument int id) {
        var book = bookService.getById(id);
        if (book != null) {
            return new ResponseType<>("success", "Book found", book);
        } else {
            return new ResponseType<>("error", "Book not found", null);
        }
    }

    @SchemaMapping
    public Author author(Book book) {
        return authorService.getById(book.getAuthorId());
    }

    @MutationMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseType<Author> addAuthor(@Argument String firstName) {
        var author = new Author(firstName);
        var response = authorService.addAuthor(author);

        if (response != null) {
            return new ResponseType<>("success", "Author created", response);
        } else {
            return new ResponseType<>("error", "Author creating error", null);
        }
    }
}
