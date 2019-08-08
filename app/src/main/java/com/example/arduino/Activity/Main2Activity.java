package com.example.arduino.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.arduino.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main2Activity extends AppCompatActivity {


    private Button bt_calibrar, btn_imprimir, btn_andar;
    public TextView outputs;
    private ListView lista;
    private ScrollView scrollView;
    private String[] calibrar = {"Sensores", "Sensores da frente"};
    private String[] imprimir = {"Paredes atuais", "Parâmetros do Mouse", "Mouse Localização/Direcão", "Paredes simples do labirinto", "Direções do labirinto", "Sensores", "Pagina de Ajuda", "Paredes do labirinto"};
    private String[] andar = {"Iniciar pesquisa de teste", "Comecar a andar", "Testar Solucao", "Resetar labirinto", "Resetar o labirinto para Japan", "Controlo remoto"};
    private static final int MESSAGE_READ = 3;
    private Handler handler;
    private BluetoothDevice meudispositivo = null;
    private UUID MEu_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothSocket meuSocket = null;
    private boolean conexao = false;
    private boolean lord = true;
    StringBuilder consola = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        bt_calibrar = findViewById(R.id.btn_calibrar);
        btn_imprimir = findViewById(R.id.btn_imprimir);
        btn_andar = findViewById(R.id.btn_andar);
        scrollView=findViewById(R.id.lista);
        outputs = findViewById(R.id.Outputs);
        lista = findViewById(R.id.Lista);
        bt_calibrar.setBackgroundResource(R.drawable.btn_calibrar);
        btn_imprimir.setBackgroundResource(R.drawable.btn_imprimir);
        btn_andar.setBackgroundResource(R.drawable.btn_andar);

        handler = new Handler(new Handler.Callback() {//Ele fica monitorando sozinho
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.what == MESSAGE_READ) {
                    String recebidos = (String) msg.obj; //Aqui eu recebo

                    consola.append(recebidos);


                    outputs.setText(consola);
                }
                return false;
            }
        });


        try {
            meuSocket = MainActivity.meudevice.createInsecureRfcommSocketToServiceRecord(MEu_UUID);
            meuSocket.connect();
            conexao = true;
        } catch (
                IOException e) {
            e.printStackTrace();
        }

        final ConnectedThread connectedThread = new ConnectedThread(meuSocket, handler);
        connectedThread.start();
        connectedThread.enviar("0");


        bt_calibrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter arrayAdapter = new ArrayAdapter(
                        Main2Activity.this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        calibrar
                );
                lista.setAdapter(arrayAdapter);
            }
        });


        btn_andar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayAdapter arrayAdapter = new ArrayAdapter(
                        Main2Activity.this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        andar
                );
                lista.setAdapter(arrayAdapter);
            }
        });


        btn_imprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter arrayAdapter = new ArrayAdapter(
                        Main2Activity.this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        imprimir
                );
                lista.setAdapter(arrayAdapter);
            }
        });


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String resultado = lista.getItemAtPosition(i).toString();


                if (resultado.equals("calibrar sensores")) {
                    connectedThread.enviar("c");
                } else if (resultado.equals("calibrar sensores da frente")) {
                    connectedThread.enviar("C");

                } else if (resultado.equals("Paredes atuais")) {
                    connectedThread.enviar("S");

                } else if (resultado.equals("Parâmetros do Mouse")) {
                    connectedThread.enviar("p");

                } else if (resultado.equals("Mouse Localização/Direcão")) {
                    connectedThread.enviar("i");

                } else if (resultado.equals("Paredes simples do labirinto")) {
                    connectedThread.enviar("m");

                } else if (resultado.equals("Direções do labirinto")) {
                    connectedThread.enviar("M");

                } else if (resultado.equals("Sensores")) {
                    connectedThread.enviar("s");

                } else if (resultado.equals("Pagina de Ajuda")) {
                    connectedThread.enviar("h");
                    connectedThread.enviar("0");

                } else if (resultado.equals("Paredes do labirinto")) {
                    connectedThread.enviar("w");

                } else if (resultado.equals("Iniciar pesquisa de teste")) {
                    connectedThread.enviar("g");

                } else if (resultado.equals("Comecar a andar")) {
                    connectedThread.enviar("G");

                } else if (resultado.equals("Testar Solucao")) {
                    connectedThread.enviar("t");

                } else if (resultado.equals("Resetar labirinto")) {
                    connectedThread.enviar("x");

                } else if (resultado.equals("Resetar o labirinto para Japan")) {
                    connectedThread.enviar("X");

                } else if (resultado.equals("Controlo remoto")) {
                    connectedThread.enviar("L");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meu_segunda_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_limpar:
                outputs.setText("");
                //Eu acho que aqui ao limpar ja vai ter de enviar um h para ele imprimir os comandos
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
