package finalassignment.poo.uqac.ca.bdpmapp;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by Paul on 2017-04-23.
 */

public class AsyncConnector extends AsyncTask<String, Integer, Socket> {
    @Override
    protected Socket doInBackground(String... params) {
        Socket socket = null;

        try {
            String Address = params[0];
            int portNumber = Integer.parseInt(params[1]);

            socket = new Socket(Address, portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return socket;
    }
}
