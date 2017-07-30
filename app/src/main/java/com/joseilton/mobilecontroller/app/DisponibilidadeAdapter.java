package com.joseilton.mobilecontroller.app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joseilton.mobilecontroller.R;
import com.joseilton.mobilecontroller.categoria.Categoria;
import com.joseilton.mobilecontroller.conta.Conta;
import com.joseilton.mobilecontroller.util.ColorUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Joseilton on 22/06/2017.
 */
public class DisponibilidadeAdapter extends RecyclerView.Adapter<DisponibilidadeAdapter.MyViewHolder> {

    private List<Conta> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView descricao, valor;

        public MyViewHolder(View view) {
            super(view);
            descricao = (TextView) view.findViewById(R.id.descricao_textView);
            valor = (TextView) view.findViewById(R.id.valor_textView);
        }
    }

    public DisponibilidadeAdapter(List<Conta> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.disponibilidade_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Conta conta = list.get(position);
        holder.descricao.setText(conta.getDescricao());
        holder.valor.setText(Formatador.formatarValorMonetario(conta.getSaldo()));

        if(conta.getSaldo().compareTo(new BigDecimal("0.00")) >= 0) {
            holder.valor.setTextColor(ColorUtil.DARK_GREEN);
        } else {
            holder.valor.setTextColor(ColorUtil.DARK_RED);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

