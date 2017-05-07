package com.joseilton.mobilecontroller.transacao;


import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.joseilton.mobilecontroller.categoria.Categoria;
import com.joseilton.mobilecontroller.conta.Conta;
import com.joseilton.mobilecontroller.transacao.Transacao;
import com.joseilton.mobilecontroller.transacao.TransacaoTipo;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class TransacaoDao extends BaseDaoImpl<Transacao, Long> {
    public TransacaoDao(ConnectionSource cs) throws SQLException {
        super(Transacao.class);
        setConnectionSource(cs);
        initialize();
    }

    /**
     * Retorna uma lista com todas as transacoes
     * se efetivado = null retorna transacoes efetivadas e nao efetivadas
     *
     * @param efetivado
     * @return
     */
    public List<Transacao> select(Boolean efetivado) throws SQLException {
        QueryBuilder<Transacao, Long> qb = queryBuilder();
        Where<Transacao, Long> where = qb.where();

        if (efetivado != null)
            where.eq("efetivado", efetivado);

        qb.orderBy("vencimento", true);
        PreparedQuery<Transacao> pq = qb.prepare();
        return query(pq);
    }

    /**
     * Retorna uma lista com todas as transacoes do tipo informado
     * se efetivado = null retorna transacoes efetivadas e nao efetivadas
     *
     * @param tipo
     * @param efetivado
     * @return
     */
    public List<Transacao> select(TransacaoTipo tipo, Boolean efetivado) throws SQLException {
        QueryBuilder<Transacao, Long> qb = queryBuilder();
        Where<Transacao, Long> where = qb.where();
        where.eq("tipo", tipo);

        if (efetivado != null) {
            where.and();
            where.eq("efetivado", efetivado);
        }

        qb.orderBy("vencimento", true);
        PreparedQuery<Transacao> pq = qb.prepare();
        return query(pq);
    }

    /**
     * Retorna uma lista com todas as transacoes do tipo informado e que contenha a conta informada
     * se efetivado = null retorna transacoes efetivadas e nao efetivadas
     *
     * @param tipo
     * @param conta
     * @param efetivado
     * @return
     */
    public List<Transacao> select(TransacaoTipo tipo, Conta conta, Boolean efetivado) throws SQLException {
        QueryBuilder<Transacao, Long> qb = queryBuilder();
        Where<Transacao, Long> where = qb.where();
        where.eq("tipo", tipo);
        where.and();
        where.eq("conta_id", conta.getId());

        if (efetivado != null) {
            where.and();
            where.eq("efetivado", efetivado);
        }

        qb.orderBy("vencimento", true);
        PreparedQuery<Transacao> pq = qb.prepare();
        return query(pq);
    }

    /**
     * Retorna uma lista com todas as transacoes do tipo informado e que contenha a categoria informada
     * se efetivado = null retorna transacoes efetivadas e nao efetivadas
     *
     * @param tipo
     * @param categoria
     * @param efetivado
     * @return
     */
    public List<Transacao> select(TransacaoTipo tipo, Categoria categoria, Boolean efetivado) throws SQLException {
        QueryBuilder<Transacao, Long> qb = queryBuilder();
        Where<Transacao, Long> where = qb.where();
        where.eq("tipo", tipo);
        where.and();
        where.eq("categoria_id", categoria.getId());

        if (efetivado != null) {
            where.and();
            where.eq("efetivado", efetivado);
        }

        qb.orderBy("vencimento", true);
        PreparedQuery<Transacao> pq = qb.prepare();
        return query(pq);
    }

    /**
     * Retorna uma lista com todas as transacoes do tipo informado e que contenha a conta e a categoria informada
     * se efetivado = null retorna transacoes efetivadas e nao efetivadas
     *
     * @param tipo
     * @param conta
     * @param categoria
     * @param efetivado
     * @return
     */
    public List<Transacao> select(TransacaoTipo tipo, Conta conta, Categoria categoria, Boolean efetivado) throws SQLException {
        QueryBuilder<Transacao, Long> qb = queryBuilder();
        Where<Transacao, Long> where = qb.where();
        where.eq("tipo", tipo);
        where.and();
        where.eq("conta_id", conta.getId());
        where.and();
        where.eq("categoria_id", categoria.getId());

        if (efetivado != null) {
            where.and();
            where.eq("efetivado", efetivado);
        }

        qb.orderBy("vencimento", true);
        PreparedQuery<Transacao> pq = qb.prepare();
        return query(pq);
    }

    /**
     * Retorna uma lista com todas as transacoes e que contem a categoria informada
     * se efetivado = null retorna transacoes efetivadas e nao efetivadas
     * <p>
     * #param efetivado
     *
     * @param categoria
     * @return
     */
    public List<Transacao> select(Categoria categoria, Boolean efetivado) throws SQLException {
        QueryBuilder<Transacao, Long> qb = queryBuilder();
        Where<Transacao, Long> where = qb.where();
        where.eq("categoria_id", categoria.getId());

        if (efetivado != null) {
            where.and();
            where.eq("efetivado", efetivado);
        }

        qb.orderBy("vencimento", true);
        PreparedQuery<Transacao> pq = qb.prepare();
        return query(pq);
    }


    /**
     * Retorna uma lista com todas as transacoes que contem a conta informada
     * se efetivado = null retorna transacoes efetivadas e nao efetivadas
     *
     * @param efetivado
     * @param conta
     * @return
     */
    public List<Transacao> select(Conta conta, Boolean efetivado) throws SQLException {
        QueryBuilder<Transacao, Long> qb = queryBuilder();
        Where<Transacao, Long> where = qb.where();
        where.eq("conta_id", conta.getId());

        if (efetivado != null) {
            where.and();
            where.eq("efetivado", efetivado);
        }

        qb.orderBy("vencimento", true);
        PreparedQuery<Transacao> pq = qb.prepare();
        return query(pq);
    }


    /**
     * Retorna uma lista com todas as transacoes entre duas datas
     * se efetivado = null retorna transacoes efetivadas e nao efetivadas
     *
     * @param efetivado
     * @param begin
     * @param end
     * @return
     */
    public List<Transacao> select(Date begin, Date end, Boolean efetivado) throws SQLException {

        QueryBuilder<Transacao, Long> qb = queryBuilder();
        Where<Transacao, Long> where = qb.where();
        where.between("vencimento", begin, end);

        if (efetivado != null) {
            where.and();
            where.eq("efetivado", efetivado);
        }

        qb.orderBy("vencimento", true);
        PreparedQuery<Transacao> pq = qb.prepare();
        return query(pq);
    }

    /**
     * Retorna uma lista com todas as transacoes entre duas datas
     * se efetivado = null retorna transacoes efetivadas e nao efetivadas
     *
     * @param efetivado
     * @param begin
     * @param end
     * @return
     */
    public List<Transacao> select(Date begin, Date end, TransacaoTipo tipo, Boolean efetivado) throws SQLException {
        //Date dataInicio = 	new Date(date.getYear(), date.getMonth(), 1);
        //Date dataFinal = DateUtil.addMes(dataInicio, 1);

        QueryBuilder<Transacao, Long> qb = queryBuilder();
        Where<Transacao, Long> where = qb.where();
        where.eq("tipo", tipo);
        where.between("vencimento", begin, end);

        if (efetivado != null) {
            where.and();
            where.eq("efetivado", efetivado);
        }

        qb.orderBy("vencimento", true);
        PreparedQuery<Transacao> pq = qb.prepare();
        return query(pq);
    }


    /**
     * Retorna uma lista com todas as transacoes entre duas datas e que contem a conta informada
     * efetivado = null retorna transacoes efetivadas e nao efetivadas
     *
     * @param begin
     * @param end
     * @param conta
     * @param efetivado
     * @return
     */
    public List<Transacao> select(Date begin, Date end, Conta conta, Boolean efetivado) throws SQLException {

        QueryBuilder<Transacao, Long> qb = queryBuilder();
        Where<Transacao, Long> where = qb.where();
        where.eq("conta_id", conta.getId());
        where.and();
        where.between("vencimento", begin, end);

        if (efetivado != null) {
            where.and();
            where.eq("efetivado", efetivado);
        }

        qb.orderBy("vencimento", true);
        PreparedQuery<Transacao> pq = qb.prepare();
        return query(pq);

    }

    /**
     * Retorna uma lista com todas as transacoes entre duas datas e que contem a categoria informada
     * efetivado = null retorna transacoes efetivadas e nao efetivadas
     *
     * @param begin
     * @param end
     * @param categoria
     * @param efetivado
     * @return
     */
    public List<Transacao> select(Date begin, Date end, Categoria categoria, Boolean efetivado) throws SQLException {

        QueryBuilder<Transacao, Long> qb = queryBuilder();
        Where<Transacao, Long> where = qb.where();
        where.eq("categoria_id", categoria.getId());
        where.and();
        where.between("vencimento", begin, end);

        if (efetivado != null) {
            where.and();
            where.eq("efetivado", efetivado);
        }

        qb.orderBy("vencimento", true);
        PreparedQuery<Transacao> pq = qb.prepare();
        return query(pq);

    }

    /**
     * Retorna uma lista com todas as transacoes entre duas datas do tipo informado e que contem a conta informada
     * efetivado = null retorna transacoes efetivadas e nao efetivadas
     *
     * @param begin
     * @param end
     * @param conta
     * @param tipo
     * @param efetivado
     * @return
     */
    public List<Transacao> select(Date begin, Date end, Conta conta, TransacaoTipo tipo, Boolean efetivado) throws SQLException {

        QueryBuilder<Transacao, Long> qb = queryBuilder();
        Where<Transacao, Long> where = qb.where();
        where.eq("conta_id", conta.getId());
        where.and();
        where.eq("tipo", tipo);
        where.and();
        where.between("vencimento", begin, end);

        if (efetivado != null) {
            where.and();
            where.eq("efetivado", efetivado);
        }

        qb.orderBy("vencimento", true);
        PreparedQuery<Transacao> pq = qb.prepare();
        return query(pq);

    }

    /**
     * Retorna uma lista com todas as transacoes entre duas datas do tipo informado e que contem a categoria informada
     * efetivado = null retorna transacoes efetivadas e nao efetivadas
     *
     * @param begin
     * @param end
     * @param tipo
     * @param categoria
     * @param efetivado
     * @return
     */
    public List<Transacao> select(Date begin, Date end, Categoria categoria, TransacaoTipo tipo, Boolean efetivado) throws SQLException {

        QueryBuilder<Transacao, Long> qb = queryBuilder();
        Where<Transacao, Long> where = qb.where();
        where.eq("categoria_id", categoria.getId());
        where.and();
        where.eq("tipo", tipo);
        where.and();
        where.between("vencimento", begin, end);

        if (efetivado != null) {
            where.and();
            where.eq("efetivado", efetivado);
        }

        qb.orderBy("vencimento", true);
        PreparedQuery<Transacao> pq = qb.prepare();
        return query(pq);

    }

    /**
     * Retorna uma lista com todas as transacoes entre duas datas e que contem a conta e a categoria informada
     * efetivado = null retorna transacoes efetivadas e nao efetivadas
     *
     * @param begin
     * @param end
     * @param conta
     * @param categoria
     * @param efetivado
     * @return
     */
    public List<Transacao> select(Date begin, Date end, Conta conta, Categoria categoria, Boolean efetivado) throws SQLException {

        QueryBuilder<Transacao, Long> qb = queryBuilder();
        Where<Transacao, Long> where = qb.where();
        where.eq("conta_id", conta.getId());
        where.and();
        where.eq("categoria_id", categoria.getId());
        where.and();
        where.between("vencimento", begin, end);

        if (efetivado != null) {
            where.and();
            where.eq("efetivado", efetivado);
        }

        qb.orderBy("vencimento", true);
        PreparedQuery<Transacao> pq = qb.prepare();
        return query(pq);

    }

    /**
     * Retorna uma lista com todas as transacoes entre duas datas do tipo informado e que contem a conta e a categoria informada
     * efetivado = null retorna transacoes efetivadas e nao efetivadas
     *
     * @param begin
     * @param end
     * @param tipo
     * @param conta
     * @param categoria
     * @param efetivado
     * @return
     */
    public List<Transacao> select(Date begin, Date end, TransacaoTipo tipo, Conta conta, Categoria categoria, Boolean efetivado) throws SQLException {

        QueryBuilder<Transacao, Long> qb = queryBuilder();
        Where<Transacao, Long> where = qb.where();
        where.eq("conta_id", conta.getId());
        where.and();
        where.eq("categoria_id", categoria.getId());
        where.and();
        where.eq("tipo", tipo);
        where.and();
        where.between("vencimento", begin, end);

        if (efetivado != null) {
            where.and();
            where.eq("efetivado", efetivado);
        }

        qb.orderBy("vencimento", true);
        PreparedQuery<Transacao> pq = qb.prepare();
        return query(pq);

    }

    /**
     * Insere um novo item no db
     *
     * @param transacao
     */
    public void insertItem(Transacao transacao) throws SQLException {
        this.create(transacao);
    }

    /**
     * Altera o item no db
     *
     * @param transacao
     */
    public void updateItem(Transacao transacao) throws SQLException {
        this.update(transacao);
    }

    /**
     * Excui o item do db
     *
     * @param transacao
     */
    public void deleteItem(Transacao transacao) throws SQLException {
        this.delete(transacao);
    }
}
