package com.example.scheduleme;

/* A model class to transger and get user schedule data */
public class ScheduleModel {
  String scheduleId,scheduleTitle,schedulePlace,scheduleDate,scheduleTime;
    public ScheduleModel(){

    }

    public ScheduleModel(String scheduleId,String scheduleTitle, String schedulePlace, String scheduleDate, String scheduleTime) {
        this.scheduleId=scheduleId;
        this.scheduleTitle = scheduleTitle;
        this.schedulePlace = schedulePlace;
        this.scheduleDate = scheduleDate;
        this.scheduleTime = scheduleTime;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public String getScheduleTitle() {
        return scheduleTitle;
    }

    public String getSchedulePlace() {
        return schedulePlace;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }
}
