package com.example.booksapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyAdapter.ListenerController{

    ArrayList<Book> books = new ArrayList<Book>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testPopulateArray();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new MyAdapter(books, this, this));
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
}