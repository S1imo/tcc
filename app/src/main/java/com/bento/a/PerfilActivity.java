package com.bento.a;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import com.bento.a.Adapters.Perfil_AAdapter;
import com.bento.a.Classes.Animal;
import com.bento.a.Classes.Connections;
import com.bento.a.Classes.User;
import com.bento.a.ViewHolders.ViewHolderAnimal;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilActivity extends AppCompatActivity {

    private ImageView but_profile, but_cad_dog, but_logout, but_adot, but_perd, but_loja, but_chat, but_edit_prof;
    private CircleImageView perf_img;
    private TextView nome_text, cidade_text;
    private FirebaseStorage mStorage;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private StorageReference myStoreRef;
    private DatabaseReference myRef;
    private String user_id;

    private RecyclerView recyclerViewMy, recyclerViewFav;
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

    private void SettingFire() {

        mStorage = FirebaseStorage.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        myStoreRef = mStorage.getReference();
        myRef = mFirebaseDatabase.getReference();

        ProfilePic();
        RecyclerMyAn();
        RecyclerFav();
    }

    private void Buttons() {
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

    private void InpToVar() {
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
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                nome_text.setText(user.getUs_nome());
                new JsonTask().execute("https://viacep.com.br/ws/"+user.getUs_cep()+"/json/");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(PerfilActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ProfilePic() {
        myRef.child("Users").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                Picasso.get().load(user.getUs_img()).into(perf_img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Picasso.get().load(R.drawable.bg_prof_all).into(perf_img);
            }
        });
    }

    //menu
    private void ButtonPerfil() {
        but_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void ButtonAdote() {
        but_adot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PerfilActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void ButtonPerdidos() {
        but_perd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PerfilActivity.this, PerdidosActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
    }

    private void ButtonLoja() {
        but_loja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PerfilActivity.this, LojaActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void ButtonChat() {
        but_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PerfilActivity.this, ChatActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void ButtonEdit() {
        but_edit_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUser();
            }
        });
    }

    private void ButtoLogOut() {
        but_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.signUp();
                startActivity(new Intent(PerfilActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
            }
        });
    }

    private void editUser() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                if (user.getUs_tip_usu().equals("Organização")) {
                    startActivity(new Intent(PerfilActivity.this, EditPerfEActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                } else if (user.getUs_tip_usu().equals("Usuário")) {
                    startActivity(new Intent(PerfilActivity.this, EditPerfUActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(PerfilActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ButtonCad() {
        but_cad_dog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PerfilActivity.this, CadAnimal.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
            }
        });
    }

    private void RecyclerMyAn() {
        assert user_id != null;
        final DatabaseReference ref = myRef.child("Animais").child(user_id);
        recyclerViewMy = findViewById(R.id.rvAnimal);
        recyclerViewMy.setHasFixedSize(true);

        options = new FirebaseRecyclerOptions.Builder<Animal>()
                .setQuery(ref, Animal.class).build();

        FirebaseRecyclerAdapter<Animal, ViewHolderAnimal> adapter = new FirebaseRecyclerAdapter<Animal, ViewHolderAnimal>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderAnimal viewHolderAnimal, int i, @NonNull final Animal animal) {
                viewHolderAnimal.t1.setText(animal.getAn_raca());

                if (animal.getAn_status().equals("Perdido")) {
                    Picasso.get().load(R.drawable.interrogacao_icon).into(viewHolderAnimal.i2);
                    viewHolderAnimal.i2.setVisibility(View.VISIBLE);
                } else if (!animal.getAn_status().equals("Perdido")) {
                    viewHolderAnimal.i2.setVisibility(View.INVISIBLE);
                }

                Picasso.get().load(animal.getAn_prof_img1()).into(viewHolderAnimal.i1);

                viewHolderAnimal.i1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String an_uid1 = animal.getAn_uid();
                        startActivity(new Intent(PerfilActivity.this, PopUpPerfilMy.class)
                                .putExtra("an_uid", an_uid1)
                                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    }
                });
            }

            @NonNull
            @Override
            public ViewHolderAnimal onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item_my, parent, false);

                return new ViewHolderAnimal(view);
            }
        };

        LinearLayoutManager mLinearLManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMy.setLayoutManager(mLinearLManager);
        adapter.startListening();
        recyclerViewMy.setAdapter(adapter);
    }

    private void RecyclerFav() {
        final ArrayList<Animal> mAnimais = new ArrayList<>();
        final Perfil_AAdapter perfil_AAdapter = new Perfil_AAdapter(getApplicationContext(), mAnimais);

        recyclerViewFav = findViewById(R.id.rvInteressados);
        recyclerViewFav.setHasFixedSize(true);

        myRef.child("Connections").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (final DataSnapshot snapshot1 : snapshot.getChildren()) {
                        final Connections connections = snapshot1.getValue(Connections.class);
                        DatabaseReference reference = mFirebaseDatabase.getReference();
                        if (snapshot1.hasChildren() && connections != null) {
                            reference.child("Animais").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                    for (DataSnapshot snapshot2 : dataSnapshot1.getChildren()) {
                                        if (Objects.equals(snapshot2.getKey(), connections.getAn_us_uid())) {
                                            for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                                Animal animal = snapshot3.getValue(Animal.class);
                                                assert animal != null;
                                                if (connections.getAn_uid().equals(animal.getAn_uid()) && !connections.getAn_us_uid().equals(user_id) && connections.getUs_uid().equals(user_id) && !Objects.requireNonNull(snapshot1.getKey()).contains("No")) {
                                                    mAnimais.add(animal);
                                                    perfil_AAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError1) {
                                }
                            });

                        } else {
                            Toast.makeText(PerfilActivity.this, "aaaa", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recyclerViewFav.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        recyclerViewFav.setAdapter(perfil_AAdapter);
    }

    private class JsonTask extends AsyncTask<String, String, JSONObject> {
        protected JSONObject doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return new JSONObject(buffer.toString());

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            try {
                cidade_text.setText(result.getString("localidade"));
            } catch (JSONException e) {
                e.printStackTrace();
                cidade_text.setText("");
            }
        }
    }
}
