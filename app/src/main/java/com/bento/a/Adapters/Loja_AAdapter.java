package com.bento.a.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.Classes.Loja;
import com.bento.a.ProdutoActivity;
import com.bento.a.R;
import com.bento.a.ViewHolders.ViewHolderLoja;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Loja_AAdapter extends RecyclerView.Adapter<ViewHolderLoja> {

    private Context mContext;
    private List<Loja> mLojas;

    public Loja_AAdapter(Context mContext, List<Loja> mLojas) {
        this.mContext = mContext;
        this.mLojas = mLojas;
    }

    @NonNull
    @Override
    public ViewHolderLoja onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.loja_item, parent, false);
        return new ViewHolderLoja(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderLoja holder, int position) {
        final Loja loja = mLojas.get(position);
        holder.text_l.setText(loja.getL_nome());
        Picasso.get().load(loja.getL_img_1()).into(holder.img_l);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ProdutoActivity.class)
                        .putExtra("l_id", loja.getL_id())
                        .putExtra("l_us_uid", loja.getL_us_uid()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLojas.size();
    }
}
