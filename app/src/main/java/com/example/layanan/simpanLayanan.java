package com.example.layanan;

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

public class simpanLayanan extends AppCompatActivity {

    private EditText uploadNama_layanan, uploadDeskripsi, uploadHarga, uploadDurasi;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simpan_layanan);

        // Inisialisasi komponen UI
        uploadNama_layanan = findViewById(R.id.namalayanan);
        uploadDeskripsi = findViewById(R.id.deskripsi);
        uploadHarga = findViewById(R.id.harga);
        uploadDurasi = findViewById(R.id.durasi);
        saveButton = findViewById(R.id.btn_simpan);

        // Setel listener tombol simpan
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {simpanLayanan();
            }
        });
    }

    private void simpanLayanan() {
        // Ambil input pengguna
        final String nama_layanan = uploadNama_layanan.getText().toString().trim();
        final String deskripsi = uploadDeskripsi.getText().toString().trim();
        final String harga = uploadHarga.getText().toString().trim();
        final String durasi = uploadDurasi.getText().toString().trim();


        // Validasi input
        if (nama_layanan.isEmpty() || deskripsi.isEmpty() ||harga.isEmpty() || durasi.isEmpty()) {
            Toast.makeText(simpanLayanan.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL untuk skrip PHP yang akan menyimpan data
        String url = new Configurasi().baseUrl() + "layanan/simpan_data_layanan.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("SimpanData", "Response: " + response);
                        Toast.makeText(simpanLayanan.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true); // Send refresh flag to indicate data change
                        finish(); // Tutup aktivitas setelah sukses menyimpan data
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("SimpanData", "Error: " + error.getMessage());
                        Toast.makeText(simpanLayanan.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("nama_layanan", nama_layanan);
                params.put("deskripsi", deskripsi);
                params.put("harga", harga);
                params.put("durasi", durasi);
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
