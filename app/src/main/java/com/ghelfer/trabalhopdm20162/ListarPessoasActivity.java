package com.ghelfer.trabalhopdm20162;


import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.*;

import org.json.JSONObject;

import java.util.Iterator;

import cz.msebera.android.httpclient.Header;

public class ListarPessoasActivity extends AppCompatActivity {

    private final String URL_GET_PESSOAS = "http://ghelfer.net/pdm/ListaPessoa.aspx";


    AsyncHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_pessoas);

        client = new AsyncHttpClient();
        //TODO: Consertar bug
        client.post(URL_GET_PESSOAS, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String data = new String(responseBody);
                    JSONObject json = new JSONObject(data).getJSONObject("Pessoa");
                    Iterator<String> keys = json.keys();
                    while(keys.hasNext()){
                        String key = keys.next();
                        Log.d("LOG_KEYS", key);
                    }



                    Toast.makeText(ListarPessoasActivity.this, data, Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Log.d("LOG_KEYS", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ListarPessoasActivity.this, "OH NOES", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
