package beaconsoft.sycorowlayouts.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

import beaconsoft.sycorowlayouts.DataSource;
import beaconsoft.sycorowlayouts.R;
import beaconsoft.sycorowlayouts.SYCOServerAccess.UpdateService;

public class MessagingActivity extends AppCompatActivity {


    private static final String PHONE_KEY = "beaconsoft.sycorowlayouts.PHONE";
    private static final String NAME_KEY = "beaconsoft.sycorowlayouts.NAME";
    private String phoneNumber = "";
    private String name = "";
    private EditText editTextMessage;
    UpdateService updateService;        //reference to the update service
    boolean mBound = false;             //to bind or not to bind...


    /**
     *
     * Defines callbacks for service binding, passed to bindService()
     *
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            // We've bound to LocalService, cast the IBinder and get LocalService instance
            beaconsoft.sycorowlayouts.SYCOServerAccess.UpdateService.UpdateServiceBinder binder = (beaconsoft.sycorowlayouts.SYCOServerAccess.UpdateService.UpdateServiceBinder) service;

            updateService = binder.getService();

            mBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this,
                UpdateService.class), mConnection, Context.BIND_AUTO_CREATE);

        if(mBound) {


        }

    }

    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }
    @Override
    protected void onPause(){

        super.onPause();
    }

    @Override
    protected void onDestroy(){

        super.onDestroy();
    }

    @Override
    protected void onResume()
    {

        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra(PHONE_KEY);
        name = intent.getStringExtra(NAME_KEY);

        TextView textViewTo = (TextView)findViewById(R.id.textViewTo);
        textViewTo.setText("To: " + name);

        editTextMessage = (EditText)findViewById(R.id.editTextMessage);

    }

    public void clearText(View view){
        editTextMessage.setText("");
    }

    public void sendMessage(View view) {
        try {
            SmsManager.getDefault().sendTextMessage(phoneNumber, null, editTextMessage.getText().toString(), null, null);
            editTextMessage.setText("");
        }catch(Exception e){
            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
