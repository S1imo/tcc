package com.bento.a;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.Adapters.ChatConv_AAdapter;
import com.bento.a.Classes.Messages;
import com.bento.a.Classes.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ChatConversaActivity extends AppCompatActivity {

    private ChatConv_AAdapter chatConv_aAdapter;
    private ArrayList<Messages> mMessages;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFire;
    private RecyclerView recyclerView;
    private String user_id, other_us_id, messages_stg, currentTime;
    private DatabaseReference mRef;
    private TextView other_us_nome;
    private FloatingActionButton but_enviar;
    private ImageView but_voltar, other_us_img;
    private EditText edit_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_conversa);

        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseDatabase.getInstance();
        mRef = mFire.getReference();
        user_id = mAuth.getUid();
        other_us_id = getIntent().getStringExtra("other_us_uid");

        InpToVar();
        ButtonVoltar();
        ButtonEnviarMsgm();
        DisplayUser();
        DisplayMsg();
    }

    private void InpToVar() {

        other_us_img = findViewById(R.id.img_chat);
        other_us_nome = findViewById(R.id.chat_nome);

        but_enviar = findViewById(R.id.enviar_msg);
        but_voltar = findViewById(R.id.voltar_chat);
        edit_chat = findViewById(R.id.text_msg);
    }

    private void ButtonVoltar() {
        but_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatConversaActivity.this, ChatActivity.class));
            }
        });
    }

    private void ButtonEnviarMsgm() {
        but_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar rightNow = Calendar.getInstance();
                currentTime = "" + rightNow.get(Calendar.HOUR_OF_DAY);
                messages_stg = edit_chat.getText().toString();
                if (messages_stg.equals("") || messages_stg.equals(" ")) {
                    Log.d("MSGN", "mensagem vazia");
                } else {
                    Messages messages = new Messages(user_id, other_us_id, messages_stg, currentTime);
                    Map<String, Object> valuesArr = new HashMap<>();
                    String var = "M" + System.currentTimeMillis();
                    valuesArr.put(var, messages.toMap());
                    mRef.child("Messages").updateChildren(valuesArr);
                    edit_chat.setText("");
                }
            }
        });
    }

    private void DisplayMsg() {
        mMessages = new ArrayList<>();
        recyclerView = findViewById(R.id.rvChatConv);
        chatConv_aAdapter = new ChatConv_AAdapter(getApplicationContext(), mMessages);
        recyclerView.setHasFixedSize(true);

        mRef.child("Messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.hasChildren()) {
                    Messages messages = dataSnapshot.getValue(Messages.class);
                    assert messages != null;
                    if (messages.getUs_sender().equals(user_id) && messages.getUs_receiver().equals(other_us_id) || messages.getUs_sender().equals(other_us_id) && messages.getUs_receiver().equals(user_id)) {
                        mMessages.add(messages);
                        chatConv_aAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatConv_aAdapter);
        recyclerView.scrollToPosition(chatConv_aAdapter.getItemCount());
    }

    private void DisplayUser() {
        mRef.child("Users").child(other_us_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                other_us_nome.setText(user.getUs_nome());
                Picasso.get().load(user.getUs_img()).into(other_us_img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
