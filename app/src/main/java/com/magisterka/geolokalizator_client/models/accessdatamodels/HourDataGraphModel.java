package com.magisterka.geolokalizator_client.models.accessdatamodels;

public class HourDataGraphModel {
    private String dateTime;
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
    public String getAccuracy() { return accuracy; }
    public String getNetwork_Provider(){
        return network_Provider;
    }
    public String getNetwork_Type(){
        return network_Type;
    }
    public String getRSSI(){
        return rssi;
    }
    public String getRSRP(){
        return rsrp;
    }
    public String getRSRQ(){
        return rsrq;
    }
    public String getRSSNR(){
        return rssnr;
    }

    public void setNetwork_Type(String network_Type) {
        this.network_Type = network_Type;
    }

    public void setNetwork_Provider(String network_Provider) {
        this.network_Provider = network_Provider;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setRsrp(String rsrp) {
        this.rsrp = rsrp;
    }

    public void setRsrq(String rsrq) {
        this.rsrq = rsrq;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public void setRssnr(String rssnr) {
        this.rssnr = rssnr;
    }
}
