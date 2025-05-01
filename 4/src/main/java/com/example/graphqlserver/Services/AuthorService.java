package com.example.graphqlserver.Services;

import com.example.graphqlserver.Entities.Author;
import com.example.graphqlserver.Repositories.AuthorRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author getById(int id) {
        return authorRepository.findById(id).orElse(null);
    }

    public Author addAuthor(Author author) { return authorRepository.save(author); }
}
