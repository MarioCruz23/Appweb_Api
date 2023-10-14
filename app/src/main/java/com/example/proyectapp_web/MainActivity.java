package com.example.proyectapp_web;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText txtUser, txtNombre, txtemail, txtrol, txtimg;
    Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtUser = findViewById(R.id.txtUser);
        txtNombre = findViewById(R.id.txtNombre);
        txtemail = findViewById(R.id.txtemail);
        txtrol = findViewById(R.id.txtrol);
        txtimg = findViewById(R.id.txtimg);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LeerWs();
                //eliminarWs();
                //actualizarWs(txtNombre.getText().toString(), txtemail.getText().toString();
            }
        });
    }

    private void LeerWs() {
        String url = "http://70.37.160.56/api/get-usuarios/9";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    txtUser.setText(jsonObject.getString("id"));
                    String nombre = jsonObject.getString("nombre");
                    txtNombre.setText(nombre);
                    txtemail.setText(jsonObject.getString("email"));
                    txtrol.setText(jsonObject.getString("rol"));
                    txtimg.setText(jsonObject.getString("img"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error en la solicitud: " + error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("error", error.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(postRequest);
    }
    private void actualizarWs(final String img, final String rol, final String email, final String nombre, final String id) {
        String url = "http://70.37.160.56/api/save-usuarios";
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, "RESULTADO = " + response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
                Toast.makeText(MainActivity.this, "Error en la solicitud PUT: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public byte[] getBody() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("nombre", nombre);
                params.put("rol", rol);
                params.put("img", img);
                return new JSONObject(params).toString().getBytes();
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(putRequest);
    }
    private void eliminarWs() {
        String url = "http://70.37.160.56/api/delete-usuarios/8";
        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DELETE Response", "Usuario eliminado correctamente");
                Toast.makeText(MainActivity.this, "RESULTADO = " + response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
                Toast.makeText(MainActivity.this, "Error en la solicitud DELETE: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(this).add(deleteRequest);
    }
}