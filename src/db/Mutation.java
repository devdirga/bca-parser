/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import util.Tools;

/**
 *
 * @author Dirga
 */
public class Mutation {
    
    private String idx;
    private String kodebank;
    private String tanggal;
    private String keterangan;
    private String idoutlet;
    private String dbkr;
    private String nominal;
    private String saldo;
    private String tglsys;
    private String waktusys;
    private String catatan1 = Tools.EMPTY;
    private String catatan2 = Tools.EMPTY;
    
    public Mutation(String Keterangan, String Kodebank, String Dbkr, String Nominal, String Saldo){
        this.keterangan = Keterangan;
        this.kodebank = Kodebank;
        this.dbkr = Dbkr;
        this.nominal = Nominal;
        this.saldo = Saldo;
    }

    public String getCatatan1() {
        return catatan1;
    }

    public void setCatatan1(String catatan1) {
        this.catatan1 = catatan1;
    }

    public String getCatatan2() {
        return catatan2;
    }

    public void setCatatan2(String catatan2) {
        this.catatan2 = catatan2;
    }

    public String getDbkr() {
        return dbkr;
    }

    public void setDbkr(String dbkr) {
        this.dbkr = dbkr;
    }

    public String getId_outlet() {
        return idoutlet;
    }

    public void setId_outlet(String idoutlet) {
        this.idoutlet = idoutlet;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getKodebank() {
        return kodebank;
    }

    public void setKodebank(String kodebank) {
        this.kodebank = kodebank;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTgl_sys() {
        return tglsys;
    }

    public void setTgl_sys(String tglsys) {
        this.tglsys = tglsys;
    }

    public String getWkt_sys() {
        return waktusys;
    }

    public void setWkt_sys(String waktusys) {
        this.waktusys = waktusys;
    }
    
}
