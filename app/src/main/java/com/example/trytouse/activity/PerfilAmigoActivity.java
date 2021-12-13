package com.example.trytouse.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.trytouse.R;
import com.example.trytouse.adapter.AdapterGrid;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.example.trytouse.helper.UsuarioFirebaseInfo;
import com.example.trytouse.model.Postagem;
import com.example.trytouse.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilAmigoActivity extends AppCompatActivity {
    private Usuario usuarioSelec;
    private Button btn_acao_perfil;
    private TextView txt_seguir;
    private CircleImageView img_amigo_perfil;
    private DatabaseReference usuariosRef, usuarioAmigo;
    private ValueEventListener valueEventListener;
    private TextView txt_pub, txt_seg, txt_seguindo;
    private DatabaseReference postagens_user;
    private Usuario usuarioLog;
    private GridView gridView;
    private AdapterGrid adapterGrid;
    private List<Postagem> postagens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_amigo);

        usuariosRef = ConfiguracaoFirebas.getFirebaseDatabase().child("usuarios");

        btn_acao_perfil = findViewById(R.id.btn_acao_perfil);
        txt_seguir = findViewById(R.id.txt_acao);
        img_amigo_perfil = findViewById(R.id.img_perfil);
        txt_pub = findViewById(R.id.txt_pub);
        txt_seg = findViewById(R.id.txt_seg);
        txt_seguindo = findViewById(R.id.txt_seguindo);
        gridView = findViewById(R.id.gridPerfil);

        btn_acao_perfil.setText("Seguir");
        txt_seguir.setText("Seguir");

        AlertDialog.Builder builder = new AlertDialog.Builder(PerfilAmigoActivity.this);
        builder.setView(getLayoutInflater().inflate(R.layout.dialog_perf_amigo, null));
        builder.setCancelable(true);
        builder.create().show();

        usuarioLog = UsuarioFirebaseInfo.getUsuarioLogadoDados();
        postagens_user = ConfiguracaoFirebas.getFirebaseDatabase().child("postagens").child(usuarioLog.getId());

        Toolbar toolbar = findViewById(R.id.toolbar_insta);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            usuarioSelec = (Usuario) bundle.getSerializable("usuarioSelecionado");

            getSupportActionBar().setTitle(usuarioSelec.getNome());
            String caminhoFoto = usuarioSelec.getCaminhoFoto();
            if (caminhoFoto != null){
                Uri url = Uri.parse(caminhoFoto);
                Glide.with(PerfilAmigoActivity.this)
                        .load(url)
                        .into(img_amigo_perfil);
            }
        }
        RecuperaDadosAmigo();
        inicializarImageLoader();
        carregarFotosPostagem();
    }

    public void carregarFotosPostagem(){
        postagens = new ArrayList<>();
        //Recupera as fotos postadas pelo usuario
        postagens_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Configurar o tamanho do grid
                int tamanhoGrid = getResources().getDisplayMetrics().widthPixels;
                int tamanhoImagem = tamanhoGrid / 3;
                gridView.setNumColumns(3);
                gridView.setColumnWidth( tamanhoImagem );

                List<String> urlFotos = new ArrayList<>();
                for( DataSnapshot ds: dataSnapshot.getChildren() ){
                    Postagem postagem = ds.getValue( Postagem.class );
                    postagens.add(postagem);
                    urlFotos.add( postagem.getCaminhoFoto() );
                }

                //Configurar adapter
                adapterGrid = new AdapterGrid( PerfilAmigoActivity.this , R.layout.grid_postagem, urlFotos );
                gridView.setAdapter( adapterGrid );

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void inicializarImageLoader() {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder( this )
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();
        ImageLoader.getInstance().init( config );

    }

    private void RecuperaDadosAmigo(){
        usuarioAmigo = usuariosRef.child(usuarioSelec.getId());
        valueEventListener = usuarioAmigo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);

                String postagens = String.valueOf(usuario.getPostagens());
                String seguindo = String.valueOf(usuario.getSeguindo());
                String seguidores = String.valueOf(usuario.getSeguidores());

                txt_pub.setText(postagens);
                txt_seguindo.setText(seguindo);
                txt_seg.setText(seguidores);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioAmigo.removeEventListener(valueEventListener);
    }
}