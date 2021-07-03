package com.mif.smartmommy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mif.smartmommy.MainActivity;
import com.mif.smartmommy.R;
import com.mif.smartmommy.adapter.AdapterListAnak;
import com.mif.smartmommy.adapter.AdapterListRiwayat;
import com.mif.smartmommy.configfile.ServerApi;
import com.mif.smartmommy.configfile.authdata;
import com.mif.smartmommy.model.ModelListAnak;
import com.mif.smartmommy.model.ModelRiwayat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RiwayatActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<ModelRiwayat> item;
    AdapterListRiwayat adapterListRiwayat;

    RequestQueue requestQueue;
    authdata authdataa;
    ProgressDialog progressDialog;

    String mIdUser, mIdAnak;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
        Intent intent = getIntent();
        getSupportActionBar().hide();
        mIdAnak = intent.getStringExtra("id_anak");

        authdataa = new authdata(this);
        progressDialog = new ProgressDialog(this);
        requestQueue = Volley.newRequestQueue(this);

        mIdUser = authdataa.getId_user();
        recyclerView = findViewById(R.id.recyclerRiwayat);

        loadRiwayat();
    }

    private void loadRiwayat(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerApi.URL_ANAK + "/riwayat?id_anak=" + mIdAnak + "&" + "id_user=" + mIdUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");

                    item = new ArrayList<>();
                    for (int i = 0; i < data.length(); i++)
                    {
                        ModelRiwayat modelRiwayat = new ModelRiwayat();
                        JSONObject datanya = data.getJSONObject(i);
                        modelRiwayat.setId_anak(datanya.getString("id_anak"));
                        modelRiwayat.setNama_anak(datanya.getString("nama_anak"));
                        modelRiwayat.setTanggal_lahir(datanya.getString("tanggal_lahir"));
                        modelRiwayat.setRiwayat(datanya.getDouble("total_point"));
                        modelRiwayat.setFoto_anak(datanya.getString("foto_anak"));
                        item.add(modelRiwayat);
                    }
                    adapterListRiwayat = new AdapterListRiwayat(RiwayatActivity.this, item);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RiwayatActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapterListRiwayat);
                } catch (JSONException e) {
                    Toast.makeText(RiwayatActivity.this, "Data tidak ada", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RiwayatActivity.this, "Data tidak ada", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent abc = new Intent(RiwayatActivity.this, MainActivity.class);
        startActivity(abc);
        finish();
    }
}