package com.example.ipt_102_finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private Context context;
    private List<Book> bookList;

    public BookAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    public void updateBooks(List<Book> books) {
        this.bookList = books;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.textTitle.setText(book.getTitle());
        holder.textAuthor.setText(book.getAuthor());
        holder.textYear.setText(book.getYear());

        String imageUrl = book.getCoverUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.bookcoverplaceholder);
        }

        // Check if the book is already a favorite
        SharedPreferences prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String userId = prefs.getString("user_id", null);

        if (userId != null) {
            // Check the database if the book is a favorite for the user
            checkFavoriteStatus(holder, book, userId);
        }

        holder.favoriteText.setOnClickListener(v -> {
            if (holder.favoriteText.getText().toString().equals("♡")) {
                // If the heart is empty, add to favorites
                holder.favoriteText.setText("♥");
                addToFavorites(context, book);
            } else {
                // If the heart is filled, remove from favorites
                holder.favoriteText.setText("♡");
                removeFromFavorites(context, book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textAuthor, textYear, favoriteText;
        ImageView imageView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textAuthor = itemView.findViewById(R.id.textAuthor);
            textYear = itemView.findViewById(R.id.textYear);
            imageView = itemView.findViewById(R.id.imageView);
            favoriteText = itemView.findViewById(R.id.btnFavorite);
        }
    }

    private void addToFavorites(Context context, Book book) {
        String url = "http://10.0.2.2/book_vault/add_favorite_books.php";

        // Retrieve user_id from SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String userId = prefs.getString("user_id", null);

        if (userId == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();
                    Log.d("Favorite", "Response: " + response);
                },
                error -> {
                    Toast.makeText(context, "Failed to add", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId); // Include user_id
                params.put("title", book.getTitle());
                params.put("author", book.getAuthor());
                params.put("year", book.getYear());
                params.put("cover_url", book.getCoverUrl());
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    private void removeFromFavorites(Context context, Book book) {
        String url = "http://10.0.2.2/book_vault/remove_favorite_books.php";

        // Retrieve user_id from SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String userId = prefs.getString("user_id", null);

        if (userId == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();
                    Log.d("Favorite", "Response: " + response);
                },
                error -> {
                    Toast.makeText(context, "Failed to remove", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId); // Include user_id
                params.put("book_id", String.valueOf(book.getBookId())); // Book ID to remove
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    // Method to check if the book is already in favorites
    private void checkFavoriteStatus(BookViewHolder holder, Book book, String userId) {
        String url = "http://10.0.2.2/book_vault/check_favorite_status.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.equals("exists")) {
                        holder.favoriteText.setText("♥"); // Filled heart if it's a favorite
                    } else {
                        holder.favoriteText.setText("♡"); // Empty heart if it's not a favorite
                    }
                },
                error -> {
                    error.printStackTrace();
                    Log.e("Favorite", "Failed to check favorite status");
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("book_id", String.valueOf(book.getBookId())); // Book ID to check
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }
}
