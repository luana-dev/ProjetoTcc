package com.example.trytouse.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.trytouse.adapter.MensagemAdapter;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.example.trytouse.helper.UsuarioFirebaseInfo;
import com.example.trytouse.model.Conversa;
import com.example.trytouse.model.Mensagens;
import com.example.trytouse.model.Usuario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trytouse.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private TextView nome_chat;
    private CircleImageView img_foto_chat;
    private Usuario usuarioDestino;
    private Conversa usuarioConversa;
    private FloatingActionButton fab_enviar;
    private EditText txt_mensagem;
    private String idUsuarioAtual, idUsuarioDestino;
    private ImageView img_camera;

    private StorageReference storage;
    private DatabaseReference mensagensRef;
    private DatabaseReference database;
    private ChildEventListener childEventListener;
    private RecyclerView recyclerMsgs;
    private List<Mensagens> msgs = new ArrayList<>();
    private MensagemAdapter adapter;
    MediaPlayer mediaPlayer;

    private static final int SELECAO_CAMERA  = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            AlertDialog.Builder builder_whats_chat = new AlertDialog.Builder(ChatActivity.this);

            builder_whats_chat.setView(getLayoutInflater().inflate(R.layout.dialog_chat, null));
            builder_whats_chat.setCancelable(true);

            AlertDialog dialog_whats_chat = builder_whats_chat.create();
            dialog_whats_chat.show();

        nome_chat = findViewById(R.id.nomeChat);
        img_foto_chat = findViewById(R.id.circle_foto_chat);
        fab_enviar = findViewById(R.id.fab_enviar);
        txt_mensagem = findViewById(R.id.editTextNomeChat);
        idUsuarioAtual = UsuarioFirebaseInfo.getIdUsuario();
        img_camera = findViewById(R.id.imageViewFotoChat);
        recyclerMsgs = findViewById(R.id.recyclerMensagens);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            if (bundle.containsKey("chatContato") == true){
                usuarioDestino = (Usuario) bundle.getSerializable("chatContato");
                nome_chat.setText(usuarioDestino.getNome());
                String foto = usuarioDestino.getCaminhoFoto();
                if (foto != null){
                    Uri url = Uri.parse(usuarioDestino.getCaminhoFoto());
                    Glide.with(ChatActivity.this)
                            .load(url)
                            .into(img_foto_chat);
                } else {
                    img_foto_chat.setImageResource(R.drawable.padrao);
                }
                idUsuarioDestino = usuarioDestino.getId();
            }
            if (bundle.containsKey("chatConversa") == true){
                usuarioConversa = (Conversa) bundle.getSerializable("chatConversa");
                usuarioDestino = usuarioConversa.getUsuarioExib();
                nome_chat.setText(usuarioDestino.getNome());
                String foto  = usuarioDestino.getCaminhoFoto();
                if (foto != null){
                    Uri url = Uri.parse(usuarioDestino.getCaminhoFoto());
                    Glide.with(ChatActivity.this)
                            .load(url)
                            .into(img_foto_chat);
                } else {
                    img_foto_chat.setImageResource(R.drawable.padrao);
                }
                idUsuarioDestino = usuarioDestino.getId();
            }

        }

        adapter = new MensagemAdapter(msgs, getApplicationContext() );

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMsgs.setLayoutManager( layoutManager );
        recyclerMsgs.setHasFixedSize( true );
        recyclerMsgs.setAdapter( adapter );

        database = ConfiguracaoFirebas.getFirebaseDatabase();
        storage = ConfiguracaoFirebas.getFirebaseStorage();
        mensagensRef = database.child("mensagens")
                .child( idUsuarioAtual )
                .child( idUsuarioDestino );

        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("MediaStore.ACTION_IMAGE_CAPTURE");
                if ( i.resolveActivity(getPackageManager()) != null ){
                    startActivityForResult(i, SELECAO_CAMERA);
                }
            }
        });


       fab_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensagem(v);
            }
        });

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.whats5);
        if (mediaPlayer != null){
            mediaPlayer.start();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK ){

            Bitmap imagem = null;

            try {

                switch ( requestCode ){
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                }

                if ( imagem != null ){

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos );
                    byte[] dadosImagem = baos.toByteArray();

                    String nomeImagem = UUID.randomUUID().toString();

                    StorageReference imagemRef = storage.child("imagens")
                            .child("fotos")
                            .child( idUsuarioAtual )
                            .child( nomeImagem );

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Erro", "Erro ao fazer upload");
                            Toast.makeText(ChatActivity.this,
                                    "Erro ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    String durl = url.toString();

                                    Mensagens mensagem = new Mensagens();
                                    mensagem.setIdUsuario(idUsuarioAtual);
                                    mensagem.setMensagem("imagem.jpeg");
                                    mensagem.setImg(durl);

                                    salvarMensagem(idUsuarioAtual, idUsuarioDestino, mensagem);
                                    salvarMensagem(idUsuarioDestino, idUsuarioAtual, mensagem);

                                    Toast.makeText(ChatActivity.this,
                                            "Sucesso ao enviar imagem",
                                            Toast.LENGTH_SHORT).show();
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

    public void enviarMensagem(View view){

        String textoMensagem = txt_mensagem.getText().toString();

        if ( !textoMensagem.isEmpty() ){

            Mensagens mensagem = new Mensagens();
            mensagem.setIdUsuario( idUsuarioAtual );
            mensagem.setMensagem( textoMensagem );

            //Salvar mensagem para o remetente
            salvarMensagem(idUsuarioAtual, idUsuarioDestino, mensagem);

            //Salvar mensagem para o destinatario
            salvarMensagem(idUsuarioDestino, idUsuarioAtual, mensagem);

            //Salvar conversa
            salvarConversa(mensagem);

        }else {
            Toast.makeText(ChatActivity.this,
                    "Digite uma mensagem para enviar!",
                    Toast.LENGTH_LONG).show();
        }

    }

    private void salvarConversa( Mensagens msg){

        //Salvar conversa remetente
        Conversa conversaRemetente = new Conversa();
        conversaRemetente.setIdRemetente( idUsuarioAtual );
        conversaRemetente.setIdDestinatario( idUsuarioDestino );
        conversaRemetente.setUltimamsg( msg.getMensagem() );
        conversaRemetente.setUsuarioExib( usuarioDestino );

        conversaRemetente.salvar();

    }

    private void salvarMensagem(String idRemetente, String idDestinatario, Mensagens msg){

        DatabaseReference dr = ConfiguracaoFirebas.getFirebaseDatabase();
        DatabaseReference msgRef = dr.child("mensagens");

        msgRef.child(idRemetente)
                .child(idDestinatario)
                .push()
                .setValue(msg);

        txt_mensagem.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarMensagens();;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mensagensRef.removeEventListener(childEventListener);
    }

    private void recuperarMensagens(){

        childEventListener = mensagensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Mensagens mensagem = dataSnapshot.getValue( Mensagens.class );
                msgs.add( mensagem );
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}