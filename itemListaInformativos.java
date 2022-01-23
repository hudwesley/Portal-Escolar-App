package com.example.portalescolar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class itemListaInformativos extends ArrayAdapter<Evento> {

    private Context contextoPai;
    private ArrayList<Evento> eventos;
    private static class ViewHolder{
        private TextView tituloTxt;
        private TextView conteudoTxt;
        private TextView dataTxt;
    }
    public itemListaInformativos(Context contexto, ArrayList<Evento> dados){
        super(contexto, R.layout.activity_item_lista_informativos, dados);

        this.contextoPai = contexto;
        this.eventos = dados;
    }
    @NonNull
    @Override
    public View getView(int indice, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(indice, convertView, parent);
        Evento eventoAtual = eventos.get(indice);
        ViewHolder novaView;
        final View resultado;

        //1º caso - é quando a lista está senda montada pela primeira vez
        if(convertView==null){
            novaView = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_item_lista_informativos, parent, false);

            //linkando os componentes do XML
            novaView.tituloTxt = (TextView) convertView.findViewById(R.id.tituloInfo);
            novaView.conteudoTxt = (TextView) convertView.findViewById(R.id.conteudoInfo);
            novaView.dataTxt = (TextView) convertView.findViewById(R.id.dataInfo);
            resultado = convertView;
            convertView.setTag(novaView);
        }else{
            //2º caso - item modificado
            novaView = (ViewHolder) convertView.getTag();
            resultado = convertView;
        }
        //setar os valores de cada campo

        novaView.tituloTxt.setText(eventoAtual.getTitulo());
        novaView.conteudoTxt.setText(eventoAtual.getConteudo());
        novaView.dataTxt.setText(eventoAtual.getData());

        return resultado;
    }
}