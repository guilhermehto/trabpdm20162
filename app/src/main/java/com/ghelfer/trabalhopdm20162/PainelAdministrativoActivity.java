package com.ghelfer.trabalhopdm20162;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PainelAdministrativoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painel_administrativo);
    }

    protected void onListarPagamentos(View v){
        Intent intent = new Intent(this, ListarPessoasActivity.class);
        startActivity(intent);
    }

    protected void onListarPessoas(View v){
        Intent intent = new Intent(this, ListarPessoasActivity.class);
        startActivity(intent);
    }

    protected void onListarFrequencia(View v){
        Intent intent = new Intent(this, ListarFrequenciasActivity.class);
        startActivity(intent);
    }
}
