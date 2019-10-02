package com.bento.a.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bento.a.Classes.Animal;
import com.bento.a.PopUpMain;
import com.bento.a.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class main_AAdapter extends ArrayAdapter<Animal> {

    public main_AAdapter(@NonNull Context context, int resource, List<Animal> rowItems) {
        super(context, resource, rowItems);
    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, View convertView, final ViewGroup parent) {
        final Animal animal = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        }


        ImageView imagem_dog = convertView.findViewById(R.id.image_adp_dog);
        ImageButton img_but_det = convertView.findViewById(R.id.info_btn_card);
        TextView text_raca_dog = convertView.findViewById(R.id.text_raca_dog);
        TextView text_idade_dog = convertView.findViewById(R.id.text_idade_dog);
        assert animal != null;
        Picasso.get().load(animal.getAn_prof_img1()).into(imagem_dog);
        text_raca_dog.setText(animal.getAn_raca() + ", ");
        text_idade_dog.setText(animal.getAn_idade());


        img_but_det.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), PopUpMain.class)
                        .putExtra("an_uid", animal.getAn_uid())
                        .putExtra("us_uid", animal.getUs_uid())
                        .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
            }
        });
        return convertView;
    }
}
