package com.example.proyectapp_web;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText txtUser, txtNombre, txtemail, txtrol, txtimg;
    Button btnEnviar, btnMostrar, btnEliminar;
    RequestQueue requestQueue;
    private ListView listView;
    private CustomAdapter adapter;
    private boolean isRequestInProgress = false;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        txtUser = findViewById(R.id.txtUser);
        txtNombre = findViewById(R.id.txtNombre);
        txtemail = findViewById(R.id.txtemail);
        txtrol = findViewById(R.id.txtrol);
        txtimg = findViewById(R.id.txtimg);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnMostrar = findViewById(R.id.btnMostrar);
        btnEliminar = findViewById(R.id.btnEliminar);
        listView = findViewById(R.id.listView);
        adapter = new CustomAdapter(this, R.layout.activity_list_item_user, new ArrayList<User>());
        listView.setAdapter(adapter);
        btnEnviar.setOnClickListener(view -> {
            actualizarWs(txtimg.getText().toString(), txtrol.getText().toString(), txtemail.getText().toString(), txtNombre.getText().toString());
        });
        btnMostrar.setOnClickListener(view -> {
            LeerWs();
        });
        btnEliminar.setOnClickListener(view -> {
            eliminarWs();
        });

    }
    private void LeerWs() {
        String url = "http://70.37.160.56/api/get-usuarios";
        StringRequest getRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    adapter.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String nombre = jsonObject.getString("nombre");
                        String email = jsonObject.getString("email");
                        String rol = jsonObject.getString("rol");
                        String img = jsonObject.getString("img");
                        User user = new User(id, nombre, email, rol, img);
                        adapter.add(user);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    mostrarMensajeError("Error al analizar la respuesta JSON");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "Error en la solicitud: " + error.getMessage();
                if (error.networkResponse != null) {
                    errorMessage += "\nStatus Code: " + error.networkResponse.statusCode;
                }
                mostrarMensajeError(errorMessage);
            }
        });
        requestQueue.add(getRequest);
    }
    private void mostrarMensajeError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
    private void actualizarWs(final String img, final String rol, final String email, final String nombre) {
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
                params.put("nombre", nombre);
                params.put("email", email);
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
        String url = "http://70.37.160.56/api/delete-usuarios/14";
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
