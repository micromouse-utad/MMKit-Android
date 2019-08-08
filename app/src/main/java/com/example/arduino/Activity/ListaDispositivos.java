package com.example.arduino.Activity;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class ListaDispositivos extends ListActivity {
    //Quando criamos um alisActivity temos de declarrar ela No AndroidManifest
    private BluetoothAdapter meuBluetoothAdapter2=null;
    static String ENDERECO_MAC=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ArrayAdapter<String> ArrayBluetooth=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);    //Definir o tipo de layout para  alista

        meuBluetoothAdapter2=BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> dispositivosPareados=meuBluetoothAdapter2.getBondedDevices();
        if (dispositivosPareados.size()>0){
            for (BluetoothDevice dispositivo:dispositivosPareados){
                String nomeBT=dispositivo.getName();
                String MACBT=dispositivo.getAddress();
                ArrayBluetooth.add(nomeBT+"\n"+MACBT);

            }
        }else {
            Toast.makeText(getApplicationContext(),"Meu liga o bluetooth!",Toast.LENGTH_LONG).show();

        }
        setListAdapter(ArrayBluetooth);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    String informacaoGeral=((TextView)v).getText().toString();
    String enderecoMac=informacaoGeral.substring(informacaoGeral.length()-17);
        Intent enviardados=new Intent();
        enviardados.putExtra(ENDERECO_MAC,enderecoMac);
        setResult(RESULT_OK,enviardados);
        finish();
    }
}
