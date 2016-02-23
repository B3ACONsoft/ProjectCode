package beaconsoft.sycorowlayouts;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;


public class MainActivity extends AppCompatActivity {

    private String permission;
    private String name;
    private static final String NAME_KEY = "beaconsoft.sycorowlayouts.NAME";
    private static final String LEVEL_KEY = "beaconsoft.sycorowlayouts.LEVEL";
    private static final String ADMIN = "ADMIN";
    private static final String COACH = "COACH";
    private DBHelper helper = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        for(String q: DatabaseDummyLoader.boom){
            db.execSQL(q);
        }
        db.setTransactionSuccessful();
        db.endTransaction();

        Intent intentLogin = getIntent();
        name = intentLogin.getStringExtra(NAME_KEY);
        permission = intentLogin.getStringExtra(LEVEL_KEY);
        switch (permission) {
            case ADMIN:
                sendToLeaguesActivity(name);
                break;
            case COACH:
                sendToCoachHomeActivity(name);
                break;
            default:
                sendToUserHomeActivity(name);
                break;
        }
    }


    public void sendToUserHomeActivity(String name) {
        Intent intent = new Intent(this, UserHomeActivity.class);
        intent.putExtra(NAME_KEY, name);
        startActivity(intent);
    }

    public void sendToLeaguesActivity(String name){

        Intent intent = new Intent(this, LeaguesActivity.class);
        intent.putExtra(NAME_KEY, name);
        startActivity(intent);
    }

    public void sendToCoachHomeActivity(String name){

        Intent intent = new Intent(this, CoachHomeActivity.class);
        intent.putExtra(NAME_KEY, name);
        startActivity(intent);
    }
}
