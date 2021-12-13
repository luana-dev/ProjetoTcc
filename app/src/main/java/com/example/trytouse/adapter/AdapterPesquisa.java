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
import com.example.trytouse.model.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPesquisa extends RecyclerView.Adapter<AdapterPesquisa.MyViewHolder> {
    private List<Usuario> lista;
    private Context context;

    public AdapterPesquisa(List<Usuario> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pesquisa, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Usuario usuario = lista.get(position);

            holder.txt_nome.setText(usuario.getNome());

            if (usuario.getCaminhoFoto() != null){
                Uri uri = Uri.parse(usuario.getCaminhoFoto());
                Glide.with(context).load(uri).into(holder.img);
            } else {
                holder.img.setImageResource(R.drawable.avatar);
            }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView txt_nome;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_ad_pesquisa);
            txt_nome = itemView.findViewById(R.id.txt_ad_pesquisa);
        }
    }
}
