package com.joseilton.mobilecontroller.conta;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.joseilton.mobilecontroller.R;
import com.joseilton.mobilecontroller.transacao.Transacao;
import com.joseilton.mobilecontroller.transacao.TransacaoService;
import com.joseilton.mobilecontroller.transacao.TransacaoTipo;
import com.joseilton.mobilecontroller.util.ColorUtil;
import com.joseilton.mobilecontroller.util.PeriodoUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Joseilton on 16/04/2017.
 */

public class ContasAdapter extends RecyclerView.Adapter<ContasAdapter.MyViewHolder> {
    private List<Conta> list;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView descricao, tipo, saldo;
        public GraphView projecaoSaldoGraph, receitaDespesaGraph;

        public MyViewHolder(View view) {
            super(view);
            descricao = (TextView) view.findViewById(R.id.descricao_textView);
            tipo = (TextView) view.findViewById(R.id.tipo_textView);
            saldo = (TextView) view.findViewById(R.id.saldo_atual_textView);
            projecaoSaldoGraph = (GraphView) view.findViewById(R.id.projecao_saldo_graph);
            receitaDespesaGraph = (GraphView) view.findViewById(R.id.receita_despesa_graph);
        }
    }

    public ContasAdapter(Context context, List<Conta> list) {
        this.list = list;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.conta_card, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Conta conta = list.get(position);
        holder.descricao.setText(conta.getDescricao());
        holder.tipo.setText(conta.getTipo().getTipo());
        holder.saldo.setText(conta.getSaldo().toString());
        startReceitaDespesaGraph(holder.receitaDespesaGraph, conta);
        startProjecaoSaldoGraph(holder.projecaoSaldoGraph, conta);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void startReceitaDespesaGraph(GraphView graph, Conta conta) {
        TransacaoService transacaoService = new TransacaoService(mContext);

        List<Transacao> transacaoList = new ArrayList<>();

        try {
            transacaoList = transacaoService.getTransacoesPeriodoConta(PeriodoUtil.thisMonth(new Date()),conta, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        BigDecimal despesaTotal = new BigDecimal("0.00");
        BigDecimal receitaTotal = new BigDecimal("0.00");

        for(Transacao t : transacaoList) {
            if(t.getTipo() == TransacaoTipo.Despesa) {
                despesaTotal = despesaTotal.add(t.getValor());
            } else {
                receitaTotal = receitaTotal.add(t.getValor());
            }
        }

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, Double.parseDouble(String.valueOf(receitaTotal))),
                new DataPoint(3, Double.parseDouble(String.valueOf(despesaTotal))),

        });

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"", "Receita", "", "Despesa", ""});

        graph.setTitle("Receitas x Despesas");
        graph.setTitleTextSize(40);
        graph.getViewport().setMaxX(4);
        graph.getViewport().setMinX(0);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(ColorUtil.BLACK);
        series.setValueDependentColor(data -> {
            int color = 0;
            if(data.getX() == 1) {
                color = ColorUtil.DARK_GREEN;
            } else if(data.getX() == 3) {
                color =  ColorUtil.DARK_RED;
            }
            return color;
        });
        series.setSpacing(20);
        series.setAnimated(true);
        graph.addSeries(series);
    }

    private void startProjecaoSaldoGraph(GraphView graph, Conta conta) {
        TransacaoService transacaoService = new TransacaoService(mContext);

        List<Transacao> transacaoList = new ArrayList<>();

        try {
            transacaoList = transacaoService.getTransacoesPeriodoConta(PeriodoUtil.thisMonth(new Date()),conta, false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        BigDecimal saldoAtual = conta.getSaldo();

        DataPoint[] points;
        int index = 0;

        if(transacaoList.size() > 1) {

            points = new DataPoint[transacaoList.size()];

            for(Transacao t : transacaoList) {

                if(t.getTipo() == TransacaoTipo.Despesa) {
                    saldoAtual = saldoAtual.subtract(t.getValor());
                } else {
                    saldoAtual = saldoAtual.add(t.getValor());
                }
                points[index] = new DataPoint(t.getVencimento().getDate(), Double.parseDouble(String.valueOf(saldoAtual)));
                index++;

            }
        } else if(transacaoList.size() == 1) {
            points = new DataPoint[3];
            for(Transacao t : transacaoList) {
                if(t.getTipo() == TransacaoTipo.Despesa) {
                    saldoAtual = saldoAtual.subtract(t.getValor());
                } else {
                    saldoAtual = saldoAtual.add(t.getValor());
                }
            }

            if(transacaoList.get(0).getVencimento().getDate() < new Date().getDate()) {
                points[0] = new DataPoint(transacaoList.get(0).getVencimento().getDate(), Double.parseDouble(String.valueOf(saldoAtual)));
                points[1] = new DataPoint(new Date().getDate(), Double.parseDouble(String.valueOf(saldoAtual)));
                points[2] = new DataPoint(new Date().getDate() + 1, Double.parseDouble(String.valueOf(saldoAtual)));
            } else if(transacaoList.get(0).getVencimento().getDate() == new Date().getDate()) {
                points[0] = new DataPoint(new Date().getDate() - 1, Double.parseDouble(String.valueOf(conta.getSaldo())));
                points[1] = new DataPoint(transacaoList.get(0).getVencimento().getDate(), Double.parseDouble(String.valueOf(saldoAtual)));
                points[2] = new DataPoint(new Date().getDate() + 1, Double.parseDouble(String.valueOf(saldoAtual)));
            } else if(transacaoList.get(0).getVencimento().getDate() > new Date().getDate()){
                points[0] = new DataPoint(new Date().getDate() - 1, Double.parseDouble(String.valueOf(conta.getSaldo())));
                points[1] = new DataPoint(new Date().getDate(), Double.parseDouble(String.valueOf(conta.getSaldo())));
                points[2] = new DataPoint(transacaoList.get(0).getVencimento().getDate(), Double.parseDouble(String.valueOf(saldoAtual)));
            }

        } else {
            points = new DataPoint[3];
            points[0] = new DataPoint(new Date().getDate() - 1, Double.parseDouble(String.valueOf(saldoAtual)));
            points[1] = new DataPoint(new Date().getDate(), Double.parseDouble(String.valueOf(saldoAtual)));
            points[2] = new DataPoint(new Date().getDate() + 1, Double.parseDouble(String.valueOf(saldoAtual)));
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);



        graph.setTitle("Projeção do Saldo");
        graph.setTitleTextSize(40);

        // set date label formatter
        //graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(mContext));
        //graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space

        // set manual x bounds to have nice steps

        /*
        if(transacaoList.size() > 0) {
            graph.getViewport().setMinX(transacaoList.get(0).getVencimento().getDate());
            graph.getViewport().setMaxX(transacaoList.get(transacaoList.size() - 1).getVencimento().getDate());


        } else {
            graph.getViewport().setMinX(new Date().getDate() -1);
            graph.getViewport().setMaxX(new Date().getDate() + 1);

        }

        graph.getViewport().setXAxisBoundsManual(true);
        */

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);

        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setAnimated(true);
        graph.addSeries(series);




    }

}
