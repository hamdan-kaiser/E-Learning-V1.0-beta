package com.hamdan.virus.e_learning;

public class SendInteltodb {

    public String userPass;
    private String occupation;
    public String userName;
    public String userEmail;
    private int eventSerial;

    public SendInteltodb(String userPass, String occupation, String userName, String userEmail, int eventSerial)
    {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPass = userPass;
        this.occupation = occupation;
        this.eventSerial = eventSerial;
    }

    public SendInteltodb() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public int getEventSerial() {
        return eventSerial;
    }

    public void setEventSerial(int eventSerial) {
        this.eventSerial = eventSerial;
    }
}