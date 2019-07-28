package com.hamdan.virus.e_learning.home.teacher;

public class AddClassModel {
    String eventName;
    String eventMessage;
    String eventDate;
    String eventTime;

    public AddClassModel(String eventName, String eventMessage, String eventDate, String eventTime) {

        this.eventName = eventName;
        this.eventMessage = eventMessage;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
    }

    public AddClassModel() {

    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventMessage() {
        return eventMessage;
    }

    public void setEventMessage(String eventMessage) {
        this.eventMessage = eventMessage;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
}
