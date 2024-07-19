package com.example.layanan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
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

public class EditDataActivityLayanan extends AppCompatActivity {

    private EditText editNama_layanan, editDeskripsi, editHarga, editDurasi;
    private Button updateButton;
    private String id_layanan;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_layanan);

        editNama_layanan = findViewById(R.id.edit_namalayanan);
        editDeskripsi = findViewById(R.id.edit_deskripsi);
        editHarga = findViewById(R.id.edit_harga);
        editDurasi = findViewById(R.id.edit_durasi);
        updateButton = findViewById(R.id.btn_edit);

        // Mendapatkan data dari intent
        Intent intent = getIntent();
        if (intent != null) {
            id_layanan = intent.getStringExtra("id_layanan");
            editNama_layanan.setText(intent.getStringExtra("nama_layanan"));
            editDeskripsi.setText(intent.getStringExtra("deskripsi"));
            editHarga.setText(intent.getStringExtra("harga"));
            editDurasi.setText(intent.getStringExtra("durasi"));

        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    private void updateData() {
        final String nama_layanan = editNama_layanan.getText().toString().trim();
        final String deskripsi = editDeskripsi.getText().toString().trim();
        final String harga = editHarga.getText().toString().trim();
        final String durasi = editDurasi.getText().toString().trim();


        // Validasi input
        if (nama_layanan.isEmpty() || deskripsi.isEmpty() ||harga.isEmpty() || durasi.isEmpty() ) {
            Toast.makeText(EditDataActivityLayanan.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL untuk skrip PHP yang akan melakukan update data
        String url = new Configurasi().baseUrl() + "layanan/update_data_layanan.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("EditDataActivityLayanan", "Response: " + response);
                        Toast.makeText(EditDataActivityLayanan.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true); // Mengirim flag refresh untuk memuat ulang data
                        finish(); // Tutup aktivitas
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("EditDataActivityLayanan", "Error: " + error.getMessage());
                        Toast.makeText(EditDataActivityLayanan.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("id_layanan", id_layanan);
                params.put("nama_layanan", nama_layanan);
                params.put("deskripsi", deskripsi);
                params.put("harga", harga);
                params.put("durasi", durasi);
                Log.d("EditDataActivityLayanan", "Params: " + params.toString()); // Log parameter untuk debugging
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
