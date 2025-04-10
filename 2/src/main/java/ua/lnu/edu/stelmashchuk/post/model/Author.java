package ua.lnu.edu.stelmashchuk.post.model;

import java.util.List;

public record Author (int id, String name, List<Book> books, Country country) { }
