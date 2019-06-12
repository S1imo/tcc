package com.bento.a;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

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

        //Text cadastro e esqueceu_senha
        TextView log_cad = (TextView) findViewById(R.id.log_cad);
        TextView log_esq_senha = (TextView) findViewById(R.id.log_esq_senha);

        //Botão de login
        Button but_log_ent = (Button) findViewById(R.id.log_ent_but);

        log_esq_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        log_cad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CadSActivity.class));
            }
        });

        but_log_ent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /*private boolean isLogged(FirebaseUser user)
    {

    }*/
}
