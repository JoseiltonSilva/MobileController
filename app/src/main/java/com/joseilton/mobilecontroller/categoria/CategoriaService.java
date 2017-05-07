package com.joseilton.mobilecontroller.categoria;

import android.content.Context;


import com.joseilton.mobilecontroller.app.DatabaseHelper;
import com.joseilton.mobilecontroller.transacao.TransacaoService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaService {
    private DatabaseHelper helper;
    private CategoriaDao dao;
    private Context mContext;

    public CategoriaService(Context context) {
        this.mContext = context;

        helper = new DatabaseHelper(mContext);
        try {
            dao = new CategoriaDao(helper.getConnectionSource());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Categoria> getCategorias() throws SQLException {
        List<Categoria> list = new ArrayList<>();
        try {
            list = dao.select();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        } finally {
            if (helper != null) helper.close();
        }

        return list;
    }

    public List<Categoria> getCategoriasDebito() throws SQLException {
        List<Categoria> list = new ArrayList<>();
        try {
            list = dao.selectByTipo(CategoriaTipo.Debito);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        } finally {
            if (helper != null) helper.close();
        }

        return list;
    }


    public List<Categoria> getCategoriasCredito() throws SQLException {
        List<Categoria> list = new ArrayList<>();
        try {
            list = dao.selectByTipo(CategoriaTipo.Credito);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        } finally {
            if (helper != null) helper.close();
        }

        return list;
    }

    public void salvarCategoria(Categoria categoria) throws SQLException {
        if (categoria.isValid()) {
            try {
                dao.insertItem(categoria);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException(e.getMessage());
            } finally {
                if (helper != null) helper.close();
            }
        } else
            throw new IllegalArgumentException("Campos inválidos.");
    }

    public void atualizarCategoria(Categoria categoria) throws SQLException {
        if (categoria.isValid()) {
            try {
                dao.updateItem(categoria);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException(e.getMessage());
            } finally {
                if (helper != null) helper.close();
            }
        } else
            throw new IllegalArgumentException("Campos inválidos.");
    }

    public void excluirCategoria(Categoria categoria) throws SQLException {
        try {
            dao.deleteItem(categoria);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        } finally {
            if (helper != null) helper.close();
        }
    }

    public boolean validarExclusao(Categoria categoria) {
        boolean validate = false;

        TransacaoService transacaoService = new TransacaoService(mContext);
        if (!transacaoService.contains(categoria)) validate = true;

        return validate;
    }
}
