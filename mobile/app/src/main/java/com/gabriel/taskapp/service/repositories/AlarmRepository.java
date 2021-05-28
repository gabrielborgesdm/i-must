package com.gabriel.taskapp.service.repositories;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.gabriel.taskapp.service.models.local.AlarmModel;
import com.gabriel.taskapp.service.models.local.DateModel;
import com.gabriel.taskapp.service.models.local.LocalTaskModel;
import com.gabriel.taskapp.service.receivers.AlarmReceiver;
import com.gabriel.taskapp.service.repositories.local.LocalAlarmsRepository;

import java.util.Calendar;

import static com.gabriel.taskapp.service.constants.AlarmConstants.ALARM_TASK_DESCRIPTION;

public class AlarmRepository {
    private AlarmManager mAlarmManager;
    private LocalAlarmsRepository mLocalAlarmsRepository = LocalAlarmsRepository.getRealmRepository();
    private Context mContext;

    public AlarmRepository(Context context) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mContext = context;
    }

    public void setAlarm(AlarmModel alarm) {
        PendingIntent alarmIntent = getAlarmIntent(alarm.getDescription(), alarm.getAlarmId());
        if(alarm.getTimeInMillis() != null) {
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), alarmIntent);
        }
    }

    public void setOrUpdateAlarmFromTask(LocalTaskModel task){
        AlarmModel alarm = new AlarmModel();
        alarm.setId(task.getId());
        AlarmModel oldAlarm = mLocalAlarmsRepository.get(task.getId());
        if(oldAlarm == null){
            alarm.setAlarmId(mLocalAlarmsRepository.getAlarmsLength() + 1);
        } else {
            alarm.setAlarmId(oldAlarm.getAlarmId());
        }
        alarm.setDescription(task.getDescription());
        alarm.setTimeInMillis(task.getDatetime());
        if(alarm.getTimeInMillis() != null && !task.getCompleted()){
            mLocalAlarmsRepository.saveOrUpdate(alarm);
            setAlarm(alarm);
        } else {
            mLocalAlarmsRepository.delete(alarm.getId());
            removeAlarm(alarm);
        }
    }

    public void removeAlarmFromTask(LocalTaskModel task) {
        AlarmModel alarm = mLocalAlarmsRepository.get(task.getId());
        if(alarm != null){
            removeAlarm(alarm);
            mLocalAlarmsRepository.delete(task.getId());
        }
    }

    public void removeAlarm(AlarmModel alarm) {
        PendingIntent alarmIntent = getAlarmIntent(alarm.getDescription(), alarm.getAlarmId());
        mAlarmManager.cancel(alarmIntent);
    }

    private PendingIntent getAlarmIntent(String alarmDescription, int alarmId) {
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        intent.putExtra(ALARM_TASK_DESCRIPTION, alarmDescription);
        return PendingIntent.getBroadcast(mContext, alarmId, intent, 0);
    }
}
