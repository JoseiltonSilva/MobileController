package com.joseilton.mobilecontroller.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;


import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.joseilton.mobilecontroller.categoria.Categoria;
import com.joseilton.mobilecontroller.categoria.CategoriaDao;
import com.joseilton.mobilecontroller.categoria.CategoriaTipo;
import com.joseilton.mobilecontroller.conta.ContaDao;
import com.joseilton.mobilecontroller.conta.Conta;
import com.joseilton.mobilecontroller.conta.ContaTipo;
import com.joseilton.mobilecontroller.transacao.Transacao;
import com.joseilton.mobilecontroller.transferencia.Transferencia;

import java.math.BigDecimal;
import java.sql.SQLException;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String databaseName = "mobile_controller.db";
    private static final int databaseVersion = 1;

    private static  final String TAG = "DatabaseHelper.";

    public DatabaseHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
    }


    @Override
    public void onCreate(SQLiteDatabase sd, ConnectionSource cs) {
        try {
            TableUtils.createTable(cs, Categoria.class);
            TableUtils.createTable(cs, Conta.class);
            TableUtils.createTable(cs, Transacao.class);
            TableUtils.createTable(cs, Transferencia.class);

            addCategoria();
            addConta();
        } catch (SQLException e) {
            Log.e("onCreate", "Erro ao criar db." + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    @SuppressLint("LongLogTag")
    public void onUpgrade(SQLiteDatabase sd, ConnectionSource cs, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(cs, Categoria.class, true);
            TableUtils.dropTable(cs, Conta.class, true);
            TableUtils.dropTable(cs, Transacao.class, true);
            TableUtils.dropTable(cs, Transferencia.class, true);
            onCreate(sd, cs);
        } catch (SQLException e) {
            Log.e(TAG + "onUpgrade", "Erro ao atualizar db." + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        super.close();
    }

    @SuppressLint("LongLogTag")
    private void addCategoria() {
        Categoria c1 = new Categoria();
        c1.setTipo(CategoriaTipo.Debito);
        c1.setDescricao("Moradia");
        c1.setCor(Color.GREEN);

        Categoria c2 = new Categoria();
        c2.setTipo(CategoriaTipo.Debito);
        c2.setDescricao("Lazer");
        c2.setCor(Color.BLUE);

        Categoria c3 = new Categoria();
        c3.setTipo(CategoriaTipo.Debito);
        c3.setDescricao("Saúde");
        c3.setCor(Color.MAGENTA);

        Categoria c4 = new Categoria();
        c4.setTipo(CategoriaTipo.Debito);
        c4.setDescricao("Educação");
        c4.setCor(Color.RED);

        Categoria c5 = new Categoria();
        c5.setTipo(CategoriaTipo.Debito);
        c5.setDescricao("Impostos");
        c5.setCor(Color.GREEN);

        Categoria c6 = new Categoria();
        c6.setTipo(CategoriaTipo.Credito);
        c6.setDescricao("Salário");
        c6.setCor(Color.GRAY);

        Categoria c7 = new Categoria();
        c7.setTipo(CategoriaTipo.Credito);
        c7.setDescricao("Outros Rendimentos");
        c7.setCor(Color.CYAN);

        try {
            CategoriaDao dao = new CategoriaDao(this.getConnectionSource());
            dao.create(c1);
            dao.create(c2);
            dao.create(c3);
            dao.create(c4);
            dao.create(c5);
            dao.create(c6);
            dao.create(c7);
        } catch (SQLException e) {
            Log.e( TAG + "addCategoria", "Erro ao adicionar categorias." + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addConta() {
        Conta c1 = new Conta();
        c1.setDescricao("Carteira");
        c1.setTipo(ContaTipo.Dinheiro);
        c1.setSaldo(new BigDecimal("0.00"));
        c1.setLimite(new BigDecimal("0.00"));

        try {
            ContaDao dao = new ContaDao(this.getConnectionSource());
            dao.create(c1);
        } catch (SQLException e) {
            Log.e(TAG + "addConta", "Erro ao adicionar conta." + e.getMessage());
            e.printStackTrace();
        }
    }
}


