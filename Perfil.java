package com.example.portalescolar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Perfil extends AppCompatActivity {

    private Button btnVoltar;
    private TextView nomeAluno, emailAluno, turmaAluno, escolaAluno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        nomeAluno = (TextView) findViewById(R.id.txtNome);
        emailAluno = (TextView) findViewById(R.id.txtEmail);
        turmaAluno = (TextView) findViewById(R.id.txtTurma);
        escolaAluno = (TextView) findViewById(R.id.txtEscola);

        nomeAluno.setText((GlobalVar.nomeAluno).toUpperCase());
        emailAluno.setText(GlobalVar.emailAluno);
        turmaAluno.setText(GlobalVar.nomeTurma);
        escolaAluno.setText(GlobalVar.nomeEscola);

        btnVoltar = (Button) findViewById(R.id.botaoVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trocaTela = new Intent(Perfil.this, telaInformativos.class);
                startActivity(trocaTela);
                finish();
            }
        });
    }
}