package com.example.pembayaran;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.layanan.simpanLayanan;
import com.example.project_uas.Configurasi;
import com.example.project_uas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditDataActivityPembayaran extends AppCompatActivity {
    private EditText jumlahBayar, metodeBayar, tanggalBayar;
    private Button saveButton;
    private Spinner spinnerIdPesan, spinnerNama;
    private ArrayList<String> idPesanList, namaList;
    private Map<String, String> idPesanToIdMap, namaToIdMap;
    private ArrayAdapter<String> adapterPembayaran;
    private String id_pembayaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pembayaran);

        // Inisialisasi komponen UI
        saveButton = findViewById(R.id.btn_edit);
        spinnerIdPesan = findViewById(R.id.edit_pesan_jasa5);
        spinnerNama = findViewById(R.id.edit_pelanggan5);
        jumlahBayar = findViewById(R.id.edit_jumlahbayar5);
        metodeBayar = findViewById(R.id.edit_metodebayar5);
        tanggalBayar = findViewById(R.id.edit_tanggalbayar5);

        // Ambil data dari Intent
        Intent intent = getIntent();
        if (intent != null) {
            id_pembayaran = intent.getStringExtra("id_pembayaran");
            jumlahBayar.setText(intent.getStringExtra("jumlah_bayar"));
            metodeBayar.setText(intent.getStringExtra("metode_bayar"));
            tanggalBayar.setText(intent.getStringExtra("tanggal_bayar"));
        }

        // Fetch data dari database untuk spinnerIdPesan
        idPesanList = new ArrayList<>();
        idPesanToIdMap = new HashMap<>();
        fetchIdPesan();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
//                Toast.makeText(EditDataActivityPembayaran.this,,Toast.LENGTH_SHORT).show();
            }
        });

        // Spinner item selected listener untuk idPesan
        spinnerIdPesan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    String item = adapterView.getItemAtPosition(position).toString();
                    // Lakukan sesuatu dengan item yang dipilih
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Tidak melakukan apa-apa
            }
        });

        // Fetch data dari database untuk spinnerNama
        namaList = new ArrayList<>();
        namaToIdMap = new HashMap<>();
        fetchNama();

        spinnerNama.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    String item = adapterView.getItemAtPosition(position).toString();
                    // Lakukan sesuatu dengan item yang dipilih
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Tidak melakukan apa-apa
            }
        });

        // Set listener untuk save button
    }

    private void updateData() {
        final String id_pembayaran1 = id_pembayaran;
        final String nama = spinnerNama.getSelectedItem().toString();
        final String id_pesan = spinnerIdPesan.getSelectedItem().toString();
        final String jumlahBayar1 = jumlahBayar.getText().toString().trim();
        final String metodeBayar1 = metodeBayar.getText().toString().trim();
        final String tanggalBayar1 = tanggalBayar.getText().toString().trim();

        String url = new Configurasi().baseUrl() + "transaksi/edit_data_pembayaran.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("SimpanData", "Response: " + response);
                        Toast.makeText(EditDataActivityPembayaran.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true); // Send refresh flag to indicate data change
                        finish(); // Tutup aktivitas setelah sukses menyimpan data
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("SimpanData", "Error: " + error.getMessage());
                        Toast.makeText(EditDataActivityPembayaran.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("id_pembayaran", id_pembayaran1);
                params.put("id_pesan", id_pesan);
                params.put("nama", nama);
                params.put("jumlah_bayar", jumlahBayar1);
                params.put("metode_bayar", metodeBayar1);
                params.put("tanggal_bayar", tanggalBayar1);
                Log.d("simpanPetugas", "Params: " + params.toString()); // Log parameter untuk debugging
                return params;
            }
        };

        // Tambahkan request ke RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void fetchIdPesan() {
        String url = new Configurasi() .baseUrl() + "transaksi/tampil_data_pesan_jasa.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String idPesan = jsonObject.optString("id_pesan");
                                idPesanList.add(idPesan);
                            }
                            adapterPembayaran = new ArrayAdapter<>(EditDataActivityPembayaran.this,
                                    android.R.layout.simple_spinner_item, idPesanList);
                            adapterPembayaran.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerIdPesan.setAdapter(adapterPembayaran);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("FetchIdPesanData", "JSON parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("FetchIdPesanData", "Volley error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void fetchNama() {
        String url = new Configurasi().baseUrl() + "tampil_data_pelanggan.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String idPelanggan = jsonObject.optString("id_pelanggan");
                                String nama = jsonObject.optString("nama");
                                Log.d("FetchNamaData", "ID: " + idPelanggan + ", Nama: " + nama); // Log fetched data
                                namaList.add(nama);
                                namaToIdMap.put(nama, idPelanggan);
                            }
                            adapterPembayaran = new ArrayAdapter<>(EditDataActivityPembayaran.this,
                                    android.R.layout.simple_spinner_item, namaList);
                            adapterPembayaran.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerNama.setAdapter(adapterPembayaran);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("FetchNamaData", "JSON parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("FetchNamaData", "Volley error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }



    private void sendRefreshFlag(boolean refresh) {
        Intent intent = new Intent();
        intent.putExtra("refreshflag", refresh);
        setResult(RESULT_OK, intent);
    }
}
