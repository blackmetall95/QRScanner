package com.jiajie.qrscanner.Adapter;

public class DataModel{
    private String scanResult;
    private String lat;
    private String lng;

    public DataModel (String scanResult, String lat, String lng){
        this.scanResult=scanResult;
        this.lat=lat;
        this.lng=lng;
    }

    /********** Get **********/
    public String getScanResult() {return this.scanResult;}
    public String getLat() {return this.lat;}
    public String getLng() {return this.lng;}

    /********** Set **********/
    public void setScanResult(String scanResult) {this.scanResult=scanResult;}
    public void setLat(String lat) {this.lat=lat;}
    public void setLng(String lng) {this.lng=lng;}
}
