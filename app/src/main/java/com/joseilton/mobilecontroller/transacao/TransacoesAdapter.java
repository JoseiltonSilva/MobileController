package com.joseilton.mobilecontroller.transacao;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.joseilton.mobilecontroller.R;
import com.joseilton.mobilecontroller.app.Formatador;
import com.joseilton.mobilecontroller.util.ColorUtil;
import com.joseilton.mobilecontroller.util.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by Joseilton on 16/04/2017.
 */

public class TransacoesAdapter extends RecyclerView.Adapter<TransacoesAdapter.MyViewHolder> {
    private List<Transacao> myItens;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView descricao, categoria, tipo, conta, valor, vencimento;
        public ImageView status, color;

        public MyViewHolder(View view) {
            super(view);
            vencimento = (TextView) view.findViewById(R.id.vencimento_textView) ;
            descricao = (TextView) view.findViewById(R.id.descricao_textView);
            categoria = (TextView) view.findViewById(R.id.categoria_textView);
            tipo = (TextView) view.findViewById(R.id.tipo_textView);
            conta = (TextView) view.findViewById(R.id.conta_textView);
            valor = (TextView) view.findViewById(R.id.valor_textView);
            status = (ImageView) view.findViewById(R.id.status_imageView);
            color = (ImageView) view.findViewById(R.id.color_imageView);
        }
    }

    public TransacoesAdapter(List<Transacao> itens) {
        this.myItens = itens;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transacao_card, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

            Transacao transacao = myItens.get(position);

            holder.descricao.setText(transacao.getDescricao());
            holder.categoria.setText(transacao.getCategoria().getDescricao());
            holder.tipo.setText(transacao.getTipo().toString());
            holder.conta.setText(transacao.getConta().getDescricao());
            holder.valor.setText(Formatador.formatarValorMonetario(transacao.getValor()));
            holder.vencimento.setText(DateUtil.getDay(transacao.getVencimento()) + ", " + transacao.getVencimento().getDate() + " de " + DateUtil.getMonth(transacao.getVencimento()));

            if (transacao.getStatus() != null) {
                holder.status.setImageResource(transacao.getStatus());
            }

            int cor;

            if (transacao.getTipo() == TransacaoTipo.Despesa) {
                cor = ColorUtil.DARK_RED;
            } else {
                cor = ColorUtil.DARK_GREEN;
            }

            holder.color.setBackgroundColor(cor);
            holder.status.setColorFilter(cor);

    }



    @Override
    public int getItemCount() {
        return myItens.size();
    }


}