package com.example.trytouse.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.trytouse.R;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.example.trytouse.helper.UsuarioFirebaseInfo;
import com.example.trytouse.model.Feed;
import com.example.trytouse.model.PostagemCurtida;
import com.example.trytouse.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.MyViewHolder>{
    private List<Feed> listaFeed;
    private Context context;

    public AdapterFeed(List<Feed> listaFeed, Context context) {
        this.listaFeed = listaFeed;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_feed, parent, false);
        return new AdapterFeed.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Feed feed = listaFeed.get(position);
        Usuario usuarioLogado = UsuarioFirebaseInfo.getUsuarioLogadoDados();

        //Carrega dados do feed
        Uri uriFotoUsuario = Uri.parse( feed.getFotoUsuario() );
        Uri uriFotoPostagem = Uri.parse( feed.getFotoPostagem() );

        Glide.with( context ).load( uriFotoUsuario ).into(holder.fotoPerfil);
        Glide.with( context ).load( uriFotoPostagem ).into(holder.fotoPostagem);

        holder.descricao.setText( feed.getDescricao() );
        holder.nome.setText( feed.getNomeUsuario() );

    }

    @Override
    public int getItemCount() {
        return listaFeed.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

    CircleImageView fotoPerfil;
    TextView nome, descricao, qtdCurtidas;
    ImageView fotoPostagem, visualizarComentario, img_heart;

    public MyViewHolder(View itemView) {
        super(itemView);

        fotoPerfil   = itemView.findViewById(R.id.imagePerfilPostagem);
        fotoPostagem = itemView.findViewById(R.id.imagePostagemSelecionada);
        nome         = itemView.findViewById(R.id.textPerfilPostagem);
        qtdCurtidas  = itemView.findViewById(R.id.textQtdCurtidasPostagem);
        descricao    = itemView.findViewById(R.id.textDescricaoPostagem);
        visualizarComentario    = itemView.findViewById(R.id.imageComentarioFeed);
        img_heart = itemView.findViewById(R.id.img_heart_insta);
    }
}

}
