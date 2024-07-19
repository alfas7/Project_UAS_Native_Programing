package com.example.project_uas;

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

import java.util.HashMap;
import java.util.Map;

public class EditDataActivityPetugas extends AppCompatActivity {

    private EditText editNama_petugas, editJabatan, editNo_telepon;
    private Button updateButton;
    private String id_petugas;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_petugas);

        editNama_petugas = findViewById(R.id.edit_nama_petugas);
        editJabatan = findViewById(R.id.edit_jabatan);
        editNo_telepon = findViewById(R.id.edit_no_telpon);
        updateButton = findViewById(R.id.btn_edit);

        // Mendapatkan data dari intent
        Intent intent = getIntent();
        if (intent != null) {
            id_petugas = intent.getStringExtra("id_petugas");
            editNama_petugas.setText(intent.getStringExtra("nama_petugas"));
            editJabatan.setText(intent.getStringExtra("jabatan"));
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
        final String nama_petugas = editNama_petugas.getText().toString().trim();
        final String jabatan = editJabatan.getText().toString().trim();
        final String no_telpon = editNo_telepon.getText().toString().trim();


        // Validasi input
        if (nama_petugas.isEmpty() || jabatan.isEmpty() || no_telpon.isEmpty()) {
            Toast.makeText(EditDataActivityPetugas.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL untuk skrip PHP yang akan melakukan update data
        String url = new Configurasi().baseUrl() + "petugas/update_data_petugas.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("EditDataActivityPetugas", "Response: " + response);
                        Toast.makeText(EditDataActivityPetugas.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true); // Mengirim flag refresh untuk memuat ulang data
                        finish(); // Tutup aktivitas
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("EditDataActivityPetugas", "Error: " + error.getMessage());
                        Toast.makeText(EditDataActivityPetugas.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("id_petugas", id_petugas);
                params.put("nama_petugas", nama_petugas);
                params.put("jabatan", jabatan);
                params.put("no_telpon", no_telpon);
                Log.d("EditDataActivityPetugas", "Params: " + params.toString()); // Log parameter untuk debugging
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
