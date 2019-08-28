package com.bento.a;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.yuyakaido.android.cardstackview.CardStackView;

import java.util.Objects;

public class Main2Activity extends AppCompatActivity {

    private CardStackView cardStackView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        cardStackView.findViewById(R.id.cardStack);
        recyclerView.findViewById(R.id.recycle);
        cardStackView.setLayoutManager(Objects.requireNonNull(recyclerView.getLayoutManager()));
        cardStackView.swipe();
    }
}
