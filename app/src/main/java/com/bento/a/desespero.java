package com.bento.a;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.ViewHolders.ViewHolderAnimal;
import com.bento.a.animals.Animal;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class desespero extends AppCompatActivity {

    private FirebaseRecyclerOptions<Animal> options;
    private ImageView but;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desespero);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getUid();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        but = findViewById(R.id.sss);

        recyclerView.setHasFixedSize(true);

        assert user_id != null;
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Animais").child(user_id);

        options = new FirebaseRecyclerOptions.Builder<Animal>()
                .setQuery(ref, Animal.class).build();

        FirebaseRecyclerAdapter<Animal, ViewHolderAnimal> adapter = new FirebaseRecyclerAdapter<Animal, ViewHolderAnimal>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderAnimal viewHolderAnimal, int i, @NonNull Animal animal) {

                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/projecttcc-ab189.appspot.com/o/Animais%2Fanimal0%2F267cdpfG9hZdHieDIBn0s4YU1aA2%2F0%2FimageAnProfPic0.jpg?alt=media&token=5df358cc-801a-4b9f-8e2a-661d4448ca85").into(viewHolderAnimal.i1, new Callback() {
                    @Override
                    public void onSuccess() {

                    }
                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(desespero.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                viewHolderAnimal.t1.setText(animal.getAn_raca());
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
