package com.bento.a;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.ViewHolders.ViewHolderAnimal;
import com.bento.a.animals.Animal;
import com.bento.a.users.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.concurrent.atomic.AtomicLong;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilActivity extends AppCompatActivity {

    private ImageView but_profile, but_cad_dog, but_logout, but_adot, but_perd, but_loja, but_chat, but_edit_prof;
    private CircleImageView perf_img;
    private TextView nome_text, cidade_text;
    private FirebaseStorage mStorage;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private StorageReference myStoreRef;
    private DatabaseReference myRef, Ref;
    private String user_id;
    private static AtomicLong idCount = new AtomicLong();

    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<Animal> options;
    private FirebaseRecyclerAdapter<Animal, ViewHolderAnimal> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getUid();

        setContentView(R.layout.profile_layout);
        SettingFire();
        InpToVar();
        PerfilTexts();
        Buttons();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        if(adapter != null)
            adapter.stopListening();
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.startListening();
    }

    private void SettingFire() {

        mStorage = FirebaseStorage.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        myStoreRef = mStorage.getReference();
        myRef = mFirebaseDatabase.getReference();

        ProfilePic();
        ProfileAn();
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
        but_cad_dog = findViewById(R.id.add_animal);
        but_edit_prof = findViewById(R.id.but_edit_prof);
        but_logout = findViewById(R.id.but_config);

        nome_text = findViewById(R.id.nome_text);
        cidade_text = findViewById(R.id.cidade_text);

        perf_img = findViewById(R.id.image_perfil);
    }

    private void PerfilTexts() {
        myRef = mFirebaseDatabase.getReference("Users/" + user_id);
        myRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                   nome_text.setText(user.getUs_nome());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(PerfilActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ProfilePic() {
        myStoreRef.child("Users").child(user_id).child("imageUserProf.jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                perf_img.setImageBitmap(Bitmap.createScaledBitmap(bmp, perf_img.getWidth(),
                        perf_img.getHeight(), false));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                perf_img.setImageDrawable(getDrawable(R.drawable.bg_prof_all));
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

                startActivity(new Intent(PerfilActivity.this, PerdidosActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
    }

    private void ButtonLoja()
    {
        but_loja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PerfilActivity.this, LojaActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

    private void ProfileAn()
    {
        assert user_id != null;
        DatabaseReference ref = myRef.child("Animais").child(user_id);

        recyclerView = findViewById(R.id.rvAnimal);
        recyclerView.setHasFixedSize(true);

        options = new FirebaseRecyclerOptions.Builder<Animal>()
                .setQuery(ref, Animal.class).build();

        FirebaseRecyclerAdapter<Animal, ViewHolderAnimal> adapter = new FirebaseRecyclerAdapter<Animal, ViewHolderAnimal>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderAnimal viewHolderAnimal, int i, @NonNull final Animal animal) {
                Picasso.get().load(animal.getAn_fprof_img()).into(viewHolderAnimal.i1, new Callback() {
                    @Override
                    public void onSuccess() {

                    }
                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(PerfilActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                viewHolderAnimal.t1.setText(animal.getAn_raca());

                viewHolderAnimal.i1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(PerfilActivity.this, PopUpPerfil.class));
                    }
                });

            }

            @NonNull
            @Override
            public ViewHolderAnimal onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);

                return new ViewHolderAnimal(view);
            }
        };

        LinearLayoutManager mLinearLManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLinearLManager);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

}
