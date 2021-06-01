package com.gabriel.i_must.service.repositories;

import com.gabriel.i_must.service.models.local.DateModel;

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

    private static String addLeftZero(int number) {
        if (number < 10) {
            return "0" + number;
        } else {
            return "" + number;
        }
    }

    public static DateModel getDateModelFromString(String date) {
        String[] datetimeComponents = date.split(" ");
        String[] dateComponents = datetimeComponents[0].split("/");
        String[] hourComponents = datetimeComponents[1].split(":");
        DateModel dateModel = new DateModel(
                        Integer.parseInt(dateComponents[2]),
                        Integer.parseInt(dateComponents[0]),
                        Integer.parseInt(dateComponents[1]),
                        Integer.parseInt(hourComponents[0]),
                        Integer.parseInt(hourComponents[1]),
                        Integer.parseInt(hourComponents[2]));
        return (dateModel);
    }
}
