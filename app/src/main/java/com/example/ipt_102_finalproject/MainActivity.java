package com.example.ipt_102_finalproject;

import android.os.Bundle; import android.widget.Button; import android.widget.EditText; import androidx.appcompat.app.AppCompatActivity; import androidx.lifecycle.ViewModelProvider; import androidx.recyclerview.widget.LinearLayoutManager; import androidx.recyclerview.widget.RecyclerView; import java.util.ArrayList;

public class MainActivity extends AppCompatActivity { private BookViewModel bookViewModel; private BookAdapter bookAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editTextSearch = findViewById(R.id.editTextSearch);
        Button buttonSearch = findViewById(R.id.buttonSearch);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewBooks);

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
    }
}