package com.example.ipt_102_finalproject;
public class Book {
    private String bookId;
    private String title;
    private String author;
    private String year;
    private String coverUrl;

    public Book(String bookId, String title, String author, String year, String coverUrl) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.year = year;
        this.coverUrl = coverUrl;
    }

    // Getters and setters
    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getYear() {
        return year;
    }

    public String getCoverUrl() {
        return coverUrl;
    }
}
