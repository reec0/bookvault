package com.example.ipt_102_finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class Login extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    Button loginButton;
    String loginUrl = "http://10.0.2.2/book_vault/login.php"; // Emulator local API endpoint
//  String loginUrl = "https://bookvault.kesug.com/book_vault/login.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginButton = findViewById(R.id.loginButton);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        loginButton.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Invalid input!", Toast.LENGTH_SHORT).show();
                return;
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, loginUrl,
                    response -> {
                        Log.d("Login", "Response: " + response);
                        try {
                            JSONObject obj = new JSONObject(response);

                            if (obj.getString("status").equals("success")) {
                                String userId = obj.getString("user_id");
                                String userEmail = obj.getString("user_email");

                                Log.d("Login", "User ID: " + userId);
                                Log.d("Login", "User Email: " + userEmail);

                                // ✅ Store to SharedPreferences
                                SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("user_id", userId);
                                editor.putString("user_email", userEmail);
                                editor.apply();

                                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(this, DashboardActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(this, "Login failed: " + obj.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("Error", "Error parsing response: " + e.getMessage());
                            Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        Log.d("VolleyError", "Error: " + error.getMessage());
                        Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };

            Volley.newRequestQueue(this).add(stringRequest);
        });
    }
}
