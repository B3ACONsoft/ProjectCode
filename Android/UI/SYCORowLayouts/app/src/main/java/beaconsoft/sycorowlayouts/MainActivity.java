package beaconsoft.sycorowlayouts;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;


public class MainActivity extends AppCompatActivity {

    private String permission;
    private String name;
    private static final String EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private static final String LEVEL_KEY = "beaconsoft.sycorowlayouts.LEVEL";
    private static final String COACH_KEY = "beaconsoft.sycorowlayouts.COACH";
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
        db.close();

        Intent intentLogin = getIntent();
        name = intentLogin.getStringExtra(EMAIL_KEY);
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
        intent.putExtra(EMAIL_KEY, name);
        startActivity(intent);
    }

    public void sendToLeaguesActivity(String email){

        Intent intent = new Intent(this, LeaguesActivity.class);
        intent.putExtra(EMAIL_KEY, email);
        startActivity(intent);
    }

    public void sendToCoachHomeActivity(String email){
        SQLiteDatabase db = helper.getReadableDatabase();
        /*This may need to come from the other database*/
        Cursor cursor = db.rawQuery("SELECT user_id FROM users WHERE email = '" + email + "'", null);
        String coachId = "did not initialize";
        if(cursor.moveToFirst()){
            do{
                coachId = cursor.getString(0);
            }while(cursor.moveToNext());
        }

        Intent intent = new Intent(this, CoachHomeActivity.class);
        intent.putExtra(EMAIL_KEY, name);
        intent.putExtra(COACH_KEY, coachId);
        startActivity(intent);
    }
}
