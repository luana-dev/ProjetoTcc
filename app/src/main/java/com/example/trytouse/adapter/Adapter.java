package com.example.trytouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trytouse.R;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    String data1[], data2[];
    int imagens[];
    Context context;

    public Adapter(Context ct, String tutorial[], String ano[], int img[]) {
        context = ct;
        data1 = tutorial;
        data2 = ano;
        imagens = img;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista, parent, false);
        //return new MyViewHolder(itemLista);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_lista, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.myText1.setText(data1[position]);
        holder.myText2.setText(data2[position]);
        holder.imagens.setImageResource(imagens[position]);

    }

    @Override
    public int getItemCount() {

        return data1.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView myText1, myText2;
        ImageView imagens;
        //ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            myText1 = itemView.findViewById(R.id.textTitulo);
            myText2 = itemView.findViewById(R.id.textAno);
            imagens = itemView.findViewById(R.id.imageadapter);
            //mainLayout = itemView.findViewById(R.id.mainLayout);


        }
    }
}
