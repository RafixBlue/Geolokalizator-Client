package com.magisterka.geolokalizator_client;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TimeCalculator {

    private static long DAY_IN_MILLISECONDS = 86400000;

    public TimeCalculator() { }

    public String[] getLastSevenDaysDates() {

        String[] dates = new String[7];

        Date day = new Date();

        String dateDay;
        for(int i=0; i < 7; i++) {

            day.setTime(Calendar.getInstance().getTimeInMillis()-((6-i)*DAY_IN_MILLISECONDS));
            dateDay=getDateTime(day);
            dates[i]=dateDay;

        }

        return dates;
    }


    public String getDateTime(Date date) {

        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String formatedDate = form.format(date);

        return formatedDate;
    }

}
