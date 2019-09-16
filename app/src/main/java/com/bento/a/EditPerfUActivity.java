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

import com.bento.a.Classes.User;
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

public class EditPerfUActivity extends AppCompatActivity {

    private Button but_aplicar, but_voltar;
    private static final int REQUEST_CODE = 1;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private CircleImageView imagemPerf;
    private MaskEditText  novo_cep_inp, novo_cpf_inp, novo_dat_inp, novo_rg_inp, novo_tel_inp;
    private EditText novo_nom_usu_inp,  novo_nom_comp_usu_inp;
    private String novo_nom_usu, tip_usu, novo_nom_comp_usu, novo_cep, novo_cpf, novo_dat, novo_rg, novo_tel, us_img;
    private FirebaseUser user = mAuth.getCurrentUser();
    private StorageReference folder;
    private String user_id = Objects.requireNonNull(user).getUid();
    private HashMap<String, Object> newPost = new HashMap<>();

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getUid() == null)
        {
            startActivity(new Intent(EditPerfUActivity.this, LoginActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_perfu_layout);
        InputToVar();
        ProfilePic();
        ImagePerfUp();
        SetValues();
        ButtonAplicar();
        ButtonVoltar();
    }

    private void SetValues() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        String user_id = user.getUid();
        DatabaseReference myRef = mFirebaseDatabase.getReference("Users/" + user_id);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                novo_nom_usu_inp.setText(user.getUs_nome());
                novo_nom_comp_usu_inp.setText(user.getUs_nome_comp());
                tip_usu = user.getUs_tip_usu();
                novo_cep_inp.setText(user.getUs_cep());
                novo_cpf_inp.setText(user.getUs_cpf());
                novo_dat_inp.setText(user.getUs_dat_nasc());
                novo_rg_inp.setText(user.getUs_rg());
                novo_tel_inp.setText(user.getUs_tel());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void permissionCheck()
    {if (ContextCompat.checkSelfPermission(EditPerfUActivity.this, Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED) {
        Toast.makeText(this, "Permissão de internet não habilitada", Toast.LENGTH_SHORT).show();
    }
    else if(ContextCompat.checkSelfPermission(EditPerfUActivity.this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
        Toast.makeText(this, "Permissão de câmera não habilitada", Toast.LENGTH_SHORT).show();
    }
    else if(ContextCompat.checkSelfPermission(EditPerfUActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED)
    {
        Toast.makeText(this, "Permissão de leitura não habilitada", Toast.LENGTH_SHORT).show();
    }
    else if(ContextCompat.checkSelfPermission(EditPerfUActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED)
    {
        Toast.makeText(this, "Permissão de gravação não habilitada", Toast.LENGTH_SHORT).show();
    }}

    private void InputToVar()
    {
        folder = FirebaseStorage.getInstance().getReference().child("Users").child(user_id);

        novo_nom_usu_inp = findViewById(R.id.edit_nom);
        novo_nom_comp_usu_inp = findViewById(R.id.edit_nomComp);
        novo_cep_inp = findViewById(R.id.edit_cep);
        novo_cpf_inp = findViewById(R.id.edit_cpf);
        novo_dat_inp = findViewById(R.id.edit_data);
        novo_rg_inp = findViewById(R.id.edit_rg);
        novo_tel_inp = findViewById(R.id.edit_tel);

        imagemPerf = findViewById(R.id.image_perfil);

        but_aplicar = findViewById(R.id.buttonAplicar);
        but_voltar = findViewById(R.id.buttonVoltar);
    }

    private void InpToString()
    {
        novo_nom_usu = novo_nom_usu_inp.getText().toString().trim();
        novo_nom_comp_usu = novo_nom_comp_usu_inp.getText().toString().trim();
        novo_cep = Objects.requireNonNull(novo_cep_inp.getText()).toString().trim();
        novo_cpf = Objects.requireNonNull(novo_cpf_inp.getText()).toString().trim();
        novo_dat = Objects.requireNonNull(novo_dat_inp.getText()).toString().trim();
        novo_rg = Objects.requireNonNull(novo_rg_inp.getText()).toString().trim();
        novo_tel = Objects.requireNonNull(novo_tel_inp.getText()).toString().trim();
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
                Toast.makeText(EditPerfUActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                imagemPerf.setImageDrawable(getDrawable(R.drawable.profile_icon));
            }
        });
    }

    private void ButtonVoltar()
    {
        but_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditPerfUActivity.this, PerfilActivity.class));
            }
        });
    }

    private void ButtonAplicar()
    {
        but_aplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InpToString();
                switch(verCampos())
                {
                    case 1:
                        Toast.makeText(EditPerfUActivity.this, "Preencha o CEP corretamente", Toast.LENGTH_SHORT).show();
                        novo_cep_inp.requestFocus();
                        break;
                    case 2:
                        Toast.makeText(EditPerfUActivity.this, "Preencha o CPF corretamente", Toast.LENGTH_SHORT).show();
                        novo_cpf_inp.requestFocus();
                        break;
                    case 3:
                        Toast.makeText(EditPerfUActivity.this, "Preencha o número de telefone corretamente", Toast.LENGTH_SHORT).show();
                        novo_tel_inp.requestFocus();
                        break;
                    case 4:
                        Toast.makeText(EditPerfUActivity.this, "Preencha a data de nascimento corretamente", Toast.LENGTH_SHORT).show();
                        novo_dat_inp.requestFocus();
                        break;
                    case 5:
                        Toast.makeText(EditPerfUActivity.this, "Preencha o RG corretamente", Toast.LENGTH_SHORT).show();
                        novo_rg_inp.requestFocus();
                        break;
                    case 0:
                        ChangeData();
                        break;
                    default:
                        Toast.makeText(EditPerfUActivity.this, "Um erro inesperado ocorreu", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private int verCampos()
    {
        if(!novo_cep.matches("^\\d{5}[-]\\d{3}$"))
        {
            return 1;
        }
        if(!novo_cpf.matches("^([0-9]{3}\\.?){3}-?[0-9]{2}$"))
        {
            return 2;
        }
        if(!novo_tel.matches("^\\(\\d{2}\\)\\d{5}-?\\d{4}$"))
        {
            return 3;
        }
        if(!novo_dat.matches("^\\d{2}[/]\\d{2}[/]\\d{4}$"))
        {
            return 4;
        }
        if(!novo_rg.matches("^\\d{2}\\.?\\d{3}\\.?\\d{3}\\.?-?\\d{1}$"))
        {
            return 5;
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
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
            assert data != null;
            Uri imageUriResultCrop = UCrop.getOutput(data);
            if(imageUriResultCrop != null)
            {
                final StorageReference Imagename = folder.child(user_id).child("image" + imageUriResultCrop.getLastPathSegment());
                Imagename.putFile(imageUriResultCrop).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(EditPerfUActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        Imagename.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                us_img = Objects.requireNonNull(task.getResult()).toString();
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
        uCrop.start(EditPerfUActivity.this);
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
        String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        User user = new User(novo_nom_usu, tip_usu, novo_nom_comp_usu, novo_cpf, novo_cep, novo_tel, novo_dat, novo_rg, us_img);

        newPost.put(user_id, user.toMap());
        ref.updateChildren(newPost).addOnCompleteListener(EditPerfUActivity.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(EditPerfUActivity.this, "Mudanças aplicadas", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditPerfUActivity.this, PerfilActivity.class));
                }
                else
                {
                    String error = Objects.requireNonNull(task.getException()).toString();
                    Toast.makeText(EditPerfUActivity.this, "Não foi possível salvar: " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
