package finalassignment.poo.uqac.ca.bdpmapp;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.InetAddress;

import model.ClientModel;
import serializable.Credentials;
import serializable.MessageType;

/**
 * A login screen that offers login via email/password.
 */
public class MainActivity extends Activity implements LoaderCallbacks<Cursor> {

    private ClientModel clientModel;
    public boolean responded=false;
    public TextView errorText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the login form.
        errorText=(TextView) findViewById(R.id.error_view);
        Button ConnectButton = (Button) findViewById(R.id.connect_button);
        ConnectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptConnect();
            }
        });

        Button LoginButton = (Button) findViewById(R.id.login_button);
        LoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button RegisterButton = (Button) findViewById(R.id.disconnect_button);
        RegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        Button HelpButton = (Button) findViewById(R.id.help_button);
        HelpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptHelp();
            }
        });

        Button HelperButton = (Button) findViewById(R.id.helper_button);
        HelperButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptHelper();
            }
        });

        Button DisconnectButton = (Button) findViewById(R.id.disconnect_button);
        DisconnectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptDisconnect();
            }
        });

    }
    public void connectionComplete(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.disconnect_button).setVisibility(View.VISIBLE);
                findViewById(R.id.login_interface).setVisibility(View.VISIBLE);
                findViewById(R.id.server_login_form).setVisibility(View.GONE);

            }
        });


    }

    public void disconnectionComplete(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
        findViewById(R.id.server_login_form).setVisibility(View.VISIBLE);
        findViewById(R.id.disconnect_button).setVisibility(View.GONE);
        findViewById(R.id.login_interface).setVisibility(View.GONE);
        findViewById(R.id.help_interface).setVisibility(View.GONE);
            }
        });
    }

    public void loginComplete(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
        findViewById(R.id.server_login_form).setVisibility(View.GONE);
        findViewById(R.id.disconnect_button).setVisibility(View.VISIBLE);
        findViewById(R.id.login_interface).setVisibility(View.VISIBLE);
        findViewById(R.id.help_interface).setVisibility(View.VISIBLE);
        }
    });
    }


    private void attemptConnect() {
        errorText.setText("");
        clientModel=new ClientModel();
        try {
            String ip= ((EditText)findViewById(R.id.server)).getText().toString();
            int port=Integer.parseInt(((EditText)findViewById(R.id.port)).getText().toString());
            clientModel.initialize(InetAddress.getByName(ip),port,this);
            clientModel.connect();
        } catch (Exception ex) {
            errorText.setText("[!] Ip address or port are not valid");
        }
    }

    private void attemptDisconnect() {
        if (clientModel!=null){
            clientModel.disconnect();
            clientModel=null;
        }
    }

    private void attemptRegister() {
        if (clientModel!=null){
            String pseudo= ((EditText)findViewById(R.id.pseudo)).getText().toString();
            String pass=(((EditText)findViewById(R.id.password)).getText().toString());
            clientModel.register(pseudo,pass);
        }
    }
    private void attemptLogin() {
        if (clientModel!=null){
            String pseudo= ((EditText)findViewById(R.id.pseudo)).getText().toString();
            String pass=(((EditText)findViewById(R.id.password)).getText().toString());
            clientModel.login(pseudo,pass);
        }
    }
    private void attemptHelp() {
        if (clientModel!=null){
            clientModel.help();
        }
    }
    private void attemptHelper() {
        if (clientModel!=null){
            clientModel.helper();
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

