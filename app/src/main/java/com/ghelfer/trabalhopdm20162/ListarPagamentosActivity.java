package com.ghelfer.trabalhopdm20162;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ListarPagamentosActivity extends AppCompatActivity {

    private final String URL_LISTA_PAGAMENTO = "http://ghelfer.net/pdm/ListaPagamentoEvento.aspx";
    private final String URL_LISTA_EVENTO = "http://ghelfer.net/pdm/ListaEvento.aspx";

    AsyncHttpClient client;

    ArrayList<String> listaIdEventos;
    ArrayList<String> listaPagamentos;
    ArrayList<HashMap<String,String>> listaDeEventos;

    ListView listViewPagamentos;

    private String ID_PESSOA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_pagamentos);

        listViewPagamentos = (ListView) findViewById(R.id.listViewPagamentos);
        listaIdEventos = new ArrayList<>();
        listaPagamentos = new ArrayList<>();
        listaDeEventos = new ArrayList<>();
        client = new AsyncHttpClient();
        ID_PESSOA = getIntent().getStringExtra("idpessoa");

        RequestParams params = new RequestParams();
        params.put("idpessoa", ID_PESSOA);

        client.post(URL_LISTA_PAGAMENTO, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try{
                    String data = new String(responseBody);
                    JSONObject res = new JSONObject(data);
                    JSONArray array = res.getJSONArray("Pagamento");
                    if(array.length() < 1){
                        Toast.makeText(ListarPagamentosActivity.this, "Usuário não possui pagamentos", Toast.LENGTH_LONG).show();
                        return;
                    }

                    for(int i = 0;i < array.length();i++){
                        JSONObject obj = array.getJSONObject(i);
                        Log.d("DEBUG", obj.getString("idevento"));
                        listaIdEventos.add(obj.getString("idevento"));
                        listaPagamentos.add(obj.getString("pagamento"));

                    }
                } catch (Exception ex){
                    Log.d("LOG_KEYS", ex.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ListarPagamentosActivity.this, "Erro, por favor, tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
            }
        });

        client = new AsyncHttpClient();
        client.post(URL_LISTA_EVENTO, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try{
                    String data = new String(responseBody);
                    JSONObject res = new JSONObject(data);
                    JSONArray array = res.getJSONArray("Evento");
                    int indexPagamento = 0;

                    for(int i = 0;i < array.length();i++) {
                        JSONObject obj = array.getJSONObject(i);
                        String idEvento = obj.getString("idevento");
                        //Toast.makeText(ListarPagamentosActivity.this,"IDevento:" +  idEvento, Toast.LENGTH_SHORT).show();
                    }


                    /*for(int i = 0; i < listaIdEventos.size();i++){
                        Toast.makeText(ListarPagamentosActivity.this, listaIdEventos.get(i), Toast.LENGTH_SHORT).show();
                    }*/

                        for(int x = 0;x<listaIdEventos.size();x++){
                            Toast.makeText(ListarPagamentosActivity.this, "ID Q EU FUI: " + listaIdEventos.get(x), Toast.LENGTH_SHORT).show();

                            for(int i = 0;i < array.length();i++) {
                                try {
                                    JSONObject obj = array.getJSONObject(i);
                                    String idEvento = obj.getString("idevento");
                                    String titulo = obj.getString("titulo");
                                    Toast.makeText(ListarPagamentosActivity.this, "Evento na web " + idEvento, Toast.LENGTH_SHORT).show();
                                    if(idEvento == listaIdEventos.get(x)){
                                        HashMap<String,String> map = new HashMap<String, String>();

                                        //TODO (>^~^)> † AQUI TEM UM BUG DO CAPIROTO † <(^~^<)
                                        //TODO CHAMEM OS HORSES, É A HORA
                                        map.put("titulo", titulo);
                                        map.put("pagamento", "true");

                                        listaDeEventos.add(map);
                                        Toast.makeText(ListarPagamentosActivity.this, "Antes do hashmap", Toast.LENGTH_SHORT).show();

                                        Toast.makeText(ListarPagamentosActivity.this, "Eror no if", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception ex) {

                            }
                        }
                        /*
                        for(String idEventoUsuario : listaIdEventos){
                            if(idEvento.equals(idEventoUsuario)){
                                HashMap<String,String> map = new HashMap();

                                map.put("titulo", obj.getString("titulo"));
                                //map.put("pagamento", listaPagamentos.get(indexPagamento));
                                map.put("pagamento", "True");
                                indexPagamento++;
                                listaDeEventos.add(map);
                            }
                        }
                        Toast.makeText(ListarPagamentosActivity.this, idEvento, Toast.LENGTH_SHORT).show();*/
                    }




                } catch (Exception ex){
                    Toast.makeText(ListarPagamentosActivity.this, "Erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

                SimpleAdapter adapter = new SimpleAdapter(getBaseContext(),
                        listaDeEventos,
                        R.layout.lista_cell_evento,
                        new String[] {"titulo", "pagamento"},
                        new int[] {R.id.itemTituloEvento, R.id.itemPagamento});
                listViewPagamentos.setAdapter(adapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });




    }


    protected void onBtnAtualizarPagamento(View v){
        Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show();
    }
}
