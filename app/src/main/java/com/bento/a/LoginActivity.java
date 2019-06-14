package com.bento.a;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private Button but_log_ent;
    private TextView log_cad, log_esq_senha;
    private EditText inp_email;
    private android.support.design.widget.TextInputEditText inp_senha;
    private String email, senha;


    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        /*if(isLogged())
        {
            startActivity(Intent());
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        inputToVar();

        textCad();
        textEsqSenha();
        button_log();

    }

    private void inputToVar()
    {
        this.but_log_ent = findViewById(R.id.log_ent_but);
        this.log_cad = findViewById(R.id.log_cad);
        this.log_esq_senha = findViewById(R.id.log_esq_senha);
        this.inp_email = findViewById(R.id.log_email_inp);
        this.inp_senha = findViewById(R.id.log_senha);
    }

    private void inputToString()
    {
        email= inp_email.getText().toString().trim();
        senha= Objects.requireNonNull(inp_senha.getText()).toString().trim();
    }

    private void textCad()
    {
        log_cad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CadSActivity.class));
            }
        });
    }

    private void textEsqSenha()
    {
        log_esq_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void button_log()
    {
        but_log_ent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputToString();
                switch (verifyLogin())
                {
                    case 1:
                        //email vazio
                        inp_email.requestFocus();
                        break;
                    case 2:
                        inp_senha.requestFocus();
                        //senha vazia
                        break;
                    case 3:
                        inp_email.requestFocus();
                        //email ou senha não encontrados
                        break;
                    case 0:
                        //conectando
                        break;
                    default:
                        //erro inesperado
                        break;
                }
            }
        });
    }

    private int verifyLogin() {
        if(email.isEmpty())
        {
            return 1;
        }
        else if(senha.isEmpty())
        {
            return 2;
        }
        else if(email.matches("") || senha.matches(""))
        {
            return 3;
        }
        else
        {
            return 0;
        }

    }

    /*private boolean isLogged(FirebaseUser user)
    {

    }*/
}
