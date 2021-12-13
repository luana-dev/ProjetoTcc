package com.example.trytouse.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
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

public class FimNove extends AppCompatActivity {
    private Button btn_abrir_nove, btn_retorna_nove, btn_clip_nove;
    private TextView txt_nove;
    private ClipboardManager myClipboard;
    private ClipData myClip;
    private DatabaseReference mDatabase, dtref;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fim_nove);

        btn_abrir_nove = findViewById(R.id.btn__abrir_nove);
        btn_retorna_nove = findViewById(R.id.btn_menu_fim_nove);
        btn_clip_nove = findViewById(R.id.btn_clip_nove_fim);
        txt_nove = findViewById(R.id.txt_nove_fim);

        user = UsuarioFirebaseInfo.getIdUsuario();
        mDatabase = ConfiguracaoFirebas.getFirebaseDatabase().child("usuarios");
        dtref = mDatabase.child(user);

        Toolbar toolbar = findViewById(R.id.toolbar99);
        toolbar.setTitle("Fim tutorial aplicativo 99");
        setSupportActionBar(toolbar);

        btn_abrir_nove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.taxis99");
                startActivity(launchIntent);
            }
        });

        btn_retorna_nove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SegundaActivity.class));
            }
        });

        btn_clip_nove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String text;
                text = txt_nove.getText().toString();

                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);

                Toast.makeText(getApplicationContext(), "Texto Copiado!",Toast.LENGTH_SHORT).show();
            }
        });

        dtref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    txt_nove.setText("Nome: " + String.valueOf(snapshot.child("nome").getValue())+
                            "\nEmail: " + String.valueOf(snapshot.child("email").getValue()) + "\nTelefone: " + String.valueOf(snapshot.child("telefone").getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}