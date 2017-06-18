package com.joseilton.mobilecontroller.categoria;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.joseilton.mobilecontroller.R;
import com.joseilton.mobilecontroller.util.MensagemUtil;

import java.sql.SQLException;

import petrov.kristiyan.colorpicker.ColorPicker;

public class DetalhesCategoriaActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "DetalhesCategoriaActivity.";

    public static final int SALVAR_ACTION = 1;
    public static final int EXCLUIR_ACTION = 2;
    private int action = 0;

    private CategoriaService mService;
    private Categoria categoria;
    private int colors[];

    private RadioButton debitoRadioButton;
    private RadioButton creditoRadioButton;
    private EditText descricaoEditText;
    private ImageView colorImageView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_categoria);

        inicializarCores();
        inicializarToolbar();
        inicializarViews();
        inicializarCores();
        init();

    }

    private void inicializarViews() {
        debitoRadioButton = (RadioButton) findViewById(R.id.debito_RadioButton);
        creditoRadioButton = (RadioButton) findViewById(R.id.credito_RadioButton);
        descricaoEditText = (EditText) findViewById(R.id.descricaoEditText);
        colorImageView = (ImageView) findViewById(R.id.colorImageView);

        colorImageView.setOnClickListener(this);
        debitoRadioButton.setOnClickListener(this);
        creditoRadioButton.setOnClickListener(this);
    }

    private void inicializarToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalhes_categoria, menu);

        if (categoria.getId() == null) {
            menu.getItem(1).setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        if (item.getItemId() == R.id.action_salvar) {
            if (validateViews()) {
                viewsToEntity();
                action = SALVAR_ACTION;
                showDialogChoice();
            }
        } else if (item.getItemId() == R.id.action_excluir) {
            action = EXCLUIR_ACTION;
            showDialogChoice();
        }

        return super.onOptionsItemSelected(item);
    }

    private void inicializarCores() {
        colors = new int[25];
        colors[0] = Color.parseColor("#00BFFF");
        colors[1] = Color.parseColor("#B0C4DE");
        colors[2] = Color.parseColor("#BC8F8F");
        colors[3] = Color.parseColor("#C0FF3E");
        colors[4] = Color.parseColor("#D2B48C");
        colors[5] = Color.parseColor("#EE82EE");
        colors[6] = Color.parseColor("#FA9F00");
        colors[7] = Color.parseColor("#00008B");
        colors[8] = Color.parseColor("#0000FF");
        colors[9] = Color.parseColor("#828282");
        colors[10] = Color.parseColor("#CD0000");
        colors[11] = Color.parseColor("#CD950C");
        colors[12] = Color.parseColor("#EE2C2C");
        colors[13] = Color.parseColor("#A9A9A9");
        colors[14] = Color.parseColor("#9A32CD");
        colors[15] = Color.parseColor("#CD00CD");
        colors[16] = Color.parseColor("#4EEE94");
        colors[17] = Color.parseColor("#00EEEE");
        colors[18] = Color.parseColor("#FFD700");
        colors[19] = Color.parseColor("#228B22");
        colors[20] = Color.parseColor("#000000");
        colors[21] = Color.parseColor("#D2691E");
        colors[22] = Color.parseColor("#E6E6FA");
        colors[23] = Color.parseColor("#FF8C00");
        colors[24] = Color.parseColor("#FF0000");
    }


    private boolean validateViews() {
        boolean validate = false;
        if (!"".equals(descricaoEditText.getText().toString().trim())) {
            validate = true;
        } else {
            Toast.makeText(getApplicationContext(), "Campo inválido", Toast.LENGTH_LONG).show();
            descricaoEditText.requestFocus();
            descricaoEditText.setText("");
            descricaoEditText.setHint("Informe a descrição...");
            descricaoEditText.setHintTextColor(Color.RED);
        }
        return validate;
    }

    private void init() {
        Intent intent = getIntent();
        categoria = (Categoria) intent.getSerializableExtra("categoria");

        entityToViews();
        if (categoria.getId() == null) {
            categoria.setTipo(CategoriaTipo.Debito);

        } else {
            debitoRadioButton.setEnabled(false);
            creditoRadioButton.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.debito_RadioButton) {
            if (((RadioButton) view).isChecked()) {
                categoria.setTipo(CategoriaTipo.Debito);
            }
        } else if (view.getId() == R.id.credito_RadioButton) {
            if (((RadioButton) view).isChecked()) {
                categoria.setTipo(CategoriaTipo.Credito);
            }
        } else if (view.getId() == R.id.colorImageView) {
            final ColorPicker colorPicker = new ColorPicker(DetalhesCategoriaActivity.this);


            colorPicker.setColors(colors);
            colorPicker.setColumns(5);
            colorPicker.setDefaultColorButton(colors[0]);
            colorPicker.setTitle("Escolha uma cor");

            colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                @Override
                public void onChooseColor(int position, int color) {
                    colorImageView.setColorFilter(color);
                    categoria.setCor(color);
                }

                @Override
                public void onCancel() {

                }
            });
            colorPicker.setRoundColorButton(true);
            colorPicker.show();
        }
    }

    private void entityToViews() {
        if (categoria.getId() != null) {
            if (categoria.getTipo().equals(CategoriaTipo.Debito)) {
                debitoRadioButton.setChecked(true);
            } else {
                creditoRadioButton.setChecked(true);
            }
            descricaoEditText.setText(categoria.getDescricao());
            colorImageView.setColorFilter(categoria.getCor());

        } else {
            debitoRadioButton.setChecked(true);
            colorImageView.setColorFilter(colors[0]);
        }

    }

    private void viewsToEntity() {
        categoria.setDescricao(descricaoEditText.getText().toString());
        if(categoria.getCor() == 0) {
            categoria.setCor(colors[0]);
        }
    }

    @SuppressLint("LongLogTag")
    public void excluir() {
        mService = new CategoriaService(this);
        if (mService.validarExclusao(categoria)) {
            try {
                mService.excluirCategoria(categoria);
                MensagemUtil.getMensagem(getApplicationContext(), "A conta foi excluida com sucesso.");
                finish();
            } catch (SQLException e) {
                Log.e(TAG + "excluir()", "Erro ao excluir categoria." + e.getMessage());
                e.printStackTrace();
                MensagemUtil.getMensagem(getApplicationContext(), e.getMessage());
            }
        } else {
            MensagemUtil.getMensagem(getApplicationContext(), "Não foi possível excluir a categoria.\nCategoria vinculada a transação.");
        }

    }

    @SuppressLint("LongLogTag")
    public void salvar() {
        if (validateViews()) {
            viewsToEntity();
            try {
                mService = new CategoriaService(this);
                if (categoria.getId() == null) {
                    mService.salvarCategoria(categoria);
                    MensagemUtil.getMensagem(getApplicationContext(), "A categoria foi salva com sucesso.");
                } else {
                    mService.atualizarCategoria(categoria);
                    MensagemUtil.getMensagem(getApplicationContext(), "A categoria foi alterada com sucesso");
                }
                finish();
            } catch (SQLException e) {
                Log.e(TAG + "salvar()", "Erro ao salvar categoria." + e.getMessage());
                e.printStackTrace();
                MensagemUtil.getMensagem(getApplicationContext(), e.getMessage());
            }
        }

    }

    public void showDialogChoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String mensagem = "", titulo = "";

        if (action == SALVAR_ACTION) {
            titulo = "Salvar";
            mensagem = "Confirme para salvar a categoria.";
        } else if (action == EXCLUIR_ACTION) {
            titulo = "Excluir";
            mensagem = "Confirme para excluir a categoria.";
        }

        builder.setMessage(mensagem);
        builder.setTitle(titulo);


        builder.setPositiveButton("Ok", (dialog, arg1) -> {

            if (action == SALVAR_ACTION) {
                salvar();
            } else if (action == EXCLUIR_ACTION) {
                excluir();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> {

        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
