package com.joseilton.mobilecontroller.transacao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.misc.*;
import com.joseilton.mobilecontroller.conta.ContaDao;
import com.joseilton.mobilecontroller.app.DatabaseHelper;
import com.joseilton.mobilecontroller.categoria.Categoria;
import com.joseilton.mobilecontroller.conta.Conta;
import com.joseilton.mobilecontroller.app.Periodo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class TransacaoService {
    private final static String LOG = "TransacaoService.";

    private DatabaseHelper mHelper;
    private TransacaoDao mDao;
    private Context mContext;

    public TransacaoService(Context context) {
        this.mContext = context;
        mHelper = new DatabaseHelper(mContext);
        try {
            mDao = new TransacaoDao(mHelper.getConnectionSource());
        } catch (SQLException e) {
            Log.e(LOG + "", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Verifica se existe alguma transacao que contenha a categoria informada
     *
     * @param categoria
     * @return
     */
    public boolean contains(Categoria categoria) {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(categoria, null);
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
     * Verifica se existe alguma transacao que contenha a conta informada
     *
     * @param conta
     * @return
     */
    public boolean contains(Conta conta) {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(conta, null);
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
     * Retorna uma lista com todas as transacoes
     * se efetivado = null retorna transacoes efetivadas e nao efetivadas
     *
     * @param efetivado
     * @return
     */
    public List<Transacao> getTransacoes(Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(efetivado);
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
     * Retorna uma lista com todas as transacoes que contenha a conta informada
     * se efetivado = null retorna transacoes efetivadas e nao efetivadas
     *
     * @param conta
     * @param efetivado
     * @return
     */
    public List<Transacao> getTransacoesConta(Conta conta, Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(conta, efetivado);
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
     * Retorna uma lista com todas as transacoes que contenha a categoria informada
     * se efetivado = null retorna transacoes efetivadas e nao efetivadas
     *
     * @param categoria
     * @param efetivado
     * @return
     */
    public List<Transacao> getTransacoesCategoria(Categoria categoria, Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(categoria, efetivado);
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
     * Retorna uma lista com todas as transacoes do periodo informado
     * se efetivado = null retorna transacoes efetivadas e nao efetivadas
     *
     * @param efetivado
     * @return
     */
    public List<Transacao> getTransacoesPeriodo(Periodo periodo, Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(periodo.getBegin(), periodo.getEnd(), efetivado);
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
     * Retorna uma lista com todas as transacoes do periodo informado e que contenha a conta informada
     * se efetivado = null retorna transacoes efetivadas e nao efetivadas
     *
     * @param conta
     * @param efetivado
     * @return
     */
    public List<Transacao> getTransacoesPeriodoConta(Periodo periodo, Conta conta, Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(periodo.getBegin(), periodo.getEnd(), conta, efetivado);
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
     * Retorna uma lista com todas as transacoes do periodo informado e que contenha a categoria informada
     * se efetivado = null retorna transacoes efetivadas e nao efetivadas
     *
     * @param categoria
     * @param efetivado
     * @return
     */
    public List<Transacao> getTransacoesPeriodoCategoria(Periodo periodo, Categoria categoria, Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(periodo.getBegin(), periodo.getEnd(), categoria, efetivado);
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
     * Retorna uma lista com todas as transacoes do periodo informado e que contenha a conta e a categoria informada
     * se efetivado = null retorna transacoes efetivadas e nao efetivadas
     *
     * @param conta
     * @param categoria
     * @param efetivado
     * @return
     */
    public List<Transacao> getTransacoesPeriodoContaCategoria(Periodo periodo, Conta conta, Categoria categoria, Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(periodo.getBegin(), periodo.getEnd(), conta, categoria, efetivado);
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
     * Retorna uma lista com todas as despesas
     * se efetivado = null retorna despesas efetivadas e nao efetivadas
     *
     * @param efetivado
     * @return
     */
    public List<Transacao> getDespesas(Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(TransacaoTipo.Despesa, efetivado);
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
     * Retorna uma lista com todas as despesas do periodo informado
     * se efetivado = null retorna despesas efetivadas e nao efetivadas
     *
     * @param periodo
     * @param efetivado
     * @return
     */
    public List<Transacao> getDespesasPeriodo(Periodo periodo, Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(periodo.getBegin(), periodo.getEnd(), TransacaoTipo.Despesa, efetivado);
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
     * Retorna uma lista com todas as despesas do perido informado e que contenha a conta informada
     * se efetivado = null retorna despesas efetivadas e nao efetivadas
     *
     * @param periodo
     * @param conta
     * @param efetivado
     * @return
     */
    public List<Transacao> getDespesasPeriodoConta(Periodo periodo, Conta conta, Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(periodo.getBegin(), periodo.getEnd(), conta, TransacaoTipo.Despesa, efetivado);
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
     * Retorna uma lista com todas as despesas do perido informado e que contenha a categoria informada
     * se efetivado = null retorna despesas efetivadas e nao efetivadas
     *
     * @param periodo
     * @param categoria
     * @param efetivado
     * @return
     */
    public List<Transacao> getDespesasPeriodoCategoria(Periodo periodo, Categoria categoria, Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(periodo.getBegin(), periodo.getEnd(), categoria, TransacaoTipo.Despesa, efetivado);
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
     * Retorna uma lista com todas as despesas do perido informado e que contenha a conta e a categoria informada
     * se efetivado = null retorna despesas efetivadas e nao efetivadas
     *
     * @param periodo
     * @param conta
     * @param efetivado
     * @return
     */
    public List<Transacao> getDespesasPeriodoContaCategoria(Periodo periodo, Conta conta, Categoria categoria, Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(periodo.getBegin(), periodo.getEnd(), TransacaoTipo.Despesa, conta, categoria, efetivado);
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
     * Retorna uma lista com todas as despesas e que contenha a conta informada
     * se efetivado = null retorna despesas efetivadas e nao efetivadas
     *
     * @param conta
     * @param efetivado
     * @return
     */
    public List<Transacao> getDespesasConta(Conta conta, Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(TransacaoTipo.Despesa, conta, efetivado);
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
     * Retorna uma lista com todas as despesas e que contenha a categoria informada
     * se efetivado = null retorna despesas efetivadas e nao efetivadas
     *
     * @param categoria
     * @param efetivado
     * @return
     */
    public List<Transacao> getDespesasCategoria(Categoria categoria, Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(TransacaoTipo.Despesa, categoria, efetivado);
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
     * Retorna uma lista com todas as despesas e que contenha a conta e a categoria informada
     * se efetivado = null retorna despesas efetivadas e nao efetivadas
     *
     * @param conta
     * @param categoria
     * @param efetivado
     * @return
     */
    public List<Transacao> getDespesasContaCategoria(Conta conta, Categoria categoria, Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(TransacaoTipo.Despesa, conta, categoria, efetivado);
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
     * Retorna uma lista com todas as receitas
     * se efetivado = null retorna receitas efetivadas e nao efetivadas
     *
     * @param efetivado
     * @return
     */
    public List<Transacao> getReceitas(Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(TransacaoTipo.Receita, efetivado);
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
     * Retorna uma lista com todas as receitas
     * se efetivado = null retorna receitas efetivadas e nao efetivadas
     *
     * @param efetivado
     * @return
     */
    public List<Transacao> getReceitasPeriodo(Periodo periodo, Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(periodo.getBegin(), periodo.getEnd(), TransacaoTipo.Receita, efetivado);
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
     * Retorna uma lista com todas as receitas do perido informado e que contenha a conta informada
     * se efetivado = null retorna despesas efetivadas e nao efetivadas
     *
     * @param periodo
     * @param conta
     * @param efetivado
     * @return
     */
    public List<Transacao> getReceitasPeriodoConta(Periodo periodo, Conta conta, Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(periodo.getBegin(), periodo.getEnd(), conta, TransacaoTipo.Receita, efetivado);
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
     * Retorna uma lista com todas as receitas do perido informado e que contenha a categoria informada
     * se efetivado = null retorna despesas efetivadas e nao efetivadas
     *
     * @param periodo
     * @param categoria
     * @param efetivado
     * @return
     */
    public List<Transacao> getReceitasPeriodoCategoria(Periodo periodo, Categoria categoria, Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(periodo.getBegin(), periodo.getEnd(), categoria, TransacaoTipo.Receita, efetivado);
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
     * Retorna uma lista com todas as receitas do perido informado e que contenha a conta e a categoria informada
     * se efetivado = null retorna despesas efetivadas e nao efetivadas
     *
     * @param periodo
     * @param conta
     * @param efetivado
     * @return
     */
    public List<Transacao> getReceitasPeriodoContaCategoria(Periodo periodo, Conta conta, Categoria categoria, Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(periodo.getBegin(), periodo.getEnd(), TransacaoTipo.Receita, conta, categoria, efetivado);
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
     * Retorna uma lista com todas as receitas e que contenha a conta informada
     * se efetivado = null retorna despesas efetivadas e nao efetivadas
     *
     * @param conta
     * @param efetivado
     * @return
     */
    public List<Transacao> getReceitasConta(Conta conta, Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(TransacaoTipo.Receita, conta, efetivado);
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
     * Retorna uma lista com todas as receitas e que contenha a categoria informada
     * se efetivado = null retorna despesas efetivadas e nao efetivadas
     *
     * @param categoria
     * @param efetivado
     * @return
     */
    public List<Transacao> getReceitasCategoria(Categoria categoria, Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(TransacaoTipo.Receita, categoria, efetivado);
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
     * Retorna uma lista com todas as receitas e que contenha a conta e a categoria informada
     * se efetivado = null retorna despesas efetivadas e nao efetivadas
     *
     * @param conta
     * @param categoria
     * @param efetivado
     * @return
     */
    public List<Transacao> getReceitasContaCategoria(Conta conta, Categoria categoria, Boolean efetivado) throws SQLException {
        List<Transacao> list = new ArrayList<>();
        try {
            list = mDao.select(TransacaoTipo.Receita, conta, categoria, efetivado);
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
     * Insere uma nova transacao no db
     *
     * @param transacao
     */
    public void salvarTransacao(Transacao transacao) throws SQLException {
        if (transacao.isValid()) {
            try {
                mDao.insertItem(transacao);
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
     * Altera a transacao informada no db
     *
     * @param transacao
     */
    public void atualizarTransacao(Transacao transacao) throws SQLException {
        if (transacao.isValid()) {
            try {
                mDao.updateItem(transacao);
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
     * Exclui do db a transacao informada
     *
     * @param transacao
     */
    public void excluirTransacao(Transacao transacao) throws SQLException {
        try {
            mDao.deleteItem(transacao);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        } finally {
            if (mHelper != null)
                mHelper.close();
        }

    }

    /**
     * Salva e Efetiva uma transacao no db
     *
     * @param transacao
     */
    public void salvarEfetivarTransacao(final Transacao transacao) throws SQLException {
        try {
            TransactionManager.callInTransaction(mHelper.getConnectionSource(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            ContaDao contaDao = new ContaDao(mHelper.getConnectionSource());
                            contaDao.updateItem(transacao.getConta());
                            mDao.insertItem(transacao);

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

    /**
     * Efetiva uma transacao no db
     *
     * @param transacao
     */
    public void efetivarTransacao(final Transacao transacao) throws SQLException {
        try {
            TransactionManager.callInTransaction(mHelper.getConnectionSource(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            ContaDao contaDao = new ContaDao(mHelper.getConnectionSource());
                            contaDao.updateItem(transacao.getConta());
                            mDao.updateItem(transacao);

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

    /**
     * Estorna uma transacao no db
     *
     * @param transacao
     */
    public void estornarTransacao(final Transacao transacao) throws SQLException {
        try {
            TransactionManager.callInTransaction(mHelper.getConnectionSource(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            ContaDao contaDao = new ContaDao(mHelper.getConnectionSource());
                            contaDao.updateItem(transacao.getConta());
                            mDao.updateItem(transacao);

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
