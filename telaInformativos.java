package com.example.portalescolar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class telaInformativos extends AppCompatActivity {
    private LinearLayout listaInfo;
    private Button listarBtn;
    private EditText txtTitulo;
    private Button botaoInfo;
    private Button btnSair;
    private Button btnPerfil;
    private ArrayList<Evento> eventos;
    private int id = GlobalVar.idEscola;
    private String idEscola = String.valueOf(GlobalVar.idEscola);
    private LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_informativos);

        txtTitulo = (EditText) findViewById(R.id.txtTitulo);
        listaInfo = (LinearLayout) findViewById(R.id.listaInformativos);
        listarBtn = (Button) findViewById(R.id.botaoLista);
        botaoInfo = (Button) findViewById(R.id.botaoPesquisar);
        btnSair = (Button) findViewById(R.id.botaoSair);
        btnPerfil = (Button) findViewById(R.id.botaoPerfil);
        carregaInformativos(idEscola);

        //chama a tela de perfil do usuário
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trocaTela = new Intent(telaInformativos.this, Perfil.class);
                startActivity(trocaTela);
                finish();
            }
        });

        //chama o método que procura um informativo pelo titulo
        botaoInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titulo = txtTitulo.getText().toString();
                if (titulo.isEmpty()) {
                    Toast.makeText(telaInformativos.this, "O titulo não pode ficar vazio", Toast.LENGTH_SHORT).show();
                } else {
                    procuraInformativos(titulo, idEscola);
                }
            }
        });

        //chama o método que carrega todos os informativos
        listarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtTitulo.setText("");
                carregaInformativos(idEscola);
            }
        });

        //logout
        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(telaInformativos.this, "LOGOUT FEITO COM SUCESSO!", Toast.LENGTH_LONG).show();
                Intent trocaTela = new Intent(telaInformativos.this, MainActivity.class);
                startActivity(trocaTela);
                finish();

            }
        });
        inflater = (LayoutInflater) telaInformativos.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //carregar todos os informativos
    public void carregaInformativos(String escola){
        eventos = new ArrayList<>();

        RequestQueue pilhas = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor+"informativo";

        StringRequest requisicao = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject resposta = new JSONObject(response);
                    if (resposta.getInt("cod") == 200) {

                        JSONArray eventosJSON = resposta.getJSONArray("informacao");
                        for (int i = 0; i < eventosJSON.length(); i++) {

                            JSONObject obj = eventosJSON.getJSONObject(i);;
                            Evento temp = new Evento(obj.getInt("idInformativo"), obj.getString("titulo"),
                                    obj.getString("conteudo"), obj.getString("data"));
                            eventos.add(temp);
                            GlobalVar.idInformativo = obj.getInt("idInformativo");
                        }
                        mostraInformativos(eventos);

                    } else {
                        Toast.makeText(telaInformativos.this,
                                resposta.getString("informacao"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> parametros = new HashMap<>();
                parametros.put("servico", "listar");
                parametros.put("idEscola", escola);
                return parametros;
            }
        };

        pilhas.add(requisicao);
    }

    // inflar o LinearLayout de informativos com as informações
    private void mostraInformativos(ArrayList<Evento> eventos) {
        listaInfo.removeAllViews();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        for (Evento e:eventos){
            View view = inflater.inflate(R.layout.activity_item_lista_informativos, null);
            ((TextView) (view.findViewById(R.id.tituloInfo))).setText(e.getTitulo().toUpperCase());
            ((TextView) (view.findViewById(R.id.conteudoInfo))).setText(e.getConteudo());
            try {
                Date dataTemp = sdf1.parse(e.getData());
                ((TextView) (view.findViewById(R.id.dataInfo))).setText(sdf2.format(dataTemp));
            } catch (ParseException parseException) {
                ((TextView) (view.findViewById(R.id.dataInfo))).setText(e.getData());
            }
            Button curtir = ((Button) (view.findViewById(R.id.btnCurtir)));
            curtir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestCurtida(e.getId(), curtir);
                }
            });
            listaInfo.addView(view);
        }
    }

    private void requestCurtida(int id, Button botao) {
        eventos = new ArrayList<>();

        RequestQueue pilhas = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor+"informativo";

        StringRequest requisicao = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject resposta = new JSONObject(response);
                    if (resposta.getInt("cod") == 200) {
                        Toast.makeText(telaInformativos.this, "Informativo curtido com sucesso!", Toast.LENGTH_SHORT).show();
                        botao.setEnabled(false);
                        botao.setClickable(false);
                    } else {
                        Toast.makeText(telaInformativos.this,
                                resposta.getString("informacao"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            protected Map<String, String> getParams(){

                Map<String, String> parametros = new HashMap<>();
                parametros.put("servico", "curtir");
                parametros.put("idInformativo", String.valueOf(id));
                return parametros;
            }
        };

        pilhas.add(requisicao);
    }

    //procurar um informativo pelo titulo
    public void procuraInformativos(String tituloInfo, String escola){

        eventos = new ArrayList<>();

        RequestQueue pilhas = Volley.newRequestQueue(telaInformativos.this);
        String url = GlobalVar.urlServidor+"informativo";

        StringRequest requisicao = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject resposta = new JSONObject(response);
                    if (resposta.getInt("cod") == 200) {

                        JSONArray eventosJSON = resposta.getJSONArray("informacao");
                        for (int i = 0; i < eventosJSON.length(); i++) {

                            JSONObject obj = eventosJSON.getJSONObject(i);
                            Evento temp = new Evento(obj.getInt("idInformativo"), obj.getString("titulo"),
                                    obj.getString("conteudo"), obj.getString("data"));
                            eventos.add(temp);
                        }
                        mostraInformativos(eventos);

                    } else {
                        Toast.makeText(telaInformativos.this,
                                resposta.getString("informacao"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            protected Map<String, String> getParams(){

                Map<String, String> parametros = new HashMap<>();
                parametros.put("servico", "consulta");
                parametros.put("titulo", tituloInfo);
                parametros.put("idEscola", escola);
                return parametros;
            }
        };

        pilhas.add(requisicao);
    }
}
