package com.mif.smartmommy.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.mif.smartmommy.MainActivity;
import com.mif.smartmommy.R;
import com.mif.smartmommy.activity.PilihAnakActivity;
import com.mif.smartmommy.configfile.ServerApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText nama, username, email, password, status;
    MaterialButton buttonsimpan;
    TextView login;

    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        getSupportActionBar().hide();

        status.setInputType(InputType.TYPE_NULL);
        status.setFocusable(false);
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Pilih Status Wali");

                // buat array list
                final String[] options = {"Orang tua balita", "Bidan", "Kader Posyandu"};

                //Pass array list di Alert dialog
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        status.setText(options[which]);
                    }
                });
                // buat dan tampilkan alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent log = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(log);
                finish();
            }
        });

        buttonsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpan();
            }
        });
    }

    private void init(){
        progressDialog = new ProgressDialog(this);
        requestQueue = Volley.newRequestQueue(this);
        nama = findViewById(R.id.edt_nama_register);
        username = findViewById(R.id.edt_username_register);
        email = findViewById(R.id.edt_email_register);
        password = findViewById(R.id.edt_password_register);
        buttonsimpan = findViewById(R.id.btn_simpan_register);
        status = findViewById(R.id.edt_status_register);
        login = findViewById(R.id.txt_login);
    }

    public void simpan(){

        final String namanya = nama.getText().toString().trim();
        final String usernamenya = username.getText().toString().trim();
        final String emailnya = email.getText().toString().trim();
        final String passwordnya = password.getText().toString().trim();
        final String statusnya = status.getText().toString().trim();

        if (namanya.matches("")){
            Toast.makeText(RegisterActivity.this, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (usernamenya.matches("")){
            Toast.makeText(RegisterActivity.this, "Username tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (passwordnya.matches("")){
            Toast.makeText(RegisterActivity.this, "Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (emailnya.matches("")){
            Toast.makeText(RegisterActivity.this, "Email tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (statusnya.matches("")){
            Toast.makeText(RegisterActivity.this, "Status tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerApi.URL_REGIS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d("TAG", "onResponse: " + jsonObject.toString());

                    Boolean status = jsonObject.getBoolean("status");
                    String message = jsonObject.getString("message");
                    if (status.equals(true)){
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                        Intent logeng = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(logeng);
                        finish();
                    } else if (status.equals(false)) {
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    Log.d("TAG", "onResponse: message " + message);
                    Log.d("TAG", "onResponse: status " + status);
                } catch (JSONException e){
                    Log.e("TAG", "onResponse: " + e.getMessage());
                    Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("TAG", "onErrorResponse: " + error.getMessage());
                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", namanya);
                params.put("username", usernamenya);
                params.put("password", passwordnya);
                params.put("email", emailnya);
                params.put("status", statusnya);
                Log.e("data dari param: ", "" + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(25000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        Intent abc = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(abc);
        finish();
    }

}