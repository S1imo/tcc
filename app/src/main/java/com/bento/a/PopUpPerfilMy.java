package com.bento.a;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bento.a.Classes.Animal;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PopUpPerfilMy extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private TextView but_exit, text_tip, text_porte, text_idade, text_vac, text_stat, text_cast, text_raca, text_desc;
    private EditText editText_raca, editText_desc;
    private RadioGroup sel_inp_porte, sel_inp_idade, sel_inp_vac, sel_inp_stat, sel_inp_cast;
    private String an_img1, an_img2, an_img3, an_img4, an_cast, an_porte, an_tip, an_idade, an_vac, an_raca, an_stat, an_desc, an_uid;
    private RadioButton but_sel_cast, but_sel_porte, but_sel_idade, but_sel_vac, but_sel_stat, porte_g, porte_p, porte_m, idade_f, idade_a, cas_s, cas_n, vac_s, vac_n, stat_adt, stat_perd;
    private CircleImageView img, image1, image2, image3, image4;
    private FirebaseDatabase mFire;
    private DatabaseReference mRef;
    private StorageReference folder;
    private FirebaseAuth mAuth;
    private String user_id;
    private Button but_editar, but_voltar, but_mapa, but_excluir;
    private boolean image_IsClick = false;
    private static int idImgCount = 0;
    private static boolean isUploaded = false;
    private String[] an_prof_img = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_perfil_my);
        mFire = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getUid();
        mFire = FirebaseDatabase.getInstance();
        mRef = mFire.getReference();
        folder = FirebaseStorage.getInstance().getReference();

        InpToVar();
        getIntentExtra();
        SetValues();
        Buttons();
    }

    private void Buttons() {
        ButtonEditar();
        ButtonImage(image1);
        ButtonImage(image2);
        ButtonImage(image3);
        ButtonImage(image4);
        ButtonAplicar();
        ButtonExcluir();
        ButtonBack();
        ButtonVoltar();
    }

    private void getIntentExtra() {
        an_uid = Objects.requireNonNull(getIntent().getStringExtra("an_uid"));
    }

    private void InpToVar() {
        image1 = findViewById(R.id.image_dog1);
        image2 = findViewById(R.id.image_dog2);
        image3 = findViewById(R.id.image_dog3);
        image4 = findViewById(R.id.image_dog4);

        text_tip = findViewById(R.id.tipo_animal);
        text_porte = findViewById(R.id.porte_animal);
        text_idade = findViewById(R.id.idade_ani);
        text_vac = findViewById(R.id.vacinado_animal);
        text_raca = findViewById(R.id.text_raca_perf); //edit
        text_stat = findViewById(R.id.status_animal);
        text_desc = findViewById(R.id.text_desc_an); //edit
        text_cast = findViewById(R.id.castrado_animal);

        editText_desc = findViewById(R.id.desc_animal);
        editText_raca = findViewById(R.id.raca_animal);


        //Buttons
        but_editar = findViewById(R.id.but_editar);
        but_mapa = findViewById(R.id.but_mapa);
        but_voltar = findViewById(R.id.but_back);
        but_excluir = findViewById(R.id.but_exc);
        but_exit = findViewById(R.id.exit_m);

        //RadioButtons
        porte_g = findViewById(R.id.porte_grande);
        porte_m = findViewById(R.id.porte_medio);
        porte_p = findViewById(R.id.porte_pequeno);
        idade_a = findViewById(R.id.adulto);
        idade_f = findViewById(R.id.filhote);
        cas_n = findViewById(R.id.castrado_nao);
        cas_s = findViewById(R.id.castrado_sim);
        vac_n = findViewById(R.id.vacinado_nao);
        vac_s = findViewById(R.id.vacinado_sim);
        stat_adt = findViewById(R.id.ani_adot);
        stat_perd = findViewById(R.id.ani_perdido);

        //parte edição
        sel_inp_idade = findViewById(R.id.radbut_idade);
        sel_inp_porte = findViewById(R.id.radbut_porte);
        sel_inp_stat = findViewById(R.id.radbut_status);
        sel_inp_vac = findViewById(R.id.radbut_vacina);
        sel_inp_cast = findViewById(R.id.radbut_castrado);

    }

    private void SetValues() {
        mRef.child("Animais").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Animal animal = snapshot.getValue(Animal.class);
                    assert animal != null;
                    if (animal.getAn_uid().equals(an_uid)) {
                        text_tip.setText(animal.getTip_an());
                        text_desc.setText(animal.getAn_descricao());
                        text_idade.setText(animal.getAn_idade());
                        text_porte.setText(animal.getAn_porte());
                        text_raca.setText(animal.getAn_raca());
                        text_stat.setText(animal.getAn_status());
                        text_vac.setText(animal.getAn_vacinado());

                        Picasso.get().load(animal.getAn_prof_img1()).into(image1);
                        Picasso.get().load(animal.getAn_prof_img2()).into(image2);
                        Picasso.get().load(animal.getAn_prof_img3()).into(image3);
                        Picasso.get().load(animal.getAn_prof_img4()).into(image4);

                        an_img1 = animal.getAn_prof_img1();
                        an_img2 = animal.getAn_prof_img2();
                        an_img3 = animal.getAn_prof_img3();
                        an_img4 = animal.getAn_prof_img4();


                        an_prof_img[1] = animal.getAn_prof_img1();
                        an_prof_img[2] = animal.getAn_prof_img2();
                        an_prof_img[3] = animal.getAn_prof_img3();
                        an_prof_img[4] = animal.getAn_prof_img4();

                        an_tip = animal.getTip_an();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void ButtonExcluir() {
        but_excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PopUpPerfilMy.this);
                builder.setTitle("Tem certeza de que quer excluir o animal");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRef.child("Animais").child(user_id).child(an_uid).removeValue();
                        mRef.child("Connections").child(an_uid).removeValue();
                        startActivity(new Intent(PopUpPerfilMy.this, PerfilActivity.class));
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    private void ButtonEditar() {
        but_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //INVISIVEL
                text_porte.setVisibility(View.GONE);
                text_desc.setVisibility(View.GONE);
                text_raca.setVisibility(View.GONE);
                text_stat.setVisibility(View.GONE);
                text_cast.setVisibility(View.GONE);
                text_idade.setVisibility(View.GONE);
                text_tip.setVisibility(View.GONE);
                text_vac.setVisibility(View.GONE);
                but_editar.setVisibility(View.GONE);
                but_excluir.setVisibility(View.GONE);

                //VISIVEL
                Picasso.get().load(R.drawable.add_an_prof).into(image1);
                Picasso.get().load(R.drawable.add_an_prof).into(image2);
                Picasso.get().load(R.drawable.add_an_prof).into(image3);
                Picasso.get().load(R.drawable.add_an_prof).into(image4);

                porte_g.setVisibility(View.VISIBLE);
                porte_p.setVisibility(View.VISIBLE);
                porte_m.setVisibility(View.VISIBLE);
                idade_a.setVisibility(View.VISIBLE);
                idade_f.setVisibility(View.VISIBLE);
                cas_n.setVisibility(View.VISIBLE);
                cas_s.setVisibility(View.VISIBLE);
                vac_n.setVisibility(View.VISIBLE);
                vac_s.setVisibility(View.VISIBLE);
                stat_adt.setVisibility(View.VISIBLE);
                stat_perd.setVisibility(View.VISIBLE);
                but_voltar.setVisibility(View.VISIBLE);
                but_mapa.setVisibility(View.VISIBLE);
                editText_desc.setEnabled(true);
                editText_desc.setVisibility(View.VISIBLE);
                editText_raca.setEnabled(true);
                editText_raca.setVisibility(View.VISIBLE);
                image1.setClickable(true);
                image2.setClickable(true);
                image3.setClickable(true);
                image4.setClickable(true);
            }
        });
    }

    private void ButtonImage(final CircleImageView image) {
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

    private void startCrop(@NonNull Uri uri) {
        String SAMPLE_CROPPED_IMG_NAME = "AnProfPic" + CreateIdImg();
        String destinationFileName = SAMPLE_CROPPED_IMG_NAME + ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        uCrop.withAspectRatio(2, 3);
        uCrop.withMaxResultSize(450, 450);
        uCrop.withOptions(getCropOptions());
        uCrop.start(PopUpPerfilMy.this);
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
                final StorageReference Imagename = folder.child("image" + imageUriResultCrop.getLastPathSegment());
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
                        Toast.makeText(PopUpPerfilMy.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        idImgCount++;
                        isUploaded = true;
                    }
                });
                img.setImageURI(imageUriResultCrop);
            }
        }
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

    private void permissionCheck() {
        if (ContextCompat.checkSelfPermission(PopUpPerfilMy.this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissão de internet não habilitada", Toast.LENGTH_SHORT).show();
        } else if (ContextCompat.checkSelfPermission(PopUpPerfilMy.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissão de câmera não habilitada", Toast.LENGTH_SHORT).show();
        } else if (ContextCompat.checkSelfPermission(PopUpPerfilMy.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissão de leitura não habilitada", Toast.LENGTH_SHORT).show();
        }
    }

    private void SetRadioText() {
        int selectId_idade = sel_inp_idade.getCheckedRadioButtonId();
        int selectId_porte = sel_inp_porte.getCheckedRadioButtonId();
        int selectId_stat = sel_inp_stat.getCheckedRadioButtonId();
        int selectId_vac = sel_inp_vac.getCheckedRadioButtonId();
        int selectId_cast = sel_inp_cast.getCheckedRadioButtonId();
        but_sel_idade = findViewById(selectId_idade);
        but_sel_porte = findViewById(selectId_porte);
        but_sel_stat = findViewById(selectId_stat);
        but_sel_vac = findViewById(selectId_vac);
        but_sel_cast = findViewById(selectId_cast);
    }

    private void GetOptions() {
        SetRadioText();
        an_desc = editText_desc.getText().toString();
        an_idade = but_sel_idade.getText().toString();
        an_porte = but_sel_porte.getText().toString();
        an_raca = editText_raca.getText().toString();
        an_stat = but_sel_stat.getText().toString();
        an_vac = but_sel_vac.getText().toString();
        an_cast = but_sel_cast.getText().toString();
    }

    private void ButtonAplicar() {
        but_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetRadioText();
                switch (VerifyText()) {
                    case 0:
                        GetOptions();
                        ReCadAn();
                        break;
                    case 1:
                        Toast.makeText(PopUpPerfilMy.this, "Selecione o porte", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(PopUpPerfilMy.this, "Selecione se o cachorro é vacinado", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(PopUpPerfilMy.this, "Selecione a idade", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(PopUpPerfilMy.this, "Selecione o status", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(PopUpPerfilMy.this, "Escreva a raça", Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(PopUpPerfilMy.this, "Escreva uma descrição", Toast.LENGTH_SHORT).show();
                        break;
                    case 7:
                        Toast.makeText(PopUpPerfilMy.this, "Selecione ao menos uma imagem", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(PopUpPerfilMy.this, "Houve um problema", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
            }
        });
    }

    private int VerifyText() {
        int constant;
        if (but_sel_porte == null) {
            constant = 1;
        } else if (but_sel_vac == null) {
            constant = 2;
        } else if (but_sel_idade == null) {
            constant = 3;
        } else if (but_sel_stat == null) {
            constant = 4;
        } else if (editText_raca.getText() == null || editText_raca.getText().equals("") || editText_raca.getText().equals(" ")) {
            constant = 5;
        } else if (editText_desc.getText() == null || editText_desc.getText().equals("") || editText_desc.getText().equals(" ")) {
            constant = 6;
        } else if (!isUploaded) {
            constant = 7;
        } else {
            constant = 0;
        }
        return constant;
    }

    private void ReCadAn() {
        System.out.println("Blz");
        idImgCount = 0;
        startActivity(new Intent(PopUpPerfilMy.this, MapActivityAn.class)
                .putExtra("an_id", an_uid)
                .putExtra("user_id", user_id)
                .putExtra("currentIdAn", an_uid)
                .putExtra("tip_animal", an_tip)
                .putExtra("an_cast", an_cast)
                .putExtra("an_idade", an_idade)
                .putExtra("an_port", an_porte)
                .putExtra("an_vac", an_vac)
                .putExtra("an_raca", an_raca)
                .putExtra("an_stat", an_stat)
                .putExtra("an_desc", an_desc)
                .putExtra("an_prof_img", new String[]{an_prof_img[0], an_prof_img[1], an_prof_img[2], an_prof_img[3], an_prof_img[4]}));

    }

    private void ButtonVoltar() {
        but_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Visivel
                text_porte.setVisibility(View.VISIBLE);
                text_desc.setVisibility(View.VISIBLE);
                text_raca.setVisibility(View.VISIBLE);
                text_stat.setVisibility(View.VISIBLE);
                text_cast.setVisibility(View.VISIBLE);
                text_idade.setVisibility(View.VISIBLE);
                text_tip.setVisibility(View.VISIBLE);
                text_vac.setVisibility(View.VISIBLE);
                but_editar.setVisibility(View.VISIBLE);
                but_excluir.setVisibility(View.VISIBLE);

                Picasso.get().load(an_img1).into(image1);
                Picasso.get().load(an_img2).into(image2);
                Picasso.get().load(an_img3).into(image3);
                Picasso.get().load(an_img4).into(image4);

                //Invisivel
                porte_g.setVisibility(View.GONE);
                porte_p.setVisibility(View.GONE);
                porte_m.setVisibility(View.GONE);
                idade_a.setVisibility(View.GONE);
                idade_f.setVisibility(View.GONE);
                cas_n.setVisibility(View.GONE);
                cas_s.setVisibility(View.GONE);
                vac_n.setVisibility(View.GONE);
                vac_s.setVisibility(View.GONE);
                stat_adt.setVisibility(View.GONE);
                stat_perd.setVisibility(View.GONE);
                but_voltar.setVisibility(View.GONE);
                but_mapa.setVisibility(View.GONE);
                editText_desc.setEnabled(false);
                editText_desc.setVisibility(View.GONE);
                editText_raca.setEnabled(false);
                editText_raca.setVisibility(View.GONE);
                image1.setClickable(false);
                image2.setClickable(false);
                image3.setClickable(false);
                image4.setClickable(false);
            }
        });
    }

    private static String CreateIdImg() {
        return String.valueOf(idImgCount);
    }

    private void ButtonBack() {
        but_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}