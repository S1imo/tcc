package com.bento.a.Classes;

import java.util.HashMap;
import java.util.Map;

public class Messages {
    private String us_sender, us_receiver, message, current_time;

    public Messages() {
    }

    public Messages(String us_sender, String us_receiver, String message, String current_time) {
        this.us_sender = us_sender;
        this.us_receiver = us_receiver;
        this.message = message;
        this.current_time = current_time;
    }

    public String getUs_sender() {
        return us_sender;
    }

    public void setUs_sender(String us_sender) {
        this.us_sender = us_sender;
    }

    public String getUs_receiver() {
        return us_receiver;
    }

    public void setUs_receiver(String us_receiver) {
        this.us_receiver = us_receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> dataInfo = new HashMap<>();
        dataInfo.put("us_sender", us_sender);
        dataInfo.put("us_receiver", us_receiver);
        dataInfo.put("message", message);
        dataInfo.put("current_time", current_time);
        return dataInfo;
    }
}
