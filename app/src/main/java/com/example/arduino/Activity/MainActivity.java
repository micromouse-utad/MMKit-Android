package com.example.arduino.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.arduino.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Button btn_conectar, btn_frente, btn_direita, btn_esquerda, btn_rodar, btn_globaltronic, btn_resetar;
    private ImageView arduino;
    private ImageView[][] paredes_verticais, paredes_horizontais;
    private ImageView parede_horizontal_0_1;
    private static double Versao = 3.7;
    char directiom = 'N';
    boolean lord = true;
    boolean rodar = true;
    Pattern bluetooth_regex = Pattern.compile("([0-9a-fA-F]{2}:[NWSE] W=[01]{3})");

    private static final int SOLICITA_ATIVACAO = 1;
    public static final int SOLICITACONEXAO = 2;
    private static final int MESSAGE_READ = 3;
    Handler handler;
    StringBuilder dadosBluetooth = new StringBuilder();
    private String MAC = null;
    ConnectedThread connectedThread;
    BluetoothAdapter bluetoothAdapter = null;  //È o proprio bluetooth do nosso dispositivo
    public static BluetoothDevice meudevice = null; //Dispositivo remoto
    BluetoothSocket meuSocket = null; //Responsavel por transmissao de dados
    private boolean conexao = false;
    private UUID MEu_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paredes_horizontais = new ImageView[6][7];
        paredes_verticais = new ImageView[7][6];


//Aqui estam as paredes O MEU DEUS
        paredes_horizontais[0][0] = findViewById(R.id.parede_horizontal_0_0);
        paredes_horizontais[1][0] = findViewById(R.id.parede_horizontal_1_0);
        paredes_horizontais[2][0] = findViewById(R.id.parede_horizontal_2_0);
        paredes_horizontais[3][0] = findViewById(R.id.parede_horizontal_3_0);
        paredes_horizontais[4][0] = findViewById(R.id.parede_horizontal_4_0);
        paredes_horizontais[5][0] = findViewById(R.id.parede_horizontal_5_0);
        paredes_horizontais[0][1] = findViewById(R.id.parede_horizontal_0_1);
        paredes_horizontais[1][1] = findViewById(R.id.parede_horizontal_1_1);
        paredes_horizontais[2][1] = findViewById(R.id.parede_horizontal_2_1);
        paredes_horizontais[3][1] = findViewById(R.id.parede_horizontal_3_1);
        paredes_horizontais[4][1] = findViewById(R.id.parede_horizontal_4_1);
        paredes_horizontais[5][1] = findViewById(R.id.parede_horizontal_5_1);
        paredes_horizontais[0][2] = findViewById(R.id.parede_horizontal_0_2);
        paredes_horizontais[1][2] = findViewById(R.id.parede_horizontal_1_2);
        paredes_horizontais[2][2] = findViewById(R.id.parede_horizontal_2_2);
        paredes_horizontais[3][2] = findViewById(R.id.parede_horizontal_3_2);
        paredes_horizontais[4][2] = findViewById(R.id.parede_horizontal_4_2);
        paredes_horizontais[5][2] = findViewById(R.id.parede_horizontal_5_2);
        paredes_horizontais[0][3] = findViewById(R.id.parede_horizontal_0_3);
        paredes_horizontais[1][3] = findViewById(R.id.parede_horizontal_1_3);
        paredes_horizontais[2][3] = findViewById(R.id.parede_horizontal_2_3);
        paredes_horizontais[3][3] = findViewById(R.id.parede_horizontal_3_3);
        paredes_horizontais[4][3] = findViewById(R.id.parede_horizontal_4_3);
        paredes_horizontais[5][3] = findViewById(R.id.parede_horizontal_5_3);
        paredes_horizontais[0][4] = findViewById(R.id.parede_horizontal_0_4);
        paredes_horizontais[1][4] = findViewById(R.id.parede_horizontal_1_4);
        paredes_horizontais[2][4] = findViewById(R.id.parede_horizontal_2_4);
        paredes_horizontais[3][4] = findViewById(R.id.parede_horizontal_3_4);
        paredes_horizontais[4][4] = findViewById(R.id.parede_horizontal_4_4);
        paredes_horizontais[5][4] = findViewById(R.id.parede_horizontal_5_4);
        paredes_horizontais[0][5] = findViewById(R.id.parede_horizontal_0_5);
        paredes_horizontais[1][5] = findViewById(R.id.parede_horizontal_1_5);
        paredes_horizontais[2][5] = findViewById(R.id.parede_horizontal_2_5);
        paredes_horizontais[3][5] = findViewById(R.id.parede_horizontal_3_5);
        paredes_horizontais[4][5] = findViewById(R.id.parede_horizontal_4_5);
        paredes_horizontais[5][5] = findViewById(R.id.parede_horizontal_5_5);
        paredes_horizontais[0][6] = findViewById(R.id.parede_horizontal_0_6);
        paredes_horizontais[1][6] = findViewById(R.id.parede_horizontal_1_6);
        paredes_horizontais[2][6] = findViewById(R.id.parede_horizontal_2_6);
        paredes_horizontais[3][6] = findViewById(R.id.parede_horizontal_3_6);
        paredes_horizontais[4][6] = findViewById(R.id.parede_horizontal_4_6);
        paredes_horizontais[5][6] = findViewById(R.id.parede_horizontal_5_6);
        paredes_verticais[0][0] = findViewById(R.id.parede_vertical_0_0);
        paredes_verticais[1][0] = findViewById(R.id.parede_vertical_1_0);
        paredes_verticais[2][0] = findViewById(R.id.parede_vertical_2_0);
        paredes_verticais[3][0] = findViewById(R.id.parede_vertical_3_0);
        paredes_verticais[4][0] = findViewById(R.id.parede_vertical_4_0);
        paredes_verticais[5][0] = findViewById(R.id.parede_vertical_5_0);
        paredes_verticais[6][0] = findViewById(R.id.parede_vertical_6_0);
        paredes_verticais[0][1] = findViewById(R.id.parede_vertical_0_1);
        paredes_verticais[1][1] = findViewById(R.id.parede_vertical_1_1);
        paredes_verticais[2][1] = findViewById(R.id.parede_vertical_2_1);
        paredes_verticais[3][1] = findViewById(R.id.parede_vertical_3_1);
        paredes_verticais[4][1] = findViewById(R.id.parede_vertical_4_1);
        paredes_verticais[5][1] = findViewById(R.id.parede_vertical_5_1);
        paredes_verticais[6][1] = findViewById(R.id.parede_vertical_6_1);
        paredes_verticais[0][2] = findViewById(R.id.parede_vertical_0_2);
        paredes_verticais[1][2] = findViewById(R.id.parede_vertical_1_2);
        paredes_verticais[2][2] = findViewById(R.id.parede_vertical_2_2);
        paredes_verticais[3][2] = findViewById(R.id.parede_vertical_3_2);
        paredes_verticais[4][2] = findViewById(R.id.parede_vertical_4_2);
        paredes_verticais[5][2] = findViewById(R.id.parede_vertical_5_2);
        paredes_verticais[6][2] = findViewById(R.id.parede_vertical_6_2);
        paredes_verticais[0][3] = findViewById(R.id.parede_vertical_0_3);
        paredes_verticais[1][3] = findViewById(R.id.parede_vertical_1_3);
        paredes_verticais[2][3] = findViewById(R.id.parede_vertical_2_3);
        paredes_verticais[3][3] = findViewById(R.id.parede_vertical_3_3);
        paredes_verticais[4][3] = findViewById(R.id.parede_vertical_4_3);
        paredes_verticais[5][3] = findViewById(R.id.parede_vertical_5_3);
        paredes_verticais[6][3] = findViewById(R.id.parede_vertical_6_3);
        paredes_verticais[0][4] = findViewById(R.id.parede_vertical_0_4);
        paredes_verticais[1][4] = findViewById(R.id.parede_vertical_1_4);
        paredes_verticais[2][4] = findViewById(R.id.parede_vertical_2_4);
        paredes_verticais[3][4] = findViewById(R.id.parede_vertical_3_4);
        paredes_verticais[4][4] = findViewById(R.id.parede_vertical_4_4);
        paredes_verticais[5][4] = findViewById(R.id.parede_vertical_5_4);
        paredes_verticais[6][4] = findViewById(R.id.parede_vertical_6_4);
        paredes_verticais[0][5] = findViewById(R.id.parede_vertical_0_5);
        paredes_verticais[1][5] = findViewById(R.id.parede_vertical_1_5);
        paredes_verticais[2][5] = findViewById(R.id.parede_vertical_2_5);
        paredes_verticais[3][5] = findViewById(R.id.parede_vertical_3_5);
        paredes_verticais[4][5] = findViewById(R.id.parede_vertical_4_5);
        paredes_verticais[5][5] = findViewById(R.id.parede_vertical_5_5);
        paredes_verticais[6][5] = findViewById(R.id.parede_vertical_6_5);


        btn_conectar = findViewById(R.id.conectar);
        btn_frente = findViewById(R.id.movimentos_frente);
        btn_direita = findViewById(R.id.movimentos_direita);
        btn_esquerda = findViewById(R.id.movimentos_esquerda);
        btn_rodar = findViewById(R.id.movimentos_rotar);
        btn_globaltronic = findViewById(R.id.btn_globaltronic);
        arduino = findViewById(R.id.arduino);
        btn_resetar = findViewById(R.id.Resetar);


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            //Verificar se o Dispositivo suporta Bluetooth
            Toast.makeText(getApplicationContext(), "Este dispositivo não suporta Bluetooth.", Toast.LENGTH_LONG).show();
//            finish();
        } else {
            //Ativar o Bluetooth

            Intent atibaoBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(atibaoBluetooth, SOLICITA_ATIVACAO);
            //startActivityForResult Esta  a espera de um numero inteiro


        }

        if (!conexao) {
            btn_frente.setBackgroundResource(R.drawable.grupo_92);
            btn_rodar.setBackgroundResource(R.drawable.grupo_164);
            btn_direita.setBackgroundResource(R.drawable.grupo_163);
            btn_esquerda.setBackgroundResource(R.drawable.grupo_165);
            btn_resetar.setVisibility(View.VISIBLE);

        }


////Butao para a Conecao com dispositivos emparelhados
        btn_conectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (conexao) {
                    //Desconnectar
                    try {

                        meuSocket.close();
                        Toast.makeText(getApplicationContext(), "Bluetooth foi desconnectado", Toast.LENGTH_LONG).show();
                        conexao = false;
                        btn_conectar.setBackgroundResource(R.drawable.btn_conectar);
                        btn_frente.setBackgroundResource(R.drawable.grupo_92);
                        btn_rodar.setBackgroundResource(R.drawable.grupo_164);
                        btn_direita.setBackgroundResource(R.drawable.grupo_163);
                        btn_esquerda.setBackgroundResource(R.drawable.grupo_165);
                        btn_resetar.setVisibility(View.VISIBLE);
                        System.out.println("Pedro **********************");
                        System.out.println("Pedro Bluetooth foi desconectado");
                        System.out.println("Pedro ***********************");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Bluetooth não foi desconnectado", Toast.LENGTH_LONG).show();
                        System.out.println("Pedro **/////////////////////*");
                        System.out.println("Pedro Erro o Bluetooth não foi desconectado");
                        System.out.println("Pedro **////////////////////////*");
                    }
                } else {
                    //Conectar
                    Intent abreLista = new Intent(MainActivity.this, ListaDispositivos.class);
                    startActivityForResult(abreLista, SOLICITACONEXAO);

                }
            }
        });


        btn_resetar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!conexao) {
                    Snackbar.make(view, "Quer mesmo resetar o labirinto?", Snackbar.LENGTH_SHORT)
                            .setAction("Confirmar", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (arduino.getTranslationX() == 0 && arduino.getTranslationY() == 0 && paredes_horizontais[0][1].getVisibility() == View.VISIBLE) {
                                        Toast.makeText(getApplicationContext(), "Erro ja esta na posicao default!", Toast.LENGTH_LONG).show();
                                    } else {
                                        arduino.setTranslationX(0);
                                        arduino.setTranslationY(0);
                                        arduino.setRotation(0);
                                        directiom = 'N';
                                        Paredes(0);
                                        Toast.makeText(getApplicationContext(), "Sucesso ao resetar:", Toast.LENGTH_LONG).show();
                                        System.out.println("Pedro  ------------------------------");
                                        System.out.println("Pedro  Sucesso ao resetar");
                                        System.out.println("Pedro  -------------------------------");
                                    }
                                }
                            }).show();
                }
            }
        });


        btn_frente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (conexao) {
                    if (lord) {
                        connectedThread.enviar("l");
                        lord = false;
                    }

                    connectedThread.enviar("1");

                    System.out.println("Pedro  btn_frente: ");

                    switch (directiom) {
                        case 'N':
                            System.out.println("Pedro: Frente: N");
                            arduino.setTranslationY(arduino.getTranslationY() - 160);
                            break;
                        case 'S':
                            System.out.println("Pedro: Frente: S");
                            arduino.setTranslationY(arduino.getTranslationY() + 160);
                            break;
                        case 'W': //Esquerdo
                            System.out.println("Pedro: Frente: W");
                            arduino.setTranslationX(arduino.getTranslationX() - 160);
                            break;
                        case 'E':
                            System.out.println("Pedro: Frente: E");
                            arduino.setTranslationX(arduino.getTranslationX() + 160);
                            break;
                    }
                    System.out.println("Pedro  btn_frente:____________________*");

                }
            }
        });


        btn_direita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (conexao) {
                    if (lord) {
                        connectedThread.enviar("l");
                        lord = false;
                    }

                    connectedThread.enviar("4");

                    if (arduino.getRotation() == 360)
                        arduino.setRotation(0);

                    arduino.setRotation(arduino.getRotation() + 90);


                    float direcao_direita = arduino.getRotation();
                    System.out.println("Pedro     btn_direita   :");
                    if (direcao_direita == 0.0 || direcao_direita == 360.0 || direcao_direita == -360.0) {
                        directiom = 'N';
                        System.out.println("Pedro: Direita: N  " + arduino.getRotation());
                    } else if (direcao_direita == 90.0 || direcao_direita == -90.0) {
                        directiom = 'E';
                        System.out.println("Pedro: Direita: E  " + arduino.getRotation());
                    } else if (direcao_direita == 180.0 || direcao_direita == -180.0) {
                        directiom = 'S';
                        System.out.println("Pedro: Direita: S  " + arduino.getRotation());
                    } else if (direcao_direita == 270.0 || direcao_direita == -270.0) {
                        System.out.println("Pedro: Direita: W  " + arduino.getRotation());
                        directiom = 'W';
                    }
                    System.out.println("Pedro     btn_direita    :____________________*");
                }
            }
        });


        btn_esquerda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (conexao) {
                    if (lord) {
                        connectedThread.enviar("l");
                        lord = false;
                    }

                    connectedThread.enviar("2");

                    if (arduino.getRotation() == -360)
                        arduino.setRotation(0);

                    arduino.setRotation(arduino.getRotation() - 90);
                    System.out.println("Pedro     btn_esquerda   :");
                    float direcao_esquerda = arduino.getRotation();
                    if (direcao_esquerda == 0.0 || direcao_esquerda == -360.0 || direcao_esquerda == 360.0) {
                        directiom = 'N';
                        System.out.println("Pedro: Esquerda: N  " + arduino.getRotation());
                    } else if (direcao_esquerda == -90.0 || direcao_esquerda == 270.0) {
                        directiom = 'W';
                        System.out.println("Pedro: Esquerda: W  " + arduino.getRotation());
                    } else if (direcao_esquerda == -180.0 || direcao_esquerda == 180.0) {
                        directiom = 'S';
                        System.out.println("Pedro: Esquerda: S  " + arduino.getRotation());
                    } else if (direcao_esquerda == -270.0 || direcao_esquerda == 90.0) {
                        System.out.println("Pedro: Esquerda: E  " + arduino.getRotation());
                        directiom = 'E';
                    }
                    System.out.println("Pedro     btn_esquerda   :____________________*");
                }
            }
        });


        btn_rodar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (conexao) {
                    if (lord) {
                        connectedThread.enviar("l");
                        lord = false;
                    }
                    connectedThread.enviar("3");
                    if (arduino.getRotation() == 360)
                        arduino.setRotation(0);
                    System.out.println("Pedro     btn_esquerda   :");
                    if (rodar) {
                        arduino.setRotation(arduino.getRotation() + 180);
                        float direcao_rodar = arduino.getRotation();

                        if (direcao_rodar == 0 || direcao_rodar == 360 || direcao_rodar == -360) {
                            directiom = 'N';
                            System.out.println("Pedro: Rodar: N");
                        } else if (direcao_rodar == 90 || direcao_rodar == -90) {
                            directiom = 'E';
                            System.out.println("Pedro: Rodar: E");
                        } else if (direcao_rodar == 180 || direcao_rodar == -180) {
                            directiom = 'S';
                            System.out.println("Pedro: Rodar: S");
                        } else if (direcao_rodar == 270 || direcao_rodar == -270) {
                            directiom = 'W';
                            System.out.println("Pedro: Rodar: W");
                        }
                        rodar = false;
                    } else if (!rodar) {
                        arduino.setRotation(arduino.getRotation() - 180);
                        float direcao_rodar = arduino.getRotation();

                        if (direcao_rodar == 0 || direcao_rodar == -360 || direcao_rodar == 360) {
                            directiom = 'N';
                            System.out.println("Pedro: Rodar (Oposto): N");
                        } else if (direcao_rodar == -90 || direcao_rodar == 90) {
                            directiom = 'W';
                            System.out.println("Pedro: Rodar (Oposto): W");
                        } else if (direcao_rodar == -180 || direcao_rodar == 180) {
                            directiom = 'S';
                            System.out.println("Pedro: Rodar (Oposto): S");
                        } else if (direcao_rodar == -270 || direcao_rodar == 270) {
                            directiom = 'E';
                            System.out.println("Pedro: Rodar (Oposto): E");
                        }
                        rodar = true;
                    }
                    System.out.println("Pedro     btn_esquerda   :____________________*");
                }
            }
        });

        btn_globaltronic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.globaltronic.pt/");
                Intent abrirJanela = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(abrirJanela);
            }
        });


        handler = new Handler(new Handler.Callback() {//Ele fica monitorando sozinho

            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.what == MESSAGE_READ) {
                    String recebidos = (String) msg.obj; //Aqui eu recebo

                    dadosBluetooth.append(recebidos);  //Aqui eu junto
                    Matcher matcher = bluetooth_regex.matcher(dadosBluetooth);

                    if (matcher.find()) {

                        String palavra = matcher.group(1);
                        Toast.makeText(getApplicationContext(), palavra, Toast.LENGTH_SHORT).show();

                        String paredes = palavra.substring(7);

                        char parede_esquerda = paredes.charAt(0);
                        char parede_direita = paredes.charAt(2);
                        char parede_frente = paredes.charAt(1);


                        boolean esquerda = parede_esquerda == '1';
                        boolean frente = parede_frente == '1';
                        boolean direita = parede_direita == '1';


                        float posicao_eixo_x = arduino.getTranslationX();
                        float posicao_eixo_y = arduino.getTranslationY();

                        int x = Math.round(posicao_eixo_x / 160);
                        int y = Math.round(-posicao_eixo_y / 160);


                        String posicao_arduino = "(" + posicao_eixo_x + ")(" + posicao_eixo_y + ")";

                        System.out.println("Pedro  Coordenadas: ______________________________");

                        // 000
                        // Esquerda
                        // Frente
                        // Direita
                        System.out.println("Pedro    X: " + x + "  Y: " + y + "         As duas juntas      " + posicao_arduino);
                        System.out.println("___________________________________________________");
                        switch (directiom) {
                            case 'N':
                                if (esquerda) {
                                    paredes_verticais[x][y].setVisibility(View.VISIBLE);
                                }
                                if (direita) {
                                    paredes_verticais[x + 1][y].setVisibility(View.VISIBLE);
                                }
                                if (frente) {
                                    paredes_horizontais[x][y + 1].setVisibility(View.VISIBLE);
                                }
                                break;
                            case 'S':
                                if (esquerda) {
                                    paredes_verticais[x + 1][y].setVisibility(View.VISIBLE);
                                }
                                if (direita) {
                                    paredes_verticais[x][y].setVisibility(View.VISIBLE);
                                }
                                if (frente) {
                                    paredes_horizontais[x][y].setVisibility(View.VISIBLE);
                                }
                                break;
                            case 'W':
                                if (direita) {
                                    paredes_horizontais[x][y + 1].setVisibility(View.VISIBLE);
                                }
                                if (esquerda) {
                                    paredes_horizontais[x][y].setVisibility(View.VISIBLE);
                                }
                                if (frente) {
                                    paredes_verticais[x + 1][y].setVisibility(View.VISIBLE);
                                }
                                break;
                            case 'E':
                                if (direita) {
                                    paredes_horizontais[x][y].setVisibility(View.VISIBLE);                // ESQ
                                }
                                if (esquerda) {
                                    paredes_horizontais[x][y + 1].setVisibility(View.VISIBLE);              // DIR
                                }
                                if (frente) {
                                    paredes_verticais[x][y].setVisibility(View.VISIBLE);
                                }
                                break;
                        }
                        System.out.println("Pedro _________");
                        dadosBluetooth.delete(0, matcher.end());
                    }
                }
                return false;
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SOLICITA_ATIVACAO:
                if (resultCode == Activity.RESULT_OK) {
                    //Ativar o Bluetooth
                } else {
                    //Fechar a Aplicacao pois o usuario não permitio
                    Toast.makeText(getApplicationContext(), "O Bluetooth não foi ativado. Encerando", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case SOLICITACONEXAO:
                if (resultCode == Activity.RESULT_OK) {
                    MAC = data.getExtras().getString(ListaDispositivos.ENDERECO_MAC);
                    meudevice = bluetoothAdapter.getRemoteDevice(MAC);
                    try {
                        meuSocket = meudevice.createInsecureRfcommSocketToServiceRecord(MEu_UUID);
                        meuSocket.connect();
                        conexao = true;
                        connectedThread = new ConnectedThread(meuSocket, handler);
                        connectedThread.start();


                        Toast.makeText(getApplicationContext(), "Voce foi conectado com o ...", Toast.LENGTH_LONG).show();
                        btn_conectar.setBackgroundResource(R.drawable.btn_desconnectar);
                        btn_frente.setBackgroundResource(R.drawable.btn_seta_cima);
                        btn_direita.setBackgroundResource(R.drawable.btn_seta_direita);
                        btn_esquerda.setBackgroundResource(R.drawable.btn_seta_esquerda);
                        btn_rodar.setBackgroundResource(R.drawable.btn_seta_rodar);
                        btn_resetar.setVisibility(View.GONE);

                        Paredes(1);

                        System.out.println("Pedro   _________________________________");
                        System.out.println("Pedro   Sucesso ao conectar");
                        System.out.println("Pedro   _________________________________");
                    } catch (IOException e) {
                        conexao = false;
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Erro ao conectar.", Toast.LENGTH_LONG).show();
                        System.out.println("Pedro   _________________________________");
                        System.out.println("Pedro   Erro ao conectar");
                        System.out.println("Pedro   _________________________________");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Falha ao obter !", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void Paredes(int valor) {
        //Coloca as paredes invisiveis
        if (valor == 1) {
            for (int x = 0; x <= 5; x++) {
                for (int y = 0; y <= 5; y++) {
                    if (y != 0)
                        paredes_horizontais[x][y].setVisibility(View.GONE);
                    if (x != 0)
                        paredes_verticais[x][y].setVisibility(View.GONE);
                }
            }
        } else {
            for (int x = 0; x <= 5; x++) {
                for (int y = 0; y <= 5; y++) {
                    if (y != 0)
                        paredes_horizontais[x][y].setVisibility(View.VISIBLE);
                    if (x != 0)
                        paredes_verticais[x][y].setVisibility(View.VISIBLE);
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Versao:
                Toast.makeText(getApplicationContext(), "Versao da aplicacao: " + Versao, Toast.LENGTH_LONG).show();
                break;


            case R.id.consola:
                if (conexao) {
                    Intent abrirAconsola = new Intent(MainActivity.this, Main2Activity.class);
                    try {
                        meuSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    startActivity(abrirAconsola);
                } else {
                    Toast.makeText(getApplicationContext(), "Primeiro Clique em conectar", Toast.LENGTH_SHORT).show();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}




