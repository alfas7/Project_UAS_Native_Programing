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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_uas.Configurasi;
import com.example.project_uas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class simpanPembayaran extends AppCompatActivity {

    private EditText jumlahBayar, metodeBayar, tanggalBayar;
    private Button saveButton;
    private Spinner spinnerIdPesan, spinnerNama;
    private ArrayList<String> idPesanList, namaList;
    private Map<String, String> idPesanToIdMap, namaToIdMap;
    private ArrayAdapter<String> adapterPembayaran;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simpan_pembayaran);

        // Initialize UI components
        saveButton = findViewById(R.id.btn_pembayaran);
        spinnerIdPesan = findViewById(R.id.spinner_pesan_jasa);
        spinnerNama = findViewById(R.id.spinner_nama);
        jumlahBayar = findViewById(R.id.jumlahbayar6);
        metodeBayar = findViewById(R.id.metodebayar6);
        tanggalBayar = findViewById(R.id.tanggalbayar6);

        // Fetch data from the database for spinnerIdPesan
        idPesanList = new ArrayList<>();
        idPesanToIdMap = new HashMap<>();
        fetchIdPesan();

        // Spinner item selected listener for idPesan
        spinnerIdPesan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    String item = adapterView.getItemAtPosition(position).toString();
                    // Do something with the selected item
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        // Fetch data from the database for spinnerNama
        namaList = new ArrayList<>();
        namaToIdMap = new HashMap<>();
        fetchNama();

        spinnerNama.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    String item = adapterView.getItemAtPosition(position).toString();
                    // Do something with the selected item
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        // Set listener for save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanData();
            }
        });
    }

    private void fetchIdPesan() {
        String url = new Configurasi().baseUrl() + "transaksi/tampil_data_pesan_jasa.php";

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
                            adapterPembayaran = new ArrayAdapter<>(simpanPembayaran.this,
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
                            adapterPembayaran = new ArrayAdapter<>(simpanPembayaran.this,
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

    private void simpanData() {
        // Get user input
        final String idPesan = spinnerIdPesan.getSelectedItem().toString().trim();
        final String nama = spinnerNama.getSelectedItem().toString().trim();
        final String jumlahBayarInput = jumlahBayar.getText().toString().trim();
        final String metodeBayarInput = metodeBayar.getText().toString().trim();
        final String tanggalBayarInput = tanggalBayar.getText().toString().trim();

        // Validate input
        if (idPesan.isEmpty() || nama.isEmpty() || jumlahBayarInput.isEmpty() || metodeBayarInput.isEmpty() || tanggalBayarInput.isEmpty()) {
            Toast.makeText(simpanPembayaran.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL for the PHP script to save data
        String url = new Configurasi().baseUrl() + "transaksi/simpan_data_pembayaran.php";

        // Create a new request
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from server
                        Log.d("SimpanData", "Response: " + response);
                        if (response.equalsIgnoreCase("duplicate")) {
                            Toast.makeText(simpanPembayaran.this, "Nama dengan ID tersebut sudah ada!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(simpanPembayaran.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                            sendRefreshFlag(true); // Send refresh flag to indicate data change
                            finish(); // Close the activity after successfully saving data
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.e("SimpanData", "Error: " + error.getMessage());
                        Toast.makeText(simpanPembayaran.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Create parameters for the request
                Map<String, String> params = new HashMap<>();
                params.put("id_pesan", idPesan);
                params.put("nama", nama);
                params.put("jumlah_bayar", jumlahBayarInput);
                params.put("metode_bayar", metodeBayarInput);
                params.put("tanggal_bayar", tanggalBayarInput);
                Log.d("SimpanData", "Params: " + params.toString()); // Log parameters for debugging
                return params;
            }
        };

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void sendRefreshFlag(boolean refresh) {
        Intent intent = new Intent();
        intent.putExtra("refreshflag", refresh);
        setResult(RESULT_OK, intent);
    }
}
