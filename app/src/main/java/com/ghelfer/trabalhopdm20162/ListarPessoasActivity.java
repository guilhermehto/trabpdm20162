package com.ghelfer.trabalhopdm20162;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class ListarPessoasActivity extends AppCompatActivity {

    private final String URL_GET_PESSOAS = "http://ghelfer.net/pdm/ListaPessoa.aspx";
    private final String URL_ATUALIZAR_PESSOA = "http://ghelfer.net/pdm/AtualizaPessoa.aspx";

    private List<Map<String,String>> lista;
    private ListView listView;
    private String idPessoa;

    private EditText txtNome;
    private EditText txtMatricula;
    private EditText txtEmail;
    private CheckBox radioStatus;



    AsyncHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_pessoas);

        txtNome = (EditText) findViewById(R.id.textNomeG6);
        txtMatricula = (EditText) findViewById(R.id.textMatriculaG6);
        txtEmail = (EditText) findViewById(R.id.textEmailG6);

        radioStatus = (CheckBox) findViewById(R.id.radioStatusG6);

        lista = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listViewPessoas);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,String> map = lista.get(position);
                //id = Integer.parseInt(mapa.get("idPessoa"));
                idPessoa = map.get("idpessoa");
                txtNome.setText(map.get("nome"));
                txtEmail.setText(map.get("email"));
                txtMatricula.setText(map.get("matricula"));
                switch (map.get("status")){
                    case "H":
                        radioStatus.setChecked(true);
                        break;
                    default:
                        radioStatus.setChecked(false);
                        break;
                }
            }
        });
        
        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ListarPessoasActivity.this, "Selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ListarPessoasActivity.this, "Long Click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });




        client = new AsyncHttpClient();
        client.post(URL_GET_PESSOAS, new AsyncHttpResponseHandler() {



            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String data = new String(responseBody);
                    JSONObject res = new JSONObject(data);
                    JSONArray array = res.getJSONArray("Pessoa");
                    for(int i = 0;i < array.length();i++){
                        JSONObject obj = array.getJSONObject(i);
                        Map<String,String> mapa = new HashMap<String, String>();
                        mapa.put("status",obj.getString("status"));
                        mapa.put("idpessoa",obj.getString("idpessoa"));
                        mapa.put("nome",obj.getString("nome"));
                        mapa.put("matricula",obj.getString("matricula"));
                        mapa.put("email",obj.getString("email"));
                        lista.add(mapa);
                    }

                    SimpleAdapter adapter = new SimpleAdapter(getBaseContext(),
                            lista,
                            R.layout.lista_cell_pessoa,
                            new String[] { "status", "idpessoa","nome", "matricula", "email"},
                            new int[] {R.id.itemStatusPessoa, R.id.itemPessoaId, R.id.itemPessoaNome, R.id.itemPessoaMatricula, R.id.itemPessoaEmail});

                    listView.setAdapter(adapter);

                } catch (Exception e){
                    Log.d("LOG_KEYS", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ListarPessoasActivity.this, "Erro: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list_pessoa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(idPessoa == null){
            Toast.makeText(this, "Selecione um usuário antes.", Toast.LENGTH_SHORT).show();
            return true;
        }

        Intent intent;
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.menuListarFrequenciaG6:
                intent = new Intent(this, ListarFrequenciasActivity.class);
                intent.putExtra("idpessoa", idPessoa);
                startActivity(intent);
                return true;
            case R.id.menuListarPagamentosG6:
                intent = new Intent(this, ListarPagamentosActivity.class);
                intent.putExtra("idpessoa", idPessoa);
                startActivity(intent);
                return true;
        }
        return true;
    }


    protected void onBtnAtualizarClicked(View v){
        String nome = txtNome.getText().toString();
        String matricula = txtMatricula.getText().toString();
        String email = txtEmail.getText().toString();
        boolean habilitado = radioStatus.isChecked();

        txtNome.setText("");
        txtMatricula.setText("");
        txtEmail.setText("");
        radioStatus.setChecked(false);

        if(nome.isEmpty() || matricula.isEmpty() || email.isEmpty()){
            Toast.makeText(this, "Você precisa preencher todos os campos", Toast.LENGTH_LONG).show();
            return;
        }

        RequestParams params = new RequestParams();
        params.add("idpessoa", idPessoa.toString());
        params.add("matr", matricula);
        params.add("nome", nome);
        params.add("email", email);
        if(habilitado) {
            params.add("status", "H");
        } else {
            params.add("status", "D");
        }

        client.post(URL_ATUALIZAR_PESSOA, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Toast.makeText(ListarPessoasActivity.this, "Pessoa atualizada com sucesso", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ListarPessoasActivity.this, "Erro na atualização de pessoa", Toast.LENGTH_SHORT).show();
            }
        });





    }
}
