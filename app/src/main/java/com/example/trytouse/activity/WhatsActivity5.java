package com.example.trytouse.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.trytouse.R;

public class WhatsActivity5 extends AppCompatActivity {
        TextView txtwhats5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats5);

        txtwhats5 = findViewById(R.id.txtwhats5);

        txtwhats5.setText("Na tela de conversas você consegue mandar mensagem de texto");

    }

    public void proximatela(View view){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }

    public void anterior(View view){
       // startActivity(new Intent(getApplicationContext(), WhatsActivity4_2.class));
    }
}