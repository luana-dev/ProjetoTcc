package com.example.trytouse.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

public class FimWhats extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private DatabaseReference dtref;
    private String user;
    private TextView txt_fim;
    private ClipboardManager myClipboard;
    private ClipData myClip;
    private Button btn_menu,btn_clip,btn_open;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fim_whats);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        toolbar.setTitle("Fim Tutorial WhatsApp");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        txt_fim = findViewById(R.id.txt_fim_whats);
        user = UsuarioFirebaseInfo.getIdUsuario();
        mDatabase = ConfiguracaoFirebas.getFirebaseDatabase().child("usuarios");
        dtref = mDatabase.child(user);
        btn_menu = findViewById(R.id.btn_menu);
        btn_clip = findViewById(R.id.btn_clip);
        btn_open = findViewById(R.id.btn_abrir_whats);

        btn_clip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String text;
                text = txt_fim.getText().toString();

                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);

                Toast.makeText(getApplicationContext(), "Text Copiado!",Toast.LENGTH_SHORT).show();
            }
        });

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SegundaActivity.class));
            }
        });

        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
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
    }
}