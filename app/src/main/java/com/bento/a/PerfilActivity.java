package com.bento.a;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bento.a.users.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilActivity extends AppCompatActivity {

    private ImageView but_profile,but_cad_dog, but_logout, but_adot, but_perd, but_loja, but_chat, but_edit_prof;
    private CircleImageView perf_img;
    private TextView nome_text, cidade_text;
    private FirebaseDatabase mFirebaseDatabase;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        SettingFire();
        InpToVar();
        PerfilTexts();
        Buttons();
    }

    private void SettingFire()
    {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        user_id = user.getUid();
        myRef = mFirebaseDatabase.getReference("Users/"+user_id);
        storageReference = FirebaseStorage.getInstance().getReference().child("Users").child(user_id);
        ProfilePic();
    }

    private void ProfilePic() {
        storageReference.child("imageUserProf.jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                perf_img.setImageBitmap(Bitmap.createScaledBitmap(bmp, perf_img.getWidth(),
                        perf_img.getHeight(), false));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(PerfilActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                perf_img.setImageDrawable(getDrawable(R.drawable.profile_icon));
            }
        });
    }

    private void Buttons()
    {
        //botões superiores - menu
        ButtonPerfil();
        ButtonAdote();
        ButtonPerdidos();
        ButtonLoja();
        ButtonChat();

        ButtonEdit();
        ButtonCad();
        ButtoLogOut();
    }

    private void InpToVar()
    {
        but_profile = findViewById(R.id.profile_icon);
        but_adot = findViewById(R.id.adot_icon);
        but_perd = findViewById(R.id.perdido_icon);
        but_loja = findViewById(R.id.shop_icon);
        but_chat = findViewById(R.id.chat_icon);
        but_edit_prof = findViewById(R.id.but_edit_prof);
        but_logout = findViewById(R.id.but_config);

        nome_text = findViewById(R.id.nome_text);
        cidade_text = findViewById(R.id.cidade_text);

        perf_img = findViewById(R.id.image_perfil);
        but_cad_dog = findViewById(R.id.imageView_addPet);

    }

    private void PerfilTexts() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                nome_text.setText(user.getUs_nome());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(PerfilActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //menu
    private void ButtonPerfil()
    {
        but_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void ButtonAdote()
    {
        but_adot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PerfilActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void ButtonPerdidos()
    {
        but_perd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(PerfilActivity.this, PerdidosActivity.class));
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
    }

    private void ButtonLoja()
    {
        but_loja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(PerfilActivity.this, LojaActivity.class));
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void ButtonChat()
    {
        but_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(PerfilActivity.this, ChatActivity.class));
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void ButtonEdit()
    {
        but_edit_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUser();
            }
        });
    }

    private void ButtoLogOut()
    {
        but_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.signUp();
                startActivity(new Intent(PerfilActivity.this, LoginActivity.class));
            }
        });
    }

    private void editUser()
    {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                if(user.getUs_tip_usu().equals("Organização"))
                {
                    startActivity(new Intent(PerfilActivity.this, EditPerfEActivity.class));
                }
                else if(user.getUs_tip_usu().equals("Usuário"))
                {
                    startActivity(new Intent(PerfilActivity.this, EditPerfUActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(PerfilActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void ButtonCad(){
        but_cad_dog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PerfilActivity.this, CadAnimal.class));

            }
        });

    }
}
