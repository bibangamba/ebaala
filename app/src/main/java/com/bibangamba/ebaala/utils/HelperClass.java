package com.bibangamba.ebaala.utils;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by davy on 5/22/2018.
 */

public class HelperClass {

    public final String getDay(int x){

        String days[] = new DateFormatSymbols().getShortWeekdays();
        //Our Week Begins Mon
        String weekday = (x == 6)?days[1]:days[x+2];
        return weekday;

    }
    public final int weekDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_WEEK) - 2;
    }

}
