package com.example.trytouse.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.trytouse.InstaActivity2;
import com.example.trytouse.R;

public class InstaActivity extends AppCompatActivity {
    Button btn_prox, btn_anterior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta);

        btn_prox = findViewById(R.id.btn_prox);
        btn_anterior = findViewById(R.id.btn_ante);

        btn_prox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), InstaActivity2.class));
            }
        });

        btn_anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SegundaActivity.class));
            }
        });

    }

}