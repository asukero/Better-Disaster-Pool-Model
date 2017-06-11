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

    private EditText mPortView;
    private View mServerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the login form.
        mServerView = (AutoCompleteTextView) findViewById(R.id.server);

        mPortView = (EditText) findViewById(R.id.port);
        mPortView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login_button || id == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });

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
                attemptHelper();
            }
        });

    }

    private void attemptConnect() {
        clientModel=new ClientModel();
        try {
            clientModel.initialize(InetAddress.getByName("192.168.1.100"),4242,this);
            clientModel.connect();
        } catch (Exception ex) {
            System.out.println("[!] Ip address or port are not valid");
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
            clientModel.register("pass","pass");
        }
    }
    private void attemptLogin() {
        if (clientModel!=null){
            clientModel.login("pass","pass");
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

