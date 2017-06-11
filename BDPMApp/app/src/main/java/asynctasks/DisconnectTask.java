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

public class DisconnectTask extends AsyncTask<Void, Void, Void> {
    public ClientModel model= null;
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            System.out.println("[*] Disconnecting from server...");
            model.sendToServer("DISCONNECT", MessageType.DISCONNECT);
            model.closeConnection();
        }
        catch (Exception ex) {
            //Log.ERROR("[!] Error couldn't establish a connection with " + model.serverHostName.toString() + ":" + model.portNumber);
        }
        return null;
    }
}