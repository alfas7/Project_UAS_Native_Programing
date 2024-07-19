package com.example.kain;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_uas.Configurasi;
import com.example.project_uas.R;

import java.util.HashMap;
import java.util.Map;

public class simpanKain extends AppCompatActivity {

    private EditText uploadNama_kain, uploadDeskripsi;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simpan_kain);

        // Inisialisasi komponen UI
        uploadNama_kain = findViewById(R.id.namakain);
        uploadDeskripsi = findViewById(R.id.deskripsi);
        saveButton = findViewById(R.id.btn_simpan);

        // Setel listener tombol simpan
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {simpanKain();
            }
        });
    }

    private void simpanKain() {
        // Ambil input pengguna
        final String nama_kain = uploadNama_kain.getText().toString().trim();
        final String deskripsi = uploadDeskripsi.getText().toString().trim();


        // Validasi input
        if (nama_kain.isEmpty() || deskripsi.isEmpty()) {
            Toast.makeText(simpanKain.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL untuk skrip PHP yang akan menyimpan data
        String url = new Configurasi().baseUrl() + "kain/simpan_data_kain.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("SimpanData", "Response: " + response);
                        Toast.makeText(simpanKain.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true); // Send refresh flag to indicate data change
                        finish(); // Tutup aktivitas setelah sukses menyimpan data
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("SimpanData", "Error: " + error.getMessage());
                        Toast.makeText(simpanKain.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("nama_kain", nama_kain);
                params.put("deskripsi", deskripsi);
                Log.d("simpanKain", "Params: " + params.toString()); // Log parameter untuk debugging
                return params;
            }
        };

        // Tambahkan request ke RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void sendRefreshFlag(boolean refresh) {
        Intent intent = new Intent();
        intent.putExtra("refreshflag", refresh);
        setResult(RESULT_OK, intent);
    }
}
