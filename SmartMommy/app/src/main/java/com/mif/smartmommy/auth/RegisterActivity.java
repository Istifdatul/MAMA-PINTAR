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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
                if (nama.getText().toString().matches("")){
                    Toast.makeText(RegisterActivity.this, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                } else if (username.getText().toString().matches("")){
                    Toast.makeText(RegisterActivity.this, "Username tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                } else if (email.getText().toString().matches("")){
                    Toast.makeText(RegisterActivity.this, "Email tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().matches("")){
                    Toast.makeText(RegisterActivity.this, "Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }
                simpan();
            }
        });
    }

    private void init(){
        progressDialog = new ProgressDialog(this);
        requestQueue = Volley.newRequestQueue(this);
        nama = findViewById(R.id.edt_nama_regis);
        username = findViewById(R.id.edt_username_regis);
        email = findViewById(R.id.edt_email_regis);
        password = findViewById(R.id.edt_password_regis);
        buttonsimpan = findViewById(R.id.btn_simpan_register);
        status = findViewById(R.id.edt_status_regis);
        login = findViewById(R.id.txt_login);
    }

    public void simpan(){
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerApi.URL_REGIS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.matches("false")){
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                        Intent done = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(done);
                        finish();
                    }
                } catch (JSONException e){
                    Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Gagal Mendaftar", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama.getText().toString().trim());
                params.put("username", username.getText().toString().trim());
                params.put("password", password.getText().toString().trim());
                params.put("email", email.getText().toString().trim());
                params.put("status", status.getText().toString().trim());
                Log.e("data dari param: ", "" + params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        Intent abc = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(abc);
        finish();
    }

}