package com.example.project_uas;

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

import java.util.HashMap;
import java.util.Map;

public class simpanPetugas extends AppCompatActivity {

    private EditText uploadNama_petugas, uploadJabatan, uploadNo_telepon;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simpan_petugas);

        // Inisialisasi komponen UI
        uploadNama_petugas = findViewById(R.id.nama_petugas);
        uploadJabatan = findViewById(R.id.jabatan);
        uploadNo_telepon = findViewById(R.id.no_telpon);
        saveButton = findViewById(R.id.btn_simpan);

        // Setel listener tombol simpan
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {simpanPetugas();
            }
        });
    }

    private void simpanPetugas() {
        // Ambil input pengguna
        final String nama_petugas = uploadNama_petugas.getText().toString().trim();
        final String jabatan = uploadJabatan.getText().toString().trim();
        final String no_telpon = uploadNo_telepon.getText().toString().trim();


        // Validasi input
        if (nama_petugas.isEmpty() || jabatan.isEmpty() || no_telpon.isEmpty()) {
            Toast.makeText(simpanPetugas.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL untuk skrip PHP yang akan menyimpan data
        String url = new Configurasi().baseUrl() + "petugas/simpan_data_petugas.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("SimpanData", "Response: " + response);
                        Toast.makeText(simpanPetugas.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true); // Send refresh flag to indicate data change
                        finish(); // Tutup aktivitas setelah sukses menyimpan data
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("SimpanData", "Error: " + error.getMessage());
                        Toast.makeText(simpanPetugas.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("nama_petugas", nama_petugas);
                params.put("jabatan", jabatan);
                params.put("no_telpon", no_telpon);
                Log.d("simpanPetugas", "Params: " + params.toString()); // Log parameter untuk debugging
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
