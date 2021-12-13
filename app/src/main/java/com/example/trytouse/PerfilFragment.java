package com.example.trytouse;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.trytouse.activity.ConfiguracoesActivityWhats;
import com.example.trytouse.activity.EditPerfilActivity;
import com.example.trytouse.adapter.AdapterGrid;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.example.trytouse.helper.UsuarioFirebaseInfo;
import com.example.trytouse.model.Postagem;
import com.example.trytouse.model.Usuario;
import com.google.firebase.auth.FirebaseUser;
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


public class PerfilFragment extends Fragment {

    private CircleImageView img_perfil;
    private TextView txt_pub, txt_seg, txt_seguindo, txt_acao;
    private Button btn_acao_perfil;
    private ProgressBar progressBar;
    private GridView gridView;
    private Usuario usuarioLog;
    private DatabaseReference usuariosRef;
    private DatabaseReference usuarioLogadoRef;
    private DatabaseReference postagens_user;
    private ValueEventListener valueEventListenerPerfil;
    private AdapterGrid adapterGrid;
    MediaPlayer mediaPlayer, mediaPlayer2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        img_perfil = view.findViewById(R.id.img_perfil);
        btn_acao_perfil = view.findViewById(R.id.btn_acao_perfil);
        txt_pub = view.findViewById(R.id.txt_pub);
        txt_seg = view.findViewById(R.id.txt_seg);
        txt_seguindo = view.findViewById(R.id.txt_seguindo);
        progressBar = view.findViewById(R.id.progressBar);
        gridView = view.findViewById(R.id.gridPerfil);
        txt_acao = view.findViewById(R.id.txt_acao);

        usuarioLog = UsuarioFirebaseInfo.getUsuarioLogadoDados();
        usuariosRef = ConfiguracaoFirebas.getFirebaseDatabase().child("usuarios");
        postagens_user = ConfiguracaoFirebas.getFirebaseDatabase().child("postagens").child(usuarioLog.getId());

        btn_acao_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditPerfilActivity.class));
            }
        });
        txt_acao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditPerfilActivity.class));
            }
        });
        inicializarImageLoader();

        carregarFotosPostagem();

        AlertDialog.Builder builder_perf = new AlertDialog.Builder(getActivity());
        AlertDialog.Builder builder_perf2 = new AlertDialog.Builder(getActivity());

        builder_perf.setView(getLayoutInflater().inflate(R.layout.dialog_perf, null));
        builder_perf2.setView(getLayoutInflater().inflate(R.layout.dialog_perf2, null));

        builder_perf2.setCancelable(true).create().show();
        builder_perf.setCancelable(true);

        AlertDialog dialog_perf = builder_perf.create();
        dialog_perf.show();

        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.insta7);
        mediaPlayer2 = MediaPlayer.create(getActivity(), R.raw.insta8);
        if (mediaPlayer != null){
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mediaPlayer2 != null){
                        mediaPlayer2.start();
                    }
                }
            });
        }

        return view;
    }

    public void carregarFotosPostagem(){

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
                    urlFotos.add( postagem.getCaminhoFoto() );
                }

                //Configurar adapter
                adapterGrid = new AdapterGrid( getActivity() , R.layout.grid_postagem, urlFotos );
                gridView.setAdapter( adapterGrid );

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * Instancia a UniversalImageLoader
     */
    public void inicializarImageLoader() {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder( getActivity() )
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();
        ImageLoader.getInstance().init( config );

    }

    private void recuperarDadosUsuarioLogado(){

        usuarioLogadoRef = usuariosRef.child( usuarioLog.getId() );
        valueEventListenerPerfil = usuarioLogadoRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Usuario usuario = dataSnapshot.getValue( Usuario.class );

                        String postagens = String.valueOf( usuario.getPostagens() );
                        String seguindo = String.valueOf( usuario.getSeguindo() );
                        String seguidores = String.valueOf( usuario.getSeguidores() );

                        //Configura valores recuperados
                        txt_pub.setText( postagens );
                        txt_seg.setText( seguidores );
                        txt_seguindo.setText( seguindo );

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

    }

    private void recuperarFotoUsuario(){
        usuarioLog = UsuarioFirebaseInfo.getUsuarioLogadoDados();

        String caminhoFoto = usuarioLog.getCaminhoFoto();

        if (caminhoFoto != null){
            Uri url = Uri.parse(caminhoFoto);
            Glide.with(getActivity())
                    .load(url)
                    .into(img_perfil);
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        recuperarDadosUsuarioLogado();
        recuperarFotoUsuario();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuarioLogadoRef.removeEventListener(valueEventListenerPerfil);
    }
}