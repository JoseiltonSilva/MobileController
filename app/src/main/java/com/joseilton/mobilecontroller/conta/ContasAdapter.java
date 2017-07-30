package com.joseilton.mobilecontroller.conta;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.joseilton.mobilecontroller.R;
import com.joseilton.mobilecontroller.app.Formatador;
import com.joseilton.mobilecontroller.app.Periodo;
import com.joseilton.mobilecontroller.transacao.Transacao;
import com.joseilton.mobilecontroller.transacao.TransacaoService;
import com.joseilton.mobilecontroller.transacao.TransacaoTipo;
import com.joseilton.mobilecontroller.util.ColorUtil;
import com.joseilton.mobilecontroller.util.DateUtil;
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
        public LineChart lineChart;
        public BarChart barChart;

        public MyViewHolder(View view) {
            super(view);
            descricao = (TextView) view.findViewById(R.id.descricao_textView);
            tipo = (TextView) view.findViewById(R.id.tipo_textView);
            saldo = (TextView) view.findViewById(R.id.saldo_atual_textView);
            lineChart = (LineChart) view.findViewById(R.id.lineChart);
            barChart = (BarChart) view.findViewById(R.id.barChart);
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
        holder.saldo.setText(Formatador.formatarValorMonetario(conta.getSaldo()));

        if(conta.getSaldo().compareTo(new BigDecimal("0.00")) >= 0) {
            holder.saldo.setTextColor(ColorUtil.DARK_GREEN);
        } else {
            holder.saldo.setTextColor(ColorUtil.DARK_RED);
        }

        exibirLineChart(holder.lineChart, conta);
        exibirBarChart(holder.barChart, conta);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void exibirLineChart(LineChart chart, Conta conta) {

        TransacaoService transacaoService = new TransacaoService(mContext);
        List<Transacao> transacaoList = new ArrayList<>();
        Periodo periodo = PeriodoUtil.thisMonth(new Date());

        try {
            transacaoList = transacaoService.getTransacoesPeriodoConta(periodo, conta, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Entradas de dados referente ao saldo previsto do período
        BigDecimal saldoPrevisto = calcularSaldoInicial(transacaoList, conta);
        List<Entry> entriesSaldoPrevisto = new ArrayList<Entry>();

        entriesSaldoPrevisto.add(new Entry(periodo.getBegin().getDate(), Float.parseFloat(String.valueOf(saldoPrevisto))));
        for(Transacao t : transacaoList) {
            if(t.getTipo() == TransacaoTipo.Despesa) {
                saldoPrevisto = saldoPrevisto.subtract(t.getValor());
            } else {
                saldoPrevisto = saldoPrevisto.add(t.getValor());
            }
            entriesSaldoPrevisto.add(new Entry(t.getVencimento().getDate(), Float.parseFloat(String.valueOf(saldoPrevisto))));
        }
        entriesSaldoPrevisto.add(new Entry(periodo.getEnd().getDate(), Float.parseFloat(String.valueOf(saldoPrevisto))));

        LineDataSet dataSetPrevisto = new LineDataSet(entriesSaldoPrevisto, "Saldo Previsto");
        dataSetPrevisto.setColor(ColorUtil.RED);

        // Entradas de dados referente ao saldo realizado do período
        BigDecimal saldoRealizado = calcularSaldoInicial(transacaoList, conta);
        List<Entry> entriesSaldoRealizado = new ArrayList<Entry>();

        entriesSaldoRealizado.add(new Entry(periodo.getBegin().getDate(), Float.parseFloat(String.valueOf(saldoRealizado))));
        for(Transacao t : transacaoList) {
            if(t.isEfetivado() && t.getTipo() == TransacaoTipo.Despesa) {
                saldoRealizado = saldoRealizado.subtract(t.getValor());
                entriesSaldoRealizado.add(new Entry(t.getVencimento().getDate(), Float.parseFloat(String.valueOf(saldoRealizado))));
            }
            if(t.isEfetivado() && t.getTipo() == TransacaoTipo.Receita) {
                saldoRealizado = saldoRealizado.add(t.getValor());
                entriesSaldoRealizado.add(new Entry(t.getVencimento().getDate(), Float.parseFloat(String.valueOf(saldoRealizado))));
            }
        }
        entriesSaldoRealizado.add(new Entry(new Date().getDate(), Float.parseFloat(String.valueOf(saldoRealizado))));

        LineDataSet dataSetRealizado = new LineDataSet(entriesSaldoRealizado, "Saldo Realizado");
        dataSetRealizado.setColor(ColorUtil.GREEN);

        Description description = new Description();
        description.setText("Projeção de Saldo - " + DateUtil.getMonthAndYear(new Date()));

        LineData lineData = new LineData(dataSetPrevisto, dataSetRealizado);
        chart.setData(lineData);
        chart.setDescription(description);
        chart.invalidate(); // refresh

    }

    private void exibirBarChart(BarChart chart, Conta conta) {

        TransacaoService transacaoService = new TransacaoService(mContext);

        List<Transacao> transacaoList = new ArrayList<>();

        try {
            transacaoList = transacaoService.getTransacoesPeriodoConta(PeriodoUtil.thisMonth(new Date()),conta, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        BigDecimal despesaTotal = new BigDecimal("0.00");
        BigDecimal despesaPaga = new BigDecimal("0.00");
        BigDecimal receitaTotal = new BigDecimal("0.00");
        BigDecimal receitaRecebida = new BigDecimal("0.00");

        for(Transacao t : transacaoList) {
            if(t.getTipo() == TransacaoTipo.Despesa) {
                despesaTotal = despesaTotal.add(t.getValor());
                if(t.isEfetivado()) despesaPaga = despesaPaga.add(t.getValor());
            } else {
                receitaTotal = receitaTotal.add(t.getValor());
                if(t.isEfetivado()) receitaRecebida = receitaRecebida.add(t.getValor());
            }
        }

        List<BarEntry> receitasEntry = new ArrayList<>();
        receitasEntry.add(new BarEntry(0f, Float.parseFloat(String.valueOf(receitaTotal))));
        receitasEntry.add(new BarEntry(0f, Float.parseFloat(String.valueOf(receitaRecebida))));
        List<BarEntry> despesasEntry = new ArrayList<>();
        despesasEntry.add(new BarEntry(1f, Float.parseFloat(String.valueOf(despesaTotal))));
        despesasEntry.add(new BarEntry(1f, Float.parseFloat(String.valueOf(despesaPaga))));

        BarDataSet receitasSet = new BarDataSet(receitasEntry, "Receitas Total x Recebidas");
        receitasSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarDataSet despesasSet = new BarDataSet(despesasEntry, "Despesas Total x Pagas");
        despesasSet.setColors(ColorTemplate.PASTEL_COLORS);

        Description description = new Description();
        description.setText("Receita x Despesa - " + DateUtil.getMonthAndYear(new Date()));

        BarData data = new BarData(receitasSet, despesasSet);
        data.setBarWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.setDescription(description);
        chart.getXAxis().setEnabled(false);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate();

    }

    private BigDecimal calcularSaldoInicial(List<Transacao> transacoes, Conta conta) {
        BigDecimal saldoInicial = conta.getSaldo();

        for(Transacao t : transacoes) {
            if(t.isEfetivado() && t.getTipo() == TransacaoTipo.Despesa) saldoInicial = saldoInicial.add(t.getValor());
            if(t.isEfetivado() && t.getTipo() == TransacaoTipo.Receita) saldoInicial = saldoInicial.subtract(t.getValor());
        }

        return saldoInicial;
    }

}
