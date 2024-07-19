package com.example.layanan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_uas.Configurasi;
import com.example.project_uas.EditDataActivityPetugas;
import com.example.project_uas.GetDataPetugas;
import com.example.project_uas.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdaptorLayanan extends BaseAdapter {
    private Context context;
    private ArrayList<GetDataLayanan> model;
    private LayoutInflater inflater;
    private ProgressBar progressBar;
    private DataChangeListener dataChangeListener;

    public interface DataChangeListener {
        void onDataChanged();
    }

    public AdaptorLayanan(Context context, ArrayList<GetDataLayanan> model, ProgressBar progressBar, DataChangeListener dataChangeListener) {
        this.context = context;
        this.model = model;
        this.progressBar = progressBar;
        this.dataChangeListener = dataChangeListener;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object getItem(int position) {
        return model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_layanan, parent, false);
            holder = new ViewHolder();
            holder.nama_layanan = convertView.findViewById(R.id.namalayanan);
            holder.deskripsi = convertView.findViewById(R.id.deskripsi);
            holder.harga = convertView.findViewById(R.id.harga);
            holder.durasi = convertView.findViewById(R.id.durasi);
            holder.editButton = convertView.findViewById(R.id.edit);
            holder.deleteButton = convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GetDataLayanan data = model.get(position);
        Toast.makeText(context,data.getNama_layanan(),Toast.LENGTH_SHORT).show();
        holder.nama_layanan.setText(data.getNama_layanan());
        holder.deskripsi.setText(data.getDeskripsi());
        holder.harga.setText(data.getHarga());
        holder.durasi.setText(data.getDurasi());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Menjalankan EditDataActivity dengan mengirimkan data yang dibutuhkan
                Intent intent = new Intent(context, EditDataActivityLayanan.class);
                intent.putExtra("id_layanan", data.getId_layanan());
                intent.putExtra("nama_layanan", data.getNama_layanan());
                intent.putExtra("deskripsi", data.getDeskripsi());
                intent.putExtra("harga", data.getHarga());
                intent.putExtra("durasi", data.getDurasi());

                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(data.getId_layanan(), position);
            }
        });

        return convertView;
    }

    public void reloadData() {
        notifyDataSetChanged();
    }

    private void deleteData(final String id_layanan, final int position) {
        String url = new Configurasi().baseUrl() + "layanan/hapus_layanan.php";

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
                            String status = jsonObject.getString("status");
                            if ("data_berhasil_di_hapus".equals(status)) {
                                Toast.makeText(context, "Data successfully deleted", Toast.LENGTH_SHORT).show();
                                model.remove(position);
                                notifyDataSetChanged();
                                // Notify data change
                                if (dataChangeListener != null) {
                                    dataChangeListener.onDataChanged();
                                }
                            } else {
                                Toast.makeText(context, "Data Berhasil Di Hapus", Toast.LENGTH_SHORT).show();
                                // Reload data even if deletion fails
                                if (dataChangeListener != null) {
                                    dataChangeListener.onDataChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error parsing response", Toast.LENGTH_SHORT).show();
                            // Reload data even if deletion fails
                            if (dataChangeListener != null) {
                                dataChangeListener.onDataChanged();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, "Error deleting data", Toast.LENGTH_SHORT).show();
                        // Reload data even if deletion fails
                        if (dataChangeListener != null) {
                            dataChangeListener.onDataChanged();
                        }
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> form = new HashMap<>();
                form.put("id_layanan", id_layanan);
                return form;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private static class ViewHolder {
        TextView nama_layanan, deskripsi, harga, durasi;
        ImageButton editButton, deleteButton;
    }
}
