package com.example.booksapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StartupActivity extends AppCompatActivity {
    public static final String QUERY_TAG = "query";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        Button searchButton = (Button) findViewById(R.id.button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartupActivity.this, MainActivity.class);
                EditText editText = (EditText) findViewById(R.id.editTextText);
                String query = editText.getText().toString();
                i.putExtra("query", query);
                startActivity(i);
            }
        });
    }
}