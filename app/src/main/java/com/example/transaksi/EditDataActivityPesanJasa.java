package com.example.transaksi;

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
import com.example.project_uas.Configurasi;
import com.example.project_uas.R;
import com.example.transaksi.simpanPesananJasa;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditDataActivityPesanJasa extends AppCompatActivity {

    private EditText tanggal_pesan, tanggal_ambil, jumlah, status_pesan;
    private Button updateButton;
    private Spinner spinnernama_petugas, spinnernama, spinnernama_layanan, spinnernama_kain;
    private ArrayList<String> nama_petugasList, namaList, nama_layananList, nama_kainList;
    private Map<String, String> nama_petugasToIdMap, namaToIdMap, nama_layananToIdMap, nama_kainToIdMap;
    private ArrayAdapter<String> adapterPesanJasa;
    private String id_pesan;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pesan_jasa);

        spinnernama_petugas = findViewById(R.id.edit_petugas2);
        spinnernama = findViewById(R.id.edit_pelanggan2);
        spinnernama_layanan = findViewById(R.id.edit_layanan2);
        spinnernama_kain = findViewById(R.id.edit_kain2);
        tanggal_pesan = findViewById(R.id.edit_tanggalpesan2);
        tanggal_ambil = findViewById(R.id.edit_tanggalambil2);
        jumlah = findViewById(R.id.edit_jumlah2);
        status_pesan = findViewById(R.id.edit_status2);
        updateButton = findViewById(R.id.btn_edit_pesan_jasa);

        // Fetch data from the database for spinnerNamaPetugas
        nama_petugasList = new ArrayList<>();
        nama_petugasToIdMap = new HashMap<>();
        fetchPetugas();

        // Spinner item selected listener for nama
        spinnernama_petugas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        // Fetch data from the database for spinnerNama_Pelanggan
        namaList = new ArrayList<>();
        namaToIdMap = new HashMap<>();
        fetchNama();

        spinnernama.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        // Fetch data from the database for spinnerNama_Layanan
        nama_layananList = new ArrayList<>();
        nama_layananToIdMap = new HashMap<>();
        fetchNamaLayanan();

        spinnernama_layanan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        // Fetch data from the database for spinnerNama_Kain
        nama_kainList = new ArrayList<>();
        nama_kainToIdMap = new HashMap<>();
        fetchNamaKain();

        spinnernama_kain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        // Mendapatkan data dari intent
        Intent intent = getIntent();

        if (intent != null) {
            id_pesan = intent.getStringExtra("id_pesan");
            tanggal_pesan.setText(intent.getStringExtra("tanggal_pesan"));
            tanggal_ambil.setText(intent.getStringExtra("tanggal_ambil"));
            jumlah.setText(intent.getStringExtra("jumlah"));
            status_pesan.setText(intent.getStringExtra("status_pesan"));
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    private void fetchPetugas() {
        String url = new Configurasi().baseUrl() + "petugas/tampil_data_petugas.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id_petugas = jsonObject.optString("id_petugas");
                                String nama_petugas = jsonObject.optString("nama_petugas");
                                Log.d("FetchNamaData", "ID: " + id_petugas + ", Nama_petugas: " + nama_petugas); // Log fetched data
                                nama_petugasList.add(nama_petugas);
                                nama_petugasToIdMap.put(nama_petugas, id_petugas);
                            }
                            adapterPesanJasa = new ArrayAdapter<>(EditDataActivityPesanJasa.this,
                                    android.R.layout.simple_spinner_item, nama_petugasList);

                            adapterPesanJasa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnernama_petugas.setAdapter(adapterPesanJasa);
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
                                String id_pelanggan = jsonObject.optString("id_pelanggan");
                                String nama = jsonObject.optString("nama");
                                Log.d("FetchBarangData", "ID: " + id_pelanggan + ", Nama: " + nama); // Log fetched data
                                namaList.add(nama);
                                namaToIdMap.put(nama, id_pelanggan);
                            }
                            adapterPesanJasa = new ArrayAdapter<>(EditDataActivityPesanJasa.this,
                                    android.R.layout.simple_spinner_item, namaList);
                            adapterPesanJasa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnernama.setAdapter(adapterPesanJasa);
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

    private void fetchNamaLayanan() {
        String url = new Configurasi().baseUrl() + "layanan/tampil_data_layanan.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id_layanan = jsonObject.optString("id_layanan");
                                String nama_layanan = jsonObject.optString("nama_layanan");
                                Log.d("FetchNamaLayananData", "ID: " + id_layanan + ", Nama_layanan: " + nama_layanan); // Log fetched data
                                nama_layananList.add(nama_layanan);
                                nama_layananToIdMap.put(nama_layanan, id_layanan);
                            }
                            adapterPesanJasa = new ArrayAdapter<>(EditDataActivityPesanJasa.this,
                                    android.R.layout.simple_spinner_item, nama_layananList);
                            adapterPesanJasa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnernama_layanan.setAdapter(adapterPesanJasa);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("FetchNamaLayananData", "JSON parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("FetchNamaLayananData", "Volley error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void fetchNamaKain() {
        String url = new Configurasi().baseUrl() + "kain/tampil_data_kain.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id_kain = jsonObject.optString("id_kain");
                                String nama_kain = jsonObject.optString("nama_kain");
                                Log.d("FetchKainData", "ID: " + id_kain + ", Nama: " + nama_kain); // Log fetched data
                                nama_kainList.add(nama_kain);
                                nama_kainToIdMap.put(nama_kain, id_kain);
                            }
                            adapterPesanJasa = new ArrayAdapter<>(EditDataActivityPesanJasa.this,
                                    android.R.layout.simple_spinner_item, nama_kainList);
                            adapterPesanJasa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnernama_kain.setAdapter(adapterPesanJasa);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("FetchKainData", "JSON parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("FetchKainData", "Volley error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }



    private void updateData() {
        final String nama_petugas = spinnernama_petugas.getSelectedItem().toString().trim();
        final String nama = spinnernama.getSelectedItem().toString().trim();
        final String nama_layanan = spinnernama_layanan.getSelectedItem().toString().trim();
        final String nama_kain = spinnernama_kain.getSelectedItem().toString().trim();
        final String tanggal_pesan1 = tanggal_pesan.getText().toString().trim();
        final String tanggal_ambil1 = tanggal_ambil.getText().toString().trim();
        final String jumlah1 = jumlah.getText().toString().trim();
        final String status_pesan1 = status_pesan.getText().toString().trim();

        // Validate input
        if (nama_petugas.isEmpty() || nama.isEmpty() || nama_layanan.isEmpty() || nama_kain.isEmpty() ||tanggal_pesan1.isEmpty() || tanggal_ambil1.isEmpty() ||  jumlah1.isEmpty() || status_pesan1.isEmpty()) {
            Toast.makeText(EditDataActivityPesanJasa.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }


        if (id_pesan == null) {
            Toast.makeText(EditDataActivityPesanJasa.this, "ID pesanan tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }


        // URL untuk skrip PHP yang akan melakukan update data
        String url = new Configurasi().baseUrl() + "transaksi/edit_data_pesan_jasa.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from server
                        Log.d("EditDataPesanJasa", "Response: " + response);
                        if (response.equalsIgnoreCase("duplicate")) {
                            Toast.makeText(EditDataActivityPesanJasa.this, "Nama dengan ID tersebut sudah ada!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditDataActivityPesanJasa.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                            sendRefreshFlag(true); // Send refresh flag to indicate data change
                            finish(); // Close the activity after successfully saving data
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("EditDataPesanJasa", "Error: " + error.getMessage());
                        Toast.makeText(EditDataActivityPesanJasa.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Create parameters for the request
                Map<String, String> params = new HashMap<>();
                params.put("id_pesan", id_pesan);
                params.put("nama_petugas", nama_petugas);
                params.put("nama", nama);
                params.put("nama_layanan", nama_layanan);
                params.put("nama_kain", nama_kain);
                params.put("tanggal_pesan", tanggal_pesan1);
                params.put("tanggal_ambil", tanggal_ambil1);
                params.put("jumlah", jumlah1);
                params.put("status_pesan", status_pesan1);
                Log.d("EditDataPesanJasa", "Params: " + params.toString()); // Log parameters for debugging
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
