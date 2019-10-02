package com.bento.a.Classes;

import java.util.HashMap;
import java.util.Map;

public class Messages {
    private String idMessage, us_uid, other_us_uid, message;

    public Messages() {
    }

    public Messages(String idMessage, String us_uid, String other_us_uid, String message) {
        this.idMessage = idMessage;
        this.us_uid = us_uid;
        this.other_us_uid = other_us_uid;
        this.message = message;
    }

    public String getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(String idMessage) {
        this.idMessage = idMessage;
    }

    public String getUs_uid() {
        return us_uid;
    }

    public void setUs_uid(String us_uid) {
        this.us_uid = us_uid;
    }

    public String getOther_us_uid() {
        return other_us_uid;
    }

    public void setOther_us_uid(String other_us_uid) {
        this.other_us_uid = other_us_uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> dataInfo = new HashMap<>();
        dataInfo.put("idMessage", idMessage);
        dataInfo.put("us_uid", us_uid);
        dataInfo.put("other_us_uid", other_us_uid);
        dataInfo.put("message", message);
        return dataInfo;
    }
}
