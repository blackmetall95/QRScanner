package com.jiajie.qrscanner.Adapter;

public class DataModel{
    /*========== VARIABLES ==========*/
    private String scanResult;
    private String lat;
    private String lng;
    private String date;
    /*========== CONSTRUCTOR ==========*/
    public DataModel (String scanResult, String lat, String lng, String date){
        this.scanResult=scanResult;
        this.lat=lat;
        this.lng=lng;
        this.date=date;
    }
    /*****
     * Get
     *****/
    public String getScanResult() {return this.scanResult;}
    public String getLat() {return this.lat;}
    public String getLng() {return this.lng;}
    public String getDate() {return this.date;}
    /*****
     * Set
     *****/
    public void setScanResult(String scanResult) {this.scanResult=scanResult;}
    public void setLat(String lat) {this.lat=lat;}
    public void setLng(String lng) {this.lng=lng;}
}