package com.joseilton.mobilecontroller.categoria;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joseilton.mobilecontroller.R;
import com.joseilton.mobilecontroller.app.RecyclerTouchListener;
import com.joseilton.mobilecontroller.util.MensagemUtil;

import java.sql.SQLException;
import java.util.List;

public class CategoriaFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private List<Categoria> mList;
    private CategoriaService mService;
    private RecyclerView recyclerView;
    private static final String TAG = "CategoriasFragment.";
    private CategoriaAdapter mAdapter;
    protected int sectionNumber = 0;

    public CategoriaFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CategoriaFragment newInstance(int sectionNumber) {
        CategoriaFragment fragment = new CategoriaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.categorias_fragment, container, false);
        sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.categorias_recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                recyclerViewOnClickAction(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                recyclerViewOnLongClickAction(position);
            }
        }));

        recarregarDados();

        return rootView;
    }

    public void recyclerViewOnClickAction(int position) {
        Intent intent = new Intent(getActivity(), DetalhesCategoriaActivity.class);
        intent.putExtra("categoria", mList.get(position));
        startActivity(intent);
    }

    public void recyclerViewOnLongClickAction(int position) {
        //TODO implement action
    }

    @Override
    public void onResume() {
        super.onResume();
        recarregarDados();
    }

    private void recarregarDados() {
        buscarCategorias(sectionNumber);
        mAdapter = new CategoriaAdapter(mList);
        recyclerView.setAdapter(mAdapter);
    }

    @SuppressLint("LongLogTag")
    private void buscarCategorias(final int sectionNumber) {
        try {
            mService = new CategoriaService(getActivity());
            if (sectionNumber == 1) {
                mList = mService.getCategoriasDebito();
            } else if (sectionNumber == 2) {
                mList = mService.getCategoriasCredito();
            }

        } catch (SQLException e) {
            Log.e(TAG + "buscarCategorias()", "Erro: " + e.getMessage());
            e.printStackTrace();
            MensagemUtil.getMensagem(getActivity().getApplicationContext(), e);
        }
    }
}
