package com.joseilton.mobilecontroller.transferencia;

import android.content.Context;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.*;
import com.joseilton.mobilecontroller.conta.Conta;
import com.joseilton.mobilecontroller.app.Validador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@DatabaseTable(tableName = "transferencia")
public class Transferencia implements Serializable {
    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false)
    private String descricao;

    @DatabaseField(canBeNull = false)
    private BigDecimal valor;

    @DatabaseField(canBeNull = false)
    private Date data;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Conta contaOrigem;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Conta contaDestino;

    public Transferencia() {
    }

    public Transferencia(Long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getData() {
        return data;
    }

    public void setContaOrigem(Conta contaOrigem) {
        this.contaOrigem = contaOrigem;
    }

    public Conta getContaOrigem() {
        return contaOrigem;
    }

    public void setContaDestino(Conta contaDestino) {
        this.contaDestino = contaDestino;
    }

    public Conta getContaDestino() {
        return contaDestino;
    }

    @Override
    public String toString() {
        return descricao;
    }

    public boolean efetivar(Context context) {
        if (Validador.validarDisponibilidade(context, this)) {
            this.getContaOrigem()
                    .setSaldo(this.getContaOrigem().getSaldo()
                            .subtract(this.getValor()));

            this.getContaDestino()
                    .setSaldo(this.getContaDestino().getSaldo()
                            .add(this.getValor()));

            return true;

        } else
            return false;
    }

    public boolean estornar(Context context) {
        if (Validador.validarDisponibilidadeEstorno(context, this)) {
            this.getContaOrigem()
                    .setSaldo(this.getContaOrigem().getSaldo()
                            .add(this.getValor()));

            this.getContaDestino()
                    .setSaldo(this.getContaDestino().getSaldo()
                            .subtract(this.getValor()));
            return true;

        } else
            return false;
    }

    public boolean isValid() {
        if (descricao.length() > 0 && contaOrigem != null && contaDestino != null && data != null && valor != null)
            return true;
        else
            return false;
    }

}
