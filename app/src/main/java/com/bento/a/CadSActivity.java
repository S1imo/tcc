package com.bento.a;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bento.a.classes.class_User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CadSActivity extends AppCompatActivity {

    private class_User cadastro = new class_User();
    private EditText cad_nom_inp, cad_cpf_inp, cad_cep_inp, cad_tel_inp, cad_rg_inp;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String nome, cpf, cep, telefone, rg;


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
        Button but_cad = findViewById(R.id.cad_but_cads);
        Button but_vol = findViewById(R.id.cad_voltar_but);
        //--

        but_vol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CadSActivity.this, LoginActivity.class));
            }
        });

        but_cad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //edit to string
                nome = cad_nom_inp.getText().toString().trim();
                cpf = cad_cpf_inp.getText().toString().trim();
                cep = cad_cep_inp.getText().toString().trim();
                telefone = cad_tel_inp.getText().toString().trim();
                rg = cad_rg_inp.getText().toString().trim();
                //-

                if(nome.isEmpty() || cpf.isEmpty() || cep.isEmpty() || telefone.isEmpty() || rg.isEmpty())
                {
                    Toast.makeText(CadSActivity.this, "Preencha os campons", Toast.LENGTH_SHORT).show();
                }
                if(!cpf.equals("\\d{3}.\\d{3}.\\d{3}-\\d{2}"))
                {
                    //https://developer.android.com/reference/java/util/regex/Pattern - site de patterns
                }
                if(!cep.equals("\\d{5}-\\d{3}"))
                {

                }
                /*if()
                {

                }
                if()
                {

                }
                if()
                {

                }*/
                cadastro.setCadastroS(nome, cpf, cep, telefone, rg);
                //jogar cadastros no bd
                startActivity(new Intent(CadSActivity.this, CadActivity.class));

            }
        });

    }
}
