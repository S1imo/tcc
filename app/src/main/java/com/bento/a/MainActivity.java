package com.bento.a;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bento.a.Adapters.arrayAdapter;
import com.bento.a.Classes.Animal;
import com.bento.a.Classes.Connections;
import com.bento.a.Classes.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private arrayAdapter arr_Adapter;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFire;
    private DatabaseReference mRef, mRefAnimal;
    private String user_id, an_uid;
    private ImageView but_profile, but_adot, but_perd, but_loja, but_chat, imagelike;
    private FloatingActionButton buttonDes, buttonLike;
    private List<Animal> rowItems;
    private SwipeFlingAdapterView flingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseDatabase.getInstance();
        user_id = mAuth.getUid();

        InpToVar(); //input para variaveis
        SwipeCard(); //função do card
        Buttons(); //função dos botoes
    }

    private void Buttons() {
        //botões superiores - menu
        ButtonPerfil();
        ButtonAdote();
        ButtonPerdidos();
        ButtonLoja();
        ButtonChat();

        //botões inferiores
        ButtonDes();
        ButtonLike();
    }

    private void InpToVar() {
        buttonDes = findViewById(R.id.deslike_btn);
        buttonLike = findViewById(R.id.like_btn);
        but_profile = findViewById(R.id.profile_icon);
        but_adot = findViewById(R.id.adot_icon);
        but_perd = findViewById(R.id.perdido_icon);
        but_loja = findViewById(R.id.shop_icon);
        but_chat = findViewById(R.id.chat_icon);

        flingContainer = findViewById(R.id.frame);
        imagelike = findViewById(R.id.imagelike);

    }


    private void SwipeCard() {
        rowItems = new ArrayList<>();
        mRefAnimal = mFire.getReference().child("Animais").child(user_id);
        rowItems = new ArrayList<>();
        mRefAnimal = mFire.getReference().child("Animais");

        mRefAnimal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                for (final DataSnapshot value : dataSnapshot.getChildren()) {
                    for (DataSnapshot value_in : value.getChildren()) {
                        final Animal animal = value_in.getValue(Animal.class);
                        assert animal != null;
                        if (!animal.getUs_uid().equals(user_id)) {
                            mRef = mFire.getReference().child("Connections").child(animal.getAn_uid());
                            mRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChildren()) {
                                        for (DataSnapshot value_con : dataSnapshot.getChildren()) {
                                            Connections connections = value_con.getValue(Connections.class);
                                            assert connections != null;
                                            if (!connections.getUs_uid().contains(user_id)) {
                                                //TODO: ele só pega o primeira da lista e compara com os outros -- erro
                                                rowItems.add(animal);
                                                arr_Adapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                    else
                                    {
                                        rowItems.add(animal);
                                        arr_Adapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        arr_Adapter = new arrayAdapter(this, R.layout.main_item, rowItems);
        flingContainer.setAdapter(arr_Adapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arr_Adapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(final Object dataObject) {
                an_uid = ((Animal) dataObject).getAn_uid();
                DatabaseReference refNew = mFire.getReference();
                int num_chat = (int) System.currentTimeMillis();
                //o uid do usuario conectado
                refNew.child("Connections").child(an_uid).child("No" + num_chat).child("us_uid").setValue(user_id);
                //uid do animal
                refNew.child("Connections").child(an_uid).child("No" + num_chat).child("an_uid").setValue(((Animal) dataObject).getAn_uid());
                //uid da pessoa que colocou o cachorro para adoção
                refNew.child("Connections").child(an_uid).child("No" + num_chat).child("an_us_uid").setValue(((Animal) dataObject).getUs_uid());
            }

            @Override
            public void onRightCardExit(final Object dataObject) {
                an_uid = ((Animal) dataObject).getAn_uid();
                DatabaseReference refNew = mFire.getReference();
                int bbb = (int) System.currentTimeMillis();
                refNew.child("Connections").child(an_uid).child("Yes" + bbb).child("us_uid").setValue(user_id);
                refNew.child("Connections").child(an_uid).child("Yes" + bbb).child("an_uid").setValue(((Animal) dataObject).getAn_uid());
                refNew.child("Connections").child(an_uid).child("Yes" + bbb).child("an_us_uid").setValue(((Animal) dataObject).getUs_uid());
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                Log.d("LIST", "Empty");
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });
    }

    private void ButtonPerfil() {
        but_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, PerfilActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
    }

    private void ButtonAdote() {
        but_adot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void ButtonPerdidos() {
        but_perd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.signUp();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                /*startActivity(new Intent(MainActivity.this, PerdidosActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);*/
            }
        });
    }

    private void ButtonLoja() {
        but_loja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, desespero.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void ButtonChat() {
        but_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    //inferior
    private void ButtonDes() {
        buttonDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animarFab(buttonDes);
                flingContainer.getTopCardListener().selectLeft();
                Toast.makeText(MainActivity.this, "Deslike", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ButtonLike() {

        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animarFab(buttonLike);
                flingContainer.getTopCardListener().selectRight();
                Toast.makeText(MainActivity.this, "Like", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void animarFab(final FloatingActionButton fab) {
        fab.animate().scaleX(1.0f).scaleY(1.0f).setDuration(500).withEndAction(new Runnable() {
            @Override
            public void run() {

            }
        });
    }


}