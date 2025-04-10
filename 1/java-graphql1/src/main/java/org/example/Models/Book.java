package org.example.Models;

public class Book {
    private int Id;
    private String Title;
    private Author Author;

    public Book(String title, Author author) {
        Title = title;
        Author = author;
    }

    public  int GetId() { return  Id; }
    public String GetTitle() { return Title; }
    public Author GetAuthor() { return Author; }
}
