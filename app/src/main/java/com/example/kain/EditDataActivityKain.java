package com.example.kain;

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

public class EditDataActivityKain extends AppCompatActivity {

    private EditText editNama_kain, editDeskripsi;
    private Button updateButton;
    private String id_kain;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kain);

        editNama_kain = findViewById(R.id.edit_namakain);
        editDeskripsi = findViewById(R.id.edit_deskripsi);
        updateButton = findViewById(R.id.btn_edit);

        // Mendapatkan data dari intent
        Intent intent = getIntent();
        if (intent != null) {
            id_kain = intent.getStringExtra("id_kain");
            editNama_kain.setText(intent.getStringExtra("nama_kain"));
            editDeskripsi.setText(intent.getStringExtra("deskripsi"));

        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    private void updateData() {
        final String nama_kain = editNama_kain.getText().toString().trim();
        final String deskripsi = editDeskripsi.getText().toString().trim();


        // Validasi input
        if (nama_kain.isEmpty() || deskripsi.isEmpty()) {
            Toast.makeText(EditDataActivityKain.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL untuk skrip PHP yang akan melakukan update data
        String url = new Configurasi().baseUrl() + "kain/update_data_kain.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("EditDataActivityKain", "Response: " + response);
                        Toast.makeText(EditDataActivityKain.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true); // Mengirim flag refresh untuk memuat ulang data
                        finish(); // Tutup aktivitas
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("EditDataActivityKain", "Error: " + error.getMessage());
                        Toast.makeText(EditDataActivityKain.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("id_kain", id_kain);
                params.put("nama_kain", nama_kain);
                params.put("deskripsi", deskripsi);
                Log.d("EditDataActivityKain", "Params: " + params.toString()); // Log parameter untuk debugging
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
