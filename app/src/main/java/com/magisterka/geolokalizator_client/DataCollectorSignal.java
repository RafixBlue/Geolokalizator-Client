package com.magisterka.geolokalizator_client;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.CellInfo;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

import static android.telephony.TelephonyManager.DATA_CONNECTED;
import static android.telephony.TelephonyManager.DATA_CONNECTING;
import static android.telephony.TelephonyManager.DATA_DISCONNECTED;
import static android.telephony.TelephonyManager.DATA_DISCONNECTING;
import static android.telephony.TelephonyManager.DATA_SUSPENDED;
import static android.telephony.TelephonyManager.NETWORK_TYPE_1xRTT;
import static android.telephony.TelephonyManager.NETWORK_TYPE_CDMA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EDGE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_0;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_A;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_B;
import static android.telephony.TelephonyManager.NETWORK_TYPE_GPRS;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSDPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSPAP;
import static android.telephony.TelephonyManager.NETWORK_TYPE_IDEN;
import static android.telephony.TelephonyManager.NETWORK_TYPE_LTE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_NR;
import static android.telephony.TelephonyManager.NETWORK_TYPE_UMTS;

public class DataCollectorSignal {



    private TelephonyManager telephonyManager;

    private ArrayList<ArrayList<String>> signalData;

    private Context context;

    public DataCollectorSignal(Context newContext) {
        context = newContext;
        telephonyManager = (TelephonyManager) newContext.getSystemService(Context.TELEPHONY_SERVICE);

        ArrayList<ArrayList<String>> signalData = new ArrayList<>(6);
        for(int i=0; i < 6; i++) {
            signalData.add(new ArrayList());
        }

        //ServiceState serviceState = telephonyManager.getServiceState();
    }

    public ArrayList<ArrayList<String>> getSignalData() {
        return signalData;
    }

    public String checkDataSignal()
    {
        String isConnected;
        switch (telephonyManager.getDataState()){
            case DATA_CONNECTED: isConnected = "DATA_CONNECTED";
            case DATA_DISCONNECTED: isConnected = "DATA_DISCONNECTED";
            case DATA_CONNECTING: isConnected = "DATA_CONNECTING";
            case DATA_SUSPENDED: isConnected = "DATA_SUSPENDED";
            case DATA_DISCONNECTING: isConnected = "DATA_DISCONNECTING";
            default: isConnected = "no";
        }

        return isConnected;

    }

    private void updateSignalData()
    {
        String rawSignalParameters;



        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            rawSignalParameters  = String.valueOf(telephonyManager.getSignalStrength());

            signalData.get(0).add(0,"Network Provider");
            signalData.get(0).add(1,telephonyManager.getNetworkOperatorName());

            signalData.get(1).add(0,"Network Type");
            signalData.get(1).add(1,getNetworkType());

            signalData.get(2).add(0,"rssi");
            signalData.get(2).add(1, getSubstring(rawSignalParameters,"rssi="," rsrp="));

            signalData.get(3).add(0,"rsrp");
            signalData.get(3).add(1, getSubstring(rawSignalParameters,"rsrp="," rsrq="));

            signalData.get(4).add(0,"rsrq");
            signalData.get(4).add(1,getSubstring(rawSignalParameters,"rsrq="," rssnr="));

            signalData.get(5).add(0,"rssnr");
            signalData.get(5).add(1,getSubstring(rawSignalParameters,"rssnr="," cqiTableIndex"));

        }

    }

    private String getSubstring(String stringSignalParameters,String startIndex, String endIndex)
    {
        String substring;
        substring = stringSignalParameters.substring(stringSignalParameters.indexOf(startIndex)+startIndex.length(),stringSignalParameters.indexOf(endIndex));;
        return substring;
    }

    private String getNetworkType() {
        String networkType = "No Network";
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
        {
            switch (telephonyManager.getDataNetworkType()) {
                case NETWORK_TYPE_EDGE:
                case NETWORK_TYPE_GPRS:
                case NETWORK_TYPE_CDMA:
                case NETWORK_TYPE_IDEN:
                case NETWORK_TYPE_1xRTT:
                    networkType = "2G";
                    break;
                case NETWORK_TYPE_UMTS:
                case NETWORK_TYPE_HSDPA:
                case NETWORK_TYPE_HSPA:
                case NETWORK_TYPE_HSPAP:
                case NETWORK_TYPE_EVDO_0:
                case NETWORK_TYPE_EVDO_A:
                case NETWORK_TYPE_EVDO_B:
                    networkType = "3G";
                    break;
                case NETWORK_TYPE_LTE:
                    networkType = "4G";
                    break;
                case NETWORK_TYPE_NR:
                    networkType = "5G";
                    break;
                default:
                    networkType = "Unknown or No Sim Card";
            }
        }

        return networkType;
    }


}
