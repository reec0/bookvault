package com.example.ipt_102_finalproject;

import android.os.Bundle;
import android.view.View;
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

public class AddBookActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextAuthor, editTextDescription, editTextPublicationYear;
    private Button buttonAddBook, buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPublicationYear = findViewById(R.id.editTextPublicationYear);
        buttonAddBook = findViewById(R.id.buttonAddBook);
        buttonBack = findViewById(R.id.buttonBack);

        buttonAddBook.setOnClickListener(view -> {
            String title = editTextTitle.getText().toString();
            String author = editTextAuthor.getText().toString();
            String description = editTextDescription.getText().toString();
            String publicationYear = editTextPublicationYear.getText().toString();

            if (!title.isEmpty() && !author.isEmpty() && !description.isEmpty() && !publicationYear.isEmpty()) {
                addBookToDatabase(title, author, description, publicationYear);
            } else {
                Toast.makeText(AddBookActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        buttonBack.setOnClickListener(view -> finish());
    }

    private void addBookToDatabase(String title, String author, String description, String publicationYear) {
        String url = "http://bookvault.great-site.net/add_book.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.equals("Book added successfully!")) {
                        Toast.makeText(AddBookActivity.this, "Book Added", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddBookActivity.this, "Failed to add book", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(AddBookActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("author", author);
                params.put("description", description);
                params.put("publication_year", publicationYear);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
