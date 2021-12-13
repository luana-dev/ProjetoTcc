package com.example.trytouse.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trytouse.R;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.example.trytouse.helper.UsuarioFirebaseInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class FimInsta extends AppCompatActivity {
    private Button btn_clip, btn_menu, btn_insta_open;
    private TextView txt_fim;
    private ClipboardManager myClipboard;
    private ClipData myClip;
    private DatabaseReference mDatabase, dt_senhas;
    private DatabaseReference dtref;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fim_insta);

       Toolbar toolbar = findViewById(R.id.toolbar_insta);
        toolbar.setTitle("Fim Tutorial Instagram");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        btn_clip = findViewById(R.id.btn_clip_insta_fim);
        btn_menu = findViewById(R.id.btn_menu_fim);
        btn_insta_open = findViewById(R.id.btn__abrir_uber);
        txt_fim = findViewById(R.id.text_insta);

        user = UsuarioFirebaseInfo.getIdUsuario();
        mDatabase = ConfiguracaoFirebas.getFirebaseDatabase().child("usuarios");
        dtref = mDatabase.child(user);
        dt_senhas = ConfiguracaoFirebas.getFirebaseDatabase().child("senhas").child("senhas_insta").child(user);

        btn_clip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String text;
                text = txt_fim.getText().toString();

                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);

                Toast.makeText(getApplicationContext(), "Texto Copiado!",Toast.LENGTH_SHORT).show();
            }
        });

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SegundaActivity.class));
            }
        });

        btn_insta_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.instagram.android");
                startActivity(launchIntent);
            }
        });

       dtref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    txt_fim.setText("Nome: " + String.valueOf(snapshot.child("nome").getValue())+
                            "\nEmail: " + String.valueOf(snapshot.child("email").getValue()) + "\nTelefone: " + String.valueOf(snapshot.child("telefone").getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

       dt_senhas.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   txt_fim.append("Senhas: " + String.valueOf(snapshot.child(user).getValue()));
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }
}