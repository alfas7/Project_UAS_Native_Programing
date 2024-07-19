package com.example.pelanggan;

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

public class EditDataActivityPelanggan extends AppCompatActivity {

    private EditText editNama, editEmail, editAlamat, editNo_telepon;
    private Button updateButton;
    private String id_pelanggan;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pelanggan);

        editNama = findViewById(R.id.edit_namapelanggan);
        editEmail = findViewById(R.id.edit_email);
        editAlamat = findViewById(R.id.edit_alamat);
        editNo_telepon = findViewById(R.id.edit_no_telpon);
        updateButton = findViewById(R.id.btn_edit);

        // Mendapatkan data dari intent
        Intent intent = getIntent();
        if (intent != null) {
            id_pelanggan = intent.getStringExtra("id_pelanggan");
            editNama.setText(intent.getStringExtra("nama"));
            editEmail.setText(intent.getStringExtra("email"));
            editAlamat.setText(intent.getStringExtra("alamat"));
            editNo_telepon.setText(intent.getStringExtra("no_telpon"));

        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    private void updateData() {
        final String nama = editNama.getText().toString().trim();
        final String email = editEmail.getText().toString().trim();
        final String alamat = editAlamat.getText().toString().trim();
        final String no_telpon = editNo_telepon.getText().toString().trim();


        // Validasi input
        if (nama.isEmpty() || email.isEmpty() || alamat.isEmpty() || no_telpon.isEmpty()) {
            Toast.makeText(EditDataActivityPelanggan.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL untuk skrip PHP yang akan melakukan update data
        String url = new Configurasi().baseUrl() + "update_data.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("EditDataActivityPelanggan", "Response: " + response);
                        Toast.makeText(EditDataActivityPelanggan.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true); // Mengirim flag refresh untuk memuat ulang data
                        finish(); // Tutup aktivitas
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("EditDataActivityPelanggan", "Error: " + error.getMessage());
                        Toast.makeText(EditDataActivityPelanggan.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("id_pelanggan", id_pelanggan);
                params.put("nama", nama);
                params.put("email", email);
                params.put("alamat", alamat);
                params.put("no_telpon", no_telpon);
                Log.d("EditDataActivityPelanggan", "Params: " + params.toString()); // Log parameter untuk debugging
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
