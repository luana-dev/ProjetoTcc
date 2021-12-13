package com.example.trytouse.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trytouse.R;
import com.example.trytouse.model.Usuario;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.example.trytouse.helper.Permissao;
import com.example.trytouse.helper.UsuarioFirebaseInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfiguracoesActivityWhats extends AppCompatActivity {
        private String[] permissoesNecessarias = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        private ImageButton img_btn_camera, img_btn_galeria;
        private static final int SELECAO_CAMERA = 1;
        private static final int SELECAO_GALERIA = 200;
        MediaPlayer mediaPlayer;
        private CircleImageView circleImageView;
        private StorageReference storageReference;
        private String id;
        private EditText nomePerfil;
        private ImageView btn_atualiza_nome;
        private Usuario userLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_whats);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.whats_conf);
        if (mediaPlayer != null){
            mediaPlayer.start();
        }

        storageReference = ConfiguracaoFirebas.getFirebaseStorage();
        id = UsuarioFirebaseInfo.getIdUsuario();
        userLogado = UsuarioFirebaseInfo.getUsuarioLogadoDados();

        AlertDialog.Builder builder_whats_conf = new AlertDialog.Builder(this);

        builder_whats_conf.setView(getLayoutInflater().inflate(R.layout.dialog_whats_conf, null));
        builder_whats_conf.setCancelable(true);

        AlertDialog dialog_whats_conf = builder_whats_conf.create();
        dialog_whats_conf.show();

        img_btn_camera = findViewById(R.id.imageButton_camera);
        img_btn_galeria = findViewById(R.id.imageButton_galeria);
        circleImageView = findViewById(R.id.imageCircleView);
        nomePerfil = findViewById(R.id.editPerfilNome);
        btn_atualiza_nome = findViewById(R.id.btn_atualiza_nome);

        img_btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //if (i.resolveActivity(getPackageManager()) == null){
                    startActivityForResult(i, SELECAO_CAMERA);
                //}

            }
        });

        img_btn_galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, SELECAO_GALERIA);
            }
        });

        btn_atualiza_nome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nomePerfil.getText().toString();
                boolean retorno = UsuarioFirebaseInfo.atualizaNome(nome);
                if (retorno){

                    userLogado.setNome(nome);
                    userLogado.atualizar();

                    Toast.makeText(ConfiguracoesActivityWhats.this, "Nome alterado com sucesso!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Validar Permissoes
        Permissao.validaPermissoes(permissoesNecessarias, this, 1);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        toolbar.setTitle("Configurações");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24);

        FirebaseUser user = UsuarioFirebaseInfo.getUsuarioAtual();
        Uri url = user.getPhotoUrl();

        if(url!= null){
            Glide.with(ConfiguracoesActivityWhats.this)
                    .load(url)
                    .into(circleImageView);
        } else {
            circleImageView.setImageResource(R.drawable.padrao);
        }
        nomePerfil.setText(user.getDisplayName());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Bitmap imagem = null;

            try {
                switch (requestCode){
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;

                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }

                if (imagem != null){
                    circleImageView.setImageBitmap(imagem);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();
                    
                    StorageReference imagemReferencia =storageReference
                            .child("imagens")
                            .child("perfil")
                            .child(id + ".jpeg");
                    UploadTask uploadTask = imagemReferencia.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ConfiguracoesActivityWhats.this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ConfiguracoesActivityWhats.this, "Sucesso ao fazer upload da imagem", Toast.LENGTH_SHORT).show();

                            imagemReferencia.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    atualizaFoto(url);
                                }
                            });
                        }
                    });
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void atualizaFoto(Uri url){
            boolean retorno = UsuarioFirebaseInfo.atualizaFoto(url);
            if (retorno){
                userLogado.setCaminhoFoto(url.toString());
                userLogado.atualizar();

                Toast.makeText(ConfiguracoesActivityWhats.this, "Foto alterada com sucesso!", Toast.LENGTH_SHORT).show();
            }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int permissaoResultado : grantResults){
            if (permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o App é necessário aceitar as permissões");
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
}