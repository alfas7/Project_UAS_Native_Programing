package com.example.project_uas;

import java.io.Serializable;

public class GetDataPetugas implements Serializable {
    private String id_petugas;
    private String nama_petugas;
    private String jabatan;
    private String no_telpon;
    public GetDataPetugas(String id_petugas , String nama_petugas, String jabatan, String no_telpon) {
        this.id_petugas = id_petugas ;
        this.nama_petugas = nama_petugas;
        this.jabatan = jabatan;
        this.no_telpon = no_telpon;
    }

    public String getId_petugas() {
        return id_petugas ;
    }

    public String getNama_petugas() {
        return nama_petugas;
    }

    public String getJabatan() {
        return jabatan;
    }

    public String getNo_telpon() { return no_telpon; }


}
