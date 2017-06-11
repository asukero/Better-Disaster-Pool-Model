package asynctasks;

import android.os.AsyncTask;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.net.ssl.SSLSocket;

import finalassignment.poo.uqac.ca.bdpmapp.R;
import model.ClientModel;
import serializable.MessageType;
import sslsocketkeystorefactory.SSLSocketKeystoreFactory;

/**
 * Created by Sacael on 6/10/2017.
 */

public class ConnectTask extends AsyncTask<Void, Void, Void> {
    public ClientModel model= null;
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            System.out.println("[*] Connecting to " + model.serverHostName.toString() + ":" + model.portNumber + "...");
            model.clientSocket = (SSLSocket) SSLSocketKeystoreFactory.getSocketWithCert(model.serverHostName, model.portNumber, model.context.getResources().openRawResource(R.raw.keystore), "SrvKeypass");
            model.clientSocket.setKeepAlive(true);

            model.outToServer = new ObjectOutputStream(model.clientSocket.getOutputStream());
            model.inFromServer = new ObjectInputStream(model.clientSocket.getInputStream());
            model.sendToServer("CONNECT", MessageType.CONNECT);
            }
        catch (Exception ex) {
            //Log.ERROR("[!] Error couldn't establish a connection with " + model.serverHostName.toString() + ":" + model.portNumber);
        }
        return null;
    }
}
