package com.example.transaksi;

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
import com.example.project_uas.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdaptorPesanJasa extends BaseAdapter {
    private Context context;
    private ArrayList<GetDataPesanJasa> model;
    private LayoutInflater inflater;
    private ProgressBar progressBar;
    private DataChangeListener dataChangeListener;

    public interface DataChangeListener {
        void onDataChanged();
    }

    public AdaptorPesanJasa(Context context, ArrayList<GetDataPesanJasa> model, ProgressBar progressBar, DataChangeListener dataChangeListener) {
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
            convertView = inflater.inflate(R.layout.list_pesan_jasa, parent, false);
            holder = new ViewHolder();
            holder.nama_petugas= convertView.findViewById(R.id.id_pesan4);
            holder.nama= convertView.findViewById(R.id.id_pelanggan3);
            holder.nama_layanan= convertView.findViewById(R.id.id_bayar4);
            holder.nama_kain= convertView.findViewById(R.id.id_metode4);
            holder.tanggal_pesan= convertView.findViewById(R.id.id_byr4);
            holder.tanggal_ambil= convertView.findViewById(R.id.id_ambil3);
            holder.jumlah= convertView.findViewById(R.id.id_jumlah3);
            holder.status_pesan= convertView.findViewById(R.id.id_status3);
            holder.editButton = convertView.findViewById(R.id.edit_pesan);
            holder.deleteButton = convertView.findViewById(R.id.delete_pesan);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GetDataPesanJasa data = model.get(position);
        holder.nama_petugas.setText(data.getNama_petugas());
        holder.nama.setText(data.getNama());
        holder.nama_layanan.setText(data.getNama_layanan());
        holder.nama_kain.setText(data.getNama_kain());
        holder.tanggal_pesan.setText(data.getTanggal_pesan());
        holder.tanggal_ambil.setText(data.getTanggal_ambil());
        holder.jumlah.setText(data.getJumlah());
        holder.status_pesan.setText(data.getStatus_pesan());


        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                // Menjalankan EditDataActivity dengan mengirimkan data yang dibutuhkan
                Intent intent = new Intent(context, EditDataActivityPesanJasa.class);
                intent.putExtra("id_pesan", data.getId_pesan());
                intent.putExtra("nama_petugas", data.getNama_petugas());
                intent.putExtra("nama", data.getNama());
                intent.putExtra("nama_kain", data.getNama_kain());
                intent.putExtra("tanggal_pesan", data.getTanggal_pesan());
                intent.putExtra("tanggal_ambil", data.getTanggal_ambil());
                intent.putExtra("jumlah", data.getJumlah());
                intent.putExtra("status_pesan", data.getStatus_pesan());

                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(data.getId_pesan(), position);
            }
        });

        return convertView;
    }

    public void reloadData() {
        notifyDataSetChanged();
    }

    private void deleteData(final String id_pesan, final int position) {
        String url = new Configurasi().baseUrl() + "transaksi/hapus_data_pesan_jasa.php";

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
                            // R
                            // eload data even if deletion fails
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
                form.put("id_pesan", id_pesan);
                return form;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private static class ViewHolder {
        TextView nama_petugas, nama, nama_layanan, nama_kain, tanggal_pesan, tanggal_ambil, jumlah, status_pesan;
        ImageButton editButton, deleteButton;
    }
}
