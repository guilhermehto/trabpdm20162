package com.ghelfer.trabalhopdm20162;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class ListarFrequenciasActivity extends AppCompatActivity {

    final String URL_POST_FREQUENCIA = "http://ghelfer.net/pdm/ListaFrequenciaAtividade.aspx";
    final String URL_GET_ATIVIDADES = "http://ghelfer.net/pdm/ListaAtividade.aspx";
    final String URL_EDITAR_FREQUENCIA = "http://ghelfer.net/pdm/AtualizaFrequenciaAtividade.aspx";


    private List<Map<String,String>> listaDeAtividades;
    final ArrayList<String> listaIdsAtividades = new ArrayList<String>();
    private List<String> listaFrequencias;

    private ListView listViewFrequencias;
    private CheckBox checkBoxFrequencia;

    String idpessoa;
    String idAtividade;

    AsyncHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_frequencias);
        checkBoxFrequencia = (CheckBox) findViewById(R.id.checkBoxFrequencia);

        idpessoa = getIntent().getStringExtra("idpessoa");
        listaDeAtividades = new ArrayList<>();
        listaFrequencias = new ArrayList<>();
        listViewFrequencias = (ListView) findViewById(R.id.listViewFrequenciasG6);
        listViewFrequencias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,String> map = listaDeAtividades.get(position);
                EditText txtTitulo = (EditText) findViewById(R.id.txtTituloAtividade);
                txtTitulo.setText(map.get("titulo"));
                idAtividade = listaIdsAtividades.get(position);
                switch(map.get("frequencia")){
                    case "true":
                        checkBoxFrequencia.setChecked(true);
                        break;
                    default:
                        checkBoxFrequencia.setChecked(false);
                        break;
                }
            }
        });



        client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("idpessoa", idpessoa);
        client.post(URL_POST_FREQUENCIA, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    String data = new String(responseBody);
                    JSONObject res = new JSONObject(data);
                    JSONArray jsonArray = res.getJSONArray("Frequencia");
                    if(jsonArray.length() < 1){
                        //TODO: Mostrar q n tem nada
                        Toast.makeText(ListarFrequenciasActivity.this, "Usuário sem frequência", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject obj = jsonArray.getJSONObject(i);
                        listaIdsAtividades.add(obj.getString("idatividade"));
                        listaFrequencias.add(obj.getString("frequencia"));
                    }

                } catch (Exception ex){
                    Log.e("ERRO", ex.getMessage());
                }

                RequestParams params = new RequestParams();
                client.get(URL_GET_ATIVIDADES, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            String data = new String(responseBody);
                            JSONObject res = new JSONObject(data);
                            JSONArray jsonArray = res.getJSONArray("Atividade");
                            for(int i = 0;i<jsonArray.length();i++){
                                JSONObject obj = jsonArray.getJSONObject(i);
                                HashMap<String,String> map = new HashMap<String, String>();
                                map.put("titulo", obj.getString("titulo"));
                                map.put("local", obj.getString("local"));
                                for(String id : listaIdsAtividades){
                                    if(obj.getString("idatividade").equals(id)){
                                        map.put("frequencia", listaFrequencias.get(i));
                                        listaDeAtividades.add(map);
                                    }
                                }
                            }

                            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(),
                                    listaDeAtividades,
                                    R.layout.lista_cell_atividade,
                                    new String[] {"titulo", "local", "frequencia"},
                                    new int[] {R.id.itemTitulo, R.id.itemLocal, R.id.itemFrequencia});
                            listViewFrequencias.setAdapter(adapter);

                        } catch (Exception ex){
                            Log.e("ERRO", ex.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ListarFrequenciasActivity.this, "Erro de conexão, tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    protected void onBtnSalvarClicked(View v){
        TextView txt = (TextView) findViewById(R.id.txtTituloAtividade);
        if(txt.getText().toString().isEmpty()){
            Toast.makeText(this, "Você precisa selecionar uma atividade para editar", Toast.LENGTH_SHORT).show();
            return;
        }
        txt.setText("");
        client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("idinscricaoatividade", idAtividade);
        if(checkBoxFrequencia.isChecked()){
            params.put("frequencia", true);
        } else {
            params.put("frequencia", false);
        }

        client.post(URL_EDITAR_FREQUENCIA, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(ListarFrequenciasActivity.this, "Frequencia editada com sucesso", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ListarFrequenciasActivity.this, "Ocorreu um erro, tente novamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
