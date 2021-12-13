package com.example.trytouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trytouse.R;

public class GridAdapter extends BaseAdapter {
    Context context;
    String[] flores;
    int[] imagens_grid;

    LayoutInflater inflater;

    public GridAdapter(Context context, String[] flores, int[] imagens_grid) {
        this.context = context;
        this.flores = flores;
        this.imagens_grid = imagens_grid;
    }

    @Override
    public int getCount() {
        return flores.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null){
            convertView = inflater.inflate(R.layout.grid_item, null);
        }

        ImageView img = convertView.findViewById(R.id.grid_image);
        TextView txt_grid = convertView.findViewById(R.id.grid_text);

        img.setImageResource(imagens_grid[position]);
        txt_grid.setText(flores[position]);

        return convertView;
    }
}
