package com.joseilton.mobilecontroller.categoria;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "categoria")
public class Categoria implements Serializable {


    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false)
    private CategoriaTipo tipo;

    @DatabaseField(canBeNull = false)
    private String descricao;

    @DatabaseField(canBeNull = false)
    private int cor;

    public Categoria() {
    }

    public Categoria(Long id) {
        this.id = id;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setTipo(CategoriaTipo tipo) {
        this.tipo = tipo;
    }

    public CategoriaTipo getTipo() {
        return tipo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getCor() {
        return cor;
    }

    public void setCor(int cor) {
        this.cor = cor;
    }

    @Override
    public String toString() {
        return descricao;
    }

    @Override
    public boolean equals(Object obj) {
        Categoria c = (Categoria) obj;
        return this.id == c.getId();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public boolean isValid() {
        if (descricao.length() > 0 && tipo != null && cor != 0)
            return true;
        else
            return false;
    }
}
