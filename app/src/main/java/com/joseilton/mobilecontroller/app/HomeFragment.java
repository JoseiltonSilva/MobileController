package com.joseilton.mobilecontroller.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.joseilton.mobilecontroller.categoria.Categoria;
import com.joseilton.mobilecontroller.categoria.CategoriasActivity;
import com.joseilton.mobilecontroller.R;
import com.joseilton.mobilecontroller.categoria.DetalhesCategoriaActivity;
import com.joseilton.mobilecontroller.conta.Conta;
import com.joseilton.mobilecontroller.conta.DetalhesContaActivity;
import com.joseilton.mobilecontroller.transacao.DetalhesTransacaoActivity;
import com.joseilton.mobilecontroller.transacao.Transacao;
import com.joseilton.mobilecontroller.transferencia.DetalhesTransferenciaActivity;
import com.joseilton.mobilecontroller.transferencia.Transferencia;


public class HomeFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_fragment, container, false);


        setHasOptionsMenu(true);
        setupToolbar();


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



}
