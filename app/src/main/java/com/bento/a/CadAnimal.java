package com.bento.a;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bento.a.animals.Animal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.StringBufferInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import de.hdodenhof.circleimageview.CircleImageView;


public class CadAnimal extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int REQUEST_CODE = 1;
    private ImageButton but_voltar;
    private EditText editTextRaca, editTextDesc;
    private Button buttonAplicar;
    private RadioGroup inp_sel_port, inp_sel_vac, inp_sel_stat, inp_sel_idade;
    private RadioButton but_rad_port, but_rad_vac, but_rad_stat, but_rad_idade;
    private Spinner inp_tip_animal;
    private CircleImageView inp_img1, inp_img2, inp_img3, inp_img4, img;
    private String an_idade, tip_animal, an_port, an_vac, an_stat, an_desc, an_raca;
    private String[] an_prof_img = new String[4];
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private StorageReference folder;
    private String currentIdAn;
    private AtomicLong idImgCount = new AtomicLong(0);
    private String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    private HashMap<String, Object> newPost = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cad_dog_layout);

        InpToVar();
        Buttons();
    }

    private void Buttons() {
        ImagePerfUp(inp_img1);
        ImagePerfUp(inp_img2);
        ImagePerfUp(inp_img3);
        ImagePerfUp(inp_img4);
        ButtonAplicar();
        ButtonVoltar();
    }

    private void startCrop(@NonNull Uri uri) {
        String SAMPLE_CROPPED_IMG_NAME = "AnProfPic" + CreateIdImg();
        String destinationFileName = SAMPLE_CROPPED_IMG_NAME + ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        uCrop.withAspectRatio(2, 3);
        uCrop.withMaxResultSize(450, 450);
        uCrop.withOptions(getCropOptions());
        uCrop.start(CadAnimal.this);
    }

    private UCrop.Options getCropOptions() {
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

    private void ImagePerfUp(final CircleImageView image) {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionCheck();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
                img = image;
            }
        });
    }

    private void permissionCheck() {
        if (ContextCompat.checkSelfPermission(CadAnimal.this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissão de internet não habilitada", Toast.LENGTH_SHORT).show();
        } else if (ContextCompat.checkSelfPermission(CadAnimal.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissão de câmera não habilitada", Toast.LENGTH_SHORT).show();
        } else if (ContextCompat.checkSelfPermission(CadAnimal.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissão de leitura não habilitada", Toast.LENGTH_SHORT).show();
        } else if (ContextCompat.checkSelfPermission(CadAnimal.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissão de gravação não habilitada", Toast.LENGTH_SHORT).show();
        }
    }

    private void InpToVar() {
        folder = FirebaseStorage.getInstance().getReference().child("Animais").child("animal" + currentIdAn).child(user_id);

        inp_img1 = findViewById(R.id.addImage);
        inp_img2 = findViewById(R.id.addImage2);
        inp_img3 = findViewById(R.id.addImage3);
        inp_img4 = findViewById(R.id.addImage4);
        inp_img1.setImageDrawable(getDrawable(R.drawable.add_an_prof));
        inp_img2.setImageDrawable(getDrawable(R.drawable.add_an_prof));
        inp_img3.setImageDrawable(getDrawable(R.drawable.add_an_prof));
        inp_img4.setImageDrawable(getDrawable(R.drawable.add_an_prof));

        currentIdAn = CreateIdAn();

        editTextDesc = findViewById(R.id.cad_descricao);
        editTextRaca = findViewById(R.id.cad_raca);

        inp_tip_animal = findViewById(R.id.cad_animal);

        inp_sel_port = findViewById(R.id.radio_port);
        inp_sel_vac = findViewById(R.id.radio_vac);
        inp_sel_stat = findViewById(R.id.radio_stat);
        inp_sel_idade = findViewById(R.id.radio_idade);

        buttonAplicar = findViewById(R.id.button_aplicar);
        but_voltar = findViewById(R.id.voltarDButton);
    }


    private void ButtonAplicar() {
        buttonAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTipAnimal();
                SetRadioText();
                RadioTxtToStg();
                int count = VerificaCad();
                if (count == 1) {
                    Toast.makeText(CadAnimal.this, "Selecione todas as opções", Toast.LENGTH_SHORT).show();
                } else {
                    CreateAn();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        img.setImageURI(null);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                startCrop(imageUri);
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            assert data != null;
            final Uri imageUriResultCrop = UCrop.getOutput(data);
            if (imageUriResultCrop != null) {
                final StorageReference Imagename = folder.child(currentIdAn).child("image" + imageUriResultCrop.getLastPathSegment());
                Imagename.putFile(imageUriResultCrop).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Imagename.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Integer result = Integer.valueOf(CreateIdImg());
                                an_prof_img[result] = Objects.requireNonNull(task.getResult()).toString();
                            }
                        });
                        Toast.makeText(CadAnimal.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        idImgCount.getAndIncrement();
                    }
                });
                img.setImageURI(imageUriResultCrop);
            }
        }
    }

    private int VerificaCad() {
        if (an_desc.isEmpty() || an_port.isEmpty() || an_raca.isEmpty() || an_stat.isEmpty() || an_vac.isEmpty()) {
            return 1;
        } else {
            return 0;
        }
    }

    private void SetRadioText() {
        int selectedid_port = inp_sel_port.getCheckedRadioButtonId();
        int selectedid_vac = inp_sel_vac.getCheckedRadioButtonId();
        int selectedid_stat = inp_sel_stat.getCheckedRadioButtonId();
        int selectedid_idade = inp_sel_idade.getCheckedRadioButtonId();
        but_rad_port = findViewById(selectedid_port);
        but_rad_vac = findViewById(selectedid_vac);
        but_rad_stat = findViewById(selectedid_stat);
        but_rad_idade = findViewById(selectedid_idade);
    }

    private void RadioTxtToStg() {
        an_idade = but_rad_idade.getText().toString().trim();
        an_port = but_rad_port.getText().toString().trim();
        an_vac = but_rad_vac.getText().toString().trim();
        an_stat = but_rad_stat.getText().toString();
        an_raca = editTextRaca.getText().toString().trim();
        an_desc = editTextDesc.getText().toString().trim();
    }

    private void setTipAnimal() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tip_animal, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inp_tip_animal.setAdapter(adapter);
        inp_tip_animal.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            this.tip_animal = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //colocando dados an
    private void CreateAn() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Animais").child(user_id);
        Animal an = new Animal(user_id, currentIdAn, tip_animal, an_idade, an_port, an_vac, an_raca, an_stat, an_desc, new String[]{an_prof_img[0], an_prof_img[1], an_prof_img[2], an_prof_img[3]}, an_prof_img[0]);
        newPost.put(currentIdAn, an.toMap());
        ref.updateChildren(newPost)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CadAnimal.this, "Cadastro efetuado", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CadAnimal.this, PerfilActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CadAnimal.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String CreateIdAn() {
        return String.valueOf(System.currentTimeMillis()*100);
    }

    private String CreateIdImg() {
        return String.valueOf(idImgCount.get());
    }

    private void ButtonVoltar() {
        but_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CadAnimal.this, PerfilActivity.class));
            }
        });
    }
}
