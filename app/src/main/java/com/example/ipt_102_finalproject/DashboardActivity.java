package com.example.ipt_102_finalproject;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle; import android.widget.Button; import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity; import androidx.lifecycle.ViewModelProvider; import androidx.recyclerview.widget.LinearLayoutManager; import androidx.recyclerview.widget.RecyclerView; import java.util.ArrayList;
public class DashboardActivity extends AppCompatActivity {
    private BookViewModel bookViewModel;
    private BookAdapter bookAdapter;
    private TextView textViewUserEmail;
    private Button buttonGoToFavorites, buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        EditText editTextSearch = findViewById(R.id.editTextSearch);
        Button buttonSearch = findViewById(R.id.buttonSearch);
        textViewUserEmail = findViewById(R.id.textViewUserEmail);
        buttonGoToFavorites = findViewById(R.id.buttonGoToFavorites);
        buttonLogout = findViewById(R.id.buttonLogout);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewBooks);

        SharedPreferences prefs = DashboardActivity.this.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String usersEmail = prefs.getString("user_email", null);
        textViewUserEmail.setText(usersEmail);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(bookAdapter);

        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        bookViewModel.getBooks().observe(this, bookAdapter::updateBooks);

        buttonSearch.setOnClickListener(v -> {
            String query = editTextSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                bookViewModel.fetchBooks(this, query);
            }
        });

        buttonGoToFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, FavoriteBooksActivity.class);
            startActivity(intent);
        });

        buttonLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("user_id");
            editor.remove("user_email");  // Remove the email or any other key you want to clear
            editor.apply();
            Intent intent = new Intent(DashboardActivity.this, LandingScreen.class);
            startActivity(intent);
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Re-fetch using the last query (defaults to empty string if none)
        bookViewModel.fetchBooks(this, bookViewModel.getLastQuery());
    }
}
