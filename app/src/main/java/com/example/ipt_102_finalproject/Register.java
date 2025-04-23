package com.example.ipt_102_finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    Button registerButton;
    String registerUrl = "http://10.0.2.2/book_vault/register.php"; // make sure this is the correct reachable URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        registerButton = findViewById(R.id.registerButton);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        registerButton.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Invalid input!", Toast.LENGTH_SHORT).show();
            } else {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, registerUrl,
                        response -> {
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (obj.getString("status").equals("success")) {
                                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                    // Go to login only if registration is successful
                                    Intent intent = new Intent(this, Login.class);
                                    startActivity(intent);
                                    finish(); // Optional: prevent returning to register screen
                                } else {
                                    Toast.makeText(this, "Registration failed: " + obj.getString("message"), Toast.LENGTH_LONG).show();
                                    Log.d("error", obj.getString("message"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                                Log.d("error", e.getMessage());

                            }
                        },
                        error -> {
                            Log.d("VolleyError", "Error: " + error.getMessage());
                            Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", email);
                        params.put("password", password);
                        return params;
                    }
                };

                Volley.newRequestQueue(this).add(stringRequest);
            }
        });
    }
}
