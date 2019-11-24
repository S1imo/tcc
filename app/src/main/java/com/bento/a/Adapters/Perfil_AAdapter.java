package com.bento.a.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.Classes.Animal;
import com.bento.a.Classes.Connections;
import com.bento.a.PopUpPerfilFav;
import com.bento.a.R;
import com.bento.a.ViewHolders.ViewHolderPerfFav;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Perfil_AAdapter extends RecyclerView.Adapter<ViewHolderPerfFav> {

    private Context mContext;
    private List<Animal> mAnimais;
    private FirebaseAuth mAuth;
    private String user_id;
    private DatabaseReference mRef;
    private FirebaseDatabase mFire;

    public Perfil_AAdapter(Context mContext, List<Animal> mAnimais) {
        this.mContext = mContext;
        this.mAnimais = mAnimais;
    }

    @NonNull
    @Override
    public ViewHolderPerfFav onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.profile_item_fav, parent, false);
        return new ViewHolderPerfFav(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderPerfFav holder, int position) {
        mFire = FirebaseDatabase.getInstance();
        mRef = mFire.getReference();
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getUid();

        final Animal animal = mAnimais.get(position);
        Picasso.get().load(animal.getAn_prof_img1()).into(holder.an_img);
        holder.an_nome.setText(animal.getAn_raca());

        mRef.child("Connections").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for(DataSnapshot snapshot1: snapshot.getChildren()){
                        Connections connections = snapshot1.getValue(Connections.class);
                        assert connections != null;
                        System.out.println(connections.getAn_fav());
                        if (connections.getUs_uid().equals(user_id) && connections.getAn_uid().equals(animal.getAn_uid()) && connections.getAn_fav().equals("verd")) {
                            Picasso.get().load(R.drawable.image_fav).into(holder.an_fav);
                            System.out.println(connections.getAn_uid());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, PopUpPerfilFav.class)
                        .putExtra("other_us_uid", animal.getUs_uid()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAnimais.size();
    }
}
