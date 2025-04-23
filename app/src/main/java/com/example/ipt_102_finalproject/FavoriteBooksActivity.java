package com.example.ipt_102_finalproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteBooksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter favoriteAdapter; // You can reuse BookAdapter or create FavoriteAdapter
    private List<Book> favoriteBooks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_books);

        recyclerView = findViewById(R.id.recyclerViewFavoriteBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoriteAdapter = new BookAdapter(this, favoriteBooks); // Reuse BookAdapter for now
        recyclerView.setAdapter(favoriteAdapter);

        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String userId = prefs.getString("user_id", null);

        if (userId != null) {
            fetchFavoriteBooks(Integer.parseInt(userId));
        } else {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchFavoriteBooks(int userId) {
        String url = "http://10.0.2.2/book_vault/get_favorite_books.php?user_id=" + userId;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        favoriteBooks.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String title = obj.getString("title");
                            String author = obj.getString("author");
                            String year = obj.getString("year");
                            String coverUrl = obj.getString("cover_url");
                            String bookId = obj.getString("book_id"); // Assuming book_id is also returned in the response

                            favoriteBooks.add(new Book(bookId, title, author, year, coverUrl));
                        }

                        favoriteAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to parse favorites", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Error fetching favorites", Toast.LENGTH_SHORT).show();
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}