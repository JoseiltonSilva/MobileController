package com.joseilton.mobilecontroller.transferencia;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.joseilton.mobilecontroller.R;
import com.joseilton.mobilecontroller.conta.Conta;
import com.joseilton.mobilecontroller.conta.ContaService;
import com.joseilton.mobilecontroller.conta.ContaTipo;
import com.joseilton.mobilecontroller.util.ColorUtil;
import com.joseilton.mobilecontroller.util.MensagemUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DetalhesTransferenciaActivity extends AppCompatActivity {

    private static final String TAG = "DetalhesTransferenciaActivity.";
    private static final int SALVAR_ACTION = 1;
    private static final int EXCLUIR_ACTION = 2;

    private int action;

    private TransferenciaService mService;
    private Transferencia transferencia;
    private List<Conta> contas;
    private ArrayAdapter<Conta> origemAdapter;
    private ArrayAdapter<Conta> destinoAdapter;

    private EditText descricaoEditText, valorEditText;
    private Spinner origemSpinner, destinoSpinner;
    private TextInputLayout descricaoInputLayout, valorInputLayout;
    private TextView dataTextView;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_transferencia);

        getContas();

        setupToolbar();
        setupViews();
        setupSpinnerOrigem();
        setupSpinnerDestino();
        init();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalhes_transferencia, menu);

        if (transferencia.getId() == null) menu.findItem(R.id.action_excluir).setVisible(false);
        else menu.findItem(R.id.action_salvar).setVisible(false);

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
                if (validateViews()) {
                    action = SALVAR_ACTION;
                    showDialogChoice();
                }
                break;
            case R.id.action_excluir:
                action = EXCLUIR_ACTION;
                showDialogChoice();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        Intent intent = getIntent();
        transferencia = (Transferencia) intent.getSerializableExtra("transferencia");

        entityToViews();

    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupViews() {
        descricaoEditText = (EditText) findViewById(R.id.descricao_editText);
        origemSpinner = (Spinner) findViewById(R.id.origem_spinner);
        destinoSpinner = (Spinner) findViewById(R.id.destino_spinner);
        valorEditText = (EditText) findViewById(R.id.valor_editText);
        dataTextView = (TextView) findViewById(R.id.data_textView);
        descricaoInputLayout = (TextInputLayout) findViewById(R.id.descricao_textInputLayout);
        valorInputLayout = (TextInputLayout) findViewById(R.id.valor_textInputLayout);
    }

    private void getContas() {
        ContaService service = new ContaService(this);
        try {
            contas = service.getContas();
        } catch (SQLException e) {
            e.printStackTrace();
            MensagemUtil.getMensagem(this, e);
        }

    }

    private void setupSpinnerOrigem() {

        origemAdapter = new ArrayAdapter<Conta>
                (this, android.R.layout.simple_spinner_item, contas);
        origemAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        origemSpinner.setAdapter(origemAdapter);
    }

    private void setupSpinnerDestino() {

        destinoAdapter = new ArrayAdapter<Conta>
                (this, android.R.layout.simple_spinner_item, contas);
        destinoAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        destinoSpinner.setAdapter(origemAdapter);
    }


    private void entityToViews() {
        descricaoEditText.setText(transferencia.getDescricao());

        if (transferencia.getId() == null) {
            valorEditText.setText("");
        } else {
            valorEditText.setText(transferencia.getValor().toString());
            origemSpinner.setSelection(origemAdapter.getPosition(transferencia.getContaOrigem()));
            destinoSpinner.setSelection(destinoAdapter.getPosition(transferencia.getContaOrigem()));
        }
    }

    private void viewsToEntity() {
        transferencia.setDescricao(descricaoEditText.getText().toString());
        transferencia.setValor(new BigDecimal(valorEditText.getText().toString()));
        transferencia.setContaOrigem(origemAdapter.getItem(origemSpinner.getSelectedItemPosition()));
        transferencia.setContaDestino(destinoAdapter.getItem(destinoSpinner.getSelectedItemPosition()));
    }

    private boolean validateViews() {
        if (validateValor() && validateDescricao() && validateData() && validateContas()) {
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
            descricaoInputLayout.setError(getString(R.string.label_erro_transferencia_descricao));
            requestFocus(descricaoEditText);
            return false;
        } else {
            descricaoInputLayout.setErrorEnabled(false);
        }
        return true;

    }

    private boolean validateValor() {
        if (valorEditText.getText().toString().trim().isEmpty()) {
            valorInputLayout.setError(getString(R.string.label_erro_transferencia_valor));
            requestFocus(valorEditText);
            return false;
        } else {
            valorInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateContas() {
        if (origemAdapter.getItem(origemSpinner.getSelectedItemPosition())
                .equals(destinoAdapter.getItem(destinoSpinner.getSelectedItemPosition()))) {
            MensagemUtil.getMensagem(this, "A Conta Destino deve ser diferente da Conta Origem.");
            requestFocus(destinoSpinner);
            return false;
        }
        return true;
    }

    private boolean validateData() {
        if (dataTextView.getText().toString().trim().isEmpty()) {
            dataTextView.setText(R.string.label_erro_transferencia_data);
            dataTextView.setTextColor(ColorUtil.DARK_RED);
            requestFocus(dataTextView);
            return false;
        }
        return true;
    }

    @SuppressLint("LongLogTag")
    public void excluir() {
        mService = new TransferenciaService(this);
        if (transferencia.estornar(this)) {
            try {
                mService.excluirTransferencia(transferencia);
                MensagemUtil.getMensagem(getApplicationContext(), "A transferência foi excluida com sucesso.");
                finish();
            } catch (SQLException e) {
                Log.e(TAG + "excluir()", "Erro ao excluir transferência." + e.getMessage());
                e.printStackTrace();
                MensagemUtil.getMensagem(getApplicationContext(), e.getMessage());
            }
        }

    }

    @SuppressLint("LongLogTag")
    public void salvar() {
        viewsToEntity();
        if (transferencia.efetivar(this)) {
            try {
                mService = new TransferenciaService(this);
                mService.salvarTransferencia(transferencia);
                MensagemUtil.getMensagem(getApplicationContext(), "A transferência foi salva com sucesso.");
                finish();
            } catch (SQLException e) {
                Log.e(TAG + "salvar()", "Erro ao salvar transferência." + e.getMessage());
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
            msn = "Confirme para salva a transferência.";
            title = "Salvar";
        } else if (action == EXCLUIR_ACTION) {
            msn = "Confirme para excluir a transferência.";
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

    @Override
    protected Dialog onCreateDialog(int id) {
        Integer dia = null, mes = null, ano = null;

        if (transferencia.getId() == null) {
            Calendar calendario = Calendar.getInstance();

            ano = calendario.get(Calendar.YEAR);
            mes = calendario.get(Calendar.MONTH);
            dia = calendario.get(Calendar.DAY_OF_MONTH);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(transferencia.getData());

            ano = cal.get(Calendar.YEAR);
            mes = cal.get(Calendar.MONTH);
            dia = cal.get(Calendar.DAY_OF_MONTH);
        }

        switch (id) {
            case R.id.data_textView:
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

            dataTextView.setText(data);
            transferencia.setData(c.getTime());
        }
    };

    public void showDialog(View v) {
        showDialog(v.getId());
    }

}
