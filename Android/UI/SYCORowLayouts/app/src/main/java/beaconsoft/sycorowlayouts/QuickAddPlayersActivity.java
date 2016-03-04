package beaconsoft.sycorowlayouts;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QuickAddPlayersActivity extends AppCompatActivity {


    private DBHelper helper = new DBHelper(this);
    private SQLiteDatabase dbw;
    private CheckBox kidBox;
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private EditText et5;
    private EditText et6;
    private EditText et7;
    private static final String   NAME_KEY = "beaconsoft.sycorowlayouts.NAME";
    private static final String  ADMIN_KEY = "beaconsoft.sycorowlayouts.ADMIN";
    private static final String  EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private static final String   TEAM_KEY = "beaconsoft.sycorowlayouts.TEAM";
    private static final String LEAGUE_KEY = "beaconsoft.sycorowlayouts.LEAGUE";

    private String email;
    private String name;
    private int currentLeague;
    private int currentTeam;
    private int currentAdmin;

    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_add_players);

        Intent intent = getIntent();
        name = intent.getStringExtra(NAME_KEY);
        email = intent.getStringExtra(EMAIL_KEY);
        currentLeague = intent.getIntExtra(LEAGUE_KEY, 0);
        currentTeam   = intent.getIntExtra(TEAM_KEY, 0);
        currentAdmin  = intent.getIntExtra(ADMIN_KEY, 0);

        TextView textViewAdminEmail = (TextView)findViewById(R.id.textViewQAAdminEmail);
        kidBox = (CheckBox)findViewById(R.id.checkBoxPlayerIsKid);

        et1 = (EditText) findViewById(R.id.editTextContactFirst);
        et2 = (EditText) findViewById(R.id.editTextContactLast);
        et3 = (EditText) findViewById(R.id.editTextChildFirst);
        et4 = (EditText) findViewById(R.id.editTextChildLast);
        et5 = (EditText) findViewById(R.id.editTextPhone);
        et6 = (EditText) findViewById(R.id.editTextEmail);
        et7 = (EditText) findViewById(R.id.editTextEmergencyPhone);

        kidBox.setChecked(true);

        et1.setText("Paddy");
        et2.setText("Currin");
        et3.setText("John");
        et4.setText("Currin");
        et5.setText("1234567891");
        et6.setText("fearth@yeti.com");
        et7.setText("9876543210");


        textViewAdminEmail.setText(email);

    }

    public void onClickCheckBox(View view){
        if(!kidBox.isChecked()){
            kidBox.setSelected(false);
            et3.setText("");
            et3.setEnabled(false);
            et4.setText("");
            et4.setEnabled(false);
            for(View lol: et3.getTouchables()){
                lol.setEnabled(false);
                lol.setClickable(false);
            }
            for(View lol: et4.getTouchables()){
                lol.setEnabled(false);
                lol.setClickable(false);
            }
        }else{
            kidBox.setSelected(true);
            et3.setText("");
            et3.setEnabled(true);
            et4.setText("");
            et4.setEnabled(true);

            for(View lol: et3.getTouchables()){
                lol.setEnabled(true);
                lol.setClickable(true);
            }
            for(View lol: et4.getTouchables()){
                lol.setEnabled(true);
                lol.setClickable(true);
            }
        }
    }

    public void quickAddPlayer(View view){

        dbw = helper.getWritableDatabase();

        ContentValues insertValues = new ContentValues();
        String  first      =                  et1.getText().toString();
        String  last       =                  et2.getText().toString();
        String  childFirst =                  et3.getText().toString();
        String  childLast  =                  et4.getText().toString();
        String  phone      =                  et5.getText().toString();
        String  email      =                  et6.getText().toString();
        String  emergency  =                  et7.getText().toString();


        try {
            insertValues.putNull("user_id");
            insertValues.put("fname", first);
            insertValues.put("lname", last);
            insertValues.put("phone", Long.parseLong(phone));
            insertValues.put("email", email);
            insertValues.put("emergency", Long.parseLong(emergency));
            insertValues.put("user_type", "USER");
            insertValues.put("pass", "PASS");
            dbw.insert("users", null, insertValues);
            boolean haveKid = (childFirst != null && childLast != null) || (childFirst.equals("") && childLast.equals("")) || kidBox.isChecked();

            if(first.length() < 1 || last.length() < 1 || (haveKid && childFirst.length() < 1) || (haveKid && childLast.length() < 1))
            {
                throw new Exception("There are appropriate name fields left to fill...");
            }

            if (haveKid) {

                childFirst = first;
                childLast = last;
            }

            cursor = dbw.rawQuery("SELECT user_id FROM users WHERE email = '" + email + "';", null);
            int userId = 0;
            if (cursor.moveToFirst() && (cursor != null && cursor.getCount() > 0)) {
                do {
                    userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                } while (cursor.moveToNext());
            }
            ContentValues playerValues = new ContentValues();
            playerValues.putNull("player_id");
            playerValues.put("fname", childFirst);
            playerValues.put("lname", childLast);
            playerValues.put("user_id", userId);

            dbw.insert("player", null, playerValues);

            ContentValues enrollmentValues = new ContentValues();
            cursor.close();
            cursor = dbw.rawQuery("SELECT player_id FROM player WHERE fname = '" + childFirst + "' AND lname = '" + childLast + "' AND user_id = " + userId, null);
            int playerId = 0;
            if (cursor.moveToFirst() && (cursor != null && cursor.getCount() > 0)) {
                do {
                    playerId = cursor.getInt(cursor.getColumnIndexOrThrow("player_id"));
                } while (cursor.moveToNext());
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String date = sdf.format(new Date());

            enrollmentValues.putNull("enrollment_id");
            enrollmentValues.put("user_id", userId);
            enrollmentValues.put("player_id", playerId);
            enrollmentValues.put("team_id", currentTeam);
            enrollmentValues.put("league_id", currentLeague);
            enrollmentValues.put("enrollment_date", date);

            dbw.insert("enrollment", null, enrollmentValues);
            cursor.close();

            clearForm(findViewById(R.id.buttonQuickAddPlayerAddPlayer));

            cursor = dbw.rawQuery("SELECT user_id, fname, lname FROM users WHERE user_id = " + userId, null);
            int successfulUserId = 0;
            String successfulFName = "fail";
            String successfulLName = "fail";

            if (cursor.moveToFirst() && (cursor != null && cursor.getCount() > 0)) {
                do {
                    successfulUserId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                    successfulFName = cursor.getString(cursor.getColumnIndexOrThrow("fname"));
                    successfulLName = cursor.getString(cursor.getColumnIndexOrThrow("lname"));
                } while (cursor.moveToNext());
            }
            cursor.close();
            TextView textViewSuccess = (TextView) findViewById(R.id.textViewSuccessfulInsert);
            textViewSuccess.setText(successfulUserId + " " + successfulFName + " " + successfulLName);

            dbw.close();
        }catch(Exception e){
            Toast toast = Toast.makeText(this, null, Toast.LENGTH_LONG);
            String msg = "";
            if(et1.getText().toString().length() < 1){
                msg = "Please fill in your first name";
            }
            if(et2.getText().toString().length() < 1){
                msg = "Please fill in your last name";
            }
            if(et3.getText().toString().length() < 1 && kidBox.isChecked()){
                msg = "Is your child the player? Please fill in their first name";
            }
            if(et4.getText().toString().length() < 1 && kidBox.isChecked()){
                msg = "Is your child the player? Please fill in their last name";
            }
            if(et5.getText().toString().length() < 1){
                msg = "Please fill in your phone number";
            }
            if(et6.getText().toString().length() < 1){
                msg = "You must enter a valid email address";
            }
            if(et7.getText().toString().length() < 1){
                msg = "Please give an emergency number";
            }
            toast.setText(msg);
            toast.show();
        }
    }

    public void clearForm(View view){

        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et5.setText("");
        et7.setText("");
        et6.setText("");
    }
}
