package com.joseilton.mobilecontroller.conta;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.joseilton.mobilecontroller.app.DatabaseHelper;
import com.joseilton.mobilecontroller.app.Periodo;
import com.joseilton.mobilecontroller.transacao.Transacao;
import com.joseilton.mobilecontroller.transacao.TransacaoTipo;
import com.joseilton.mobilecontroller.transacao.TransacaoService;
import com.joseilton.mobilecontroller.transferencia.TransferenciaService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContaService {
    private final static String LOG = "ContaService.";

    private DatabaseHelper helper;
    private ContaDao dao;
    private Context mContext;

    @SuppressLint("LongLogTag")
    public ContaService(Context context) {
        this.mContext = context;

        helper = new DatabaseHelper(mContext);
        try {
            dao = new ContaDao(helper.getConnectionSource());
        } catch (SQLException e) {
            Log.e(LOG + "ContaService()", e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressLint("LongLogTag")
    public List<Conta> getContas() throws SQLException {
        List<Conta> list = new ArrayList<>();
        try {
            list = dao.select();
        } catch (SQLException e) {
            Log.e(LOG + "getContas()", e.getMessage());
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        } finally {
            if (helper != null)
                helper.close();
        }

        return list;
    }

    @SuppressLint("LongLogTag")
    public void salvarConta(Conta conta) throws SQLException {
        if (conta.isValid()) {
            try {
                dao.insertItem(conta);
            } catch (SQLException e) {
                Log.e(LOG + "salvarConta()", e.getMessage());
                e.printStackTrace();
                throw new SQLException(e.getMessage());
            } finally {
                if (helper != null)
                    helper.close();
            }
        } else
            throw new IllegalArgumentException("Campos inválidos.");
    }

    @SuppressLint("LongLogTag")
    public void atualizarConta(Conta conta) throws SQLException {
        if (conta.isValid()) {
            try {
                dao.updateItem(conta);
            } catch (SQLException e) {
                Log.e(LOG + "AtualizarConta()", e.getMessage());
                e.printStackTrace();
                throw new SQLException(e.getMessage());
            } finally {
                if (helper != null)
                    helper.close();
            }
        } else
            throw new IllegalArgumentException("Campos inválidos.");
    }

    @SuppressLint("LongLogTag")
    public void excluirConta(Conta conta) throws SQLException {
        try {
            dao.deleteItem(conta);
        } catch (SQLException e) {
            Log.e(LOG + "excluirConta()", e.getMessage());
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        } finally {
            if (helper != null)
                helper.close();
        }
    }


    @SuppressLint("LongLogTag")
    public BigDecimal calcularSaldoPrevisto(Periodo periodo, Conta conta) {
        List<Transacao> list = new ArrayList<>();

        try {
            TransacaoService TransacaoService = new TransacaoService(mContext);
            list = TransacaoService.getTransacoesPeriodoConta(periodo, conta, false);

        } catch (SQLException e) {
            Log.e(LOG + "calcularSaldoPrevisto()", e.getMessage());
            e.printStackTrace();
        }

        BigDecimal entradas = new BigDecimal("0.00");
        BigDecimal saidas = new BigDecimal("0.00");

        for (Transacao t : list) {


            if (t.getTipo() == TransacaoTipo.Despesa) {
                saidas = saidas.add(t.getValor());
            } else {
                entradas = entradas.add(t.getValor());
            }
        }

        return conta.getSaldo().add(entradas).subtract(saidas);
    }

    public boolean validarExclusao(Conta conta) {
        boolean validate = false;

        TransferenciaService transferenciaService = new TransferenciaService(mContext);
        TransacaoService transacaoService = new TransacaoService(mContext);
        if (!transacaoService.contains(conta) && !transferenciaService.contains(conta))
            validate = true;
        else
            validate = false;

        return validate;
    }
}
