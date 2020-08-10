package com.example.booksapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.security.acl.LastOwnerException;

public class Book {
    private String title;
    private String authors;
    private double rating;
    private String date;
    private String uri;

    public Book(String title, String authors, double rating, String date) {
        this.title = title;
        this.authors = authors;
        this.rating = rating;
        this.date = date;
    }
    public Book(String title, JSONArray authors, double rating, String date, String uri){
        this.title = title;
        this.rating = rating;
        this.date = date;
        this.uri = uri;
        StringBuilder stringBuilder = new StringBuilder();

        if(authors == null){
            this.authors = "";
        }else {
            for (int i = 0; i < authors.length(); i++) {
                try {
                    stringBuilder.append(authors.getString(i));
                    if(i != authors.length() -1 ){
                        stringBuilder.append(", ");
                    }
                } catch (JSONException e) {
                    Log.e("EXCEPTION", "ERROR in reading the authors array");
                    this.authors = "";
                }
            }
            this.authors = stringBuilder.toString();
        }
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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
