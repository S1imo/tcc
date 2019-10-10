package com.bento.a.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.Classes.Animal;
import com.bento.a.PopUpPerfilFav;
import com.bento.a.R;
import com.bento.a.ViewHolders.ViewHolderPerfFav;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Perfil_AAdapter extends RecyclerView.Adapter<ViewHolderPerfFav> {

    private Context mContext;
    private List<Animal> mAnimais;

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
    public void onBindViewHolder(@NonNull ViewHolderPerfFav holder, int position) {

        final Animal animal = mAnimais.get(position);
        Picasso.get().load(animal.getAn_prof_img1()).into(holder.an_img);
        holder.an_nome.setText(animal.getAn_raca());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, PopUpPerfilFav.class)
                        .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("other_us_uid", animal.getUs_uid()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAnimais.size();
    }
}
