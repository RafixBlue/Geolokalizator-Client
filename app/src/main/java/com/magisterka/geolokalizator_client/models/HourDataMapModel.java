package com.magisterka.geolokalizator_client.models;

public class HourDataMapModel {

    private String dateTime;
    private String latitude;
    private String altitude;
    private String longitude;
    private String accuracy;

    private String network_Provider;
    private String network_Type;
    private String rssi;
    private String rsrp;
    private String rsrq;
    private String rssnr;

    public String getDateTime(){
        return dateTime;
    }
    public String getLatitude(){
        return latitude;
    }
    public String getAltitude(){
        return altitude;
    }
    public String getLongitude(){
        return longitude;
    }
    public String getAccuracy(){
        return accuracy;
    }
    public String getNetwork_Provider(){
        return network_Provider;
    }
    public String getNetwork_Type(){
        return network_Type;
    }
    public String getRssi(){
        return rssi;
    }
    public String getRsrp(){
        return rsrp;
    }
    public String getRsrq(){
        return rsrq;
    }
    public String getRssnr(){
        return rssnr;
    }

    public void setAccuracy(String nAccuracy) {
        accuracy = nAccuracy;
    }

    public void setAltitude(String nAltitude) {
        altitude = nAltitude;
    }

    public void setDateTime(String nDateTime) {
        dateTime = nDateTime;
    }

    public void setLatitude(String nLatitude) {
        latitude = nLatitude;
    }

    public void setLongitude(String nLongitude) {
        longitude = nLongitude;
    }

    public void setNetwork_Provider(String nNetwork_Provider) {
        network_Provider = nNetwork_Provider;
    }

    public void setNetwork_Type(String nNetwork_Type) {
        network_Type = nNetwork_Type;
    }

    public void setRssnr(String nRssnr) {
        rssnr = nRssnr;
    }

    public void setRssi(String nRssi) {
        rssi = nRssi;
    }

    public void setRsrp(String nRsrp) {
        rsrp = nRsrp;
    }

    public void setRsrq(String nRsrq) {
        rsrq = nRsrq;
    }
}
