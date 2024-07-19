package com.example.layanan;

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
import com.example.project_uas.AdaptorPetugas;
import com.example.project_uas.Configurasi;
import com.example.project_uas.GetDataPetugas;
import com.example.project_uas.R;
import com.example.project_uas.simpanPetugas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityLayanan extends AppCompatActivity implements AdaptorLayanan.DataChangeListener {
    private static final int REQUEST_CODE_SIMPAN_DATA = 1;
    private static final int EDIT_DATA_REQUEST = 2;
    private static final int RELOAD_DELAY_MS = 1000; // 1 second delay
    private ListView listView;
    private ProgressBar progressBar;
    private ArrayList<GetDataLayanan> model;
    private AdaptorLayanan adaptorlayanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layanan);

        listView = findViewById(R.id.list);
        progressBar = findViewById(R.id.progressBar);

        FloatingActionButton fab = findViewById(R.id.btn_tambah);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLayanan.this, simpanLayanan.class); // Start SimpanData activity
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
        String url = new Configurasi().baseUrl() + "layanan/tampil_data_layanan.php";
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
                            model = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject getData = jsonArray.getJSONObject(i);
                                model.add(new GetDataLayanan(
                                        getData.getString("id_layanan"),
                                        getData.getString("nama_layanan"),
                                        getData.getString("deskripsi"),
                                        getData.getString("harga"),
                                        getData.getString("durasi")
                                ));
                            }

                            adaptorlayanan = new AdaptorLayanan(ActivityLayanan.this, model, progressBar, ActivityLayanan.this);
                            listView.setAdapter(adaptorlayanan);
                            Log.d("", "Data loaded successfully");

                        } catch (JSONException e) {
                            Log.e("ActivityLayanan", "JSON Parsing error: " + e.getMessage());
                            Toast.makeText(ActivityLayanan.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                            // Reload data on error
                            reloadData();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("ActivityLayanan", "Volley error: " + volleyError.getMessage());
                        Toast.makeText(ActivityLayanan.this, "Terjadi kesalahan saat memuat data", Toast.LENGTH_SHORT).show();
                        // Reload data on error
                        reloadData();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onDataChanged() {
        Log.d("ActivityLayanan", "Data change detected, reloading data");
        reloadData(); // Reload data when data changes
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_CODE_SIMPAN_DATA || requestCode == EDIT_DATA_REQUEST) && resultCode == RESULT_OK && data != null) {
            boolean refresh = data.getBooleanExtra("refreshflag", false);
            if (refresh) {
                Log.d("ActivityLayanan", "Refresh flag received, reloading data...");
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
