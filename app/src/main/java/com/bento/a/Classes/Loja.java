package com.bento.a.Classes;

import java.util.HashMap;
import java.util.Map;

public class Loja {

    private String l_id, l_us_uid, l_nome, l_qtd, l_val, l_desc, l_img_1, l_img_2;

    public Loja() {
    }

    public Loja(String l_id, String l_us_uid, String l_nome, String l_qtd, String l_val, String l_desc, String l_img_1, String l_img_2) {
        this.l_id = l_id;
        this.l_us_uid = l_us_uid;
        this.l_nome = l_nome;
        this.l_qtd = l_qtd;
        this.l_val = l_val;
        this.l_desc = l_desc;
        this.l_img_1 = l_img_1;
        this.l_img_2 = l_img_2;
    }

    public String getL_id() {
        return l_id;
    }

    public void setL_id(String l_id) {
        this.l_id = l_id;
    }

    public String getL_us_uid() {
        return l_us_uid;
    }

    public void setL_us_uid(String l_us_uid) {
        this.l_us_uid = l_us_uid;
    }

    public String getL_nome() {
        return l_nome;
    }

    public void setL_nome(String l_nome) {
        this.l_nome = l_nome;
    }

    public String getL_qtd() {
        return l_qtd;
    }

    public void setL_qtd(String l_qtd) {
        this.l_qtd = l_qtd;
    }

    public String getL_val() {
        return l_val;
    }

    public void setL_val(String l_val) {
        this.l_val = l_val;
    }

    public String getL_desc() {
        return l_desc;
    }

    public void setL_desc(String l_desc) {
        this.l_desc = l_desc;
    }

    public String getL_img_1() {
        return l_img_1;
    }

    public void setL_img_1(String l_img_1) {
        this.l_img_1 = l_img_1;
    }

    public String getL_img_2() {
        return l_img_2;
    }

    public void setL_img_2(String l_img_2) {
        this.l_img_2 = l_img_2;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> dataInfo = new HashMap<>();
        dataInfo.put("l_nome", l_nome);
        dataInfo.put("l_qtd", l_qtd);
        dataInfo.put("l_val", l_val);
        dataInfo.put("l_desc", l_desc);
        dataInfo.put("l_img_1", l_img_1);
        dataInfo.put("l_img_2", l_img_2);
        return dataInfo;
    }
}
