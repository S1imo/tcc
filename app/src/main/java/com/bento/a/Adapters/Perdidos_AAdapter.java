package com.bento.a.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.Classes.Animal;
import com.bento.a.PopUpPerdidos;
import com.bento.a.R;
import com.bento.a.ViewHolders.ViewHolderPerdidos;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Perdidos_AAdapter extends RecyclerView.Adapter<ViewHolderPerdidos> {

    Context mContext;
    List<Animal> animalList;

    public Perdidos_AAdapter(Context mContext, List<Animal> animalList) {
        this.mContext = mContext;
        this.animalList = animalList;
    }

    @NonNull
    @Override
    public ViewHolderPerdidos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.perdidos_item, parent, false);
        return new ViewHolderPerdidos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPerdidos holder, int position) {
        final Animal animal = animalList.get(position);
        holder.text_nome.setText(animal.getAn_raca());
        holder.text_idade.setText(animal.getAn_idade());
        Picasso.get().load(animal.getAn_prof_img1()).into(holder.an_img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, PopUpPerdidos.class)
                        .putExtra("other_us_id", animal.getUs_uid())
                        .putExtra("an_id", animal.getAn_uid()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return animalList.size();
    }
}
