package com.example.pembayaran;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.example.transaksi.EditDataActivityPesanJasa;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdaptorPembayaran extends BaseAdapter {
    private Context context;
    private ArrayList<GetDataPembayaran> model;
    private LayoutInflater inflater;
    private ProgressBar progressBar;
    private DataChangeListener dataChangeListener;

    public interface DataChangeListener {
        void onDataChanged();
    }

    public AdaptorPembayaran(Context context, ArrayList<GetDataPembayaran> model, ProgressBar progressBar, DataChangeListener dataChangeListener) {
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
            convertView = inflater.inflate(R.layout.list_pembayaran, parent, false);
            holder = new ViewHolder();
            holder.id_pesan = convertView.findViewById(R.id.id_pesan4);
            holder.id_pelanggan = convertView.findViewById(R.id.id_pelanggan4);
            holder.jumlah_bayar = convertView.findViewById(R.id.id_bayar4);
            holder.metode_bayar = convertView.findViewById(R.id.id_metode4);
            holder.tanggal_bayar = convertView.findViewById(R.id.id_byr4);
            holder.editButton = convertView.findViewById(R.id.edit_pembayaran);
            holder.deleteButton = convertView.findViewById(R.id.delete_pesan);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GetDataPembayaran data = model.get(position);
        holder.id_pesan.setText(data.getId_pesan());
        holder.id_pelanggan.setText(data.getId_pelanggan());
        holder.jumlah_bayar.setText(data.getJumlah_bayar());
        holder.metode_bayar.setText(data.getMetode_bayar());
        holder.tanggal_bayar.setText(data.getTanggal_bayar());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Menjalankan EditDataActivity dengan mengirimkan data yang dibutuhkan
                Intent intent = new Intent(context, EditDataActivityPembayaran.class);
                intent.putExtra("id_pembayaran", data.getId_pembayaran());
                intent.putExtra("id_pesan", data.getId_pesan());
                intent.putExtra("id_pelanggam", data.getId_pelanggan());
                intent.putExtra("jumlah_bayar", data.getJumlah_bayar());
                intent.putExtra("metode_bayar", data.getMetode_bayar());
                intent.putExtra("tanggal_bayar", data.getTanggal_bayar());

                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(data.getId_pembayaran(), position);
            }
        });

        return convertView;
    }

    public void reloadData() {
        notifyDataSetChanged();
    }

    private void deleteData(final String id_pembayaran, final int position) {
        String url = new Configurasi().baseUrl() + "transaksi/hapus_data_pembayaran.php";

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
                                Toast.makeText(context, "Failed to delete data", Toast.LENGTH_SHORT).show();
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
                form.put("id_pembayaran", id_pembayaran);
                return form;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private static class ViewHolder {
        TextView id_pesan, id_pelanggan, jumlah_bayar, metode_bayar, tanggal_bayar;
        ImageButton editButton, deleteButton;
    }
}
