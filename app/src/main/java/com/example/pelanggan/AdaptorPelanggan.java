package com.example.pelanggan;

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

public class AdaptorPelanggan extends BaseAdapter {
    private Context context;
    private ArrayList<GetDataPelanggan> model;
    private LayoutInflater inflater;
    private ProgressBar progressBar;
    private DataChangeListener dataChangeListener;

    public interface DataChangeListener {
        void onDataChanged();
    }

    public AdaptorPelanggan(Context context, ArrayList<GetDataPelanggan> model, ProgressBar progressBar, DataChangeListener dataChangeListener) {
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
            convertView = inflater.inflate(R.layout.list_pelanggan, parent, false);
            holder = new ViewHolder();
            holder.nama = convertView.findViewById(R.id.nama);
            holder.email = convertView.findViewById(R.id.email);
            holder.alamat = convertView.findViewById(R.id.alamat);
            holder.no_telpon = convertView.findViewById(R.id.no_telpon);
            holder.editButton = convertView.findViewById(R.id.edit);
            holder.deleteButton = convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GetDataPelanggan data = model.get(position);
        Toast.makeText(context,data.getNama(),Toast.LENGTH_SHORT).show();
        holder.nama.setText(data.getNama());
        holder.email.setText(data.getEmail());
        holder.alamat.setText(data.getAlamat());
        holder.no_telpon.setText(data.getNo_telpon());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Menjalankan EditDataActivity dengan mengirimkan data yang dibutuhkan
                Intent intent = new Intent(context, EditDataActivityPelanggan.class);
                intent.putExtra("id_pelanggan", data.getId_pelanggan());
                intent.putExtra("nama", data.getNama());
                intent.putExtra("email", data.getEmail());
                intent.putExtra("alamat", data.getAlamat());
                intent.putExtra("no_telpon", data.getNo_telpon());
                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(data.getId_pelanggan(), position);
            }
        });

        return convertView;
    }

    public void reloadData() {
        notifyDataSetChanged();
    }

    private void deleteData(final String id_pelanggan, final int position) {
        String url = new Configurasi().baseUrl() + "hapus.php";

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
                form.put("id_pelanggan", id_pelanggan);
                return form;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private static class ViewHolder {
        TextView nama,email,alamat, no_telpon;
        ImageButton editButton, deleteButton;
    }
}
