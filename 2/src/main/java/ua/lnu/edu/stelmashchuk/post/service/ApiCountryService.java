package ua.lnu.edu.stelmashchuk.post.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.lnu.edu.stelmashchuk.post.model.api.ApiCountry;
import java.util.List;
import java.util.Set;

@Service
public class ApiCountryService {
    private static final Logger logger = LoggerFactory.getLogger(ApiCountry.class);

    private final List<ApiCountry> apiCountries;

    public ApiCountryService() {
        this.apiCountries = JsonFileReaderService.readListFromJsonFile(
                "src/main/resources/data/countries.json", new TypeReference<>() {});
    }

    public List<ApiCountry> getCountryForAuthor(Integer authorId) {
        logger.info("REST API: get country for author with id = [{}]", authorId);
        return apiCountries.stream()
                .filter(country -> authorId == country.authorId())
                .toList();
    }

    public List<ApiCountry> getCountriesForAuthors(Set<Integer> authorId) {
        logger.info("REST API: get countries for author with ids = {}", authorId);
        return apiCountries.stream()
                .filter(books -> authorId.contains(books.authorId()))
                .toList();
    }

    public ApiCountry getById(int authorId) {
        logger.info("REST API: get country for author with id: {}", authorId);
        return apiCountries.stream()
                .filter(country -> country.authorId() == authorId)
                .findFirst()
                .orElse(null);
    }
}
