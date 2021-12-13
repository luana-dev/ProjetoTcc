package com.example.trytouse.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.trytouse.R;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.example.trytouse.helper.Permissao;
import com.example.trytouse.helper.UsuarioFirebaseInfo;
import com.example.trytouse.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginUber extends AppCompatActivity {
    FloatingActionButton btn_avanca;
    private EditText tel_uber;
    private String user;
    private DatabaseReference dt;
    private DatabaseReference dtref;
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private ImageView img_back_uber2, img_home_uber2;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_uber);
        AlertDialog.Builder builder_login = new AlertDialog.Builder(this);

        builder_login.setView(getLayoutInflater().inflate(R.layout.dialog_login_uber, null));
        builder_login.setCancelable(true);
        builder_login.create().show();

        Permissao.validaPermissoes(permissoes, this, 1);

        btn_avanca = findViewById(R.id.fa_btn_avanca);
        tel_uber = findViewById(R.id.editTextPhoneUber);
        user = UsuarioFirebaseInfo.getIdUsuario();
        dt = ConfiguracaoFirebas.getFirebaseDatabase().child("usuarios");
        dtref = dt.child(user).child("telefone");
        img_back_uber2 = findViewById(R.id.img_back);
        img_home_uber2 = findViewById(R.id.img_home);

        img_back_uber2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                startActivity(new Intent(getApplicationContext(), TelaInicialUber.class));
            }
        });

        img_home_uber2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                startActivity(new Intent(getApplicationContext(), SegundaActivity.class));
            }
        });

        dtref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    tel_uber.setText(String.valueOf(snapshot.getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", error.getMessage());
            }
        });

        btn_avanca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                startActivity(new Intent(getApplicationContext(), PassageiroActivity.class));
            }
        });

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.uber3);
        if (mediaPlayer != null){
            mediaPlayer.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissaoResultado : grantResults){
            if( permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }

    }

    private void alertaValidacaoPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void recuperar(){

    }
}