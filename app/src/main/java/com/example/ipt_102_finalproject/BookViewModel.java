package com.example.ipt_102_finalproject;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class BookViewModel extends ViewModel {
    private MutableLiveData<List<Book>> books = new MutableLiveData<>();
    private String lastQuery = ""; // <- add this to remember the last search

    public LiveData<List<Book>> getBooks() {
        return books;
    }

    public String getLastQuery() {
        return lastQuery;
    }

    public void fetchBooks(Context context, String query) {
        lastQuery = query; // <- save the last search query

        String url = "https://openlibrary.org/search.json?q=" + query.replace(" ", "+");
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    List<Book> bookList = new ArrayList<>();
                    try {
                        JSONArray docsArray = response.getJSONArray("docs");
                        for (int i = 0; i < Math.min(docsArray.length(), 20); i++) {
                            JSONObject obj = docsArray.getJSONObject(i);
                            String title = obj.optString("title", "No Title");
                            String author = obj.has("author_name") ? obj.getJSONArray("author_name").getString(0) : "Unknown";
                            String year = obj.has("first_publish_year") ? String.valueOf(obj.getInt("first_publish_year")) : "N/A";

                            String coverUrl = "";
                            if (obj.has("cover_i")) {
                                int coverId = obj.getInt("cover_i");
                                coverUrl = "https://covers.openlibrary.org/b/id/" + coverId + "-L.jpg";
                            }

                            String bookId = obj.optString("key", "");
                            bookList.add(new Book(bookId, title, author, year, coverUrl));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    books.setValue(bookList);
                },
                error -> error.printStackTrace());

        queue.add(request);
    }
}
