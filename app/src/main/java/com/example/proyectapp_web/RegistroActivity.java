package com.example.proyectapp_web;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
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

import java.io.UnsupportedEncodingException;

public class RegistroActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_registro);
        Button btnRegistro = findViewById(R.id.btnRegistro);
        btnRegistro.setOnClickListener(v -> {
            String nombre = obtenerNombreDelCampo();
            String email = obtenerEmailDelCampo();
            String password = obtenerPasswordDelCampo();
            String confirmPassword = obtenerConfirmPasswordDelCampo();
            register(nombre, email, password, confirmPassword);
        });
    }
    private void register(String name, String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            return;
        }
        String url = "http://70.37.160.56/api/register";
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", name);
            requestBody.put("email", email);
            requestBody.put("password", password);
            requestBody.put("password_confirmation", confirmPassword); // Agregar password_confirmation
            JsonObjectRequest registerRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, response -> {
                try {
                    int status = response.getInt("status");
                    if (status == 200) {
                        String accessToken = response.getString("accessToken");
                        Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String message = response.getString("message");
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                if (error.networkResponse != null) {
                    Log.e("Error", "Código de estado HTTP: " + error.networkResponse.statusCode);
                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        Log.e("Error", "Respuesta del servidor: " + responseBody);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                Log.e("Error", "Error en la solicitud de registro: " + error.getMessage());
                Toast.makeText(this, "Error en la solicitud de registro", Toast.LENGTH_LONG).show();
            });
            requestQueue.add(registerRequest);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al registrar: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private String obtenerNombreDelCampo() {
        EditText campoNombre = findViewById(R.id.txtNombre);
        return campoNombre.getText().toString();
    }
    private String obtenerEmailDelCampo() {
        EditText campoEmail = findViewById(R.id.txtEmail);
        return campoEmail.getText().toString();
    }
    private String obtenerPasswordDelCampo() {
        EditText campoPassword = findViewById(R.id.txtPassword);
        return campoPassword.getText().toString();
    }
    private String obtenerConfirmPasswordDelCampo() {
        EditText campoConfirmPassword = findViewById(R.id.txtConfirmPassword);
        return campoConfirmPassword.getText().toString();
    }
}

