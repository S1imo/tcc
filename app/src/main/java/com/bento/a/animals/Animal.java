package com.bento.a.animals;

import java.util.HashMap;
import java.util.Map;

public class Animal {
    private String tip_an;
    private String an_porte;
    private String an_vacinado;
    private String an_raca;
    private String an_status;
    private String an_descricao;
    private String[] an_prof_img;
    private String an_fprof_img;

    public Animal() { }

    public Animal(String tip_an, String an_porte, String an_vacinado, String an_raca, String an_status, String an_descricao, String[] an_prof_img, String an_fprof_img) {
        this.tip_an = tip_an;
        this.an_porte = an_porte;
        this.an_vacinado = an_vacinado;
        this.an_raca = an_raca;
        this.an_status = an_status;
        this.an_descricao = an_descricao;
        this.an_prof_img = an_prof_img;
        this.an_fprof_img = an_fprof_img;
    }

    public String getTip_an() {
        return tip_an;
    }

    public void setTip_an(String tip_an) {
        this.tip_an = tip_an;
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

    public String[] getAn_prof_img() {
        return an_prof_img;
    }

    public void setAn_prof_img(String[] an_prof_img) {
        this.an_prof_img = an_prof_img;
    }

    public String getAn_fprof_img()
    {
        return an_fprof_img;
    }

    public void setAn_fprof_img(String an_fprof_img) {
        this.an_fprof_img = an_fprof_img;
    }

    public Map<String, Object> toMap()
    {
        HashMap<String, Object> dataInfo = new HashMap<>();
            dataInfo.put("tip_an",tip_an);
            dataInfo.put("an_porte", an_porte);
            dataInfo.put("an_vacinado", an_vacinado);
            dataInfo.put("an_raca", an_raca);
            dataInfo.put("an_status", an_status);
            dataInfo.put("an_descricao", an_descricao);
            dataInfo.put("an_fprof_img", an_prof_img[1]);
            for (int i = 0; i < 4; i++)
                dataInfo.put("an_prof_img"+i, an_prof_img[i]);
        return dataInfo;
    }
}
