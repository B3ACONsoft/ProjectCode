package beaconsoft.sycorowlayouts.activities;

import android.content.Intent;
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

public class MessagingActivity extends AppCompatActivity {

    DataSource dataSource = new DataSource(this);
    private static final String PHONE_KEY = "beaconsoft.sycorowlayouts.PHONE";
    private static final String NAME_KEY = "beaconsoft.sycorowlayouts.NAME";
    private String phoneNumber = "";
    private String name = "";
    private EditText editTextMessage;

    @Override
    protected void onPause(){
        dataSource.close();
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        dataSource.close();
        super.onDestroy();
    }

    @Override
    protected void onResume()
    {
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
