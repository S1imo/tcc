package com.bento.a;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bento.a.classes.login_class;

public class CadSActivity extends AppCompatActivity {

    private login_class cadastro;
    EditText cad_nom_inp, cad_cpf_inp, cad_cep_inp, cad_tel_inp, cad_rg_inp;
    String nome, cpf, cep, telefone, rg;
    Button but_cad, but_vol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cad_s_layout);

        cad_nom_inp = findViewById(R.id.cad_nome_inp);
        cad_cpf_inp = findViewById(R.id.cad_cpf_inp);
        cad_cep_inp = findViewById(R.id.cad_cep_inp);
        cad_tel_inp = findViewById(R.id.cad_tel_inp);
        cad_rg_inp = findViewById(R.id.cad_rg_inp);

        //definição dos botoes
        but_cad = findViewById(R.id.cad_but_cads);
        but_vol = findViewById(R.id.cad_voltar_but);
        //--

        but_vol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CadSActivity.this, CadActivity.class));
            }
        });

        but_cad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nome = cad_nom_inp.getText().toString();
                cpf = cad_cpf_inp.getText().toString();
                cep = cad_cep_inp.getText().toString();
                telefone = cad_tel_inp.getText().toString();
                rg = cad_rg_inp.getText().toString();

                cadastro.getCadastroS(nome, cpf, cep, telefone, rg);
            }
        });

    }
}
