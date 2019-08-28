package com.bento.a;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bento.a.users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.santalu.maskedittext.MaskEditText;

import java.util.HashMap;
import java.util.Objects;

public class EditPerfEActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ImageView imagemPerf;
    private Button but_aplicar, but_voltar;
    private MaskEditText novo_cep_inp, novo_cnpj_inp, novo_tel_inp;
    private EditText novo_nom_usu_inp, novo_nom_emp_inp;
    private String novo_nom_usu, tip_usu, novo_nom_emp, novo_cep, novo_cnpj, novo_tel;
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String user_id = user.getUid();

    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_perfe_layout);

        InputToVar();
        SetValues();
        ImagePerfUp();
        ButtonAplicar();
        ButtonVoltar();
    }

    private void SetValues() {
        DatabaseReference myRef = mFirebaseDatabase.getReference("Users/" + user_id);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                novo_nom_emp_inp.setText(user.getUs_nome());
                novo_nom_usu_inp.setText(user.getUs_nome_comp());
                tip_usu = user.getUs_tip_usu();
                novo_cnpj_inp.setText(user.getUs_cnpj());
                novo_cep_inp.setText(user.getUs_cep());
                novo_tel_inp.setText(user.getUs_tel());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void InputToVar() {

        storageReference = FirebaseStorage.getInstance().getReference("perf_img");

        imagemPerf = findViewById(R.id.image_perfil);

        novo_nom_emp_inp = findViewById(R.id.edit_nomEU);
        novo_nom_usu_inp = findViewById(R.id.edit_nomE);
        novo_cep_inp = findViewById(R.id.edit_cepE);
        novo_cnpj_inp = findViewById(R.id.edit_cnpjE);
        novo_tel_inp = findViewById(R.id.edit_telE);

        but_aplicar = findViewById(R.id.buttonAplicar);
        but_voltar = findViewById(R.id.buttonVoltar);
    }

    private void InpToString() {
        novo_nom_usu = novo_nom_usu_inp.getText().toString().trim();
        novo_nom_emp = novo_nom_emp_inp.getText().toString().trim();
        novo_cep = Objects.requireNonNull(novo_cep_inp.getText()).toString().trim();
        novo_cnpj = Objects.requireNonNull(novo_cnpj_inp.getText()).toString().trim();
        novo_tel = Objects.requireNonNull(novo_tel_inp.getText()).toString().trim();
    }

    private void ButtonVoltar() {
        but_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditPerfEActivity.this, PerfilActivity.class));
            }
        });
    }

    private void ButtonAplicar() {
        but_aplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InpToString();
                switch (verCampos()) {
                    case 1:
                        Toast.makeText(EditPerfEActivity.this, "Preencha os campos", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(EditPerfEActivity.this, "Preencha o CEP corretamente", Toast.LENGTH_SHORT).show();
                        novo_cep_inp.requestFocus();
                        break;
                    case 3:
                        Toast.makeText(EditPerfEActivity.this, "Preencha o CNPJ corretamente", Toast.LENGTH_SHORT).show();
                        novo_cnpj_inp.requestFocus();
                        break;
                    case 4:
                        Toast.makeText(EditPerfEActivity.this, "Preencha o número de telefone corretamente", Toast.LENGTH_SHORT).show();
                        novo_tel_inp.requestFocus();
                        break;
                    case 0:
                        ChangeData();
                        break;
                    default:
                        Toast.makeText(EditPerfEActivity.this, "Um erro inesperado ocorreu", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private int verCampos() {
        if (novo_nom_usu.isEmpty() || novo_cep.isEmpty() || novo_cnpj.isEmpty() || novo_tel.isEmpty()) {
            return 1;
        }
        if (!novo_cep.matches("^\\d{5}[-]\\d{3}$")) {
            return 2;
        }
        if (!novo_cnpj.matches("^\\d{2}\\.\\d{3}\\.\\d{3}[/]\\d{4}[-]\\d{2}$")) {
            return 3;
        }
        if (!novo_tel.matches("^\\(\\d{2}\\)\\d{5}-?\\d{4}$")) {
            return 4;
        }
        else
        {
            return 0;
        }
    }

    private void ImagePerfUp()
    {
        imagemPerf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });
    }

    private void openImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            mImageUri = data.getData();
            imagemPerf.setImageURI(mImageUri);
        }
    }

    private void ChangeData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        User user = new User(novo_nom_usu,tip_usu, novo_nom_emp, novo_cep, novo_cnpj, novo_tel);
        HashMap<String, Object> newPost = new HashMap<>();
        newPost.put(user_id, user.toMap());
        UploadFile();
        ref.updateChildren(newPost).addOnCompleteListener(EditPerfEActivity.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(EditPerfEActivity.this, "Mudanças aplicadas", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditPerfEActivity.this, PerfilActivity.class));
                }
                else
                {
                    String error = Objects.requireNonNull(task.getException()).toString();
                    Toast.makeText(EditPerfEActivity.this, "Não foi possível salvar: " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void UploadFile()
    {
        final DatabaseReference myRef = mFirebaseDatabase.getReference("Animais/" + user_id +"/img");
        if(mImageUri != null)
        {
            final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

            fileRef.putFile(mImageUri);
        /*.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
            {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw Objects.requireNonNull(task.getException());
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        assert downloadUri != null;
                        Log.e("SCCS_DB", "then: " + downloadUri.toString());
                        Upload upload = new Upload(downloadUri.toString());
                        myRef.setValue(upload);


                    } else
                    {
                        Toast.makeText(EditPerfEActivity.this, "upload failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });*/
        }
        else
        {
            Toast.makeText(this, "Nenhum arquivo selecionado", Toast.LENGTH_SHORT).show();
        }
    }
}