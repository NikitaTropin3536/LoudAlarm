package com.example.loudalarm.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@Entity
public class AlarmEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public int minute;

    public boolean isAlarmAdjustVolume;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getVol() {
        return vol;
    }

    public void setVol(int vol) {
        this.vol = vol;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getTime_on_clock_in_hours_and_minutes() {
        return time_on_clock_in_hours_and_minutes;
    }

    public void setTime_on_clock_in_hours_and_minutes(String time_on_clock_in_hours_and_minutes) {
        this.time_on_clock_in_hours_and_minutes = time_on_clock_in_hours_and_minutes;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isVib() {
        return vib;
    }

    public void setVib(boolean vib) {
        this.vib = vib;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public boolean isToday() {
        return today;
    }

    public void setToday(boolean today) {
        this.today = today;
    }

    public boolean isMonday() {
        return monday;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    public void setSunday(boolean sunday) {
        this.sunday = sunday;
    }

    public boolean isAlarmAdjustVolume() {
        return isAlarmAdjustVolume;
    }

    public void setAlarmAdjustVolume(boolean alarmAdjustVolume) {
        this.isAlarmAdjustVolume = alarmAdjustVolume;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public int vol, hours, minutes;
    public String time_on_clock_in_hours_and_minutes = " ", days;
    public long time;
    public boolean vib;
    public boolean on;

    public boolean today;
    public boolean monday;
    public boolean tuesday;
    public boolean wednesday;
    public boolean thursday;
    public boolean friday, saturday, sunday;

    public boolean isAlarmCanPlay() {
        return alarmCanPlay;
    }

    public String music;
    public String textMessage;
    public boolean alarmCanPlay;

    public void setAlarmCanPlay(boolean alarmCanPlay) {
        this.alarmCanPlay = alarmCanPlay;
    }


    public AlarmEntity(String music) {
        this.alarmCanPlay = false;
        this.hours = Calendar.getInstance().getTime().getHours();
        this.minutes = Calendar.getInstance().getTime().getMinutes();
        this.days = "";
        this.saturday = false;
        this.time_on_clock_in_hours_and_minutes = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(Calendar.getInstance().getTime());
        this.thursday = false;
        this.monday = false;
        this.wednesday = false;
        this.sunday = false;
        this.friday = false;
        this.tuesday = false;
        this.on = true;
        this.music = music;
        this.vol = 10;
        this.minute = 2;
        this.time = System.currentTimeMillis();
    }
}

