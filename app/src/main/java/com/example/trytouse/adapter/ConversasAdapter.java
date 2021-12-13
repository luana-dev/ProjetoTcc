package com.example.trytouse.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.trytouse.R;
import com.example.trytouse.model.Conversa;
import com.example.trytouse.model.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversasAdapter extends RecyclerView.Adapter<ConversasAdapter.MyViewHolder>{

    private List<Conversa> conversas;
    private Context context;

    public ConversasAdapter(List<Conversa> conversas, Context context) {
        this.conversas = conversas;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemListaConversas = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_conversas, parent, false);
        return new MyViewHolder(itemListaConversas);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Conversa conversa = conversas.get(position);
        holder.txt_msg_conversa.setText(conversa.getUltimamsg());

        Usuario usuario = conversa.getUsuarioExib();
        holder.txt_nome_conversa.setText(usuario.getNome());

        if ( usuario.getCaminhoFoto() != null ){
            Uri uri = Uri.parse( usuario.getCaminhoFoto() );
            Glide.with( context ).load( uri ).into( holder.img_conversas);
        }else {
            holder.img_conversas.setImageResource(R.drawable.padrao);
        }


    }

    @Override
    public int getItemCount() {
        return conversas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt_nome_conversa, txt_msg_conversa;
        CircleImageView img_conversas;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_nome_conversa = itemView.findViewById(R.id.txt_nome_conversas);
            txt_msg_conversa = itemView.findViewById(R.id.txt_ultima_msg);
            img_conversas = itemView.findViewById(R.id.img_conversas);
        }
    }
}
