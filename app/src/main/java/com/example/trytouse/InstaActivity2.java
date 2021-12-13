package com.example.trytouse;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trytouse.activity.Insta_Activity3;
import com.example.trytouse.activity.SegundaActivity;
import com.example.trytouse.model.Usuario;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.example.trytouse.helper.UsuarioFirebaseInfo;

public class InstaActivity2 extends AppCompatActivity {

    EditText cmpusuario, cmpsenha;
    Button btn_entrar;
    TextView txt_prox;
    FirebaseUser userAtual;
    ImageView back4, home4;
    MediaPlayer mediaPlayer1, mediaPlayer2;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta2);

        back4 = findViewById(R.id.img_back);
        home4 = findViewById(R.id.img_home);

        back4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SegundaActivity.class));
            }
        });

        home4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SegundaActivity.class));
            }
        });

        AlertDialog.Builder builder_insta = new AlertDialog.Builder(this);
        AlertDialog.Builder builder_insta2 = new AlertDialog.Builder(this);

        builder_insta2.setView(getLayoutInflater().inflate(R.layout.dialog_init_insta2, null));
        builder_insta2.setCancelable(true);

        builder_insta.setView(getLayoutInflater().inflate(R.layout.dialog_init_insta, null));
        builder_insta.setCancelable(true);
        builder_insta.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                builder_insta2.create().show();
            }
        });

        AlertDialog dialog_insta = builder_insta.create();
        dialog_insta.show();

        database = ConfiguracaoFirebas.getFirebaseDatabase().child("infos_insta");
        userAtual = UsuarioFirebaseInfo.getUsuarioAtual();

        cmpusuario = findViewById(R.id.cmpusuario2);
        cmpsenha = findViewById(R.id.cmpsenha2);
        btn_entrar = findViewById(R.id.btn_entrar2);
        txt_prox = findViewById(R.id.txt_prox_tela);


        txt_prox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Insta_Activity3.class));
            }
        });

        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Feito", Toast.LENGTH_SHORT);
                String textoNome = cmpusuario.getText().toString();
                String textoSenha = cmpsenha.getText().toString();

                if(textoNome != null){
                    Usuario usuario = new Usuario();
                    usuario.setId(userAtual.getUid());
                    usuario.setEmail_insta(textoNome);
                    usuario.setSenha_insta(textoSenha);
                    usuario.salvar_insta();
                    Toast.makeText(InstaActivity2.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();

                }

                if (mediaPlayer1.isPlaying()){
                    mediaPlayer1.stop();
                }
                if (mediaPlayer2.isPlaying()){
                    mediaPlayer2.stop();
                }

                startActivity(new Intent(getApplicationContext(), TelaInicialInsta.class));

            }
        });

        mediaPlayer1 = MediaPlayer.create(getApplicationContext(), R.raw.insta1);
        mediaPlayer2 = MediaPlayer.create(getApplicationContext(), R.raw.insta2);
        if (mediaPlayer1 != null){
            mediaPlayer1.start();
            mediaPlayer1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer2.start();
                }
            });
        }
    }
}