package com.bento.a;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.santalu.maskedittext.MaskEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CadSEActivity extends AppCompatActivity {

    private EditText cad_nom_inp;
    private MaskEditText cad_cep_inp, cad_tel_inp, cad_cnpj_inp;
    private String nome_comp, cnpj, cep, telefone, nom_usu, tip_usu, email, senha;
    private Button but_cad, but_vol;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cad_se_layout);

        inputToVar();
        intentGetVar();

        buttonVoltar(but_vol);
        buttonCadIr(but_cad);
    }

    private void inputToVar() {

        cad_nom_inp = findViewById(R.id.cad_nome_inp);
        cad_cep_inp = findViewById(R.id.cad_cep_inp);
        cad_tel_inp = findViewById(R.id.cad_tel_inp);
        cad_cnpj_inp = findViewById(R.id.cad_cnpj_inp);

        but_cad = findViewById(R.id.cad_but_cads);
        but_vol = findViewById(R.id.cad_voltar_but);
    }

    private void buttonVoltar(Button butt_vol)
    {
        butt_vol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CadSEActivity.this, CadActivity.class));
            }
        });
    }

    private void buttonCadIr(Button butt_cad)
    {
        butt_cad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textToString();

                switch(verfCamps(nome_comp, cnpj, cep, telefone,nom_usu, tip_usu, email,senha)) {

                    case 1:
                        Toast.makeText(CadSEActivity.this, "Preencha os campos", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(CadSEActivity.this, "O campo nome não pode conter numeros", Toast.LENGTH_SHORT).show();
                        cad_nom_inp.requestFocus();
                        break;
                    case 3:
                        Toast.makeText(CadSEActivity.this, "Preencha inteiramente o CNPJ: " + cnpj, Toast.LENGTH_SHORT).show();
                        cad_cnpj_inp.requestFocus();
                        break;
                    case 4:
                        Toast.makeText(CadSEActivity.this, "Preencha inteiramente o CEP", Toast.LENGTH_SHORT).show();
                        cad_cep_inp.requestFocus();
                        break;
                    case 5:
                        Toast.makeText(CadSEActivity.this, "Preencha inteiramente o Telefone", Toast.LENGTH_SHORT).show();
                        cad_tel_inp.requestFocus();
                        break;
                    case 6:
                        Toast.makeText(CadSEActivity.this, "Alguma coisa null: " + senha + nom_usu + tip_usu + email, Toast.LENGTH_SHORT).show();
                        cad_nom_inp.requestFocus();
                        break;
                    case 0:
                        connectDB(email, senha);
                        break;
                    default:
                        Toast.makeText(CadSEActivity.this, "Um erro inesperado aconteceu", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private static int verfCamps(String nome, String cnpj, String cep, String telefone, String nom_usu, String tip_usu, String email, String senha)
    {
        if(nome.isEmpty() || cnpj.isEmpty() || cep.isEmpty() || telefone.isEmpty())
        {
            return 1;
        }
        if(nome.matches("^[0-9]$"))
        {
            return 2;
        }
        if(!cnpj.matches("^\\d{2}\\.\\d{3}\\.\\d{3}[/]\\d{4}[-]\\d{2}$"))
        {
            return 3;
        }
        if(!cep.matches("^\\d{5}[-]\\d{3}$"))
        {
            return 4;
        }
        if(!telefone.matches("^\\(\\d{2}\\)\\d{5}-?\\d{4}$"))
        {
            return 5;
        }
       if(nom_usu.isEmpty() || tip_usu.isEmpty() || email.isEmpty() || senha.isEmpty())
        {
            return 6;
        }
        else
        {
            return 0;
        }
    }

    private void textToString()
    {
        this.nome_comp = cad_nom_inp.getText().toString().trim();
        this.cnpj = Objects.requireNonNull(cad_cnpj_inp.getText()).toString().trim();
        this.cep = Objects.requireNonNull(cad_cep_inp.getText()).toString().trim();
        this.telefone = Objects.requireNonNull(cad_tel_inp.getText()).toString().trim();
    }

    private void intentGetVar()
    {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        nom_usu = bundle.getString("nom_usu");
        tip_usu = bundle.getString("tip_usu");
        senha = bundle.getString("senha");
        email = bundle.getString("email");
    }

    private void connectDB(String email, String senha)
    {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(CadSEActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(CadSEActivity.this, "Cadastro efetuado", Toast.LENGTH_SHORT).show();
                            dataDB();
                            startActivity(new Intent(CadSEActivity.this, LoginActivity.class));

                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(CadSEActivity.this, "Cadastro não efetuado: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //metodo para cadastrar informações no bd
    private void dataDB()
    {
        String user_id = mAuth.getCurrentUser().getUid();
        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        Map<String, String> dataInfo = new HashMap<>();
        dataInfo.put("Nome_Usuario",nom_usu);
        dataInfo.put("Nome_Empresa", nome_comp);
        dataInfo.put("CNPJ", cnpj);
        dataInfo.put("CEP", cep);
        dataInfo.put("Telefone", telefone);

        current_user_db.setValue(dataInfo);
    }

}
