package com.example.ipt_102_finalproject;

public class Book {
    private String id;
    private String title;
    private String author;
    private String description;
    private String publicationYear;
    private String year;

    private String coverUrl;
    public Book(String title, String author, String year, String coverUrl) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.coverUrl = coverUrl;
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
