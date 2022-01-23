package com.example.portalescolar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtSenha;
    private Button btnEntrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtEmail = (EditText) findViewById(R.id.emailTxt);
        txtSenha = (EditText) findViewById(R.id.senhaTxt);
        btnEntrar = (Button) findViewById(R.id.entrarBtn);
        configuraPermissao();

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEmail.getText().toString().trim();
                String senha = txtSenha.getText().toString().trim();
                if(email.isEmpty() || senha.isEmpty()){
                    Toast.makeText(MainActivity.this, "Preencha todos os campos!.", Toast.LENGTH_LONG).show();
                }else{
                    confirmaLogin(email, senha);
                }
            }
        });


    }
    private void configuraPermissao(){
        /*if(ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission.INTERNET}, 0);
        }*/
        if(ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, 0);
        }
    }
    private void confirmaLogin(String emailAluno, String senhaAluno) {
        RequestQueue pilha = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor + "aluno";

        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resposta = new JSONObject(response);

                    if (resposta.getInt("cod") == 200) {
                        JSONObject obj = resposta.getJSONObject("informacao");

                        GlobalVar.idAluno = obj.getInt("idAluno");

                        if (GlobalVar.idAluno < 0) {

                            Toast.makeText(MainActivity.this, "Email ou senha incorretos.", Toast.LENGTH_LONG).show();

                        } else {
                            // seta as informações em variáveis globais para utilizar no app
                            GlobalVar.emailAluno = obj.getString("email");
                            GlobalVar.nomeAluno = obj.getString("nome");
                            GlobalVar.nomeTurma = obj.getString("nomeTurma");
                            GlobalVar.nomeEscola = obj.getString("nomeEscola");
                            GlobalVar.idEscola = obj.getInt("idEscola");

                            Toast.makeText(MainActivity.this, "Login feito com sucesso!", Toast.LENGTH_SHORT).show();
                            Intent trocaTela = new Intent(MainActivity.this, telaInformativos.class);
                            startActivity(trocaTela);
                            finish();
                        }
                    } else {
                        Toast.makeText(MainActivity.this,
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
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("servico", "login");
                parametros.put("email", emailAluno);
                parametros.put("senha", senhaAluno);

                return parametros;
            }
        };
        pilha.add(jsonRequest);
    }
}