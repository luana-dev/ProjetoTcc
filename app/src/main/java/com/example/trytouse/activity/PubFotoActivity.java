package com.example.trytouse.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trytouse.R;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.example.trytouse.helper.UsuarioFirebaseInfo;
import com.example.trytouse.model.Postagem;
import com.example.trytouse.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class PubFotoActivity extends AppCompatActivity {
    private EditText editDescrip;
    private ImageView img_post;
    private Button btn_pub;
    private Bitmap imagem;
    private Bitmap imagemSave;
    private String idUsuario;
    private StorageReference storageReference;
    private Usuario usuarioLog;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_foto);

        /*AlertDialog.Builder builder_pub = new AlertDialog.Builder(getApplicationContext());

        builder_pub.setView(getLayoutInflater().inflate(R.layout.dialog_pub, null));
        builder_pub.setCancelable(true);

        AlertDialog dialog_pub = builder_pub.create();
        dialog_pub.show();*/

        editDescrip = findViewById(R.id.editDescrip);
        img_post = findViewById(R.id.img_post);
        btn_pub = findViewById(R.id.btn_pub);

        idUsuario = UsuarioFirebaseInfo.getIdUsuario();
        storageReference = ConfiguracaoFirebas.getFirebaseStorage();
        usuarioLog = UsuarioFirebaseInfo.getUsuarioLogadoDados();

        Toolbar toolbar = findViewById(R.id.toolbar_insta);
        toolbar.setTitle("Publicação");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
           byte[] b = bundle.getByteArray("fotoEscolhida");
            imagem = BitmapFactory.decodeByteArray(b, 0, b.length );
            img_post.setImageBitmap( imagem );
            imagemSave = imagem.copy(imagem.getConfig(), true);
        }

        btn_pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               salvar_postagem();
               mediaPlayer.stop();
            }
        });

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.insta6);
        if (mediaPlayer != null){
            mediaPlayer.start();
        }
    }

    private void salvar_postagem(){
        final Postagem postagem = new Postagem();
        postagem.setIdUsuario(idUsuario);
        postagem.setDescricao(editDescrip.getText().toString());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagemSave.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] dadosImagem = baos.toByteArray();

        StorageReference storageReference = ConfiguracaoFirebas.getFirebaseStorage();
        StorageReference postagemRef = storageReference.child("imagens").child("publicacoes").child(postagem.getId() + ".jpeg");

        UploadTask uploadTask = postagemRef.putBytes(dadosImagem);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PubFotoActivity.this,
                        "Erro ao salvar a imagem, tente novamente!",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                postagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri url = task.getResult();

                        //Atualizar qtde de postagens
                        int qtdPostagem = usuarioLog.getPostagens() + 1;
                        usuarioLog.setPostagens( qtdPostagem );
                        System.out.println(usuarioLog.getPostagens());
                        usuarioLog.atualizarQtdPostagem();

                        postagem.setCaminhoFoto(url.toString());
                        postagem.salvar();
                        Toast.makeText(PubFotoActivity.this,
                                "Publicação salva com sucesso!",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}