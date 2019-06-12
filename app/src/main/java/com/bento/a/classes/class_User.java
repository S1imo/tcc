package com.bento.a.classes;

public class class_User {
    private String nom_usu, tip_usu, email, senha, conf_senha, nome, cpf, cep, telefone, rg;

    public void setCadastro(String nom_usu, String tip_usu, String email, String senha, String conf_senha)
    {
        this.nom_usu = nom_usu;
        this.tip_usu = tip_usu;
        this.email = email;
        this.senha = senha;
        this.conf_senha = conf_senha;
    }

    public void setCadastroS(String nome, String cpf, String cep, String telefone, String rg)
    {
        this.nome = nome;
        this.cpf = cpf;
        this.cep = cep;
        this.telefone = telefone;
        this.rg = rg;
    }

    public String getEmail() {
        return this.email;
    }

    public String getSenha() {
        return this.senha;
    }

}
