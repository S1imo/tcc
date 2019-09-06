package com.bento.a;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bento.a.users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
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

                switch(verfCamps(nome_comp, cnpj, cep, telefone)) {

                    case 1:
                        Toast.makeText(CadSEActivity.this, "Preencha os campos", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(CadSEActivity.this, "Preencha inteiramente o CNPJ: " + cnpj, Toast.LENGTH_SHORT).show();
                        cad_cnpj_inp.requestFocus();
                        break;
                    case 3:
                        Toast.makeText(CadSEActivity.this, "Preencha inteiramente o CEP", Toast.LENGTH_SHORT).show();
                        cad_cep_inp.requestFocus();
                        break;
                    case 4:
                        Toast.makeText(CadSEActivity.this, "Preencha inteiramente o Telefone", Toast.LENGTH_SHORT).show();
                        cad_tel_inp.requestFocus();
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

    private static int verfCamps(String nome, String cnpj, String cep, String telefone)
    {
        if(nome.isEmpty() || cnpj.isEmpty() || cep.isEmpty() || telefone.isEmpty())
        {
            return 1;
        }
        if(!cnpj.matches("^\\d{2}\\.\\d{3}\\.\\d{3}[/]\\d{4}[-]\\d{2}$"))
        {
            return 2;
        }
        if(!cep.matches("^\\d{5}[-]\\d{3}$"))
        {
            return 3;
        }
        if(!telefone.matches("^\\(\\d{2}\\)\\d{5}-?\\d{4}$"))
        {
            return 4;
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
        this.nom_usu = bundle.getString("nom_usu");
        this.tip_usu = bundle.getString("tip_usu");
        this.senha = bundle.getString("senha");
        this.email = bundle.getString("email");
    }

    private void connectDB(String email, String senha)
    {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(CadSEActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(CadSEActivity.this, "Cadastro efetuado", Toast.LENGTH_SHORT).show();
                            dataDB();
                            startActivity(new Intent(CadSEActivity.this, LoginActivity.class));
                        }
                        else
                        {
                         exceptionsFire(task);
                        }
                    }
                });
    }

    //metodo para cadastrar informações no bd
    private void dataDB()
    {
        String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        User user = new User(nom_usu, tip_usu, nome_comp, cep, cnpj, telefone);
        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        Map<String, Object> valuesArr = new HashMap<>();
        valuesArr.put("us_info", user.toMap());
        current_user_db.updateChildren(valuesArr);
    }

    private void exceptionsFire(Task<AuthResult> task)
    {
        try
        {
            throw Objects.requireNonNull(task.getException());
        }
        catch(FirebaseAuthWeakPasswordException e)
        {
            Toast.makeText(CadSEActivity.this, "Senha muito fraca.", Toast.LENGTH_LONG).show();
        }
        catch(FirebaseAuthInvalidCredentialsException e)
        {
            Toast.makeText(CadSEActivity.this, "O e-mail é inválido.", Toast.LENGTH_LONG).show();
        }
        catch(FirebaseAuthUserCollisionException e)
        {
            Toast.makeText(CadSEActivity.this, "O e-mail ou senha já estão cadastrados.", Toast.LENGTH_LONG).show();
        }
        catch(Exception e)
        {
            Log.e("ERRCad", e.getMessage());
        }
    }

}
