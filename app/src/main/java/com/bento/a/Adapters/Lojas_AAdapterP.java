package com.bento.a.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.Classes.Loja;
import com.bento.a.PopUpShopPerfil;
import com.bento.a.R;
import com.bento.a.ViewHolders.ViewHolderLojaP;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Lojas_AAdapterP extends RecyclerView.Adapter<ViewHolderLojaP> {

    private Context mContext;
    private List<Loja> mLojas;

    public Lojas_AAdapterP(Context mContext, List<Loja> mLojas) {
        this.mContext = mContext;
        this.mLojas = mLojas;
    }

    @NonNull
    @Override
    public ViewHolderLojaP onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.profile_item_shop, parent, false);
        return new ViewHolderLojaP(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderLojaP holder, int position) {
        final Loja loja = mLojas.get(position);
        holder.nom_prod.setText(loja.getL_nome());
        Picasso.get().load(loja.getL_img_1()).into(holder.img_prod);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, PopUpShopPerfil.class)
                        .putExtra("l_id", loja.getL_id()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLojas.size();
    }
}
