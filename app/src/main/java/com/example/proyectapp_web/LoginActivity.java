package com.example.proyectapp_web;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
public class LoginActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_login);
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            String email = obtenerEmailDelCampo();
            String password = obtenerPasswordDelCampo();
            login(email, password);
        });
    }
    private void login(String email, String password) {
        Log.d("LoginActivity", "Inicio de sesión iniciado");
        email = "correo@ejemplo.com";
        password = "contraseña_secreta";
        String url = "http://70.37.160.56/api/login";
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("email", email);
            requestBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, response -> {
            try {
                int status = response.getInt("status");
                if (status == 200) {
                    String accessToken = response.getString("accessToken");
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("accessToken", accessToken);
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    String message = response.getString("message");
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Error", "Error en la solicitud de inicio de sesión: " + e.getMessage());
            }
        }, error -> {
            if (error.networkResponse != null) {
                Log.e("Error", "Código de estado HTTP: " + error.networkResponse.statusCode);
            }
            Log.e("Error", "Error en la solicitud de inicio de sesión: " + error.getMessage());
        });
        requestQueue.add(loginRequest);
    }
    private String obtenerEmailDelCampo() {
        EditText campoEmail = findViewById(R.id.txtEmail);
        return campoEmail.getText().toString();
    }
    private String obtenerPasswordDelCampo() {
        EditText campoPassword = findViewById(R.id.txtPassword);
        return campoPassword.getText().toString();
    }
}
