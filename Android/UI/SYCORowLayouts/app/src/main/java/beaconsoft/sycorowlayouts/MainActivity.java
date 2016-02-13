package beaconsoft.sycorowlayouts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static beaconsoft.sycorowlayouts.LoginActivity.*;

public class MainActivity extends AppCompatActivity {

    private String permission = "ADMIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        Intent intentLogin = getIntent();
        String loginLevel = intentLogin.getStringExtra(LOGIN_KEY);


        if(permission.equals(loginLevel)){

            sendToLeaguesActivity();
        }
        else{
            TextView textView = new TextView(this);
            textView.setTextSize(64);
            textView.setText("NOT YETI!!!");
        }
    }

    public void sendToLeaguesActivity(){

        Intent intent = new Intent(this, LeaguesActivity.class);
        startActivity(intent);
    }
}
