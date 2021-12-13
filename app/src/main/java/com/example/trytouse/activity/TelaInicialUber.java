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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trytouse.R;

public class TelaInicialUber extends AppCompatActivity {

    TextView txt_comeca;
    ImageView img_back_uber, img_home_uber;
    MediaPlayer mediaPlayer1, mediaPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial_uber);
        AlertDialog.Builder builder = new AlertDialog.Builder(TelaInicialUber.this);
        AlertDialog.Builder builder_permissao = new AlertDialog.Builder(TelaInicialUber.this);

        txt_comeca = findViewById(R.id.txt_comeca);
        img_back_uber = findViewById(R.id.img_back);
        img_home_uber = findViewById(R.id.img_home);

        img_back_uber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SegundaActivity.class));
            }
        });

        img_home_uber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SegundaActivity.class));
            }
        });

        txt_comeca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer1.isPlaying() | mediaPlayer2.isPlaying()){
                    mediaPlayer1.stop();
                    mediaPlayer2.stop();
                }
                startActivity(new Intent(getApplicationContext(), LoginUber.class));
            }
        });

        builder.setView(getLayoutInflater().inflate(R.layout.dialog_uber, null));
        builder.setCancelable(true);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                builder_permissao.setView(getLayoutInflater().inflate(R.layout.dialog_permissao, null));
                builder_permissao.setCancelable(true);
                builder_permissao.create().show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        mediaPlayer1 = MediaPlayer.create(getApplicationContext(), R.raw.uber1);
        mediaPlayer2 = MediaPlayer.create(getApplicationContext(), R.raw.uber2);
        if (mediaPlayer1 != null){
            mediaPlayer1.start();
            mediaPlayer1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mediaPlayer2 != null){
                        mediaPlayer2.start();
                    }

                }
            });
        }
    }

}