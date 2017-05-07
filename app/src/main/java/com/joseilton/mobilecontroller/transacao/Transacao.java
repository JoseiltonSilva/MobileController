package com.joseilton.mobilecontroller.transacao;

import android.content.Context;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.joseilton.mobilecontroller.R;
import com.joseilton.mobilecontroller.categoria.Categoria;
import com.joseilton.mobilecontroller.conta.Conta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;


@DatabaseTable(tableName = "transacao")
public class Transacao implements Serializable {


    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false)
    private String descricao;

    @DatabaseField(canBeNull = false)
    private Date vencimento = new Date();

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Categoria categoria;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Conta conta;

    @DatabaseField(canBeNull = false)
    private TransacaoTipo tipo;

    @DatabaseField(canBeNull = false)
    private BigDecimal valor = new BigDecimal("0.00");

    @DatabaseField
    private boolean efetivado;

    private Efetivacao efetivacao;

    public Transacao() {
    }

    public Transacao(Long id) {
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

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public Conta getConta() {
        return conta;
    }

    public void setTipo(TransacaoTipo tipo) {
        this.tipo = tipo;
    }

    public TransacaoTipo getTipo() {
        return tipo;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setEfetivado(boolean efetivado) {
        this.efetivado = efetivado;
    }

    public boolean isEfetivado() {
        return efetivado;
    }

    public boolean efetivar(Context context, Transacao transacao) {
        efetivacao = transacao.getTipo().getEfetivacao();
        if (efetivacao.efetivar(context, transacao))
            return true;
        else
            return false;
    }

    public boolean estornar(Context context, Transacao transacao) {
        efetivacao = transacao.getTipo().getEfetivacao();
        if (efetivacao.estornar(context, transacao))
            return true;
        else
            return false;
    }

    public Integer getStatus() {
        Integer status = 0;

        Calendar hoje = Calendar.getInstance();
        Calendar venc = Calendar.getInstance();
        venc.setTime(vencimento);

        if (efetivado) {
            status = R.drawable.ic_check_circle_black_24dp;
        } else {
            //se hoje for maior ou igual ao vencimento
            if (hoje.compareTo(venc) <= 0) {
                status = null;
            }
            //se hoje for menor que o vencimento
            else if (hoje.compareTo(venc) > 0) {
                status = R.drawable.ic_alarm_black_24dp;
            }
        }
        return status;
    }

    public boolean isValid() {
        if (descricao.length() > 0 && tipo != null && categoria != null && conta != null && vencimento != null && valor != null)
            return true;
        else
            return false;
    }

}
