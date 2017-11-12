package com.example.lenovo.preco;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * Created by LENOVO on 11-11-2017.
 */

public class BluetoothConnectionService {
    private static final String TAG = "BluetoothConnectionServ";

    private static final String appName = "MYAPP";

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private final BluetoothAdapter mBluetoothAdapter;
    Context mContext;
    ProgressDialog mProgressDialog;
    private AcceptThread mInsecureAcceptThread;
    private ConnectThread mConnectThread;
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    private ConnectedThread mConnectedThread;

    public BluetoothConnectionService(Context context) {
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
    }

    public synchronized void start() {
        Log.d(TAG, "start");
        //cancel any thread making connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = new AcceptThread(true);
            mInsecureAcceptThread.start();
        }
    }

    public void startClient(BluetoothDevice device, UUID uuid) {
        Log.d(TAG, "startClient: Started");

        //initprogress dialog
        mProgressDialog = ProgressDialog.show(mContext, "Connecting Bluetooth",
                "Please Wait", true);
        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        Log.d(TAG, "connected: Starting");
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    public void write(byte[] out) {
        ConnectedThread r;
        Log.d(TAG, "write: Write Called");
        mConnectedThread.write(out);
    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;
        private String mSocketType;


        public AcceptThread(boolean secure) {
            BluetoothServerSocket tmp = null;
            mSocketType = secure ? "Secure" : "Insecure";

            // Create a new listening server socket
            try {

                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(
                        appName, MY_UUID_INSECURE);
                Log.d(TAG, "AcceptThread: Setting up server using:" + MY_UUID_INSECURE);
            } catch (IOException e) {
                Log.d(TAG, "AcceptThread: IOException: " + e.getMessage());
            }
            mmServerSocket = tmp;

        }

        public void run() {
            Log.d(TAG, "run: AcceptThreadRunning.");
            BluetoothSocket socket = null;
            try {

                Log.d(TAG, "run:RFCOM server socket start..");

                socket = mmServerSocket.accept();
                Log.d(TAG, "run:RFCOM server socket accepted connection..");
            } catch (IOException e) {
                Log.d(TAG, "AcceptThread: IOException: " + e.getMessage());
            }

            if (socket != null) {
                connected(socket, mmDevice);
            }
            Log.i(TAG, "END mAcceptThread");
        }

        public void cancel() {
            Log.d(TAG, "cancel: Cancelling AcceptThread");
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: Close of AcceptThread ServerSocket failed" + e.getMessage());
            }
        }
    }

    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;

        private ConnectThread(BluetoothDevice device, UUID uuid) {
            Log.d(TAG, "ConnectThread: started");
            mmDevice = device;
            deviceUUID = uuid;
        }

        public void run() {
            BluetoothSocket tmp = null;
            Log.i(TAG, "RUN mConnectThread");
            //Get the bluetooth socket for a connection for the given device
            try {
                Log.d(TAG, "ConnectThread: Trying to create InsecureRfcommSocket using UUID: " + MY_UUID_INSECURE);
                tmp = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Log.d(TAG, "ConnectThread: Could not create InsecureRfcommSocket " + e.getMessage());
            }
            mmSocket = tmp;

            //Always cancel Discovery
            mBluetoothAdapter.cancelDiscovery();

            //Make a connection to bluetooth socket
            try {
                mmSocket.connect();
                Log.d(TAG, "run: ConnectThread connected");
            } catch (IOException e) {
                //Close the socket
                try {
                    mmSocket.close();
                    Log.d(TAG, "run: Closed Socket");
                } catch (IOException e1) {
                    Log.d(TAG, "mConnectThread: run: Unable to close connection in socket " + e.getMessage());
                }
                Log.d(TAG, "ConnectThread: Could not connect to UUID: " + MY_UUID_INSECURE);
            }

            connected(mmSocket, mmDevice);
        }

        public void cancel() {

            try {
                Log.d(TAG, "cancel: Closing Client Socket");
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: Close of mmSocket in ConnectThread failed" + e.getMessage());
            }
        }
    }

    public class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "ConnectedThread: Starting");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            //dismiss progressDialog when connection is established
            try {
                mProgressDialog.dismiss();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            mProgressDialog.dismiss();
            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    String incomingMessage = new String(buffer, 0, bytes);
                    Log.d(TAG, "InputStream: " + incomingMessage);
                } catch (IOException e) {
                    Log.d(TAG, "write: Error reading inputstream" + e.getMessage());
                    break;
                }

            }
        }

        //this will be called from main to send datato remote device
        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write: Writing to outputstream: " + text);
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.d(TAG, "write: Error writing to outputstream" + e.getMessage());
            }
        }

        //this will shutdown the connection and will be called from main
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {

            }
        }
    }
}