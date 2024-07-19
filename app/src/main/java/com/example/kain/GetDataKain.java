package com.example.kain;

import java.io.Serializable;

public class GetDataKain implements Serializable {
    private String id_kain;
    private String nama_kain;
    private String deskripsi;
    public GetDataKain(String id_kain , String nama_kain, String deskripsi) {
        this.id_kain = id_kain;
        this.nama_kain = nama_kain;
        this.deskripsi = deskripsi;
    }

    public String getId_kain() {
        return id_kain ;
    }

    public String getNama_kain() {
        return nama_kain;
    }

    public String getDeskripsi() {
        return deskripsi;
    }



}
