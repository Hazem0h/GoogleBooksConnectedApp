package com.example.booksapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.mtp.MtpConstants;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyAdapter.ListenerController,
        LoaderManager.LoaderCallbacks<String>{

    private ArrayList<Book> books = new ArrayList<Book>();
    private static final String query = "https://www.googleapis.com/books/v1/volumes?q=android";
    private MyAdapter rvAdapter;
    static final int LOADER_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //AsyncTaskLoader
        getSupportLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
        //RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        rvAdapter = new MyAdapter(books, this, this);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,
                false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
    }

    @Override
    public void controlMethod(int position) {
        Toast.makeText(this, "Clicked on item"+position, Toast.LENGTH_SHORT).show();
    }

    void testPopulateArray(){
        String t = "lol";
        String a = "loller";
        String d = "2020";
        books.add(new Book(t, a, 4, d));
        books.add(new Book(t, a, 3.2, d));
        books.add(new Book(t, a, 2.5, d));
        books.add(new Book(t, a, 1.3, d));
        books.add(new Book(t, a, 0.6, d));
        books.add(new Book(t, a, 5, d));
        books.add(new Book(t, a, 3, d));
        books.add(new Book(t, a, 3, d));
        books.add(new Book(t, a, 3, d));
        books.add(new Book(t, a, 2, d));
    }

    @Override
    protected void onDestroy() {
        books.clear();
        super.onDestroy();
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        return new MyLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        if (data == null){
            return;
        }
        JSONArray itemsArray;
        try {
            JSONObject root = new JSONObject(data);
            itemsArray = root.getJSONArray("items");
            for (int i = 0; i<itemsArray.length(); i++){
                JSONObject currentBook = itemsArray.getJSONObject(i).getJSONObject("volumeInfo");
                books.add(new Book(
                        currentBook.getString("title"),
                        currentBook.getJSONArray("authors"),
                        currentBook.getDouble("averageRating"),
                        currentBook.getString("publishedDate")));
            }

            rvAdapter.notifyDataSetChanged();
            //to tell the rv that our data source was modified

        }catch (JSONException exception){
            Log.e("EXCEPTION", "Error while parsing JSON");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        books.clear();
    }

    static class MyLoader extends AsyncTaskLoader<String>{
        public MyLoader(Context context){
            super(context);
        }
        @Nullable
        @Override
        public String loadInBackground() {
            URL url;
            try{
                url = new URL(query);
            }catch (MalformedURLException e){
                Log.e("EXCEPTION", "the query wasn't converted to a URL object");
                return null;
            }

            HttpURLConnection connection;
            try{
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.connect();
                int response = connection.getResponseCode()/100;
                switch (response){
                    case 4:
                        Log.e("CONNECTION","client side error");
                        return null;
                    case 5:
                        Log.e("CONNECTION", "server side error");
                        return null;
                }
            } catch (IOException e){
                Log.e("EXCEPTION", "url object can't open connection");
                return null;
            }
            try {
                InputStream inStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));

                //4- Reading
                StringBuilder builder = new StringBuilder();
                String line = bufferedReader.readLine();
                while(line != null){
                    builder.append(line);
                    line = bufferedReader.readLine();
                }
                inStream.close();
                return builder.toString();

            } catch (IOException exception) {
                Log.e("EXCEPTION", "error while getting input stream from open connection");
                return null;
            }finally {
                connection.disconnect();
            }


        }
    }
}