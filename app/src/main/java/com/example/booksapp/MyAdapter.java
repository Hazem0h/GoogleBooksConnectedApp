package com.example.booksapp;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    Context hostContext;
    ArrayList<Book> bookArrayList;
    ListenerController listenerController;
    public MyAdapter(ArrayList<Book> data, Context context, ListenerController controller){
        bookArrayList = data;
        hostContext = context;
        listenerController = controller;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(hostContext).inflate(R.layout.list_item, parent, false);
        return (new ViewHolder(view, hostContext, listenerController));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book currentBook = bookArrayList.get(position);
        double rating = currentBook.getRating();
        if(rating == -1){
            holder.ratingTextView.setText("?");
        }else {
            holder.ratingTextView.setText(
                    currentBook.getRating() + "");
        }
        holder.titleTextView.setText(
                currentBook.getTitle());
        holder.authorTextView.setText(
                currentBook.getAuthors());
        holder.dateTextView.setText(
                currentBook.getDate());
        setRatingColor(holder, currentBook.getRating());
    }

    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }

    private void setRatingColor(ViewHolder viewHolder, double rating){
        GradientDrawable gradientDrawable =(GradientDrawable) viewHolder.
                ratingTextView.getBackground();
        int colorId;
        int truncated = (int) rating;
        switch (truncated){
            case 0:
                colorId = R.color.zero;
                break;
            case 1:
                colorId = R.color.one;
                break;
            case 2:
                colorId = R.color.two;
                break;
            case 3:
                colorId = R.color.three;
                break;
            case 4:
                colorId = R.color.four;
                break;
            case 5:
                colorId = R.color.four;
                break;
            default:
                colorId = R.color.gray;
        }
        gradientDrawable.setColor(viewHolder.hostContext.getResources().getColor(colorId));
    }

    public interface ListenerController{
        public void controlMethod(int position);
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTextView;
        private TextView authorTextView;
        private TextView dateTextView;
        private TextView ratingTextView;
        private Context hostContext;
        private ListenerController listenerController;

        public ViewHolder(View itemView, Context context, ListenerController controller){
            super(itemView);
           titleTextView = itemView.findViewById(R.id.title_textview);
           authorTextView = itemView.findViewById(R.id.author_textview);
           dateTextView = itemView.findViewById(R.id.date_textview);
           ratingTextView = itemView.findViewById(R.id.rating_textview);
           hostContext = context;
           listenerController = controller;

           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   listenerController.controlMethod(getAdapterPosition());
               }
           });
        }
    }
}
