package com.example.trytouse.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.trytouse.R;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.example.trytouse.helper.UsuarioFirebaseInfo;
import com.example.trytouse.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditPerfilActivity extends AppCompatActivity {
    private CircleImageView img_perfil;
    private EditText txt_nome_perfil, txt_email_perfil;
    private TextView txt_alterar_foto;
    private StorageReference storageRef;
    private String idUsuario;
    private Usuario usuarioLogado;
    private Button btn_salva_alt;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil);

        img_perfil = findViewById(R.id.img_edit_perfil);
        txt_nome_perfil = findViewById(R.id.txt_nome_perfil);
        txt_email_perfil = findViewById(R.id.txt_email_perfil);
        txt_alterar_foto = findViewById(R.id.txt_alterar_foto);
        btn_salva_alt = findViewById(R.id.btn_salva_alt);

        storageRef = ConfiguracaoFirebas.getFirebaseStorage();
        idUsuario = UsuarioFirebaseInfo.getIdUsuario();
        usuarioLogado = UsuarioFirebaseInfo.getUsuarioLogadoDados();

        Toolbar toolbar = findViewById(R.id.toolbar_insta);
        toolbar.setTitle("Editar Perfil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        /*AlertDialog.Builder builder_edit = new AlertDialog.Builder(getApplicationContext());

        builder_edit.setView(getLayoutInflater().inflate(R.layout.dialog_editperf, null));
        builder_edit.setCancelable(true);

        AlertDialog dialog_edit = builder_edit.create();
        dialog_edit.show();*/

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.insta9);
        if (mediaPlayer != null){
            mediaPlayer.start();
        }

        FirebaseUser user = UsuarioFirebaseInfo.getUsuarioAtual();
        Uri url = user.getPhotoUrl();

        if(url!= null){
            Glide.with(EditPerfilActivity.this)
                    .load(url)
                    .into(img_perfil);
        } else {
            img_perfil.setImageResource(R.drawable.avatar);
        }
        txt_nome_perfil.setText(user.getDisplayName());
        txt_email_perfil.setText(user.getEmail());

        txt_alterar_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    openSomeActivityForResult();
                }
            });

        btn_salva_alt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = txt_nome_perfil.getText().toString();
                boolean retorno = UsuarioFirebaseInfo.atualizaNome(nome);
                if (retorno){

                    usuarioLogado.setNome(nome);
                    usuarioLogado.atualizar();

                    Toast.makeText(EditPerfilActivity.this, "Nome alterado com sucesso!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Bitmap imagem = null;

                        try {
                            Uri localImagem = data.getData();
                            imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagem);

                            if (imagem != null){
                                img_perfil.setImageBitmap(imagem);

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                                byte[] dadosImagem = baos.toByteArray();

                                final StorageReference imagemRef = storageRef
                                        .child("imagens")
                                        .child("perfil")
                                        .child(idUsuario + ".jpeg");

                                UploadTask uploadT = imagemRef.putBytes(dadosImagem);
                                uploadT.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditPerfilActivity.this, "Erro ao fazer upload da imagem!", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                Uri url = task.getResult();
                                                atualizarFoto(url);
                                            }
                                        });

                                        Toast.makeText(EditPerfilActivity.this, "Sucesso ao fazer upload da imagem!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }
            });

    private void atualizarFoto(Uri local){
        boolean retorno = UsuarioFirebaseInfo.atualizaFoto(local);
        if (retorno){
            usuarioLogado.setCaminhoFoto(local.toString());
            usuarioLogado.atualizar();
            Toast.makeText(EditPerfilActivity.this, "Foto alterada com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }

    public void openSomeActivityForResult() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null){
        someActivityResultLauncher.launch(intent);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}