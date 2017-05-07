package com.joseilton.mobilecontroller.conta;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.math.BigDecimal;

@DatabaseTable(tableName = "conta")
public class Conta implements Serializable {
    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false)
    private ContaTipo tipo;

    @DatabaseField(canBeNull = false)
    private String descricao;

    @DatabaseField(canBeNull = false)
    private BigDecimal saldo;

    @DatabaseField(canBeNull = false)
    private BigDecimal limite;

    public Conta() {
    }

    public Conta(Long id) {
        this.id = id;
    }

    public void setLimite(BigDecimal limite) {
        this.limite = limite;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setTipo(ContaTipo tipo) {
        this.tipo = tipo;
    }

    public ContaTipo getTipo() {
        return tipo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return descricao;
    }

    public boolean isValid() {
        if (descricao.length() > 0 && tipo != null && saldo != null && limite != null)
            return true;
        else
            return false;
    }


}
