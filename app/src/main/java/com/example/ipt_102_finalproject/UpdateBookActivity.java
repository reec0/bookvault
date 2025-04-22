package com.example.ipt_102_finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class UpdateBookActivity extends AppCompatActivity {

    private EditText editTextUpdateTitle, editTextUpdateAuthor, editTextUpdateDescription, editTextUpdateYear;
    private Button buttonUpdateBook, buttonDeleteBook;
    private String bookId, title, author, description, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);

        editTextUpdateTitle = findViewById(R.id.editTextUpdateTitle);
        editTextUpdateAuthor = findViewById(R.id.editTextUpdateAuthor);
        editTextUpdateDescription = findViewById(R.id.editTextUpdateDescription);
        editTextUpdateYear = findViewById(R.id.editTextUpdateYear);
        buttonUpdateBook = findViewById(R.id.buttonUpdateBook);
        buttonDeleteBook = findViewById(R.id.buttonDeleteBook);

        Intent intent = getIntent();
        bookId = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        author = intent.getStringExtra("author");
        description = intent.getStringExtra("description");
        year = intent.getStringExtra("year");

        editTextUpdateTitle.setText(title);
        editTextUpdateAuthor.setText(author);
        editTextUpdateDescription.setText(description);
        editTextUpdateYear.setText(year);

        buttonUpdateBook.setOnClickListener(view -> updateBook());
        buttonDeleteBook.setOnClickListener(view -> deleteBook());
    }

    private void updateBook() {
        String url = "http://bookvault.great-site.net/update_book.php";
        String updatedTitle = editTextUpdateTitle.getText().toString();
        String updatedAuthor = editTextUpdateAuthor.getText().toString();
        String updatedDescription = editTextUpdateDescription.getText().toString();
        String updatedYear = editTextUpdateYear.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(UpdateBookActivity.this, response, Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> Toast.makeText(UpdateBookActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", bookId);
                params.put("title", updatedTitle);
                params.put("author", updatedAuthor);
                params.put("description", updatedDescription);
                params.put("publication_year", updatedYear);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    private void deleteBook() {
        String url = "http://bookvault.great-site.net/delete_book.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(UpdateBookActivity.this, response, Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> Toast.makeText(UpdateBookActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", bookId);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}
