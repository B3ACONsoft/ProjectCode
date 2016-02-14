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
    private String adminName = "";
    private String loginLevel;
    private static final String ADMIN_KEY = "beaconsoft.sycorowlayouts.ADMIN_NAME_PASSED";
    private static final String LOGIN_KEY = "beaconsoft.sycorowlayouts.LoginActivity.DUMMY_CREDENTIALS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        Intent intentLogin = getIntent();
        loginLevel = intentLogin.getStringExtra(LOGIN_KEY);
        adminName = intentLogin.getStringExtra(ADMIN_KEY);

        if(permission.equals(loginLevel)){

            sendToLeaguesActivity(adminName);
        }
        else{
            TextView textView = new TextView(this);
            textView.setTextSize(64);
            textView.setText("NOT YETI!!!");
        }
    }

    public void sendToLeaguesActivity(String email){

        Intent intent = new Intent(this, LeaguesActivity.class);
        intent.putExtra(ADMIN_KEY, email);
        startActivity(intent);
    }
}
