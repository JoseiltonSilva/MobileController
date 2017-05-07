package com.joseilton.mobilecontroller.conta;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.Spinner;

import com.joseilton.mobilecontroller.R;
import com.joseilton.mobilecontroller.conta.Conta;
import com.joseilton.mobilecontroller.conta.ContaTipo;
import com.joseilton.mobilecontroller.conta.ContaService;
import com.joseilton.mobilecontroller.util.MensagemUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetalhesContaActivity extends AppCompatActivity {

    private static final String TAG = "DetalhesContaActivity.";
    private static final int SALVAR_ACTION = 1;
    private static final int EXCLUIR_ACTION = 2;

    private int action;

    private ContaService mService;
    private Conta conta;
    private List<ContaTipo> tipos;
    private ArrayAdapter<ContaTipo> spinnerAdapter;

    private EditText descricaoEditText, saldoEditText, limiteEditText;
    private Spinner tipoSpinner;
    private TextInputLayout descricaoInputLayout, saldoInputLayout, limiteInputLayout;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_conta);

        setupToolbar();
        setupViews();
        setupSpinner();
        init();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalhes_conta, menu);

        if (conta.getId() == null) {
            menu.findItem(R.id.action_excluir).setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }


        switch (id) {
            case R.id.action_salvar:
                action = SALVAR_ACTION;
                showDialogChoice();
                break;
            case R.id.action_excluir:
                action = EXCLUIR_ACTION;
                showDialogChoice();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void entityToViews() {
        descricaoEditText.setText(conta.getDescricao());

        if (conta.getId() == null) {
            saldoEditText.setText("");
            limiteEditText.setText("");
        } else {
            saldoEditText.setText(conta.getSaldo().toString());
            limiteEditText.setText(conta.getLimite().toString());
            tipoSpinner.setSelection(spinnerAdapter.getPosition(conta.getTipo()));
        }
    }

    private void viewsToEntity() {
        conta.setDescricao(descricaoEditText.getText().toString());
        conta.setSaldo(new BigDecimal(saldoEditText.getText().toString()));
        conta.setLimite(new BigDecimal(limiteEditText.getText().toString()));
        conta.setTipo(spinnerAdapter.getItem(tipoSpinner.getSelectedItemPosition()));
    }

    private void setupSpinner() {
        tipos = new ArrayList<>();
        for(ContaTipo tipo : ContaTipo.values()) {
            tipos.add(tipo);
        }
        spinnerAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, tipos);
        spinnerAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        tipoSpinner.setAdapter(spinnerAdapter);
    }

    private boolean validateViews() {
        if (validateDescricao() && validateSaldo() && validateLimite()) {
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
            descricaoInputLayout.setError(getString(R.string.label_erro_conta_descricao));
            requestFocus(descricaoEditText);
            return false;
        } else {
            descricaoInputLayout.setErrorEnabled(false);
        }
        return true;

    }

    private boolean validateSaldo() {
        if (saldoEditText.getText().toString().trim().isEmpty()) {
            saldoInputLayout.setError(getString(R.string.label_erro_conta_saldo));
            requestFocus(saldoEditText);
            return false;
        } else {
            saldoInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateLimite() {
        if (limiteEditText.getText().toString().trim().isEmpty()) {
            limiteInputLayout.setError(getString(R.string.label_erro_conta_limite));
            requestFocus(limiteEditText);
            return false;
        } else {
            limiteInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void init() {
        Intent intent = getIntent();
        conta = (Conta) intent.getSerializableExtra("conta");

        entityToViews();

        if (conta.getId() != null) {
            saldoEditText.setEnabled(false);
        }
    }

    private void setupViews() {
        descricaoEditText = (EditText) findViewById(R.id.descricao_editText);
        tipoSpinner = (Spinner) findViewById(R.id.tipo_spinner);
        saldoEditText = (EditText) findViewById(R.id.saldo_editText);
        limiteEditText = (EditText) findViewById(R.id.limite_editText);
        descricaoInputLayout = (TextInputLayout) findViewById(R.id.descricao_textInputLayout);
        saldoInputLayout = (TextInputLayout) findViewById(R.id.saldo_textInputLayout);
        limiteInputLayout = (TextInputLayout) findViewById(R.id.limite_textInputLayout);
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @SuppressLint("LongLogTag")
    public void excluir() {
        mService = new ContaService(this);
        if (mService.validarExclusao(conta)) {
            try {
                mService.excluirConta(conta);
                MensagemUtil.getMensagem(getApplicationContext(), "A conta foi excluida com sucesso.");
                finish();
            } catch (SQLException e) {
                Log.e(TAG + "excluir()", "Erro ao excluir conta." + e.getMessage());
                e.printStackTrace();
                MensagemUtil.getMensagem(getApplicationContext(), e.getMessage());
            }
        } else {
            MensagemUtil.getMensagem(getApplicationContext(), "Não foi possível excluir a conta.\nConta vinculada a transação ou transferência.");
        }

    }

    @SuppressLint("LongLogTag")
    public void salvar() {
        if (validateViews()) {
            viewsToEntity();
            try {
                mService = new ContaService(this);
                if (conta.getId() == null) {
                    mService.salvarConta(conta);
                    MensagemUtil.getMensagem(getApplicationContext(), "A conta foi salva com sucesso.");
                } else {
                    mService.atualizarConta(conta);
                    MensagemUtil.getMensagem(getApplicationContext(), "A conta foi alterada com sucesso");
                }
                finish();
            } catch (SQLException e) {
                Log.e(TAG + "salvar()", "Erro ao salvar conta." + e.getMessage());
                e.printStackTrace();
                MensagemUtil.getMensagem(getApplicationContext(), e.getMessage());
            }
        }

    }


    public void showDialogChoice() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);


        String msn = "";
        String title = "";


        if (action == SALVAR_ACTION) {
            msn = "Confirme para salva a conta.";
            title = "Salvar";
        } else if (action == EXCLUIR_ACTION) {
            msn = "Confirme para excluir a conta.";
            title = "Excluir";
        }


        alertDialogBuilder.setMessage(msn);
        alertDialogBuilder.setTitle(title);


        alertDialogBuilder.setPositiveButton("Ok", (dialog, arg1) -> {

            if (action == SALVAR_ACTION) {
                salvar();
            } else if (action == EXCLUIR_ACTION) {
                excluir();
            }

        });

        alertDialogBuilder.setNegativeButton("Cancelar", (dialog, which) -> {

        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
