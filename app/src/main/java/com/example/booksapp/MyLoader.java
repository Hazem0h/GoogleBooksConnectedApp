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
import android.content.Intent;
import android.mtp.MtpConstants;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
class MyLoader extends AsyncTaskLoader<String>{
    String query;
    public MyLoader(Context context, String query){
        super(context);
        this.query = query;
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
                    return MainActivity.CLIENT_FAIL;
                case 5:
                    Log.e("CONNECTION", "server side error");
                    return MainActivity.SERVER_FAIL;
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