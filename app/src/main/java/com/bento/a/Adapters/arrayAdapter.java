package com.bento.a.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bento.a.MainActivity;
import com.bento.a.R;
import com.bento.a.Classes.Animal;
import com.squareup.picasso.Picasso;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<Animal> {

    private MainActivity mainActivity;

    public arrayAdapter(@NonNull Context context, int resource, List<Animal> rowItems)
    {
        super(context, resource, rowItems);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        Animal animal = getItem(position);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        }

        ImageView imagem_dog = convertView.findViewById(R.id.image_adp_dog);

        assert animal != null;
        Picasso.get().load(animal.getAn_prof_img1()).into(imagem_dog);

        return convertView;
    }
}
