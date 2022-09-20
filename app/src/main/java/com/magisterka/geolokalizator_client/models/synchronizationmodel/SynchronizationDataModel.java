package com.magisterka.geolokalizator_client.models.synchronizationmodel;

public class SynchronizationDataModel {

    private String dateTime;
    private String latitude;
    private String altitude;
    private String longitude;
    private String accuracy;
    private String timeZone;

    private String network_Provider;
    private String network_Type;
    private String rssi;
    private String rsrp;
    private String rsrq;
    private String rssnr;



    public void setRssnr(String nRssnr) {
        rssnr = nRssnr;
    }

    public void setRssi(String nRssi) {
        rssi = nRssi;
    }

    public void setRsrq(String nRsrq) {
        rsrq = nRsrq;
    }

    public void setRsrp(String nRsrp) {
        rsrp = nRsrp;
    }

    public void setDateTime(String nDateTime) {
        dateTime = nDateTime;
    }

    public void setNetwork_Provider(String nNetwork_Provider) {
        network_Provider = nNetwork_Provider;
    }

    public void setNetwork_Type(String nNetwork_Type) {
        network_Type = nNetwork_Type;
    }

    public void setLongitude(String nLongitude) {
        longitude = nLongitude;
    }

    public void setLatitude(String nLatitude) {
        latitude = nLatitude;
    }

    public void setAltitude(String nAltitude) {
        altitude = nAltitude;
    }

    public void setAccuracy(String nAccuracy) {
        accuracy = nAccuracy;
    }

    public void setTimeZone(String nTimeZone) {
        timeZone = nTimeZone;
    }



}
