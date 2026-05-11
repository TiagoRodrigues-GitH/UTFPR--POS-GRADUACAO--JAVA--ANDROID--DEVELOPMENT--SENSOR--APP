package com.example.estudoemonitoramentodesensores.ui.info;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.estudoemonitoramentodesensores.R;

public class InfoAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        setTitle(R.string.sobre);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idMenuItem = item.getItemId();
        if (idMenuItem == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void abrirSiteAutor(View view) {
        abrirSite("https://lattes.cnpq.br/2528547933345822");
    }

    private void abrirSite(String endereco) {
        Intent intentAbertura = new Intent(Intent.ACTION_VIEW);
        intentAbertura.setData(Uri.parse(endereco));

        if (intentAbertura.resolveActivity(getPackageManager()) != null) {
            startActivity(intentAbertura);
        } else {
            Toast.makeText(this,
                    R.string.nenhum_aplicativo_para_abrir_paginas_web,
                    Toast.LENGTH_LONG).show();
        }
    }

    public void enviarEmailAutoria(View view) {
        enviarEmail(new String[]{"tiagorodrigues@alunos.utfpr.edu.br"},
                getString(R.string.contato_pelo_aplicativo_estudo_e_monitoramento_de_sensores));
    }

    private void enviarEmail(String[] enderecos, String assunto) {
        Intent intentAbertura = new Intent(Intent.ACTION_SENDTO);
        intentAbertura.setData(Uri.parse("mailto:"));
        intentAbertura.putExtra(Intent.EXTRA_EMAIL, enderecos);
        intentAbertura.putExtra(Intent.EXTRA_SUBJECT, assunto);

        if (intentAbertura.resolveActivity(getPackageManager()) != null) {
            startActivity(intentAbertura);
        } else {
            Toast.makeText(this,
                    R.string.nenhum_aplicativo_para_enviar_e_mail,
                    Toast.LENGTH_LONG).show();
        }
    }
}