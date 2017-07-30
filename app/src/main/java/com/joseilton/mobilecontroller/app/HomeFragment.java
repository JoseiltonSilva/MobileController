package com.joseilton.mobilecontroller.app;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;

import com.joseilton.mobilecontroller.categoria.Categoria;
import com.joseilton.mobilecontroller.categoria.CategoriasActivity;
import com.joseilton.mobilecontroller.R;
import com.joseilton.mobilecontroller.categoria.DetalhesCategoriaActivity;
import com.joseilton.mobilecontroller.conta.Conta;
import com.joseilton.mobilecontroller.conta.ContaService;
import com.joseilton.mobilecontroller.conta.ContasAdapter;
import com.joseilton.mobilecontroller.conta.DetalhesContaActivity;
import com.joseilton.mobilecontroller.transacao.DetalhesTransacaoActivity;
import com.joseilton.mobilecontroller.transacao.Transacao;
import com.joseilton.mobilecontroller.transferencia.DetalhesTransferenciaActivity;
import com.joseilton.mobilecontroller.transferencia.Transferencia;
import com.joseilton.mobilecontroller.util.ColorUtil;
import com.joseilton.mobilecontroller.util.MensagemUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment.";

    private RecyclerView disponibilidades_recyclerView;
    private List<Conta> contas;
    private ContaService contaService;
    private DisponibilidadeAdapter disponibilidadeAdapter;

    private TextView disponibilidadeTotal;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_fragment, container, false);

        setHasOptionsMenu(true);
        setupToolbar();

        disponibilidadeTotal = (TextView) v.findViewById(R.id.totalDisponibilidade_textView);

        disponibilidades_recyclerView = (RecyclerView) v.findViewById(R.id.disponibilidade_recyclerView);
        disponibilidadeAdapter = new DisponibilidadeAdapter(contas);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        disponibilidades_recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        disponibilidades_recyclerView.setAdapter(disponibilidadeAdapter);
        disponibilidades_recyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(getActivity()
                        .getApplicationContext(), disponibilidades_recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), DetalhesContaActivity.class);
                        intent.putExtra("conta", contas.get(position));
                        startActivity(intent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        onClick(view, position);
                    }
                }));

        buscarContas();
        calcularDisponibilidadeTotal();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_nova_categoria) {
            Intent intent = new Intent(getActivity(), DetalhesCategoriaActivity.class);
            intent.putExtra("categoria", new Categoria());
            startActivity(intent);
        }
        if(id == R.id.action_nova_conta) {
            Intent intent = new Intent(getActivity(), DetalhesContaActivity.class);
            intent.putExtra("conta", new Conta());
            startActivity(intent);
        }
        if(id == R.id.action_nova_transacao) {
            Intent intent = new Intent(getActivity(), DetalhesTransacaoActivity.class);
            intent.putExtra("transacao", new Transacao());
            startActivity(intent);
        }
        if(id == R.id.action_nova_transferencia) {
            Intent intent = new Intent(getActivity(), DetalhesTransferenciaActivity.class);
            intent.putExtra("transferencia", new Transferencia());
            startActivity(intent);
        }


        if (id == R.id.action_categorias) {
            startActivity(new Intent(getActivity(), CategoriasActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        Toolbar mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.title_home));
    }


    @SuppressLint("LongLogTag")
    private void buscarContas() {
        contaService = new ContaService(getActivity());
        try {
            contas = contaService.getContas();
        } catch (SQLException e) {
            Log.e(TAG + "buscarContas()", "Erro: " + e.getMessage());
            e.printStackTrace();
            MensagemUtil.getMensagem(getActivity().getApplicationContext(), e);
        }
    }

    private void recarregarDados() {
        disponibilidadeAdapter = new DisponibilidadeAdapter( contas);
        disponibilidades_recyclerView.setAdapter(disponibilidadeAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        buscarContas();
        recarregarDados();
        calcularDisponibilidadeTotal();
    }

    private void calcularDisponibilidadeTotal() {
        BigDecimal total = new BigDecimal("0.00");

        for(Conta c : contas) {
            total = total.add(c.getSaldo());
        }

        disponibilidadeTotal.setText(Formatador.formatarValorMonetario(total));


        if(total.compareTo(new BigDecimal("0.00")) >= 0) {
            disponibilidadeTotal.setTextColor(ColorUtil.DARK_GREEN);
        } else {
            disponibilidadeTotal.setTextColor(ColorUtil.DARK_RED);
        }
    }
}
