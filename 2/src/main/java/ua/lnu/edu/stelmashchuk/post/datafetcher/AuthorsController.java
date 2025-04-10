package ua.lnu.edu.stelmashchuk.post.datafetcher;

import ua.lnu.edu.stelmashchuk.post.model.Author;
import ua.lnu.edu.stelmashchuk.post.model.Country;
import ua.lnu.edu.stelmashchuk.post.model.api.ApiBook;
import ua.lnu.edu.stelmashchuk.post.service.ApiBooksService;
import ua.lnu.edu.stelmashchuk.post.service.ApiCountryService;
import org.dataloader.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import ua.lnu.edu.stelmashchuk.post.model.Book;
import ua.lnu.edu.stelmashchuk.post.service.ApiAuthorService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Controller
public class AuthorsController {
    public static final Logger logger = LoggerFactory.getLogger(AuthorsController.class);

    private final ApiAuthorService apiAuthorService;
    private final ApiBooksService apiBooksService;
    private final ApiCountryService apiCountryService;

    public AuthorsController(ApiAuthorService apiAuthorService, ApiBooksService apiBooksService,
                             ApiCountryService apiCountryService, BatchLoaderRegistry batchLoaderRegistry) {
        this.apiAuthorService = apiAuthorService;
        this.apiBooksService = apiBooksService;
        this.apiCountryService = apiCountryService;

        batchLoaderRegistry.forTypePair(Author.class, List.class).registerBatchLoader(
                (authors, environment) -> booksDataLoader(authors));

        batchLoaderRegistry.forTypePair(Author.class, Country.class).registerBatchLoader(
                (authors, environment) -> countryDataLoader(authors));
    }

    @QueryMapping
    public List<Author> authors() {
        return apiAuthorService.getApiAuthors().stream()
                .map(apiAuthor -> new Author(
                        apiAuthor.id(),
                        apiAuthor.name(),
                        null,
                        null)
                )
                .collect(Collectors.toList());
    }

    @QueryMapping
    public Author authorById(@Argument Integer id) {
        var apiAuthor = apiAuthorService.getApiAuthorById(id);
        return new Author(apiAuthor.id(), apiAuthor.name(), null, null);
    }

    @SchemaMapping
    public CompletableFuture<List<Book>> books(Author author, DataLoader<Author, List<Book>> dataLoader) {
        logger.info("Fetching books for author with id = [{}]", author.id());
        return dataLoader.load(author);
    }

    @SchemaMapping
    public CompletableFuture<Country> countryDataLoader(Author author, DataLoader<Author, Country> dataLoader) {
        logger.info("Fetching country for author with id = [{}]", author.id());
        return dataLoader.load(author);
    }

    @SchemaMapping
    public Country country(Author author) {
        var apiCountry = apiCountryService.getById(author.id());
        return new Country(apiCountry.id(), apiCountry.name());
    }

    private Flux<List> booksDataLoader(List<Author> authors) {
        List<Integer> bookIds = authors.stream()
                .map(Author::id)
                .toList();
        return Flux.fromIterable(getAuthorsViaBatchHTTPApi(bookIds));
    }

    private List<List<Book>> getAuthorsViaBatchHTTPApi(List<Integer> authorIds) {
        return apiBooksService.getBooksForAuthors(new HashSet<>(authorIds))
                .stream()
                .collect(Collectors.groupingBy(ApiBook::authorId))
                .values()
                .stream()
                .map(apiBooks -> apiBooks.stream()
                        .map(apiBook -> new Book(
                                apiBook.id(),
                                apiBook.title())
                        )
                        .toList()
                )
                .toList();
    }

    private Flux<Country> countryDataLoader(List<Author> authors) {
        Set<Integer> authorIdSet = authors.stream()
                .map(Author::id).collect(Collectors.toSet());

        List<Country> countries = apiCountryService.getCountriesForAuthors(authorIdSet).stream()
                .map(apiCountry -> new Country(apiCountry.id(), apiCountry.name()))
                .collect(Collectors.toList());

        return Flux.fromIterable(countries);
    }
}
