package com.joseilton.mobilecontroller.conta;


import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class ContaDao extends BaseDaoImpl<Conta, Long> {
    public ContaDao(ConnectionSource cs) throws SQLException {
        super(Conta.class);
        setConnectionSource(cs);
        initialize();
    }

    public List<Conta> selectByTipo(ContaTipo tipo) throws SQLException {
        QueryBuilder<Conta, Long> qb = queryBuilder();
        qb.where().eq("tipo", tipo);
        qb.orderBy("descricao", true);
        PreparedQuery<Conta> pq = qb.prepare();
        return query(pq);
    }

    public List<Conta> select() throws SQLException {
        QueryBuilder<Conta, Long> qb = queryBuilder();
        qb.orderBy("tipo", true);
        qb.orderBy("descricao", true);
        PreparedQuery<Conta> pq = qb.prepare();
        return query(pq);
    }

    public void insertItem(Conta conta) throws SQLException {
        this.create(conta);
    }

    public void updateItem(Conta conta) throws SQLException {
        this.update(conta);
    }

    public void deleteItem(Conta conta) throws SQLException {
        this.delete(conta);
    }
}
