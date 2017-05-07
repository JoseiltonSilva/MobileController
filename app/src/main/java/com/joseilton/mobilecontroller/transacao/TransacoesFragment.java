package com.joseilton.mobilecontroller.transacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.joseilton.mobilecontroller.R;
import com.joseilton.mobilecontroller.app.RecyclerTouchListener;
import com.joseilton.mobilecontroller.categoria.CategoriasActivity;
import com.joseilton.mobilecontroller.util.ColorUtil;
import com.joseilton.mobilecontroller.util.DateUtil;
import com.joseilton.mobilecontroller.util.MensagemUtil;
import com.joseilton.mobilecontroller.util.PeriodoUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;


public class TransacoesFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = "TransacoesFragment.";

    private RecyclerView recyclerView;
    private List<Transacao> transacoes;
    private Toolbar mToolbar;
    private Transacao transacao;
    private TransacaoService mService;
    private TransacoesAdapter mAdapter;
    private Date dataFiltro;
    private ImageButton previousImageButton, nextImageButton;
    private TextView filtroTextView;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.transacoes_fragment, container, false);

        setHasOptionsMenu(true);

        inicializarToolbar();
        inicializarViews();

        dataFiltro = new Date();
        filtroTextView.setText(DateUtil.getMonthAndYear(dataFiltro));
        filtroTextView.setOnClickListener(this);

        buscarTransacoes();
        inicializarRecyclerView();

        return mView;
    }

    private void inicializarToolbar() {
        if(mToolbar == null)
            mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        mToolbar.setTitle(getString(R.string.title_transacoes));
    }

    private void inicializarRecyclerView() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.transacoes_recyclerView);
        mAdapter = new TransacoesAdapter(transacoes);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(getActivity()
                        .getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        transacao = transacoes.get(position);
                        Intent intent = new Intent(getActivity(), DetalhesTransacaoActivity.class);
                        intent.putExtra("transacao", transacao);
                        startActivity(intent);

                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        onClick(view, position);
                    }
                })
        );
    }

    private void inicializarViews() {
        previousImageButton = (ImageButton) mView.findViewById(R.id.previous_imageButton);
        previousImageButton.setOnClickListener(this);
        nextImageButton = (ImageButton) mView.findViewById(R.id.next_imageButton);
        nextImageButton.setOnClickListener(this);
        filtroTextView = (TextView) mView.findViewById(R.id.filtro_textView);
    }

    public void filtrarMesAnterior() {
        Date d = dataFiltro;
        dataFiltro = DateUtil.addMes(d, -1);
        filtroTextView.setText(DateUtil.getMonthAndYear(dataFiltro));
        buscarTransacoes();
        recarregarDados();
    }

    public void filtrarProximoMes() {
        Date d = dataFiltro;
        dataFiltro = DateUtil.addMes(d, 1);
        filtroTextView.setText(DateUtil.getMonthAndYear(dataFiltro));
        buscarTransacoes();
        recarregarDados();
    }

    @SuppressLint("LongLogTag")
    private void buscarTransacoes() {
        try {
            mService = new TransacaoService(getActivity());
            transacoes = mService.getTransacoesPeriodo(PeriodoUtil.thisMonth(dataFiltro), null);
        } catch (SQLException e) {
            Log.e(TAG +
                    "buscarTransacoes()", "Erro: " + e.getMessage());
            e.printStackTrace();
            MensagemUtil.getMensagem(getActivity().getApplicationContext(), e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        buscarTransacoes();
        recarregarDados();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_transacoes, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_novo) {
            transacao = new Transacao();
            Intent intent = new Intent(getActivity(), DetalhesTransacaoActivity.class);
            intent.putExtra("transacao", transacao);
            startActivity(intent);

        } else if (id == R.id.action_categorias) {
            startActivity(new Intent(getActivity(), CategoriasActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void recarregarDados() {
        mAdapter = new TransacoesAdapter(transacoes);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
       if (view.getId() == R.id.previous_imageButton) {
            filtrarMesAnterior();
        } else if (view.getId() == R.id.next_imageButton) {
            filtrarProximoMes();
        } else if (view.getId() == R.id.filtro_textView) {
            inicializarGraphLayout();
        }
    }

    private void inicializarGraphLayout() {

        View view;

        BigDecimal despesaTotal = new BigDecimal("0.00");
        BigDecimal receitaTotal = new BigDecimal("0.00");

        for(Transacao t : transacoes) {
            if(t.getTipo() == TransacaoTipo.Despesa) {
                despesaTotal = despesaTotal.add(t.getValor());
            } else {
                receitaTotal = receitaTotal.add(t.getValor());
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.transacao_graph, null);
        builder.setView(view)
                .setNegativeButton(getString(R.string.label_action_sair), (dialog, which) -> {

                }
        );

        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, Double.parseDouble(String.valueOf(receitaTotal))),
                new DataPoint(3, Double.parseDouble(String.valueOf(despesaTotal))),

        });

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"", getString(R.string.label_transacao_receita), "", getString(R.string.label_transacao_despesa), ""});

        graph.setTitle(getString(R.string.label_receita_x_despesa));
        graph.setTitleTextSize(60);
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

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
