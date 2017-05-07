package com.joseilton.mobilecontroller.app;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.joseilton.mobilecontroller.R;
import com.joseilton.mobilecontroller.conta.ContasFragment;
import com.joseilton.mobilecontroller.transacao.TransacoesFragment;
import com.joseilton.mobilecontroller.util.MensagemUtil;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private long lastBackPressTime = 0;
    private Toast toast;

    private Toolbar toolbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

                switch (item.getItemId()) {
                    case R.id.navigation_home :
                        fragment = new HomeFragment();
                        break;
                    case R.id.navigation_transacoes :
                        fragment = new TransacoesFragment();
                        break;
                    case R.id.navigation_contas :
                        fragment = new ContasFragment();
                        break;

                }

                final FragmentTransaction transaction;
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content, fragment);
                //transaction.addToBackStack("pilha");
                transaction.commit();

                return true;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            fragment = new HomeFragment();
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content, fragment);
            //transaction.addToBackStack("pilha");
            transaction.commit();
        }

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onBackPressed() {

        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            toast = Toast.makeText(this, "Pressione o Botão Voltar novamente para fechar o Aplicativo.", Toast.LENGTH_LONG);
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            if (toast != null) {
                toast.cancel();
            }
            super.onBackPressed();
        }
    }
}
