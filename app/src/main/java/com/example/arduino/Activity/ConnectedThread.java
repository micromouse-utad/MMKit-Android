package com.example.arduino.Activity;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import android.os.Handler;
import android.os.Parcelable;

public  class ConnectedThread extends Thread implements Serializable{
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private Handler handler;
        private static final int MESSAGE_READ = 3;


    public ConnectedThread(BluetoothSocket socket, Handler handler) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            this.mmInStream = tmpIn;
            this.mmOutStream = tmpOut;
            this.handler = handler;
        }
        public void run() {  //Dados que vêm do arduino
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);

                    String dadosBT = new String(buffer, 0, bytes);

                    handler.obtainMessage(MESSAGE_READ, bytes, -1, dadosBT).sendToTarget();

                } catch (IOException e) {
                    break;
                }
            }
        }

        public void enviar(String dadosEnviados) {   //Enviar dados
            byte[] msgBuffer = dadosEnviados.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {

            }
        }
}