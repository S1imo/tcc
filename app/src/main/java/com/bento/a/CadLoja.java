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
import android.widget.ImageButton;
import android.widget.Toast;

import com.bento.a.Classes.Loja;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CadLoja extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private static int idImgCount = 0;
    private ImageButton but_voltar;
    private Button but_cad;
    private String l_nome, l_qtd, l_valor, l_desc, us_uid;
    private String[] l_img = new String[1];
    private CircleImageView img, img1, img2;
    private EditText cad_nome, cad_qtd, cad_valor, cad_desc;

    private StorageReference mSRef;
    private FirebaseDatabase mFire;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cad_loja_layout);
        permissionCheck();
        InpToVar();
        Buttons();
    }

    private void Buttons() {
        ButtonCad();
        ButtonVoltar();
    }

    private void InpToVar() {
        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseDatabase.getInstance();
        mRef = mFire.getReference();
        mSRef = FirebaseStorage.getInstance().getReference();
        us_uid = mAuth.getUid();

        img1 = findViewById(R.id.image_View1);
        img2 = findViewById(R.id.image_View2);

        but_voltar = findViewById(R.id.voltarLButton);
        but_cad = findViewById(R.id.but_cad_prod);

        cad_nome = findViewById(R.id.cad_nome_prod);
        cad_qtd = findViewById(R.id.cad_qtd_prod);
        cad_valor = findViewById(R.id.cad_valor_prod);
        cad_desc = findViewById(R.id.cad_desc_prod);
    }

    private void permissionCheck() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            ImageProd(img1);
                            ImageProd(img2);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(CadLoja.this);
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
                startActivity(new Intent(CadLoja.this, PerfilActivity.class));
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

    private void ImageProd(final CircleImageView image) {
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
                        Toast.makeText(CadLoja.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        idImgCount++;
                    }
                });
                img.setImageURI(imageUriResultCrop);
            }
        }
    }

    private void startCrop(@NonNull Uri uri) {
        String SAMPLE_CROPPED_IMG_NAME = "AnProfPic" + CreateIdImg();
        String destinationFileName = SAMPLE_CROPPED_IMG_NAME + ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        uCrop.withAspectRatio(2, 3);
        uCrop.withMaxResultSize(450, 450);
        uCrop.withOptions(getCropOptions());
        uCrop.start(CadLoja.this);
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

    private static String CreateIdImg() {
        return String.valueOf(idImgCount);
    }

    private void GetEditToString() {
        l_nome = cad_nome.getText().toString();
        l_qtd = cad_qtd.getText().toString();
        l_valor = cad_valor.getText().toString();
        l_desc = cad_desc.getText().toString();
    }

    private int VerifyEdits() {
        GetEditToString();
        if (l_nome == null) {
            return 1;
        } else if (l_qtd == null) {
            return 2;
        } else if (l_valor == null) {
            return 3;
        } else if (l_desc == null) {
            return 4;
        } else if (l_img[0] == null) {
            return 5;
        }
        return 0;
    }

    private void ShowErrors() {
        switch (VerifyEdits()) {
            case 0:
                CadProd();
                break;
            case 1:
                Toast.makeText(this, "Escreva um nome ao produto", Toast.LENGTH_SHORT).show();
                cad_nome.isFocused();
                break;
            case 2:
                Toast.makeText(this, "Escreva a quantidade total de produtos", Toast.LENGTH_SHORT).show();
                cad_qtd.isFocused();
                break;
            case 3:
                Toast.makeText(this, "Escreva o valor do produto (unitário)", Toast.LENGTH_SHORT).show();
                cad_valor.isFocused();
                break;
            case 4:
                Toast.makeText(this, "Escreva uma descrição ao produto", Toast.LENGTH_SHORT).show();
                cad_desc.isFocused();
                break;
            case 5:
                Toast.makeText(this, "Selecione ao menos uma imagem", Toast.LENGTH_SHORT).show();
                img1.isFocused();
                break;
            default:
                Toast.makeText(this, "Houve algum problema inesperado", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void ButtonCad() {
        idImgCount = 0;
        ShowErrors();
        but_cad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetEditToString();
                ShowErrors();
            }
        });
    }

    private void CadProd(){
        String l_id = "P"+System.currentTimeMillis() * 100;
        Loja loja = new Loja(l_id, us_uid, l_nome, l_qtd, l_valor, l_desc, l_img[0], l_img[1]);
        Map<String, Object> valuesArr = new HashMap<>();
        valuesArr.put(l_id, loja.toMap());
        mRef.child("Produto").child(us_uid).setValue(valuesArr);
        startActivity(new Intent(CadLoja.this, PerfilActivity.class));
    }

    private void ButtonVoltar() {
        but_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CadLoja.this, PerfilActivity.class));
            }
        });
    }

}
