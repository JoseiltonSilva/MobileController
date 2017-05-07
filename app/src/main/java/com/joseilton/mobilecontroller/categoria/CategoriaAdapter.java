package com.joseilton.mobilecontroller.categoria;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joseilton.mobilecontroller.R;

import java.util.List;

/**
 * Created by Joseilton on 15/04/2017.
 */
public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.MyViewHolder> {

    private List<Categoria> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView descricao;
        public ImageView cor;

        public MyViewHolder(View view) {
            super(view);
            descricao = (TextView) view.findViewById(R.id.descricao_textView);
            cor = (ImageView) view.findViewById(R.id.colorImageView);
        }
    }

    public CategoriaAdapter(List<Categoria> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categoria_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Categoria categoria = list.get(position);
        holder.descricao.setText(categoria.getDescricao());
        holder.cor.setColorFilter(categoria.getCor());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

