package com.bento.a;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class CadAnimal extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int REQUEST_CODE = 1;
    private ImageButton but_voltar;
    private EditText editTextRaca, editTextDesc;
    private Button buttonAplicar;
    private RadioGroup inp_sel_port, inp_sel_vac, inp_sel_stat, inp_sel_idade, inp_sel_cast;
    private RadioButton but_rad_port, but_rad_vac, but_rad_stat, but_rad_idade, but_rad_cast;
    private Spinner inp_tip_animal;
    private CircleImageView inp_img1, inp_img2, inp_img3, inp_img4, img;
    private String an_idade, tip_animal, an_port, an_vac, an_stat, an_desc, an_raca, an_cast;
    private String[] an_prof_img = new String[5];
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private StorageReference folder;
    private static int idImgCount = 0;
    private static boolean isUploaded = false;
    private String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid(), currentIdAn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cad_dog_layout);
        folder = FirebaseStorage.getInstance().getReference();

        if (ContextCompat.checkSelfPermission(CadAnimal.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            PermissionsMaps();
        }

        currentIdAn = CreateIdAn();

        InpToVar();
        permissionCheck();
        Buttons();
    }

    private void Buttons() {
        setTipAnimal();
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
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
                img = image;
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
                            ImagePerfUp(inp_img1);
                            ImagePerfUp(inp_img2);
                            ImagePerfUp(inp_img3);
                            ImagePerfUp(inp_img4);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(CadAnimal.this);
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
                startActivity(new Intent(CadAnimal.this, PerfilActivity.class));
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

    private void InpToVar() {
        folder = folder.child("Animais").child(currentIdAn).child(user_id);

        inp_img1 = findViewById(R.id.addImage);
        inp_img2 = findViewById(R.id.addImage2);
        inp_img3 = findViewById(R.id.addImage3);
        inp_img4 = findViewById(R.id.addImage4);
        inp_img1.setImageDrawable(getDrawable(R.drawable.add_an_prof));
        inp_img2.setImageDrawable(getDrawable(R.drawable.add_an_prof));
        inp_img3.setImageDrawable(getDrawable(R.drawable.add_an_prof));
        inp_img4.setImageDrawable(getDrawable(R.drawable.add_an_prof));

        editTextDesc = findViewById(R.id.cad_descricao);
        editTextRaca = findViewById(R.id.cad_raca);

        inp_tip_animal = findViewById(R.id.cad_animal);

        inp_sel_port = findViewById(R.id.radio_port);
        inp_sel_vac = findViewById(R.id.radio_vac);
        inp_sel_stat = findViewById(R.id.radio_stat);
        inp_sel_idade = findViewById(R.id.radio_idade);
        inp_sel_cast = findViewById(R.id.radio_castrado);

        buttonAplicar = findViewById(R.id.button_aplicar);
        but_voltar = findViewById(R.id.voltarDButton);
    }


    private void ButtonAplicar() {
        buttonAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetRadioText();
                RadioTxtToStg();
                if (VerificaCad() == 1) {
                    Toast.makeText(CadAnimal.this, "Selecione todas as opções", Toast.LENGTH_SHORT).show();
                } else if (VerificaCad() == 2) {
                    Toast.makeText(CadAnimal.this, "Selecione ao menos uma imagem", Toast.LENGTH_SHORT).show();
                } else {
                    CreateAn();
                }
            }
        });
    }

    private void PermissionsMaps() {
        Dexter.withActivity(CadAnimal.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Log.d("PMS", "Permissão habilitada");
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CadAnimal.this);
                        builder.setTitle("Permissão negada")
                                .setMessage("Sua permissão foi permanentemente negada. Caso queira ativa-la, verifique estas nas configurações.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                                        intent.setData(Uri.fromParts("package", getPackageName(), null));
                                        startActivity(intent);
                                    }
                                })
                                .show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
    }

    //Colocando dados FB - Storage
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
                        Toast.makeText(CadAnimal.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        idImgCount++;
                        isUploaded = true;
                    }
                });
                img.setImageURI(imageUriResultCrop);
            }
        }
    }

    private int VerificaCad() {
        if (an_desc.isEmpty() || an_port.isEmpty() || an_raca.isEmpty() || an_stat.isEmpty() || an_vac.isEmpty() || inp_sel_port.getCheckedRadioButtonId() == -1 || inp_sel_idade.getCheckedRadioButtonId() == -1 || inp_sel_stat.getCheckedRadioButtonId() == -1 || inp_sel_vac.getCheckedRadioButtonId() == -1) {
            return 1;
        } else if (!isUploaded) {
            return 2;
        } else {
            return 0;
        }
    }

    private void SetRadioText() {
        int selectedid_port = inp_sel_port.getCheckedRadioButtonId();
        int selectedid_vac = inp_sel_vac.getCheckedRadioButtonId();
        int selectedid_stat = inp_sel_stat.getCheckedRadioButtonId();
        int selectedid_idade = inp_sel_idade.getCheckedRadioButtonId();
        int selectedid_cast = inp_sel_cast.getCheckedRadioButtonId();
        but_rad_port = findViewById(selectedid_port);
        but_rad_vac = findViewById(selectedid_vac);
        but_rad_stat = findViewById(selectedid_stat);
        but_rad_idade = findViewById(selectedid_idade);
        but_rad_cast = findViewById(selectedid_cast);
    }

    private void RadioTxtToStg() {
        an_idade = but_rad_idade.getText().toString().trim();
        an_port = but_rad_port.getText().toString().trim();
        an_vac = but_rad_vac.getText().toString().trim();
        an_stat = but_rad_stat.getText().toString();
        an_cast = but_rad_cast.getText().toString();
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
        this.tip_animal = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    //Colocando dados FB - DB
    private void CreateAn() {
        idImgCount = 0;
        startActivity(new Intent(CadAnimal.this, MapActivityAn.class)
                .putExtra("an_id", currentIdAn)
                .putExtra("user_id", user_id)
                .putExtra("currentIdAn", currentIdAn)
                .putExtra("tip_animal", tip_animal)
                .putExtra("an_cast", an_cast)
                .putExtra("an_idade", an_idade)
                .putExtra("an_port", an_port)
                .putExtra("an_vac", an_vac)
                .putExtra("an_raca", an_raca)
                .putExtra("an_stat", an_stat)
                .putExtra("an_desc", an_desc)
                .putExtra("an_prof_img", new String[]{an_prof_img[0], an_prof_img[1], an_prof_img[2], an_prof_img[3], an_prof_img[4]}));
    }

    private String CreateIdAn() {
        return String.valueOf(System.currentTimeMillis() * 100);
    }

    private static String CreateIdImg() {
        return String.valueOf(idImgCount);
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
