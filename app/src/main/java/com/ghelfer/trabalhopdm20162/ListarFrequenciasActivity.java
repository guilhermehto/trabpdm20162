package com.ghelfer.trabalhopdm20162;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ListarFrequenciasActivity extends AppCompatActivity {

    final String URL_POST_FREQUENCIA = "http://ghelfer.net/pdm/ListaFrequenciaAtividade.aspx";
    final String URL_GET_ATIVIDADES = "http://ghelfer.net/pdm/ListaAtividade.aspx";

    String idpessoa;

    AsyncHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_frequencias);

        idpessoa = getIntent().getStringExtra("idpessoa");

        RequestParams params = new RequestParams();
        params.add("idpessoa", idpessoa);

        client = new AsyncHttpClient();
        client.post(URL_POST_FREQUENCIA, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ArrayList<String> listaIdsAtividades = new ArrayList<String>();
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
                        Toast.makeText(ListarFrequenciasActivity.this, obj.getString("idatividade"), Toast.LENGTH_SHORT).show();
                        listaIdsAtividades.add(obj.getString("idatividade"));
                    }

                } catch (Exception ex){
                    Log.e("ERRO", ex.getMessage());
                }

                for(String id : listaIdsAtividades){
                    client.get(URL_GET_ATIVIDADES, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                String data = new String(responseBody);
                                JSONObject res = new JSONObject(data);
                                JSONArray jsonArray = res.getJSONArray("Atividade");
                                JSONObject obj = jsonArray.getJSONObject(0);
                                Toast.makeText(ListarFrequenciasActivity.this,obj.getString("titulo") , Toast.LENGTH_SHORT).show();
                            } catch (Exception ex){
                                Log.e("ERRO", ex.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ListarFrequenciasActivity.this, "Erro de conexão, tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
