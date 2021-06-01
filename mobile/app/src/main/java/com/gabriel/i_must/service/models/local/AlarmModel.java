package com.gabriel.i_must.service.models.local;

import android.util.Log;

import com.gabriel.i_must.service.repositories.DateRepository;

import java.util.Calendar;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

import static com.gabriel.i_must.service.constants.TaskConstants.TASK_TAG;

public class AlarmModel extends RealmObject {
    @PrimaryKey
    private String id;
    @Index
    private int alarmId;
    private String description;
    private Long timeInMillis;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(String formattedDate) {
        Long timeInMillis = setDateAndGetMillis(formattedDate);
        if(timeInMillis != null){
            this.timeInMillis = timeInMillis;
            Log.d(TASK_TAG, "setTimeInMillis: " + timeInMillis);
        } else {
            Log.d(TASK_TAG, "setTimeInMillis: nao");
        }
    }

    private Long setDateAndGetMillis(String formattedDate){
        if(formattedDate.length() == 0) return  null;
        Long timeInMillis = null;
        DateModel dateModel = DateRepository.getDateModelFromString(formattedDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.YEAR, dateModel.getYear());
        calendar.set(Calendar.MONTH, dateModel.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, dateModel.getDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY, dateModel.getHourOfDay());
        calendar.set(Calendar.MINUTE, dateModel.getMinute());
        calendar.set(Calendar.SECOND, dateModel.getSeconds());

        Long calendarTimeInMillis = calendar.getTimeInMillis();
        if(calendarTimeInMillis > System.currentTimeMillis()){
            timeInMillis = calendarTimeInMillis;
        }
        return timeInMillis;
    }
}
