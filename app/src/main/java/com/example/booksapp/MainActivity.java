package com.example.booksapp;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyAdapter.ListenerController,
        LoaderManager.LoaderCallbacks<String>{

    //data members
    private ArrayList<Book> books = new ArrayList<Book>();
    private static String query;
    private MyAdapter rvAdapter;
    static final int LOADER_ID = 1;
    static final String CLIENT_FAIL = "Client side failure";
    static final String SERVER_FAIL = "Server side failure";
    static final String QUERY_PREFIX = "https://www.googleapis.com/books/v1/volumes?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //the behaviour will depend on internet connection
        if (isConnected()){
            setupQuery();
            setupRecyclerView();
            getSupportLoaderManager().initLoader(LOADER_ID, null, MainActivity.this).forceLoad();
        }else{
            TextView auxTextView = (TextView) findViewById(R.id.aux_textView);
            auxTextView.setText("No internet Connection");
            auxTextView.setVisibility(View.VISIBLE);
            findViewById(R.id.progressBar).setVisibility(View.GONE);
        }
    }

    @Override
    public void controlMethod(int position) {
        Uri bookUri = Uri.parse(books.get(position).getUri());
        Intent i = new Intent(Intent.ACTION_VIEW, bookUri);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        books.clear();
        super.onDestroy();
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        return new MyLoader(this,
                query);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        TextView auxTextView = (TextView) findViewById(R.id.aux_textView);
        if(data.equals(SERVER_FAIL)){
            auxTextView.setText(SERVER_FAIL);
            auxTextView.setVisibility(View.VISIBLE);
        }else if(data.equals(CLIENT_FAIL)){
            auxTextView.setText(CLIENT_FAIL);
            auxTextView.setVisibility(View.VISIBLE);
        }else {
            Utils.postExecute(data, books);
            if(books.size() == 0){
                auxTextView.setText("No Results for such query");
                auxTextView.setVisibility(View.VISIBLE);
            }
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            rvAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        books.clear();
    }

    private void setupRecyclerView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        rvAdapter = new MyAdapter(books, this, this);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,
                false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
    }

    private boolean isConnected(){
        final ConnectivityManager coMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = coMgr.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private void setupQuery(){
        Intent parentIntent = getIntent();
        String searchItems = parentIntent.getStringExtra(StartupActivity.QUERY_TAG);
        String[] itemArray = searchItems.split(" ");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(QUERY_PREFIX);
        if(itemArray == null){
            queryBuilder.append(searchItems);
        }else{
            for (int i = 0; i<itemArray.length; i++){
                queryBuilder.append(itemArray[i]);
            }
        }
        query = queryBuilder.toString();

    }
}