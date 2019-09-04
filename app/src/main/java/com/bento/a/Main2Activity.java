package com.bento.a;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Adapter;
import android.widget.AdapterView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

public class Main2Activity extends AppCompatActivity implements CardStackListener {

    private CardStackView cardStackView;
    private DrawerLayout drawerLayout;
    private CardStackLayoutManager manager;
    private FloatingActionButton deslike;
    private SwipeAnimationSetting.Builder setting;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        InpToVar();
        setupCardStackView();
        setupButton();

    }

    private void InpToVar() {
        manager = new CardStackLayoutManager(this,this);
        cardStackView = findViewById(R.id.card_stack_view);
        drawerLayout = findViewById(R.id.drawer_layout);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        }else {
            super.onBackPressed();
        }
    }

    private void setupCardStackView(){
        initialize();
    }

    private void setupButton(){
        deslike = findViewById(R.id.skip_button);
        deslike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting = new SwipeAnimationSetting.Builder();

            }
        });
    }
    private void initialize(){
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(true);
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        cardStackView.setLayoutManager(manager);

    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {
        Log.d("CardStackView","onCardDragging: d = ${direction.name},r = $ration");
    }

    @Override
    public void onCardSwiped(Direction direction) {
        Log.d("CardStackView", "onCardSwiped: p = ${manager.topPosition}, d = $direction");
        if (manager.getTopPosition()== adapter.getCount() - 5){
            paginate();
        }
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }

    private void paginate(){
    }
}
