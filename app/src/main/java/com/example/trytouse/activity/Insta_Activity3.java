package com.example.trytouse.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.trytouse.InstaActivity2;
import com.example.trytouse.R;
import com.example.trytouse.TelaInicialInsta;

public class Insta_Activity3 extends AppCompatActivity {
        Button btn_criar_conta;
        ImageView back5, home5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta_3);

        back5 = findViewById(R.id.img_back);
        home5 = findViewById(R.id.img_home);

        back5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), InstaActivity2.class));
            }
        });

        home5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SegundaActivity.class));
            }
        });

        AlertDialog.Builder builder_new_insta = new AlertDialog.Builder(this);

        builder_new_insta.setView(getLayoutInflater().inflate(R.layout.dialog_insta_new, null));
        builder_new_insta.setCancelable(true);
        builder_new_insta.create().show();

        btn_criar_conta = findViewById(R.id.btn_criar_conta);

        btn_criar_conta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CadastroInsta.class));
            }
        });
    }
}