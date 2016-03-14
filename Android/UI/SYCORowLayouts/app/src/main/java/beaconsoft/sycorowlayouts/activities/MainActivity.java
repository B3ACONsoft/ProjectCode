package beaconsoft.sycorowlayouts.activities;

import beaconsoft.sycorowlayouts.DataSource;
import beaconsoft.sycorowlayouts.MySQLiteHelper;
import beaconsoft.sycorowlayouts.dbobjects.Users;
import string.utils.ProperCase;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.sql.SQLException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private String permission;
    private String email;
    private static final String EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private static final String LEVEL_KEY = "beaconsoft.sycorowlayouts.LEVEL";
    private static final String COACH_KEY = "beaconsoft.sycorowlayouts.COACH";
    private static final String ADMIN_KEY = "beaconsoft.sycorowlayouts.ADMIN";
    private static final String NAME_KEY  = "beaconsoft.sycorowlayouts.NAME";
    private static final String ADMIN = "ADMIN";
    private static final String COACH = "COACH";
    private DataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataSource = new DataSource(this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            Log.w(MySQLiteHelper.class.getName(), e.getMessage());
        }

        Intent intentLogin = getIntent();
        email = intentLogin.getStringExtra(EMAIL_KEY);
        permission = intentLogin.getStringExtra(LEVEL_KEY);
        switch (permission) {
            case ADMIN:
                sendToLeaguesActivity(email);
                break;
            case COACH:
                sendToCoachHomeActivity(email);
                break;
            default:
                sendToUserHomeActivity(email);
                break;
        }
    }

    public void sendToUserHomeActivity(String name) {
        Intent intent = new Intent(this, UserHomeActivity.class);
        intent.putExtra(EMAIL_KEY, name);
        startActivity(intent);
        dataSource.close();
    }

    public void sendToLeaguesActivity(String email){

        Users tempUser = dataSource.getUserByEmail(email.toUpperCase());
                int adminId  = tempUser.getUserID();
                String fname = tempUser.getFname();
                fname = ProperCase.toProperCase(fname);
                String lname = tempUser.getLname();
                lname = ProperCase.toProperCase(lname);
                String name  = fname + lname;
                email = tempUser.getEmail();
                email = email.toLowerCase();
        Intent intent = new Intent(this, LeaguesActivity.class);
        intent.putExtra(EMAIL_KEY, email);
        intent.putExtra(ADMIN_KEY, adminId);
        intent.putExtra(NAME_KEY, name);
        startActivity(intent);
        dataSource.close();
    }

    public void sendToCoachHomeActivity(String email){

        Users tempUser = dataSource.getUserByEmail(email.toUpperCase());
        int coachId  = tempUser.getUserID();
        email = email.toUpperCase();

        Intent intent = new Intent(this, CoachHomeActivity.class);
        intent.putExtra(EMAIL_KEY, email);
        intent.putExtra(COACH_KEY, coachId);
        startActivity(intent);
        dataSource.close();
    }
}
