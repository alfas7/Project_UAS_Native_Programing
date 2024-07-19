package com.example.pembayaran;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_uas.Configurasi;
import com.example.project_uas.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityPembayaran extends AppCompatActivity implements AdaptorPembayaran.DataChangeListener {
    private static final int REQUEST_CODE_SIMPAN_DATA = 1;
    private static final int EDIT_DATA_REQUEST = 2;
    private static final int RELOAD_DELAY_MS = 1000; // 1 second delay
    private ListView listView;
    private ProgressBar progressBar;
    private ArrayList<GetDataPembayaran> model1;
    private AdaptorPembayaran adaptorPembayaran;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        listView = findViewById(R.id.list1_pembayaran);
        progressBar = findViewById(R.id.progressBar);

        FloatingActionButton fab = findViewById(R.id.btn_tambah_pembayaran);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityPembayaran.this, simpanPembayaran.class); // Start SimpanData activity
                startActivityForResult(intent, REQUEST_CODE_SIMPAN_DATA);
            }
        });

        loadData(); // Initial data load
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(); // Load data again when the activity resumes
    }

    private void loadData() {
        String url = new Configurasi().baseUrl() + "transaksi/tampil_data_pembayaran.php";
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            model1 = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject getData = jsonArray.getJSONObject(i);
                                String idPembayaran = getData.optString("id_pembayaran");
                                String idPesan = getData.optString("id_pesan");
                                String id_pelanggan = getData.optString("id_pelanggan"); // Default to "N/A" if "nama" is missing
                                String jumlahBayar = getData.optString("jumlah_bayar");
                                String metodeBayar = getData.optString("metode_bayar");
                                String tanggalBayar = getData.optString("tanggal_bayar");

                                model1.add(new GetDataPembayaran(
                                        idPembayaran,
                                        idPesan,
                                        id_pelanggan,
                                        jumlahBayar,
                                        metodeBayar,
                                        tanggalBayar
                                ));
                            }

                            adaptorPembayaran = new AdaptorPembayaran(ActivityPembayaran.this, model1, progressBar, ActivityPembayaran.this);
                            listView.setAdapter(adaptorPembayaran);
                            Log.d("ActivityPembayaran", "Data loaded successfully");

                        } catch (JSONException e) {
                            Log.e("ActivityPembayaran", "JSON Parsing error: " + e.getMessage());
                            Toast.makeText(ActivityPembayaran.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                            // Reload data on error
                            reloadData();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("ActivityPembayaran", "Volley error: " + volleyError.getMessage());
                        Toast.makeText(ActivityPembayaran.this, "Terjadi kesalahan saat memuat data", Toast.LENGTH_SHORT).show();
                        // Reload data on error
                        reloadData();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onDataChanged() {
        Log.d("ActivityPembayaran", "Data change detected, reloading data");
        reloadData(); // Reload data when data changes
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_CODE_SIMPAN_DATA || requestCode == EDIT_DATA_REQUEST) && resultCode == RESULT_OK && data != null) {
            boolean refresh = data.getBooleanExtra("refreshflag", false);
            if (refresh) {
                Log.d("ActivityPembayaran", "Refresh flag received, reloading data...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData(); // Reload data
                    }
                }, RELOAD_DELAY_MS);
            }
        }
    }

    private void reloadData() {
        loadData(); // Reload data
    }
}
