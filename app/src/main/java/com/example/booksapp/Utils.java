package com.example.booksapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//this is a utility class to clean the code a bit
public class Utils {
    public static void postExecute(String data, ArrayList<Book> books){
        JSONArray itemsArray;
        try {
            JSONObject root = new JSONObject(data);
            itemsArray = root.getJSONArray("items");
            for (int i = 0; i<itemsArray.length(); i++){
                JSONObject currentBook = itemsArray.getJSONObject(i).getJSONObject("volumeInfo");
                double currentRating = adjustRating(currentBook);
                books.add(new Book(
                        currentBook.getString("title"),
                        currentBook.getJSONArray("authors"),
                        currentRating,
                        currentBook.getString("publishedDate"),
                        currentBook.getString("infoLink")));
            }
        }catch (JSONException exception){
            Log.e("EXCEPTION", "Error while parsing JSON");
        }
    }

    private static double adjustRating(JSONObject currentBook){
        try{
           return currentBook.getDouble("averageRating");
        }catch (JSONException e){
            Log.e("Exception", "Error while parsing JSON for rating");
            return -1;
        }
    }
}
