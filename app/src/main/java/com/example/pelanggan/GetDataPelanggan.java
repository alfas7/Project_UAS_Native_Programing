package com.example.pelanggan;

import java.io.Serializable;

public class GetDataPelanggan implements Serializable {
    private String id_pelanggan;
    private String nama;
    private String email;
    private String alamat;
    private String no_telpon;
    public GetDataPelanggan(String id_pelanggan , String nama, String email, String alamat, String no_telpon) {
        this.id_pelanggan = id_pelanggan ;
        this.nama = nama;
        this.email = email;
        this.alamat = alamat;
        this.no_telpon = no_telpon;
    }

    public String getId_pelanggan() {
        return id_pelanggan ;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {return email; }

    public String getAlamat() {return alamat ;}

    public String getNo_telpon() { return no_telpon; }


}
