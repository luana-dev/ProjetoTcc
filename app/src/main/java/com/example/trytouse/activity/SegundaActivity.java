package com.example.trytouse.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.trytouse.InstaActivity2;
import com.example.trytouse.R;
import com.example.trytouse.RecyclerItemClickListener;
import com.example.trytouse.adapter.Adapter;
import com.example.trytouse.model.Tutoriais;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;

public class SegundaActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String tutorial[], ano[];
    int imagens[] = {R.drawable.logo_whats, R.drawable.logo_insta, R.drawable.logo_uber, R.drawable.logo99};
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        AlertDialog.Builder builder = new AlertDialog.Builder(SegundaActivity.this);

        builder.setView(getLayoutInflater().inflate(R.layout.dialog_escolha_itens, null));
        builder.setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.show();

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.escolhatutorial);
        if (mediaPlayer != null){
            mediaPlayer.start();
        }

        recyclerView = findViewById(R.id.recyclerView);
        tutorial = getResources().getStringArray(R.array.tutoriais);
        ano = getResources().getStringArray(R.array.ano);

        //Configurar adapter
        Adapter adapter = new Adapter(this, tutorial, ano, imagens);

        //Configurar recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setAdapter(adapter);

        //evento de click
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if (position==0){startActivity(new Intent(getApplicationContext(), WhatsActivity2.class));
                                mediaPlayer.stop();}
                                if(position == 2){ startActivity(new Intent(getApplicationContext(), TelaInicialUber.class));
                                mediaPlayer.stop();}
                                if (position == 1){startActivity(new Intent(getApplicationContext(), InstaActivity2.class));
                                    mediaPlayer.stop();}
                                if (position == 3){startActivity(new Intent(getApplicationContext(), NovenoveActivity.class));
                                    mediaPlayer.stop();}

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                if (position==0){startActivity(new Intent(getApplicationContext(), WhatsActivity2.class));
                                    mediaPlayer.stop();}
                                if(position == 2){ startActivity(new Intent(getApplicationContext(), TelaInicialUber.class));
                                    mediaPlayer.stop();}
                                if (position == 1){startActivity(new Intent(getApplicationContext(), InstaActivity2.class));
                                    mediaPlayer.stop();}
                                if (position == 3){startActivity(new Intent(getApplicationContext(), NovenoveActivity.class));
                                    mediaPlayer.stop();}
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
    }
}