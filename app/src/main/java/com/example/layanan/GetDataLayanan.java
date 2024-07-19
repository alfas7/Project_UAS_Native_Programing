package com.example.layanan;

import java.io.Serializable;

public class GetDataLayanan implements Serializable {
    private String id_layanan;
    private String nama_layanan;
    private String deskripsi;
    private String harga;
    private String durasi;
    public GetDataLayanan(String id_layanan , String nama_layanan, String deskripsi, String harga, String durasi) {
        this.id_layanan = id_layanan ;
        this.nama_layanan = nama_layanan;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.durasi = durasi;
    }

    public String getId_layanan() {
        return id_layanan ;
    }

    public String getNama_layanan() {
        return nama_layanan;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getHarga() { return harga; }

    public String getDurasi() { return durasi; }


}
