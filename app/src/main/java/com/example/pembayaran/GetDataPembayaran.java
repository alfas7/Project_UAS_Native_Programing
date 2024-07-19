package com.example.pembayaran;

import java.io.Serializable;

public class GetDataPembayaran implements Serializable {
    private String id_pembayaran;
    private String id_pesan;
    private String id_pelanggan;
    private String jumlah_bayar;
    private String metode_bayar;
    private String tanggal_bayar;


    public GetDataPembayaran(String id_pembayaran , String id_pesan, String nama, String jumlah_bayar, String metode_bayar , String tanggal_bayar) {
        this.id_pembayaran = id_pembayaran ;
        this.id_pesan = id_pesan ;
        this.id_pelanggan = nama;
        this.jumlah_bayar = jumlah_bayar;
        this.metode_bayar = metode_bayar;
        this.tanggal_bayar = tanggal_bayar;

    }


    public String getId_pembayaran() {
        return id_pembayaran ;
    }
    public String getId_pesan() {
        return id_pesan;
    }
    public String getId_pelanggan() {
        return id_pelanggan;
    }
    public String getJumlah_bayar() {
        return jumlah_bayar;
    }
    public String getMetode_bayar() { return  metode_bayar; }
    public String getTanggal_bayar () { return tanggal_bayar; }



        }