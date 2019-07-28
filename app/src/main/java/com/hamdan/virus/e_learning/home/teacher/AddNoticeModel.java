package com.hamdan.virus.e_learning.home.teacher;

public class AddNoticeModel {

    String notice;
    String name;
    String subject;

    public AddNoticeModel(String name, String notice) {
        this.notice = notice;
        this.name = name;
    }

    public AddNoticeModel() {
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
