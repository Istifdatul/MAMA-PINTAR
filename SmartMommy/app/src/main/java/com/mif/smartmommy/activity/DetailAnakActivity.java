package com.mif.smartmommy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mif.smartmommy.MainActivity;
import com.mif.smartmommy.R;
import com.mif.smartmommy.configfile.ServerApi;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailAnakActivity extends AppCompatActivity {
    TextView nama, tgllahir, jenisklmn, umur;
    ImageView imageView;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    String id_anak, linkfoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_anak);
        init();
        getSupportActionBar().hide();

        Intent intent = getIntent();
        id_anak = intent.getStringExtra("id_anak");
        Log.e("idnak", id_anak);
        loadAnak();
    }

    private void init(){
        progressDialog = new ProgressDialog(this);
        requestQueue = Volley.newRequestQueue(this);
        nama = findViewById(R.id.text_nama_detail);
        tgllahir = findViewById(R.id.text_tgllahir_detail);
        jenisklmn = findViewById(R.id.text_jeniskelamin_detail);
        umur = findViewById(R.id.text_umur_detail);
        imageView = findViewById(R.id.image_detail_foto);
    }

    public void loadAnak(){
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerApi.URL_ANAK + "/detailanak?id_anak=" + id_anak, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");
                    JSONObject datanya = data.getJSONObject(0);
                    nama.setText(":  " + datanya.getString("nama_anak"));
                    tgllahir.setText(":  " + datanya.getString("tanggal_lahir"));
                    jenisklmn.setText(":  " + datanya.getString("jenis_kelamin"));
                    umur.setText(":  " + datanya.getString("umur"));
                    linkfoto = ServerApi.FotoAnak + datanya.getString("foto_anak");
                    Picasso.get().load(linkfoto).into(imageView);
//                    foto = ServerApi.FotoUser + authdataa.getFoto();
//                    Picasso.get().load(foto).into(circleImageView);
                } catch (JSONException e){
                    Toast.makeText(DetailAnakActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DetailAnakActivity.this, "Data tidak ada", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent abc = new Intent(DetailAnakActivity.this, MainActivity.class);
        startActivity(abc);
        finish();
    }
}