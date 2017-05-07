package com.joseilton.mobilecontroller.categoria;



import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.joseilton.mobilecontroller.categoria.Categoria;
import com.joseilton.mobilecontroller.categoria.CategoriaTipo;

import java.sql.SQLException;
import java.util.List;

public class CategoriaDao extends BaseDaoImpl<Categoria, Long> {
    public CategoriaDao(ConnectionSource cs) throws SQLException {
        super(Categoria.class);
        setConnectionSource(cs);
        initialize();
    }

    public List<Categoria> selectByTipo(CategoriaTipo tipo) throws SQLException {
        QueryBuilder<Categoria, Long> qb = queryBuilder();
        qb.where().eq("tipo", tipo);
        qb.orderBy("descricao", true);
        PreparedQuery<Categoria> pq = qb.prepare();
        return query(pq);
    }

    public List<Categoria> select() throws SQLException {
        QueryBuilder<Categoria, Long> qb = queryBuilder();
        qb.orderBy("tipo", true);
        qb.orderBy("descricao", true);
        PreparedQuery<Categoria> pq = qb.prepare();
        return query(pq);
    }

    public void insertItem(Categoria categoria) throws SQLException {
        this.create(categoria);
    }


    public void updateItem(Categoria categoria) throws SQLException {
        this.update(categoria);
    }


    public void deleteItem(Categoria categoria) throws SQLException {
        this.delete(categoria);
    }

}
