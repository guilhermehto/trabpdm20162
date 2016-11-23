package com.ghelfer.trabalhopdm20162;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdministradorActivity extends AppCompatActivity {

    private final String LOGIN_PAINEL = "admin";
    private final String SENHA_PAINEL = "admin";

    private EditText editLogin;
    private EditText editSenha;
    private Button   btnEntrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);

        editLogin = (EditText) findViewById(R.id.editLoginAdminG6);
        editSenha = (EditText) findViewById(R.id.editSenhaAdminG6);
        btnEntrar = (Button)   findViewById(R.id.btnEntrarPainelG6);
    }


    protected void onBtnEntrarClickedG6(View v){
        String senha = editSenha.getText().toString();
        String login = editLogin.getText().toString();
        if(senha.equals(SENHA_PAINEL) && login.equals(LOGIN_PAINEL)){
            Intent i = new Intent(this, PainelAdministrativoActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(AdministradorActivity.this, "Senha ou login inválido", Toast.LENGTH_SHORT).show();
        }


    }
}
