package com.bento.a;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bento.a.Classes.Loja;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PopUpShopPerfil extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private FirebaseDatabase mFire;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private StorageReference mSRef;
    private String user_id, l_uid, l_img_1, l_img_2, l_nome, l_qtd, l_valor, l_desc;
    private String[] l_img = new String[3];
    private TextView exit, nome_prod, qtd_prod, valor_prod, desc_prod;
    private ImageView img, img1, img2;
    private EditText nome_edit, qtd_edit, valor_edit, desc_edit;
    private Button bt_edit, bt_exc, bt_cancel, bt_apply;
    private static int idImgCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_shop_perfil_layout);
        l_uid = getIntent().getStringExtra("l_id");

        InpToVar();
        DisplayInfo();
        permissionCheck();
        Buttons();
    }

    private void InpToVar() {
        mFire = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mRef = mFire.getReference();
        mSRef = FirebaseStorage.getInstance().getReference();
        user_id = mAuth.getUid();

        exit = findViewById(R.id.exit_prod);
        bt_edit = findViewById(R.id.editar_prod);
        bt_exc = findViewById(R.id.prod_exc);
        bt_cancel = findViewById(R.id.but_cancel);
        bt_apply = findViewById(R.id.but_apli);

        nome_prod = findViewById(R.id.nome_prod);
        qtd_prod = findViewById(R.id.qtd_prod);
        valor_prod = findViewById(R.id.valor_prod);
        desc_prod = findViewById(R.id.desc_prod);

        img1 = findViewById(R.id.image_prod1);
        img2 = findViewById(R.id.image_prod2);

        nome_edit = findViewById(R.id.edit_prod_nome);
        qtd_edit = findViewById(R.id.edit_prod_qtd);
        valor_edit = findViewById(R.id.edit_prod_valor);
        desc_edit = findViewById(R.id.edit_prod_desc);

    }

    private void Buttons() {
        ButtonEditar();
        ButtonExcluir();
        ButtonAplicar();
        ButtonCancel();
        ButtonExit();
    }

    private void DisplayInfo() {
        mRef.child("Produto").child(user_id).child(l_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Loja loja = dataSnapshot.getValue(Loja.class);
                assert loja != null;
                nome_prod.setText(loja.getL_nome());
                qtd_prod.setText(loja.getL_qtd());
                valor_prod.setText(loja.getL_val());
                desc_prod.setText(loja.getL_desc());
                Picasso.get().load(loja.getL_img_1()).into(img1);
                Picasso.get().load(loja.getL_img_2()).into(img2);

                l_img_1 = loja.getL_img_1();
                l_img_2 = loja.getL_img_2();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ButtonExcluir() {
        bt_exc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PopUpShopPerfil.this);
                builder.setTitle("Tem certeza de que quer excluir o produto");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRef.child("Produtos").child(user_id).child(l_uid).removeValue();
                        startActivity(new Intent(PopUpShopPerfil.this, PerfilActivity.class));
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
        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //INVISIVEL
                nome_prod.setVisibility(View.GONE);
                qtd_prod.setVisibility(View.GONE);
                valor_prod.setVisibility(View.GONE);
                desc_prod.setVisibility(View.GONE);
                bt_edit.setVisibility(View.GONE);
                bt_exc.setVisibility(View.GONE);

                Picasso.get().load(R.drawable.quad_plus_perf).into(img1);
                Picasso.get().load(R.drawable.quad_plus_perf).into(img2);

                //VISIVEL
                nome_edit.setVisibility(View.VISIBLE);
                qtd_edit.setVisibility(View.VISIBLE);
                valor_edit.setVisibility(View.VISIBLE);
                desc_edit.setVisibility(View.VISIBLE);
                bt_cancel.setVisibility(View.VISIBLE);
                bt_apply.setVisibility(View.VISIBLE);
                img1.setClickable(true);
                img2.setClickable(true);
            }
        });
    }

    private void permissionCheck() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            ButtonImage(img1);
                            ButtonImage(img2);
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            ShowSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    private void ShowSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PopUpShopPerfil.this);
        builder.setTitle("Permissões");
        builder.setMessage("Esse aplicativo precisa de certas permissões para ser utilizado.");
        builder.setPositiveButton("Configurações", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(PopUpShopPerfil.this, PerfilActivity.class));
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void ButtonImage(final ImageView image) {
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
        uCrop.start(PopUpShopPerfil.this);
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
                final StorageReference Imagename = mSRef.child("image" + imageUriResultCrop.getLastPathSegment());
                Imagename.putFile(imageUriResultCrop).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Imagename.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Integer result = Integer.valueOf(CreateIdImg());
                                l_img[result] = Objects.requireNonNull(task.getResult()).toString();
                            }
                        });
                        Toast.makeText(PopUpShopPerfil.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        idImgCount++;
                    }
                });
                img.setImageURI(imageUriResultCrop);
            }
        }
    }

    //cadastrar
    private void ButtonAplicar() {
        bt_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (VerifyReCad()) {
                    case 0:
                        ReCadProd();
                        break;
                    case 1:
                        Toast.makeText(PopUpShopPerfil.this, "Preencha o campo nome", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(PopUpShopPerfil.this, "Preencha a quantidade de produto", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(PopUpShopPerfil.this, "Preencha o valor", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(PopUpShopPerfil.this, "Preencha a descrição", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(PopUpShopPerfil.this, "Houve um problema inesperado", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private int VerifyReCad() {
        int constante = 0;
        if (nome_edit == null) {
            constante = 1;
        } else if (qtd_edit == null) {
            constante = 2;
        } else if (valor_edit == null) {
            constante = 3;
        } else if (desc_edit == null) {
            constante = 4;
        }
        return constante;
    }

    private void GetEditToText() {
        l_nome = nome_edit.getText().toString();
        l_qtd = qtd_edit.getText().toString();
        l_valor = valor_edit.getText().toString();
        l_desc = desc_edit.getText().toString();
    }

    private void ReCadProd() {
        GetEditToText();
        Loja loja;
        if (l_img[0] == null) {
            loja = new Loja(l_uid, user_id, l_nome, l_qtd, l_valor, l_desc, l_img_1, l_img_2);
        } else {
            loja = new Loja(l_uid, user_id, l_nome, l_qtd, l_valor, l_desc, l_img[1], l_img[2]);
        }
        Map<String, Object> valuesArr = new HashMap<>();
        valuesArr.put(l_uid, loja.toMap());
        mRef.child("Produto").child(user_id).setValue(valuesArr);
        startActivity(new Intent(PopUpShopPerfil.this, PerfilActivity.class));
    }

    private static String CreateIdImg() {
        return String.valueOf(idImgCount);
    }

    private void ButtonCancel() {
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //VISICEL
                nome_prod.setVisibility(View.VISIBLE);
                qtd_prod.setVisibility(View.VISIBLE);
                valor_prod.setVisibility(View.VISIBLE);
                desc_prod.setVisibility(View.VISIBLE);
                bt_edit.setVisibility(View.VISIBLE);
                bt_exc.setVisibility(View.VISIBLE);

                Picasso.get().load(l_img_1).into(img1);
                Picasso.get().load(l_img_2).into(img2);

                //INVISIVEL
                nome_edit.setVisibility(View.GONE);
                qtd_edit.setVisibility(View.GONE);
                valor_edit.setVisibility(View.GONE);
                desc_edit.setVisibility(View.GONE);
                bt_cancel.setVisibility(View.GONE);
                bt_apply.setVisibility(View.GONE);
                img1.setClickable(false);
                img2.setClickable(false);
            }
        });
    }

    private void ButtonExit() {
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
