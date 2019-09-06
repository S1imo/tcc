package com.bento.a.animals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.PerfilActivity;
import com.bento.a.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    PerfilActivity perfilActivity;
    ArrayList<Animal> animals;

    public MyAdapter(PerfilActivity perfilActivity, ArrayList<Animal> animals) {
        this.perfilActivity = perfilActivity;
        this.animals = animals;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(perfilActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.image_item, null, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mImage.setImageDrawable(null);


    }

    @Override
    public int getItemCount() {
        return animals.size();
    }
}
