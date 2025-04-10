package ua.lnu.edu.stelmashchuk.post.service;

import com.fasterxml.jackson.core.type.TypeReference;
import ua.lnu.edu.stelmashchuk.post.model.api.ApiBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ApiBooksService {
    private static final Logger logger = LoggerFactory.getLogger(ApiBook.class);

    private final List<ApiBook> apiBooks;

    public ApiBooksService() {
        this.apiBooks = JsonFileReaderService.readListFromJsonFile(
                "src/main/resources/data/books.json", new TypeReference<>() {});
    }

    public List<ApiBook> getBooksForAuthor(Integer authorId) {
        logger.info("REST API: get all books for author with id = [{}]", authorId);
        return apiBooks.stream()
                .filter(book -> authorId == book.authorId())
                .toList();
    }

    public List<ApiBook> getBooksForAuthors(Set<Integer> authorId) {
        logger.info("REST API: get all books for author with ids = {}", authorId);
        return apiBooks.stream()
                .filter(books -> authorId.contains(books.authorId()))
                .toList();
    }

    public ApiBook getById(int id) {
        logger.info("REST API: Searching for book with ID: {}", id);
        return apiBooks.stream()
                .filter(author -> author.id() == id)
                .findFirst()
                .orElse(null);
    }
}
