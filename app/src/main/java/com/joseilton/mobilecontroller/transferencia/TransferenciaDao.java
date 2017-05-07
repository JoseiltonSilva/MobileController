package com.joseilton.mobilecontroller.transferencia;


import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.joseilton.mobilecontroller.conta.Conta;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class TransferenciaDao extends BaseDaoImpl<Transferencia, Long> {
    public TransferenciaDao(ConnectionSource cs) throws SQLException {
        super(Transferencia.class);
        setConnectionSource(cs);
        initialize();
    }

    /**
     * Retorna uma lista com todas as transferencias
     *
     * @return
     */
    public List<Transferencia> select() throws SQLException {
        QueryBuilder<Transferencia, Long> qb = queryBuilder();

        qb.orderBy("data", true);
        PreparedQuery<Transferencia> pq = qb.prepare();
        return query(pq);
    }

    /**
     * Retorna uma lista com todas as transferencias que contenham a conta informada
     *
     * @param conta
     * @return
     */
    public List<Transferencia> select(Conta conta) throws SQLException {
        QueryBuilder<Transferencia, Long> qb = queryBuilder();
        Where<Transferencia, Long> where = qb.where();
        where.eq("contaOrigem_id", conta.getId());
        where.or();
        where.eq("contaDestino_id", conta.getId());
        qb.orderBy("data", true);
        PreparedQuery<Transferencia> pq = qb.prepare();
        return query(pq);
    }

    /**
     * Retorna uma lista com todas as transferencias entre duas datas
     *
     * @param begin
     * @param end
     * @return
     */
    public List<Transferencia> select(Date begin, Date end) throws SQLException {
        QueryBuilder<Transferencia, Long> qb = queryBuilder();
        Where<Transferencia, Long> where = qb.where();
        where.between("data", begin, end);
        qb.orderBy("data", true);
        PreparedQuery<Transferencia> pq = qb.prepare();
        return query(pq);
    }

    /**
     * Retorna uma lista com todas as transferencias entre duas datas e que contenha a conta informada
     *
     * @param conta
     * @param begin
     * @param end
     * @return
     */
    public List<Transferencia> select(Date begin, Date end, Conta conta) throws SQLException {

        QueryBuilder<Transferencia, Long> qb = queryBuilder();
        Where<Transferencia, Long> where = qb.where();
        where.eq("contaOrigem_id", conta.getId());
        where.or();
        where.eq("contaDestino_id", conta.getId());
        where.and();
        where.between("data", begin, end);
        qb.orderBy("data", true);
        PreparedQuery<Transferencia> pq = qb.prepare();
        return query(pq);
    }

    /**
     * Insere um novo item no db
     *
     * @param transferencia
     */
    public void insertItem(Transferencia transferencia) throws SQLException {
        this.create(transferencia);
    }

    /**
     * Altera o item no db
     *
     * @param transferencia
     */
    public void updateItem(Transferencia transferencia) throws SQLException {
        this.update(transferencia);
    }

    /**
     * Exclui o item do db
     *
     * @param transferencia
     */
    public void deleteItem(Transferencia transferencia) throws SQLException {
        this.delete(transferencia);
    }

}
