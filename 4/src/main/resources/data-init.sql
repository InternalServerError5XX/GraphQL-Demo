USE [java-graphql];
GO

IF NOT EXISTS (SELECT 1 FROM dbo.authors WHERE first_name = 'Author 1')
BEGIN
    INSERT INTO authors (id, first_name) VALUES (1, 'Author 1');
    INSERT INTO authors (id, first_name) VALUES (2, 'Author 2');
END

IF NOT EXISTS (SELECT 1 FROM dbo.books WHERE name = 'Book 1')
BEGIN
    INSERT INTO books (id, name, author_id) VALUES (1, 'Book 1', 1);
    INSERT INTO books (id, name, author_id) VALUES (2, 'Book 2', 2);
    INSERT INTO books (id, name, author_id) VALUES (3, 'Book 3', 2);
END
