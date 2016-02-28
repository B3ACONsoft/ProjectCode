package beaconsoft.sycorowlayouts;

import string.utils.ProperCase;
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
    private static final String ADMIN_KEY = "beaconsoft.sycorowlayouts.ADMIN";
    private static final String NAME_KEY  = "beaconsoft.sycorowlayouts.NAME";
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

        String name = "no name initialized";
        String adminId = "did not initialize";

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT user_id, fname, lname, email FROM users WHERE email = 'HENRYJ@LOVEMEAYETI.NET' AND user_type = 'ADMIN'", null);

        if(cursor.moveToFirst()){
            do{
                adminId = cursor.getString(0);
                String fname = cursor.getString(1);
                fname = ProperCase.toProperCase(fname);
                String lname = cursor.getString(2);
                lname = ProperCase.toProperCase(lname);
                name = fname + lname;
                email = cursor.getString(3);
                email = email.toLowerCase();
            }while(cursor.moveToNext());
        }

        Intent intent = new Intent(this, LeaguesActivity.class);
        intent.putExtra(EMAIL_KEY, email);
        intent.putExtra(ADMIN_KEY, adminId);
        intent.putExtra(NAME_KEY, name);
        startActivity(intent);
    }

    public void sendToCoachHomeActivity(String email){

        String coachId = "no id initialized";
        String name    = "no name initialized";

        SQLiteDatabase db = helper.getReadableDatabase();
        /*This may need to come from the other database*/
        Cursor cursor = db.rawQuery("SELECT user_id FROM users WHERE email = '" + email + "'", null);

        if(cursor.moveToFirst()){
            do{
                coachId = cursor.getString(0);
            }while(cursor.moveToNext());
        }
        db.close();
        Intent intent = new Intent(this, CoachHomeActivity.class);
        intent.putExtra(EMAIL_KEY, email);
        intent.putExtra(COACH_KEY, coachId);
        startActivity(intent);
    }
}
