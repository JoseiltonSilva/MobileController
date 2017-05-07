package com.joseilton.mobilecontroller.transferencia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.joseilton.mobilecontroller.R;
import com.joseilton.mobilecontroller.app.RecyclerTouchListener;
import com.joseilton.mobilecontroller.util.MensagemUtil;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class TransferenciasActivity extends AppCompatActivity implements View.OnClickListener{

    private final static String LOG = "TransferenciasActivity";

    private RecyclerView recyclerView;
    private List<Transferencia> transferencias;
    private FloatingActionButton fab;
    private Transferencia transferencia;
    private TransferenciaService mService;
    private TransferenciasAdapter mAdapter;
    private Toolbar mToolbar;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferencias);

        setupToolbar();
        setupFab();

        setupRecyclerView();

        try {
            mService = new TransferenciaService(this);
            transferencias = mService.getTransferencias();
        } catch (SQLException e) {
            Log.e(LOG + "onCreate()", "Erro: " + e.getMessage());
            e.printStackTrace();
            MensagemUtil.getMensagem(getApplicationContext(), e);
        }


    }

    private void setupToolbar() {
        if(mToolbar == null) mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.transferencias_recyclerView);
        mAdapter = new TransferenciasAdapter(transferencias);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        transferencia = transferencias.get(position);
                        Intent intent = new Intent(TransferenciasActivity.this, DetalhesTransferenciaActivity.class);
                        intent.putExtra("transferencia", transferencia);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        onClick(view, position);
                    }
                }));
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            mService = new TransferenciaService(this);
            transferencias = mService.getTransferencias();
            release();
        } catch (SQLException e) {
            MensagemUtil.getMensagem(getApplicationContext(), e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_transferencias, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) onBackPressed();

        if (id == R.id.action_novo) novo();

        if (id == R.id.action_voltar) onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    private void release() {
        mAdapter = new TransferenciasAdapter(transferencias);
        recyclerView.setAdapter(mAdapter);
    }

    private void setupFab() {
        if (fab == null) fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setEnabled(true);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) novo();
    }

    private void novo() {
        transferencia = new Transferencia();
        transferencia.setData(new Date());
        Intent intent = new Intent(this, DetalhesTransferenciaActivity.class);
        intent.putExtra("transferencia", transferencia);
        startActivity(intent);
    }
}
