package com.bento.a.Classes;

public class Connections {

    private String us_uid, an_uid, an_us_uid, an_fav;

    public Connections() {
    }

    public Connections(String us_uid, String an_uid, String an_us_uid, String an_fav) {
        this.us_uid = us_uid;
        this.an_uid = an_uid;
        this.an_us_uid = an_us_uid;
        this.an_fav = an_fav;
    }

    public String getUs_uid() {
        return us_uid;
    }

    public void setUs_uid(String us_uid) {
        this.us_uid = us_uid;
    }

    public String getAn_uid() {
        return an_uid;
    }

    public void setAn_uid(String an_uid) {
        this.an_uid = an_uid;
    }

    public String getAn_us_uid() {
        return an_us_uid;
    }

    public void setAn_us_uid(String an_us_uid) {
        this.an_us_uid = an_us_uid;
    }

    public String getAn_fav() {
        return an_fav;
    }

    public void setAn_fav(String an_fav) {
        this.an_fav = an_fav;
    }
}

