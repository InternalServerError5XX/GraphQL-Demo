package ua.lnu.edu.stelmashchuk.post.service;

import com.fasterxml.jackson.core.type.TypeReference;
import ua.lnu.edu.stelmashchuk.post.model.api.ApiAuthor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiAuthorService {
    private static final Logger logger = LoggerFactory.getLogger(ApiAuthor.class);

    private final List<ApiAuthor> apiAuthors;

    public ApiAuthorService() {
        this.apiAuthors = JsonFileReaderService.readListFromJsonFile(
                "src/main/resources/data/authors.json", new TypeReference<>() {});
    }

    public List<ApiAuthor> getApiAuthors() {
        logger.info("REST API: get all authors");
        return apiAuthors;
    }

    public ApiAuthor getApiAuthorById(Integer id) {
        logger.info("REST API: get author by id = {}", id);
        return apiAuthors.stream()
                .filter(author -> author.id() == id)
                .findFirst()
                .orElse(null);
    }
}
