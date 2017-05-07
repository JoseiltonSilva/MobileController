package com.joseilton.mobilecontroller.transferencia;

import android.content.Context;
import android.util.Log;


import com.j256.ormlite.misc.TransactionManager;
import com.joseilton.mobilecontroller.conta.ContaDao;
import com.joseilton.mobilecontroller.app.DatabaseHelper;
import com.joseilton.mobilecontroller.conta.Conta;
import com.joseilton.mobilecontroller.app.Periodo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class TransferenciaService {
    private final static String LOG = "TransferenciaService.";

    private DatabaseHelper mHelper;
    private TransferenciaDao mDao;
    private Context mContext;

    public TransferenciaService(Context context) {
        this.mContext = context;
        mHelper = new DatabaseHelper(mContext);
        try {
            mDao = new TransferenciaDao(mHelper.getConnectionSource());
        } catch (SQLException e) {
            Log.e(LOG + "", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Verifica se existe alguma transferencia que contenha a conta informada
     *
     * @param conta
     * @return
     */
    public boolean contains(Conta conta) {
        List<Transferencia> list = new ArrayList<>();
        try {
            list = mDao.select(conta);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (mHelper != null)
                mHelper.close();
        }

        if (list.size() > 0)
            return true;
        else
            return false;

    }

    /**
     * Retorna uma lista com todas as transferencias
     *
     * @return
     */
    public List<Transferencia> getTransferencias() throws SQLException {
        List<Transferencia> list = new ArrayList<>();
        try {
            list = mDao.select();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        } finally {
            if (mHelper != null)
                mHelper.close();
        }

        return list;
    }

    /**
     * Retorna uma lista com todas as transferencias do periodo informado
     *
     * @param periodo
     * @return
     */
    public List<Transferencia> getTransferenciasPeriodo(Periodo periodo) throws SQLException {
        List<Transferencia> list = new ArrayList<>();
        try {
            list = mDao.select(periodo.getBegin(), periodo.getEnd());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        } finally {
            if (mHelper != null)
                mHelper.close();
        }

        return list;
    }

    /**
     * Retorna uma lista com todas as transferencias que contenham a conta informada
     *
     * @param conta
     * @return
     */
    public List<Transferencia> getTransferenciasConta(Conta conta) throws SQLException {
        List<Transferencia> list = new ArrayList<>();
        try {
            list = mDao.select(conta);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        } finally {
            if (mHelper != null)
                mHelper.close();
        }

        return list;
    }

    /**
     * Retorna uma lista com todas as transferencias do periodo informado e que contenham a conta informada
     *
     * @param periodo
     * @return
     */
    public List<Transferencia> getTransferenciasPeriodoConta(Periodo periodo, Conta conta) throws SQLException {
        List<Transferencia> list = new ArrayList<>();
        try {
            list = mDao.select(periodo.getBegin(), periodo.getEnd(), conta);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        } finally {
            if (mHelper != null)
                mHelper.close();
        }

        return list;
    }

    /**
     * Insere uma nova transferecia no db
     *
     * @param transferencia
     */
    public void salvarTransferencia(final Transferencia transferencia) throws SQLException {
        if (transferencia.isValid()) {
            try {
                TransactionManager.callInTransaction(mHelper.getConnectionSource(),
                        new Callable<Void>() {
                            public Void call() throws Exception {

                                ContaDao contaDao = new ContaDao(mHelper.getConnectionSource());
                                contaDao.updateItem(transferencia.getContaDestino());
                                contaDao.updateItem(transferencia.getContaOrigem());
                                mDao.insertItem(transferencia);

                                return null;
                            }
                        });
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException(e.getMessage());
            } finally {
                if (mHelper != null)
                    mHelper.close();
            }


        } else
            throw new IllegalArgumentException("Campos inválidos.");
    }


    /**
     * Exclui a transferecia do db
     *
     * @param transferencia
     */
    public void excluirTransferencia(final Transferencia transferencia) throws SQLException {
        try {
            TransactionManager.callInTransaction(mHelper.getConnectionSource(),
                    new Callable<Void>() {
                        public Void call() throws Exception {

                            ContaDao contaDao = new ContaDao(mHelper.getConnectionSource());
                            contaDao.updateItem(transferencia.getContaDestino());
                            contaDao.updateItem(transferencia.getContaOrigem());
                            mDao.deleteItem(transferencia);

                            return null;
                        }
                    });
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        } finally {
            if (mHelper != null)
                mHelper.close();
        }

    }


}
