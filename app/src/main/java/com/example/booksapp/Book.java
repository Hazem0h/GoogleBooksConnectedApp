package com.example.booksapp;

public class Book {
    private String title;
    private String authors;
    private double rating;
    private String date;

    public Book(String title, String authors, double rating, String date) {
        this.title = title;
        this.authors = authors;
        this.rating = rating;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthor(String authors) {
        this.authors = authors;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
