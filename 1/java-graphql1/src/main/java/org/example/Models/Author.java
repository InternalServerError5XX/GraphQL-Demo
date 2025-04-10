package org.example.Models;

public class Author {
    private  int Id;
    private String Name;

    public Author(String name) {
        Name = name;
    }

    public int GetId() { return  Id; }
    public String GetName() { return Name; }
}
