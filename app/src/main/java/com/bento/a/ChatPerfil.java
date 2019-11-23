package com.bento.a;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bento.a.Classes.Connections;
import com.bento.a.Classes.Messages;
import com.bento.a.Classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class ChatPerfil extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFire;
    private DatabaseReference mRef;
    private ImageView but_voltar, us_img;
    private Button but_call, but_bloq;
    private TextView us_nome, us_cid, us_numero;
    private String other_us_uid, us_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_perfil_layout);

        InpToVar();
        DisplayInf();
        ButtonCall();
        ButtonBlock();
        ButtonVoltar();
    }

    private void InpToVar() {
        other_us_uid = getIntent().getStringExtra("other_us_uid");

        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseDatabase.getInstance();
        mRef = mFire.getReference();
        us_uid = mAuth.getUid();

        us_nome = findViewById(R.id.nome_chat);
        us_cid = findViewById(R.id.cid_chat);
        us_numero = findViewById(R.id.chat_tel);
        us_img = findViewById(R.id.perfil_chat);

        but_call = findViewById(R.id.call_chat);
        but_bloq = findViewById(R.id.block_chat);
        but_voltar = findViewById(R.id.voltar_chat);
    }

    private void DisplayInf() {
        mRef.child("Users").child(other_us_uid).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                us_nome.setText("Nome: " + user.getUs_nome());
                us_numero.setText("Número: " + user.getUs_tel());
                Picasso.get().load(user.getUs_img()).into(us_img);
                new JsonTask().execute("https://viacep.com.br/ws/" + user.getUs_cep() + "/json/");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ButtonCall() {
        but_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChatPerfil.this);
                alertDialog.setTitle("Ligar para " + us_nome.getText());
                alertDialog
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mRef.child("Users").child(other_us_uid).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);
                                        assert user != null;
                                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+user.getUs_tel())));
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                alertDialog.show();
            }
        });
    }

    private void ButtonBlock() {
        but_bloq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChatPerfil.this);
                alertDialog.setTitle("Tem certeza que deseja bloquear o usuário" + us_nome.getText());
                alertDialog
                        .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mRef.child("BloqueadosList").child(us_uid).child(other_us_uid).setValue(true);
                                mRef.child("BloqueadosList").child(other_us_uid).child(us_uid).setValue(true);
                                mRef.child("ChatList").child(us_uid).child(other_us_uid).removeValue();
                                mRef.child("ChatList").child(other_us_uid).child(us_uid).removeValue();
                                mRef.child("Connections").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            for(DataSnapshot snapshot1: snapshot.getChildren()){
                                                Connections connections = snapshot1.getValue(Connections.class);
                                                assert connections != null;
                                                if(connections.getAn_us_uid().equals(us_uid) && connections.getUs_uid().equals(other_us_uid) || connections.getAn_us_uid().equals(other_us_uid) && connections.getUs_uid().equals(us_uid)){
                                                    mRef.child(Objects.requireNonNull(dataSnapshot.getKey())).child(Objects.requireNonNull(snapshot.getKey())).removeValue();
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                mRef.child("Messages").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Messages messages = snapshot.getValue(Messages.class);
                                            assert messages != null;
                                            if (messages.getUs_sender().equals(us_uid) && messages.getUs_receiver().equals(other_us_uid) || messages.getUs_sender().equals(other_us_uid) && messages.getUs_receiver().equals(us_uid)) {
                                                mRef.child("Messages").child(messages.getMs_id()).removeValue();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                startActivity(new Intent(ChatPerfil.this, ChatActivity.class));
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                alertDialog.show();
            }
        });
    }

    private void ButtonVoltar() {
        but_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatPerfil.this, ChatConversaActivity.class)
                        .putExtra("other_us_uid", other_us_uid));
            }
        });
    }

    private class JsonTask extends AsyncTask<String, String, JSONObject> {
        protected JSONObject doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return new JSONObject(buffer.toString());

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            try {
                us_cid.setText(result.getString("localidade"));
            } catch (JSONException e) {
                us_cid.setText("");
                e.printStackTrace();
            }
        }
    }
}
