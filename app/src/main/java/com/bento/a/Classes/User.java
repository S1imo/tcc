package com.bento.a.Classes;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String us_nome, us_status, us_tip_usu, us_nome_comp, us_cpf, us_cnpj, us_cep, us_tel, us_dat_nasc, us_rg, us_img;

    public User(){}

    public User(String nome, String tip_usu, String nome_comp, String cpf, String cep, String tel, String dat_nasc, String rg, String us_img) {
        this.us_nome = nome;
        this.us_tip_usu = tip_usu;
        this.us_nome_comp = nome_comp;
        this.us_cpf = cpf;
        this.us_cep = cep;
        this.us_tel = tel;
        this.us_dat_nasc = dat_nasc;
        this.us_rg = rg;
        this.us_img = us_img;
    }

    public User(String nome, String tip_usu, String nome_comp, String cep, String cnpj, String tel, String us_img) {
        this.us_nome = nome;
        this.us_tip_usu = tip_usu;
        this.us_nome_comp = nome_comp;
        this.us_cep = cep;
        this.us_cnpj = cnpj;
        this.us_tel = tel;
        this.us_img = us_img;
    }

    public String getUs_nome() {
        return this.us_nome;
    }

    public String getUs_tip_usu() {
        return this.us_tip_usu;
    }

    public String getUs_nome_comp() {
        return this.us_nome_comp;
    }

    public String getUs_cpf() {
        return this.us_cpf;
    }

    public String getUs_cnpj() {
        return this.us_cnpj;
    }

    public String getUs_cep() {
        return this.us_cep;
    }

    public String getUs_tel() {
        return this.us_tel;
    }

    public String getUs_dat_nasc() {
        return this.us_dat_nasc;
    }

    public String getUs_rg() {
        return this.us_rg;
    }

    public String getUs_img(){return this.us_img;}

    public String getUs_status() {
        return us_status;
    }

    public void setUs_status(String us_status) {
        this.us_status = us_status;
    }

    public void setUs_nome(String us_nome) {
        this.us_nome = us_nome;
    }

    public void setUs_tip_usu(String us_tip_usu) {
        this.us_tip_usu = us_tip_usu;
    }

    public void setUs_nome_comp(String us_nome_comp) {
        this.us_nome_comp = us_nome_comp;
    }

    public void setUs_cpf(String us_cpf) {
        this.us_cpf = us_cpf;
    }

    public void setUs_cnpj(String us_cnpj) {
        this.us_cnpj = us_cnpj;
    }

    public void setUs_cep(String us_cep) {
        this.us_cep = us_cep;
    }

    public void setUs_tel(String us_tel) {
        this.us_tel = us_tel;
    }

    public void setUs_dat_nasc(String us_dat_nasc) {
        this.us_dat_nasc = us_dat_nasc;
    }

    public void setUs_rg(String us_rg) {
        this.us_rg = us_rg;
    }

    public void setUs_img(String us_img){this.us_img = us_img;}

    public Map<String, Object> toMap()
    {
        HashMap<String, Object> dataInfo = new HashMap<>();
        if(us_tip_usu.equals("Usuário")){
            dataInfo.put("us_nome",us_nome);
            dataInfo.put("us_nome_comp", us_nome_comp);
            dataInfo.put("us_tip_usu", us_tip_usu);
            dataInfo.put("us_cep", us_cep);
            dataInfo.put("us_cpf", us_cpf);
            dataInfo.put("us_tel", us_tel);
            dataInfo.put("us_dat_nasc", us_dat_nasc);
            dataInfo.put("us_rg", us_rg);
            dataInfo.put("us_img", us_img);

            return dataInfo;
        }
        else if(us_tip_usu.equals("Organização")) {
            dataInfo.put("us_nome", us_nome);
            dataInfo.put("us_nome_comp", us_nome_comp);
            dataInfo.put("us_tip_usu", us_tip_usu);
            dataInfo.put("us_cnpj", us_cnpj);
            dataInfo.put("us_cep", us_cep);
            dataInfo.put("us_tel", us_tel);

            return dataInfo;
        }
        return null;
    }

    public void signUp() {
        FirebaseAuth.getInstance().signOut();
    }
}


