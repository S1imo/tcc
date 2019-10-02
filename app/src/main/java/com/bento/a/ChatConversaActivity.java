package com.bento.a;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.Classes.Messages;
import com.bento.a.ViewHolders.ViewHolderChatMessages;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ChatConversaActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFire;
    private String user_id, other_us_id, messages_stg, idMessage;
    private DatabaseReference mRef;
    private FloatingActionButton but_enviar;
    private ImageView but_voltar;
    private EditText edit_chat;
    private static int MSG_DIRECTION = 0;

    private FirebaseRecyclerOptions<Messages> options;

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
        DisplayMsg();

    }

    private void InpToVar() {
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
                messages_stg = edit_chat.getText().toString();
                if (messages_stg.equals("") || messages_stg.equals(" ")) {
                    Log.d("MSGN", "mensagem vazia");
                } else {
                    idMessage = "IDChat"+System.currentTimeMillis();
                    Messages messages = new Messages(idMessage, user_id, other_us_id, messages_stg);
                    Map<String, Object> valuesArr = new HashMap<>();
                    valuesArr.put("M" + (int) System.currentTimeMillis(), messages.toMap());
                    mRef.child("Messages").child(user_id).updateChildren(valuesArr);
                    edit_chat.setText("");
                }
            }
        });
    }

    private void DisplayMsg() {
        RecyclerView recyclerViewMessages = findViewById(R.id.recyclerMessages);
        //TODO: criar referencia que verifique se um dos ids é do usuário e outro do other_uid

        options = new FirebaseRecyclerOptions.Builder<Messages>()
                .setQuery(mRef.child("Messages").child(user_id), Messages.class)
                .build();

        FirebaseRecyclerAdapter<Messages, ViewHolderChatMessages> adapter = new FirebaseRecyclerAdapter<Messages, ViewHolderChatMessages>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderChatMessages viewHolderChatMessages, int i, @NonNull Messages messages) {
                if (messages.getUs_uid().equals(user_id)) {
                    //direita - cor branco, texto preto
                    MSG_DIRECTION = 0;
                    viewHolderChatMessages.itemView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    viewHolderChatMessages.text_currentTime.setText(messages.getMessage());
                    viewHolderChatMessages.text_messages.setText(messages.getMessage());
                } else {
                    //esquerda - cor verde, texto preto
                    MSG_DIRECTION = 1;
                    viewHolderChatMessages.itemView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    viewHolderChatMessages.text_currentTime.setText(messages.getMessage());
                    viewHolderChatMessages.text_messages.setText(messages.getMessage());
                }
            }

            @NonNull
            @Override
            public ViewHolderChatMessages onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view;
                if (MSG_DIRECTION == 0) {
                    view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.chat_messages_item_l, parent, false);
                } else {
                    view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.chat_messages_item_r, parent, false);
                }
                return new ViewHolderChatMessages(view);
            }
        };
        recyclerViewMessages.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutM = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutM.setStackFromEnd(true);
        recyclerViewMessages.setLayoutManager(mLinearLayoutM);
        adapter.startListening();
        recyclerViewMessages.setAdapter(adapter);
    }
}
