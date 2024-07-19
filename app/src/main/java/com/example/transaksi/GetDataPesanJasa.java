package com.example.transaksi;

import java.io.Serializable;

public class GetDataPesanJasa implements Serializable {
    private String id_pesan;
    private String nama_petugas;
    private String nama;
    private String nama_layanan;
    private String nama_kain;
    private String tanggal_pesan;
    private String tanggal_ambil;
    private String jumlah;
    private String status_pesan;

    public GetDataPesanJasa(String id_pesan , String nama_petugas, String nama, String nama_layanan, String nama_kain , String tanggal_pesan, String tanggal_ambil, String jumlah, String status_pesan) {
        this.id_pesan = id_pesan ;
        this.nama_petugas = nama_petugas ;
        this.nama = nama;
        this.nama_layanan = nama_layanan;
        this.nama_kain = nama_kain;
        this.tanggal_pesan = tanggal_pesan;
        this.tanggal_ambil = tanggal_ambil;
        this.jumlah = jumlah;
        this.status_pesan = status_pesan;
    }


    public String getId_pesan() {
        return id_pesan ;
    }
    public String getNama_petugas() {
        return nama_petugas;
    }
    public String getNama() {
        return nama;
    }
    public String getNama_layanan() {
        return nama_layanan;
    }
    public String getNama_kain() { return  nama_kain; }
    public String getTanggal_pesan () { return tanggal_pesan; }
    public String getTanggal_ambil () { return tanggal_ambil; }
    public String getJumlah () { return jumlah; }
    public String getStatus_pesan () { return status_pesan; }


        }