package com.joseilton.mobilecontroller.transacao;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.joseilton.mobilecontroller.R;
import com.joseilton.mobilecontroller.categoria.Categoria;
import com.joseilton.mobilecontroller.categoria.CategoriaTipo;
import com.joseilton.mobilecontroller.conta.Conta;
import com.joseilton.mobilecontroller.categoria.CategoriaService;
import com.joseilton.mobilecontroller.conta.ContaService;
import com.joseilton.mobilecontroller.util.DateUtil;
import com.joseilton.mobilecontroller.util.MensagemUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DetalhesTransacaoActivity extends AppCompatActivity {

    private static final String TAG = "TransacaoActivity.";

    private static final int SALVAR_ACTION = 1;
    private static final int EXCLUIR_ACTION = 2;
    private static final int EFETIVAR_ACTION = 3;
    private static final int ESTORNAR_ACTION = 4;

    private int action;

    private ContaService contaService;
    private CategoriaService categoriaService;
    private TransacaoService mService;
    private Transacao transacao;

    private List<Conta> contas;
    private ArrayAdapter<Conta> contasAdapter;
    private List<Categoria> categorias;
    private ArrayAdapter<Categoria> categoriasAdapter;

    private EditText valorEditText, descricaoEditText;
    private TextView vencimentoTextView;
    private Spinner contasSpinner, categoriasSpinner;
    private CheckBox efetivadoCheckBox;
    private RadioButton despesaRadioButton, receitaRadioButton;
    private RadioGroup tipoRadioGroup;
    private TextInputLayout valorInputLayout, descricaoInputLayout;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_transacao);

        setupToolbar();
        setupViews();
        init();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalhes_transacao, menu);

        if (transacao.getId() == null) {
            menu.findItem(R.id.action_excluir).setVisible(false);
            menu.findItem(R.id.action_efetivar).setVisible(false);
            menu.findItem(R.id.action_estornar).setVisible(false);
        } else {
            if (transacao.isEfetivado()) {
                menu.findItem(R.id.action_salvar).setVisible(false);
                menu.findItem(R.id.action_excluir).setVisible(false);
                menu.findItem(R.id.action_efetivar).setVisible(false);
            } else {
                menu.findItem(R.id.action_estornar).setVisible(false);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        switch (item.getItemId()) {
            case R.id.action_salvar:
                if (validateViews()) {
                    action = SALVAR_ACTION;
                    showDialogChoice();
                }
                break;

            case R.id.action_excluir:
                action = EXCLUIR_ACTION;
                showDialogChoice();
                break;

            case R.id.action_efetivar:
                action = EFETIVAR_ACTION;
                showDialogChoice();
                break;

            case R.id.action_estornar:
                action = ESTORNAR_ACTION;
                showDialogChoice();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void init() {
        Intent intent = getIntent();
        transacao = (Transacao) intent.getSerializableExtra("transacao");

        if (transacao.getId() == null) {
            transacao.setTipo(TransacaoTipo.Despesa);
            setupCategoriasSpinner(CategoriaTipo.Debito);
        } else {
            setupCategoriasSpinner(transacao.getCategoria().getTipo());
            efetivadoCheckBox.setVisibility(View.INVISIBLE);
        }

        setupContasSpinner();

        if (transacao.isEfetivado()) {
            despesaRadioButton.setEnabled(false);
            receitaRadioButton.setEnabled(false);
            valorEditText.setEnabled(false);
            descricaoEditText.setEnabled(false);
            categoriasSpinner.setEnabled(false);
            contasSpinner.setEnabled(false);
        }

        tipoRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.despesa_radioButton:
                    setupCategoriasSpinner(CategoriaTipo.Debito);
                    break;

                case R.id.receita_radioButton:
                    setupCategoriasSpinner(CategoriaTipo.Credito);
                    break;
            }
        });
        entityToViews();
    }

    private void entityToViews() {
        if (TransacaoTipo.Despesa.equals(transacao.getTipo())) {
            despesaRadioButton.setChecked(true);
        } else if (TransacaoTipo.Receita.equals(transacao.getTipo())) {
            receitaRadioButton.setChecked(true);
        }

        descricaoEditText.setText(transacao.getDescricao());
        vencimentoTextView.setText(DateUtil.getFormatData(transacao.getVencimento()));

        if (transacao.getId() != null) {
            valorEditText.setText(transacao.getValor().toString());
            categoriasSpinner.setSelection(categoriasAdapter.getPosition(transacao.getCategoria()));
            contasSpinner.setSelection(contasAdapter.getPosition(transacao.getConta()));
        }
        efetivadoCheckBox.setChecked(transacao.isEfetivado());
    }


    private void viewsToEntity() {
        if (despesaRadioButton.isChecked()) {
            transacao.setTipo(TransacaoTipo.Despesa);
        } else if (receitaRadioButton.isChecked()) {
            transacao.setTipo(TransacaoTipo.Receita);
        }
        transacao.setDescricao(descricaoEditText.getText().toString());
        transacao.setValor(new BigDecimal(valorEditText.getText().toString()));
        //transacao.setVencimento(new Date(vencimentoTextView.getText().toString()));
        transacao.setCategoria(categoriasAdapter.getItem(categoriasSpinner.getSelectedItemPosition()));
        transacao.setConta(contasAdapter.getItem(contasSpinner.getSelectedItemPosition()));
        //transacao.setEfetivado(efetivadoCheckBox.isChecked());

    }

    @SuppressLint("LongLogTag")
    private void setupCategoriasSpinner(CategoriaTipo tipo) {
        try {
            categoriaService = new CategoriaService(this);
            if (tipo == CategoriaTipo.Debito) {
                categorias = categoriaService.getCategoriasDebito();
            } else if (tipo == CategoriaTipo.Credito) {
                categorias = categoriaService.getCategoriasCredito();
            }
        } catch (SQLException e) {
            Log.e(TAG + "setupCategoriasSpinner()", "Erro: " + e.getMessage());
            MensagemUtil.getMensagem(getApplicationContext(), e);
        }

        categoriasAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, categorias);
        categoriasAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        categoriasSpinner.setAdapter(categoriasAdapter);
    }

    @SuppressLint("LongLogTag")
    private void setupContasSpinner() {
        try {
            contaService = new ContaService(this);
            contas = contaService.getContas();
        } catch (SQLException e) {
            Log.e(TAG + "setupContasSpinner()", "Erro: " + e.getMessage());
            MensagemUtil.getMensagem(getApplicationContext(), e);
        }

        contasAdapter = new ArrayAdapter<Conta>
                (this, android.R.layout.simple_spinner_item, contas);
        contasAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        contasSpinner.setAdapter(contasAdapter);
    }

    private boolean validateViews() {
        if (validateValor() && validateDescricao()) {
            return true;
        } else {
            return false;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateDescricao() {
        if (descricaoEditText.getText().toString().trim().isEmpty()) {
            descricaoInputLayout.setError(getString(R.string.label_erro_transacao_descricao));
            requestFocus(descricaoEditText);
            return false;
        } else {
            descricaoInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateValor() {
        if (valorEditText.getText().toString().trim().isEmpty()) {
            valorInputLayout.setError(getString(R.string.label_erro_transacao_valor));
            requestFocus(valorEditText);
            return false;
        } else {
            valorInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void setupViews() {
        descricaoEditText = (EditText) findViewById(R.id.descricao_editText);
        categoriasSpinner = (Spinner) findViewById(R.id.categoria_spinner);
        contasSpinner = (Spinner) findViewById(R.id.conta_spinner);
        valorEditText = (EditText) findViewById(R.id.valor_editText);
        vencimentoTextView = (TextView) findViewById(R.id.vencimento_textView);
        descricaoInputLayout = (TextInputLayout) findViewById(R.id.descricao_textInputLayout);
        valorInputLayout = (TextInputLayout) findViewById(R.id.valor_textInputLayout);
        efetivadoCheckBox = (CheckBox) findViewById(R.id.efetivado_checkBox);
        tipoRadioGroup = (RadioGroup) findViewById(R.id.tipo_RadioGroup);
        despesaRadioButton = (RadioButton) findViewById(R.id.despesa_radioButton);
        receitaRadioButton = (RadioButton) findViewById(R.id.receita_radioButton);
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @SuppressLint("LongLogTag")
    public void excluir() {
        try {
            mService = new TransacaoService(this);
            mService.excluirTransacao(transacao);
            MensagemUtil.getMensagem(getApplicationContext(), "A transação foi excluida.");
            finish();
        } catch (SQLException e) {
            Log.e(TAG + "excluir()", "Erro ao excluir transação." + e.getMessage());
            e.printStackTrace();
            MensagemUtil.getMensagem(getApplicationContext(), e);
        }
    }

    @SuppressLint("LongLogTag")
    public void salvar() {

        viewsToEntity();

        try {

            if (efetivadoCheckBox.isChecked()) {
                transacao.setEfetivado(true);
                if (transacao.efetivar(getApplicationContext(), transacao)) {
                    mService = new TransacaoService(this);
                    mService.salvarEfetivarTransacao(transacao);
                    MensagemUtil.getMensagem(getApplicationContext(), "A transação foi salva e efetivada.");
                }
            } else {
                transacao.setEfetivado(false);
                mService = new TransacaoService(this);
                if (transacao.getId() == null) {
                    mService.salvarTransacao(transacao);
                    MensagemUtil.getMensagem(getApplicationContext(), "A transação foi salva.");
                } else {
                    mService.atualizarTransacao(transacao);
                    MensagemUtil.getMensagem(getApplicationContext(), "A transação foi alterada.");
                }
            }
            finish();

        } catch (SQLException e) {

            Log.e(TAG + "salvar()", "Erro ao salvar transação." + e.getMessage());
            e.printStackTrace();
            MensagemUtil.getMensagem(getApplicationContext(), e);

        }
    }

    @SuppressLint("LongLogTag")
    private void efetivar() {
        if (transacao.efetivar(getApplicationContext(), transacao)) {
            try {
                mService = new TransacaoService(this);
                mService.efetivarTransacao(transacao);
                MensagemUtil.getMensagem(getApplicationContext(), "A transação foi efetivada.");
                finish();

            } catch (SQLException e) {
                MensagemUtil.getMensagem(getApplicationContext(), e);
                e.printStackTrace();
                Log.e(TAG + "efetivar()", "Erro ao efetivar transação. Erro: " + e.getMessage());
            }
        }
    }

    @SuppressLint("LongLogTag")
    private void estornar() {
        if (transacao.estornar(getApplicationContext(), transacao)) {
            try {
                mService = new TransacaoService(this);
                mService.estornarTransacao(transacao);
                MensagemUtil.getMensagem(getApplicationContext(), "A transaçao foi estornada.");
                finish();
            } catch (SQLException e) {
                MensagemUtil.getMensagem(getApplicationContext(), e);
                e.printStackTrace();
                Log.e(TAG + "estornar()", "Erro ao estornar transação. Erro: " + e.getMessage());
            }
        }
    }

    public void showDialogChoice() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        String msn = "";
        String title = "";

        if (action == SALVAR_ACTION) {
            msn = "Confirme para salvar a transação.";
            title = "Salvar";
        } else if (action == EXCLUIR_ACTION) {
            msn = "Confirme para excluir a transação.";
            title = "Excluir";
        } else if (action == EFETIVAR_ACTION) {
            msn = "Confirme para efetivar a transação.";
            title = "Efetivar";
        } else if (action == ESTORNAR_ACTION) {
            msn = "Confirme para estornar a transação.";
            title = "Estornar";
        }

        alertDialogBuilder.setMessage(msn);
        alertDialogBuilder.setTitle(title);

        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                if (action == SALVAR_ACTION) {
                    salvar();
                } else if (action == EXCLUIR_ACTION) {
                    excluir();
                } else if (action == EFETIVAR_ACTION) {
                    efetivar();
                } else if (action == ESTORNAR_ACTION) {
                    estornar();
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Integer dia = null, mes = null, ano = null;

        if (transacao.getId() == null) {
            Calendar calendario = Calendar.getInstance();

            ano = calendario.get(Calendar.YEAR);
            mes = calendario.get(Calendar.MONTH);
            dia = calendario.get(Calendar.DAY_OF_MONTH);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(transacao.getVencimento());

            ano = cal.get(Calendar.YEAR);
            mes = cal.get(Calendar.MONTH);
            dia = cal.get(Calendar.DAY_OF_MONTH);
        }

        switch (id) {
            case R.id.vencimento_textView:
                return new DatePickerDialog(this, mDateSetListener, ano, mes, dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        String data;

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            data = dayOfMonth + "/"
                    + (monthOfYear + 1) + "/" + year;

            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, monthOfYear);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            vencimentoTextView.setText(data);
            transacao.setVencimento(c.getTime());
        }
    };

    public void showDialog(View v) {
        showDialog(v.getId());
    }

}