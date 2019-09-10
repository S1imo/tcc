package com.bento.a;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bento.a.users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.UploadTask;
import com.santalu.maskedittext.MaskEditText;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditPerfEActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private CircleImageView imagemPerf;
    private Button but_aplicar, but_voltar;
    private MaskEditText novo_cep_inp, novo_cnpj_inp, novo_tel_inp;
    private EditText novo_nom_usu_inp, novo_nom_emp_inp;
    private String novo_nom_usu, tip_usu, novo_nom_emp, novo_cep, novo_cnpj, novo_tel;
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private StorageReference folder;
    private String user_id = mAuth.getUid();
    private HashMap<String, Object> newPost = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_perfe_layout);

        InputToVar();
        SetValues();
        ProfilePic();
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
                novo_nom_emp_inp.setText(user.getUs_nome_comp());
                novo_nom_usu_inp.setText(user.getUs_nome());
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

    private void permissionCheck()
    {if (ContextCompat.checkSelfPermission(EditPerfEActivity.this, Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED) {
        Toast.makeText(this, "Permissão de internet não habilitada", Toast.LENGTH_SHORT).show();
    }
    else if(ContextCompat.checkSelfPermission(EditPerfEActivity.this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
        Toast.makeText(this, "Permissão de câmera não habilitada", Toast.LENGTH_SHORT).show();
    }
    else if(ContextCompat.checkSelfPermission(EditPerfEActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED)
    {
        Toast.makeText(this, "Permissão de leitura não habilitada", Toast.LENGTH_SHORT).show();
    }
    else if(ContextCompat.checkSelfPermission(EditPerfEActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED)
    {
        Toast.makeText(this, "Permissão de gravação não habilitada", Toast.LENGTH_SHORT).show();
    }}

    private void InputToVar() {
        folder = FirebaseStorage.getInstance().getReference().child("Users").child(user_id);

        imagemPerf = findViewById(R.id.image_perfil);

        novo_nom_usu_inp = findViewById(R.id.edit_nomEU);
        novo_nom_emp_inp = findViewById(R.id.edit_nomE);
        novo_cep_inp = findViewById(R.id.edit_cepE);
        novo_cnpj_inp = findViewById(R.id.edit_cnpjE);
        novo_tel_inp = findViewById(R.id.edit_telE);

        but_aplicar = findViewById(R.id.buttonAplicar);
        but_voltar = findViewById(R.id.buttonVoltar);
    }

    private void InpToString() {
        novo_nom_usu = novo_nom_usu_inp.getText().toString();
        novo_nom_emp = novo_nom_emp_inp.getText().toString();
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

    private void ProfilePic() {
        folder.child("imageUserProf.jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                imagemPerf.setImageBitmap(Bitmap.createScaledBitmap(bmp, imagemPerf.getWidth(),
                        imagemPerf.getHeight(), false));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(EditPerfEActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                imagemPerf.setImageDrawable(getDrawable(R.drawable.profile_icon));
            }
        });
    }

    private int verCampos() {
        if (novo_nom_usu.isEmpty() || novo_nom_emp.isEmpty() || novo_cep.isEmpty() || novo_cnpj.isEmpty() || novo_tel.isEmpty()) {
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
                permissionCheck();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imagemPerf.setImageURI(null);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            Uri imageUri = data.getData();
            if(imageUri != null)
            {
                startCrop(imageUri);
            }
        }
        else if(requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK)
        {
            assert data != null;
            Uri imageUriResultCrop = UCrop.getOutput(data);
            if(imageUriResultCrop != null)
            {
                final StorageReference Imagename = folder.child(user_id).child("image" + imageUriResultCrop.getLastPathSegment());
                Imagename.putFile(imageUriResultCrop).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(EditPerfEActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        Imagename.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                newPost.put("us_img", Objects.requireNonNull(task.getResult()).toString());
                            }
                        });
                    }
                });
                imagemPerf.setImageURI(imageUriResultCrop);
            }
        }

    }

    private void startCrop(@NonNull Uri uri)
    {
        String SAMPLE_CROPPED_IMG_NAME = "UserProf";
        String destinationFileName = SAMPLE_CROPPED_IMG_NAME + ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        uCrop.withAspectRatio(2, 3);
        uCrop.withMaxResultSize(450,450);
        uCrop.withOptions(getCropOptions());
        uCrop.start(EditPerfEActivity.this);
    }

    private UCrop.Options getCropOptions(){
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(70);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
        options.setStatusBarColor(getResources().getColor(R.color.color_branco_menu));
        options.setToolbarColor(getResources().getColor(R.color.color_cinza_menu));
        options.setToolbarTitle("Recortar imagem");
        return options;
    }

    private void ChangeData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        User user = new User(novo_nom_usu, tip_usu, novo_nom_emp, novo_cep, novo_cnpj, novo_tel);
        newPost.put(user_id, user.toMap());
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
}