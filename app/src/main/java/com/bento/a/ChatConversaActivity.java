package com.bento.a;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.Adapters.ChatConv_AAdapter;
import com.bento.a.Classes.Messages;
import com.bento.a.Classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChatConversaActivity extends AppCompatActivity {

    private ChatConv_AAdapter chatConv_aAdapter;
    private ArrayList<Messages> mMessages;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFire;
    private StorageReference folder;
    private RecyclerView recyclerView;
    private String user_id, other_us_id, messages_stg, currentTime, us_lat, us_long;
    private DatabaseReference mRef;
    private TextView other_us_nome;
    private FloatingActionButton but_enviar;
    private ImageButton but_map, but_foto;
    private ImageView but_voltar, other_us_img, but_clip, but_exclude;
    private EditText edit_chat;
    private ConstraintLayout constraintLayout;
    private static final int REQUEST_CODE = 1;

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
        ButtonClip();
        ButtonPerfil();
        but_exclude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonExclude();
            }
        });
        SendMap();
        DisplayUser();
        DisplayMsg();
    }


    private void InpToVar() {

        other_us_img = findViewById(R.id.img_chat);
        other_us_nome = findViewById(R.id.chat_nome);

        but_clip = findViewById(R.id.clip_chat);
        but_exclude = findViewById(R.id.trash_chat);
        but_enviar = findViewById(R.id.enviar_msg);
        but_voltar = findViewById(R.id.voltar_chat);
        edit_chat = findViewById(R.id.text_msg);

        constraintLayout = findViewById(R.id.constraint_chatconv);

    }

    private void ButtonPerfil() {
        other_us_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatConversaActivity.this, ChatPerfil.class)
                        .putExtra("other_us_uid", other_us_id));
            }
        });
    }

    private void ButtonExclude() {
        //alertDialog - perguntando se quer excluir só as mensagens ou da listagem
        final CharSequence[] charSequences = new CharSequence[]{"Excluir da lista do chat"};
        final boolean[] checkItem = new boolean[]{false};
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        mRef.child("Users").child(other_us_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                alertDialog.setTitle("Apagar conversa com " + user.getUs_nome());
                alertDialog.setMultiChoiceItems(charSequences, checkItem, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkItem[0] = isChecked;
                    }
                });
                alertDialog.setPositiveButton("Apagar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        if (!checkItem[0]) {
                            mRef.child("ChatList").child(user_id).child(other_us_id).removeValue();
                            startActivity(new Intent(ChatConversaActivity.this, ChatActivity.class));
                            dialog.dismiss();
                        } else {
                            mRef.child("ChatList").child(user_id).child(other_us_id).removeValue();
                            mRef.child("Messages").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Messages messages = snapshot.getValue(Messages.class);
                                        assert messages != null;
                                        if (messages.getUs_sender().equals(user_id) && messages.getUs_receiver().equals(other_us_id) || messages.getUs_sender().equals(other_us_id) && messages.getUs_receiver().equals(user_id)) {
                                            mRef.child("Messages").child(messages.getMs_id()).removeValue();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            startActivity(new Intent(ChatConversaActivity.this, ChatActivity.class));
                            dialog.dismiss();
                        }
                    }
                })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //só voltar para activity
                                dialog.dismiss();
                            }
                        });
                AlertDialog alerta = alertDialog.create();
                alerta.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void ButtonClip() {
        but_clip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (constraintLayout.getAlpha() != 0) {

                    constraintLayout.animate().translationY(0);
                    constraintLayout.animate().alpha(0).setDuration(2000);


                } else {
                    constraintLayout.animate().translationY(100);
                    constraintLayout.animate().alpha(1).setDuration(600);

                    but_map = findViewById(R.id.imageButton);
                    but_foto = findViewById(R.id.imageButton2);

                    ButtonFoto();
                    ButtonMap();
                }
            }
        });
    }

    private void ButtonFoto() {
        but_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                startCrop(imageUri);
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            folder = FirebaseStorage.getInstance().getReference().child("Messages");
            assert data != null;
            Uri imageUriResultCrop = UCrop.getOutput(data);
            if (imageUriResultCrop != null) {
                final StorageReference Imagename = folder.child(user_id).child("M" + System.currentTimeMillis() + imageUriResultCrop.getLastPathSegment());
                Imagename.putFile(imageUriResultCrop).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ChatConversaActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        Imagename.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                String var = "M" + System.currentTimeMillis();
                                currentTime = Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE);
                                Messages messages = new Messages(user_id, other_us_id, Objects.requireNonNull(task.getResult()).toString(), currentTime, var);
                                Map<String, Object> valuesArr = new HashMap<>();
                                valuesArr.put(var, messages.toMap());
                                mRef.child("Messages").updateChildren(valuesArr);
                                mRef.child("ChatList").child(user_id).child(other_us_id).setValue(true);
                                mRef.child("ChatList").child(other_us_id).child(user_id).setValue(true);
                            }
                        });
                    }
                });
            }
        }
    }

    private void startCrop(@NonNull Uri uri) {
        String SAMPLE_CROPPED_IMG_NAME = "UserProf";
        String destinationFileName = SAMPLE_CROPPED_IMG_NAME + ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        uCrop.withAspectRatio(2, 3);
        uCrop.withMaxResultSize(450, 450);
        uCrop.withOptions(getCropOptions());
        uCrop.start(ChatConversaActivity.this);
    }

    private UCrop.Options getCropOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(70);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
        options.setStatusBarColor(getResources().getColor(R.color.color_branco_menu));
        options.setToolbarColor(getResources().getColor(R.color.color_cinza_menu));
        options.setToolbarTitle("Recortar imagem");
        return options;
    }

    private void ButtonMap() {
        but_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatConversaActivity.this, MapActivityChat.class)
                        .putExtra("other_us_uid", other_us_id));
            }
        });
    }

    private void SendMap() {
        us_lat = getIntent().getStringExtra("latitude");
        us_long = getIntent().getStringExtra("longitude");

        if (us_lat != null && us_long != null) {
            currentTime = Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE);
            String var = "M" + System.currentTimeMillis();
            Messages messages = new Messages(user_id, other_us_id, us_lat + "-" + us_long, currentTime, var);
            Map<String, Object> valuesArr = new HashMap<>();
            valuesArr.put(var, messages.toMap());
            mRef.child("Messages").updateChildren(valuesArr);
            mRef.child("ChatList").child(user_id).child(other_us_id).setValue(true);
            mRef.child("ChatList").child(other_us_id).child(user_id).setValue(true);
        }
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
                currentTime = Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE);
                messages_stg = edit_chat.getText().toString();
                if (messages_stg.equals("") || messages_stg.equals(" ")) {
                    Log.d("MSGN", "mensagem vazia");
                } else {
                    String var = "M" + System.currentTimeMillis();
                    Messages messages = new Messages(user_id, other_us_id, messages_stg, currentTime, var);
                    Map<String, Object> valuesArr = new HashMap<>();
                    valuesArr.put(var, messages.toMap());
                    mRef.child("Messages").updateChildren(valuesArr);
                    mRef.child("ChatList").child(user_id).child(other_us_id).setValue(true);
                    mRef.child("ChatList").child(other_us_id).child(user_id).setValue(true);
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
