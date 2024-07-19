package com.example.project_uas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kain.ActivityKain;
import com.example.layanan.ActivityLayanan;
import com.example.pelanggan.ActivityPelanggan;
import com.example.pembayaran.ActivityPembayaran;
import com.example.transaksi.ActivityPesanJasa;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        LinearLayout petugas =findViewById(R.id.petugas);
        LinearLayout pelanggan = findViewById(R.id.pelanggan);
        LinearLayout layanan = findViewById(R.id.layanan);
        LinearLayout kain = findViewById(R.id.kain);
        Button pesan_jasa = findViewById(R.id.pesan_jasa);
        Button pembayaran = findViewById(R.id.pembayaran);



        petugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ActivityPetugas.class);
                startActivity(intent);
            }
        });

        pelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ActivityPelanggan.class);
                startActivity(intent);
            }
        });
        layanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ActivityLayanan.class);
                startActivity(intent);
            }
        });
        kain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ActivityKain.class);
                startActivity(intent);
            }
        });

        pesan_jasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ActivityPesanJasa.class);
                startActivity(intent);
            }
        });

        pembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ActivityPembayaran.class);
                startActivity(intent);
            }
        });
    }
}