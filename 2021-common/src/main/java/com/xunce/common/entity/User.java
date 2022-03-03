package com.xunce.common.entity;

public class User {
    String bccode;
    String entrytime;

    public User() {
    }


    public String getBccode() {
        return bccode;
    }

    public void setBccode(String bccode) {
        this.bccode = bccode;
    }

    public String getEntrytime() {
        return entrytime;
    }

    public void setEntrytime(String entrytime) {
        this.entrytime = entrytime;
    }

    @Override
    public String toString() {
        return "User{" +
                "bccode='" + bccode + '\'' +
                ", entrytime='" + entrytime + '\'' +
                '}';
    }
}
