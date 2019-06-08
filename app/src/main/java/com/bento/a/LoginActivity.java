package com.bento.a;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    Button but_log_ent;
    TextView log_cad, log_esq_senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        //Text cadastro e esqueceu_senha
        log_cad = (TextView)findViewById(R.id.log_cad);
        log_esq_senha = (TextView)findViewById(R.id.log_esq_senha);

        //Botão de login
        but_log_ent = (Button)findViewById(R.id.log_ent_but);

        log_esq_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        log_cad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CadActivity.class));
            }
        });

        but_log_ent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
