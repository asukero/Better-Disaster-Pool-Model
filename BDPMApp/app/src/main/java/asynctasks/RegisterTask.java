package asynctasks;

import android.os.AsyncTask;

import model.ClientModel;
import serializable.Credentials;
import serializable.MessageType;

/**
 * Created by Sacael on 6/10/2017.
 */

public class RegisterTask extends AsyncTask<Void, Void, Void> {
    public ClientModel model= null;
    String nickName;
    String password;
    public RegisterTask(String _nickName,String _password){
        nickName=_nickName;
        password=_password;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            model.sendToServer(new Credentials(nickName, password), MessageType.REGISTER);

        }
        catch (Exception ex) {
            //Log.ERROR("[!] Error couldn't establish a connection with " + model.serverHostName.toString() + ":" + model.portNumber);
        }
        return null;
    }
}