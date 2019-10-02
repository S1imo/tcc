package com.bento.a;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static int PERMISSION_CODE_EX_ST = 1, PERMISSION_CODE_WR_ST = 1, PERMISSION_CODE_INTERNET = 1;
    private Button but_log_ent;
    private TextView log_cad, log_esq_senha;
    private EditText inp_email;
    private TextInputEditText inp_senha;
    private String email, senha;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ProgressBar progressBar;


    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        if (!(ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) || !(ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) || !(ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED)) {
            requestStoragePermission();
        } else {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                Log.d("USRLOG", "Usuário logado");
                startActivity(new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
            } else {
                Log.d("ERRLOG", "Usuário não logado");
            }
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permissão necessária")
                    .setMessage("Estas permissões são necessárias para o funcionamento do aplicativo")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE_EX_ST);
                            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE_WR_ST);
                            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.INTERNET}, PERMISSION_CODE_INTERNET);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE_EX_ST);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE_WR_ST);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PERMISSION_CODE_INTERNET);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE_WR_ST && requestCode == PERMISSION_CODE_EX_ST && requestCode == PERMISSION_CODE_INTERNET) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("PER20", "Permissão garantida");
            } else {
                Log.d("PER20", "Permissão não garantida");
            }
        }
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

    private void inputToVar() {
        this.but_log_ent = findViewById(R.id.log_ent_but);
        this.log_cad = findViewById(R.id.log_cad);
        this.log_esq_senha = findViewById(R.id.log_esq_senha);
        this.inp_email = findViewById(R.id.log_email_inp);
        this.inp_senha = findViewById(R.id.log_senha);
        this.progressBar = findViewById(R.id.load);
    }

    private void inputToString() {
        email = inp_email.getText().toString().trim();
        senha = Objects.requireNonNull(inp_senha.getText()).toString().trim();
    }

    private void textCad() {
        log_cad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CadActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
            }
        });
    }

    private void textEsqSenha() {
        log_esq_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, EsqueceuSenhaActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
            }
        });
    }

    private void button_log() {
        but_log_ent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputToString();
                switch (verifyLogin()) {
                    case 1:
                        //email vazio
                        Toast.makeText(LoginActivity.this, "Preencha o campo e-mail", Toast.LENGTH_SHORT).show();
                        inp_email.requestFocus();
                        progressBar.setVisibility(View.GONE);
                        break;
                    case 2:
                        Toast.makeText(LoginActivity.this, "Preencha o campo senha", Toast.LENGTH_SHORT).show();
                        inp_senha.requestFocus();
                        progressBar.setVisibility(View.GONE);
                        //senha vazia
                        break;
                    case 0:
                        //conectando
                        logUser();
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    default:
                        //erro inesperado

                        progressBar.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    private int verifyLogin() {
        if (email.isEmpty()) {
            return 1;
        } else if (senha.isEmpty()) {
            return 2;
        } else {
            return 0;
        }

    }

    private void logUser() {
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressBar.setVisibility(View.VISIBLE);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                        } else {
                            Toast.makeText(LoginActivity.this, "E-mail ou senha não registrados", Toast.LENGTH_SHORT).show();
                            inp_email.requestFocus();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
