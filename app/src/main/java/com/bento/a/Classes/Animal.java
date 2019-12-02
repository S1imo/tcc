package com.bento.a.Classes;

import java.util.HashMap;
import java.util.Map;

public class Animal {
    private String us_uid, an_uid, tip_an, an_cast, an_idade, an_porte, an_vacinado, an_raca, an_status, an_descricao, an_prof_img1, an_prof_img2, an_prof_img3, an_prof_img4, an_lat, an_long;

    public Animal() {
    }

    public Animal(String us_uid, String an_uid, String tip_an, String an_cast, String an_idade, String an_porte, String an_vacinado, String an_raca, String an_status, String an_descricao, String an_prof_img1, String an_prof_img2, String an_prof_img3, String an_prof_img4, String an_lat, String an_long) {
        this.us_uid = us_uid;
        this.an_uid = an_uid;
        this.tip_an = tip_an;
        this.an_cast = an_cast;
        this.an_idade = an_idade;
        this.an_porte = an_porte;
        this.an_vacinado = an_vacinado;
        this.an_raca = an_raca;
        this.an_status = an_status;
        this.an_descricao = an_descricao;
        this.an_prof_img1 = an_prof_img1;
        this.an_prof_img2 = an_prof_img2;
        this.an_prof_img3 = an_prof_img3;
        this.an_prof_img4 = an_prof_img4;
        this.an_lat = an_lat;
        this.an_long = an_long;
    }


    public String getUs_uid() {
        return this.us_uid;
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

    public String getTip_an() {
        return tip_an;
    }

    public void setTip_an(String tip_an) {
        this.tip_an = tip_an;
    }

    public String getAn_cast() {
        return an_cast;
    }

    public void setAn_cast(String an_cast) {
        this.an_cast = an_cast;
    }

    public String getAn_porte() {
        return an_porte;
    }

    public void setAn_porte(String an_porte) {
        this.an_porte = an_porte;
    }

    public String getAn_vacinado() {
        return an_vacinado;
    }

    public void setAn_vacinado(String an_vacinado) {
        this.an_vacinado = an_vacinado;
    }

    public String getAn_raca() {
        return an_raca;
    }

    public void setAn_raca(String an_raca) {
        this.an_raca = an_raca;
    }

    public String getAn_status() {
        return an_status;
    }

    public void setAn_status(String an_status) {
        this.an_status = an_status;
    }

    public String getAn_descricao() {
        return an_descricao;
    }

    public void setAn_descricao(String an_descricao) {
        this.an_descricao = an_descricao;
    }

    public String getAn_idade() {
        return an_idade;
    }

    public void setAn_idade(String an_idade) {
        this.an_idade = an_idade;
    }

    public String getAn_prof_img1() {
        return an_prof_img1;
    }

    public void setAn_prof_img1(String an_prof_img1) {
        this.an_prof_img1 = an_prof_img1;
    }

    public String getAn_prof_img2() {
        return an_prof_img2;
    }

    public void setAn_prof_img2(String an_prof_img2) {
        this.an_prof_img2 = an_prof_img2;
    }

    public String getAn_prof_img3() {
        return an_prof_img3;
    }

    public void setAn_prof_img3(String an_prof_img3) {
        this.an_prof_img3 = an_prof_img3;
    }

    public String getAn_prof_img4() {
        return an_prof_img4;
    }

    public void setAn_prof_img4(String an_prof_img4) {
        this.an_prof_img4 = an_prof_img4;
    }

    public String getAn_lat() {
        return an_lat;
    }

    public void setAn_lat(String an_lat) {
        this.an_lat = an_lat;
    }

    public String getAn_long() {
        return an_long;
    }

    public void setAn_long(String an_long) {
        this.an_long = an_long;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> dataInfo = new HashMap<>();
        dataInfo.put("us_uid", us_uid);
        dataInfo.put("an_uid", an_uid);
        dataInfo.put("tip_an", tip_an);
        dataInfo.put("an_idade", an_idade);
        dataInfo.put("an_porte", an_porte);
        dataInfo.put("an_vacinado", an_vacinado);
        dataInfo.put("an_raca", an_raca);
        dataInfo.put("an_status", an_status);
        dataInfo.put("an_descricao", an_descricao);
        dataInfo.put("an_prof_img1", an_prof_img1);
        dataInfo.put("an_prof_img2", an_prof_img2);
        dataInfo.put("an_prof_img3", an_prof_img3);
        dataInfo.put("an_prof_img4", an_prof_img4);
        dataInfo.put("an_lat", an_lat);
        dataInfo.put("an_long", an_long);
        return dataInfo;
    }
}
