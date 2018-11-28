package com.thayskeillasimone.cervejaki.cervaki;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    Button btcadastrapessoa, btlistarpessoas;
    Button btcadastrar, btcancelar;
    Button btvoltar, btavancar;
    TextView txtnome, txtendereco, txtavaliacao;
    EditText ednome, edendereco, edavaliacao;
    SQLiteDatabase db;
    Cursor dados;
    int numero_registros = 0;
    int posicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{
            db = openOrCreateDatabase("banco_dados", Context.MODE_PRIVATE, null);
            db.execSQL("create table if not exists cadcervej(codusurario integer primary key autoincrement, " + "nome text not null, endereco text not null, avaliacao integer not null)");
        } catch(Exception e){
            ExibirMensagem(e.toString());
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            try{
                db = openOrCreateDatabase("banco_dados", Context.MODE_PRIVATE, null);
                db.execSQL("create table if not exists cadcervej(codusurario integer primary key autoincrement, " + "nome text not null, endereco text not null, avaliacao integer not null)");
            } catch(Exception e){
                ExibirMensagem(e.toString());
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new cadastroFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_cadastro);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_cadastro:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new cadastroFragment()).commit();
                //acesso ao banco
                dados = db.query("cadcervej",(new String[] {"nome", "endereco", "avaliacao"}), null, null, null, null, null);
                numero_registros = dados.getCount();
                btcadastrar.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        try {
                            String nome = ednome.getText().toString();
                            String endereco = edendereco.getText().toString();
                            String avaliacao = edavaliacao.getText().toString();
                            //insercao dos dados dentro do banco
                            numero_registros++;
                            db.execSQL("insert into cadcervej values(" + String.valueOf(numero_registros) + ",'" + nome + "','" + endereco + "'," + avaliacao + ")");
                            ExibirMensagem("Registro cadastrado com sucesso");
                        } catch (Exception e) {
                            ExibirMensagem("Erro ao cadastrar");
                        }
                    }
                });
                break;
            case R.id.nav_lista:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new listaFragment()).commit();
                try {
                    dados = db.query("cadcervej", (new String[]{"nome", "endereco", "avaliacao"}), null, null, null, null, null);
                    numero_registros = dados.getCount();
                    posicao = 1;
                }catch(Exception e) {
                    ExibirMensagem("Erro ao obter dados do banco");

                    //return;
                }
                // se nao houver registros a serem mostrados, mostra-se a mensagene carrega-se a tela principal novamente
                if(numero_registros == 0)
                {
                    ExibirMensagem("Numero registro cadastrado");

                    //return;
                }
                else {
                    //carrega a tela de pesqueisa e mostra os dados encontrados dentro do banco
                    //btvoltar = (Button) findViewById(R.id.btvoltar);
                    //btavancar = (Button) findViewById(R.id.btavancar);
                    //txtnome = (TextView) findViewById(R.id.txtnome);
                    //txtendereco = (TextView) findViewById(R.id.txtendereco);
                    //txtavaliacao = (TextView) findViewById(R.id.txtavaliacao);

                    dados.moveToFirst();

                    //txtnome.setText(dados.getString(dados.getColumnIndex("nome")));
                    //txtendereco.setText(dados.getString(dados.getColumnIndex("endereco")));
                    //txtavaliacao.setText(dados.getString(dados.getColumnIndex("avaliacao")));
                    btvoltar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            if (posicao == 1)
                                return;
                            posicao--;
                            dados.moveToPrevious();
                            //txtnome.setText(dados.getString(dados.getColumnIndex("nome")));
                            //txtendereco.setText(dados.getString(dados.getColumnIndex("endereco")));
                            //txtavaliacao.setText(dados.getString(dados.getColumnIndex("avaliacao")));
                        }
                    });

                    btavancar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            if (posicao == numero_registros)
                                return;
                            posicao++;
                            dados.moveToNext();
                            //txtnome.setText(dados.getString(dados.getColumnIndex("nome")));
                            //txtendereco.setText(dados.getString(dados.getColumnIndex("endereco")));
                            //txtavaliacao.setText(dados.getString(dados.getColumnIndex("avaliacao")));

                        }
                    });
                    //btmenu_principal.setOnClickListener(new View.OnClickListener() {
                    //  @Override
                    //public void onClick (View v){
                    //  CarregarTelaPrincipal();
                    //}
                    //});
                    //});
                }
                break;
            case R.id.nav_sobre:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new messageFragment()).commit();
                break;
            case R.id.nav_info:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new infoFragment()).commit();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Compartilhar", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);


        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
        super.onBackPressed();
    }

            //metodo que exibe mensagem na tela
            public void ExibirMensagem(String mensagem){
                AlertDialog.Builder dialogo=new AlertDialog.Builder(MainActivity.this);
                dialogo.setTitle("Aviso");
                dialogo.setMessage(mensagem);
                dialogo.setNeutralButton("OK",null);
                dialogo.show();
            }
}


        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
*/