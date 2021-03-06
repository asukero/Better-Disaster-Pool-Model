package asynctasks;

import android.os.AsyncTask;

import model.ClientModel;
import serializable.MessageType;

/**
 * Created by Sacael on 6/10/2017.
 */

public class HelpTask extends AsyncTask<Void, Void, Void> {
    public ClientModel model= null;
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            model.sendToServer("HELP", MessageType.HELP);

        }
        catch (Exception ex) {
            //Log.ERROR("[!] Error couldn't establish a connection with " + model.serverHostName.toString() + ":" + model.portNumber);
        }
        return null;
    }
}