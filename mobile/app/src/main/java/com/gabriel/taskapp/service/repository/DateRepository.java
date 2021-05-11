package com.gabriel.taskapp.service.repository;

public class DateRepository {
    private static String dateFormat = "MM/dd/yyyy HH:mm:ss";

    public static String getFormattedDatetime(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
        String mYear = addLeftZero(year);
        String mMonth = addLeftZero(month);
        String mDayOfMonth = addLeftZero(dayOfMonth);
        String mHourOfDay = addLeftZero(hourOfDay);
        String mMinute = addLeftZero(minute);
        return mMonth + "/" + mDayOfMonth + "/" + mYear + " " + mHourOfDay + ":" + mMinute + ":00";
    }

    private static String addLeftZero(int number){
        if(number < 10){
            return "0" + number;
        } else {
            return "" + number;
        }
    }
}
