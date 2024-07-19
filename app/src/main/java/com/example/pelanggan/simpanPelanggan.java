package com.example.pelanggan;

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

public class simpanPelanggan extends AppCompatActivity {

    private EditText uploadNama, uploadEmail, uploadAlamat, uploadNo_telepon;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simpan_pelanggan);

        // Inisialisasi komponen UI
        uploadNama = findViewById(R.id.namapelanggan);
        uploadEmail = findViewById(R.id.email);
        uploadAlamat = findViewById(R.id.alamat);
        uploadNo_telepon = findViewById(R.id.no_telpon);
        saveButton = findViewById(R.id.btn_simpan);

        // Setel listener tombol simpan
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {simpanPelanggan();
            }
        });
    }

    private void simpanPelanggan() {
        // Ambil input pengguna
        final String nama = uploadNama.getText().toString().trim();
        final String email = uploadEmail.getText().toString().trim();
        final String alamat = uploadAlamat.getText().toString().trim();
        final String no_telpon = uploadNo_telepon.getText().toString().trim();


        // Validasi input
        if (nama.isEmpty() || email.isEmpty() || alamat.isEmpty() || no_telpon.isEmpty()) {
            Toast.makeText(simpanPelanggan.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL untuk skrip PHP yang akan menyimpan data
        String url = new Configurasi().baseUrl() + "simpan_data.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("SimpanData", "Response: " + response);
                        Toast.makeText(simpanPelanggan.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true); // Send refresh flag to indicate data change
                        finish(); // Tutup aktivitas setelah sukses menyimpan data
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("SimpanData", "Error: " + error.getMessage());
                        Toast.makeText(simpanPelanggan.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("email", email);
                params.put("alamat", alamat);
                params.put("no_telpon", no_telpon);
                Log.d("simpanPelanggan", "Params: " + params.toString()); // Log parameter untuk debugging
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
