package com.joseilton.mobilecontroller.transferencia;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joseilton.mobilecontroller.R;
import com.joseilton.mobilecontroller.util.DateUtil;

import java.util.List;

/**
 * Created by Joseilton on 16/04/2017.
 */

public class TransferenciasAdapter extends RecyclerView.Adapter<TransferenciasAdapter.MyViewHolder> {
    private List<Transferencia> mList;
    private Transferencia transferencia;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView descricao, contaOrigem, contaDestino, data, valor;

        public MyViewHolder(View view) {
            super(view);
            descricao = (TextView) view.findViewById(R.id.descricao_textView);
            contaOrigem = (TextView) view.findViewById(R.id.contaOrigem_textView);
            contaDestino = (TextView) view.findViewById(R.id.contaDestino_textView);
            data = (TextView) view.findViewById(R.id.data_textView);
            valor = (TextView) view.findViewById(R.id.valor_textView);
        }
    }

    public TransferenciasAdapter(List<Transferencia> list) {
        this.mList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transferencia_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        transferencia = mList.get(position);
        holder.descricao.setText(transferencia.getDescricao());
        holder.contaOrigem.setText(transferencia.getContaOrigem().getDescricao());
        holder.contaDestino.setText(transferencia.getContaDestino().getDescricao());
        holder.data.setText(DateUtil.getFormatData(transferencia.getData()));
        holder.valor.setText(transferencia.getValor().toString());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
