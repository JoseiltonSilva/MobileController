package com.joseilton.mobilecontroller.conta;

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

import com.joseilton.mobilecontroller.R;
import com.joseilton.mobilecontroller.app.RecyclerTouchListener;
import com.joseilton.mobilecontroller.transferencia.TransferenciasActivity;
import com.joseilton.mobilecontroller.util.MensagemUtil;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class ContasFragment extends Fragment implements View.OnClickListener {
    private final static String LOG = "ContasFragment.";

    private RecyclerView recyclerView;
    private List<Conta> contas;
    private Conta conta;
    private ContaService mService;
    private ContasAdapter mAdapter;

    private Date filtroData;

    @SuppressLint("LongLogTag")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contas_fragment, container, false);

        filtroData = new Date();
        setHasOptionsMenu(true);
        setupToolbar();

        recyclerView = (RecyclerView) v.findViewById(R.id.contas_recyclerView);
        mAdapter = new ContasAdapter(getActivity(), contas);
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
                        conta = contas.get(position);
                        Intent intent = new Intent(getActivity(), DetalhesContaActivity.class);
                        intent.putExtra("conta", conta);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        onClick(view, position);
                    }
                }));

        try {
            mService = new ContaService(getActivity());
            contas = mService.getContas();
        } catch (SQLException e) {
            Log.e(LOG + "onCreateView", "Erro: " + e.getMessage());
            e.printStackTrace();
            MensagemUtil.getMensagem(getActivity().getApplicationContext(), e);
        }

        return v;
    }

    private void setupToolbar() {
        Toolbar mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.title_contas));
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            mService = new ContaService(getActivity());
            contas = mService.getContas();
            release();
        } catch (SQLException e) {
            Log.e(LOG + "onResume", "Erro: " + e.getMessage());
            e.printStackTrace();
            MensagemUtil.getMensagem(getActivity().getApplicationContext(), e);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_contas, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_novo) novo();

        if (id == R.id.action_transferencias) startActivity(new Intent(getActivity(), TransferenciasActivity.class));

        return super.onOptionsItemSelected(item);
    }

    private void release() {
        mAdapter = new ContasAdapter(getActivity(), contas);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) novo();
    }

    private void novo() {
        conta = new Conta();
        Intent intent = new Intent(getActivity(), DetalhesContaActivity.class);
        intent.putExtra("conta", conta);
        startActivity(intent);
    }
}
