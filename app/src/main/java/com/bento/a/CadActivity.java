package com.bento.a;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.bento.a.classes.login_class;

public class CadActivity extends AppCompatActivity {

    EditText inp_nom_usu, inp_tip_usu, inp_email, inp_senha, inp_conf_senha;
    TextView alert_camps_text;
    private String nom_usu, tip_usu, email, senha, conf_senha;
    Button but_cad_prox, but_cad_volt;
    private login_class cadastrar = new login_class();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cad_layout);

        //input dos textos
            inp_nom_usu = findViewById(R.id.cad_nom_usu_inp);
            inp_tip_usu = findViewById(R.id.cad_tip_usu_inp);
            inp_email = findViewById(R.id.cad_email_inp);
            inp_senha = findViewById(R.id.cad_senha_inp);
            inp_conf_senha = findViewById(R.id.cad_conf_senha_inp);
        //-

        //alertas
            alert_camps_text = findViewById(R.id.cad_att_pr_camps_text);
        //-

        //botao de voltar e proximo
        but_cad_prox = (Button)findViewById(R.id.cad_but_prox);
        but_cad_volt = (Button)findViewById(R.id.cad_but_vol);

        but_cad_prox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //edittext para string
                nom_usu = inp_nom_usu.getText().toString();
                tip_usu = inp_tip_usu.getText().toString();
                email = inp_email.getText().toString();
                senha = inp_senha.getText().toString();
                conf_senha = inp_conf_senha.getText().toString();

                if(nom_usu.isEmpty() || tip_usu.isEmpty() || email.isEmpty() || senha.isEmpty() || conf_senha.isEmpty())
                    {
                            alert_camps_text.setText("Preencha os campos");
                    }
                else if(!email.equals("firebase"))
                    {
                        alert_camps_text.setText("Email já cadastrado");
                    }
                else if(!senha.equals(conf_senha))
                    {
                        alert_camps_text.setText("Senhas não são iguais");
                    }
                else
                    {
                        //function cadastrar e ir para a próxima activity
                        cadastrar.getCadastro(nom_usu, tip_usu, email, senha, conf_senha);
                        startActivity(new Intent(CadActivity.this, CadSActivity.class));
                        //-
                    }
            }
        });


        but_cad_volt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CadActivity.this, LoginActivity.class));
            }
        });
        //-
    }
}
