package com.magisterka.geolokalizator_client.models.accessdatamodels;

import com.jjoe64.graphview.series.DataPoint;

public class GraphDataPointsModel {
    public DataPoint[] pointsRSRP;
    public DataPoint[] pointsRSRQ;
    public DataPoint[] pointsRSSI;
    public DataPoint[] pointsRSSNR;
    public DataPoint[] pointsAccuracy;

    public GraphDataPointsModel(int sizeDataPoint)
    {
        pointsRSRP = new DataPoint[sizeDataPoint];
        pointsRSRQ = new DataPoint[sizeDataPoint];
        pointsRSSI = new DataPoint[sizeDataPoint];
        pointsRSSNR = new DataPoint[sizeDataPoint];
        pointsAccuracy = new DataPoint[sizeDataPoint];
    }
}
