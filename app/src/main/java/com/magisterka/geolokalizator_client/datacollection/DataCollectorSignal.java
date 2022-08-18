package com.magisterka.geolokalizator_client.datacollection;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresApi;
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

    private ContentValues signalData;

    private Context context;

    public DataCollectorSignal(Context newContext) {
        context = newContext;
        telephonyManager = (TelephonyManager) newContext.getSystemService(Context.TELEPHONY_SERVICE);

        signalData = new ContentValues();

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public ContentValues getSignalData() {
        updateSignalData();
        return signalData;
    }

    public boolean checkDataSignal() {
        //TODO add proper check

        if (getNetworkType().equals("Unknown")) {
            return false;
        } else {
            return true;
        }

    }

    private void test() {
        String rawSignalParameters;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            rawSignalParameters = String.valueOf(telephonyManager.getSignalStrength());

            signalData.put("Network_Provider", telephonyManager.getNetworkOperatorName());

            signalData.put("Network_Type", getNetworkType());

            signalData.put("RSSI", getSubstring(rawSignalParameters, "rssi=", " rsrp="));

            signalData.put("RSRP", getSubstring(rawSignalParameters, "rsrp=", " rsrq="));

            signalData.put("RSRQ", getSubstring(rawSignalParameters, "rsrq=", " rssnr="));

            signalData.put("RSSNR", getSubstring(rawSignalParameters, "rssnr=", " cqiTableIndex"));
        }

        return;
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void updateSignalData() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) { return; }

        signalData.put("Network_Provider", telephonyManager.getNetworkOperatorName());
        signalData.put("Network_Type", getNetworkType());

        List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();
        for (CellInfo cellInfo : cellInfoList) {
            if (cellInfo instanceof CellInfoLte) {
                signalData.put("RSRP",((CellInfoLte) cellInfo).getCellSignalStrength().getRsrp());
                signalData.put("RSSI",((CellInfoLte) cellInfo).getCellSignalStrength().getRssi());
                signalData.put("RSRQ",((CellInfoLte) cellInfo).getCellSignalStrength().getRsrq());
                signalData.put("RSSNR",((CellInfoLte) cellInfo).getCellSignalStrength().getRsrq());
            }
            if(cellInfo instanceof CellInfoWcdma)
            {
                signalData.put("RSRP","0");
                signalData.put("RSSI",((CellInfoWcdma) cellInfo).getCellSignalStrength().getDbm());
                signalData.put("RSRQ","0");
                signalData.put("RSSNR","0");
            }
            if (cellInfo instanceof CellInfoGsm) {
                signalData.put("RSRP","0");
                signalData.put("RSSI",((CellInfoGsm) cellInfo).getCellSignalStrength().getRssi());
                signalData.put("RSRQ","0");
                signalData.put("RSSNR","0");
            }
        }
    }

    private String getSubstring(String stringSignalParameters,String startIndex, String endIndex)
    {
        String substring;
        substring = stringSignalParameters.substring(stringSignalParameters.indexOf(startIndex)+startIndex.length(),stringSignalParameters.indexOf(endIndex));
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
                    networkType = "Unknown";
            }
        }

        return networkType;
    }


}
