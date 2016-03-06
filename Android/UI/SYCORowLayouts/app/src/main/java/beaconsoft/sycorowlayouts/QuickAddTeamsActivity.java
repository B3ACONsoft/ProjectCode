package beaconsoft.sycorowlayouts;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;

public class QuickAddTeamsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int currentLeague;
    private static final String  ADMIN_KEY = "beaconsoft.sycorowlayouts.ADMIN";
    private static final String  EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private static final String LEAGUE_KEY = "beaconsoft.sycorowlayouts.LEAGUE";
    private DBHelper helper = new DBHelper(this);
    private ArrayList<String> arrayListCoachNames = new ArrayList<>();
    private ArrayList<Integer>arrayListCoachIds   = new ArrayList<>();
    private HashMap<String, Integer> hashMapCoaches = new HashMap<>();
    private SQLiteDatabase db;
    private Spinner spinnerCoaches;
    private ArrayAdapter adapterSpinnerCoaches;
    private Integer currentCoach;
    private EditText editTextFirst;
    private EditText editTextLast;
    private EditText editTextPhone;
    private EditText editTextEmergency;
    private EditText editTextCoachEmail;
    private ContentValues ccv;
    private Toast toastCoach;
    private Toast toastTeam;
    private String fname;
    private String lname;
    private String teamName;
    private long phone;
    private long emerg;
    private String email;
    private Cursor cursor;
    private TextView textViewTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_add_teams);

        db = helper.getWritableDatabase();

        Intent intent = getIntent();
        email = intent.getStringExtra(EMAIL_KEY);
        currentLeague = intent.getIntExtra(LEAGUE_KEY, 0);
        editTextFirst     = (EditText)findViewById(R.id.editTextCoachFirst);
        editTextLast      = (EditText)findViewById(R.id.editTextCoachLast );
        editTextPhone     = (EditText)findViewById(R.id.editTextCoachPhone);
        editTextEmergency = (EditText)findViewById(R.id.editTextCoachEmergency);
        editTextCoachEmail= (EditText)findViewById(R.id.editTextCoachEmail);
        editTextFirst        .setText("El");
        editTextLast         .setText("Guapo");
        editTextCoachEmail   .setText("GoodLooking@ndAvailable.com");
        editTextPhone        .setText("1594658789");
        editTextEmergency    .setText("3626659856");

        loadSpinner();
    }

    public void loadSpinner(){

        cursor = db.rawQuery(

                        "SELECT user_id, fname, lname " +
                        "  FROM users       " +
                        " WHERE user_type =    'COACH';", null);

        if(cursor.moveToFirst() && (cursor != null && cursor.getCount()>0)){
            arrayListCoachIds.clear();
            arrayListCoachNames.clear();
            hashMapCoaches.clear();
            do{
                int tempId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                arrayListCoachIds.add(tempId);
                String tempFirstAndLastName = cursor.getString(cursor.getColumnIndexOrThrow("fname")) + " " +
                        cursor.getString(cursor.getColumnIndexOrThrow("lname"));
                arrayListCoachNames.add(tempFirstAndLastName);
                hashMapCoaches.put(tempFirstAndLastName, tempId);
            }while(cursor.moveToNext());
        }

        spinnerCoaches = (Spinner)findViewById(R.id.spinnerCoachesQuickAddTeams);
        adapterSpinnerCoaches = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayListCoachNames);

        adapterSpinnerCoaches.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCoaches.setAdapter(adapterSpinnerCoaches);
        spinnerCoaches.setOnItemSelectedListener(this);
        spinnerCoaches.setEnabled(true);
        spinnerCoaches.setSelection(0);
        cursor.close();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String choice = parent.getItemAtPosition(position).toString();
        currentCoach = hashMapCoaches.get(choice);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void createTeam(View view){

        try {
            EditText editTeamName = (EditText) findViewById(R.id.editTextNewTeamName);
            teamName = editTeamName.getText().toString().toUpperCase();
            //currentCoach...
            ContentValues cv = new ContentValues(4);
            cv.put("team_name", teamName.toUpperCase());
            cv.put("league_id", currentLeague);
            cv.putNull("team_id");
            cv.put("user_id", currentCoach);
            db.insert("team", null, cv);
            textViewTop = (TextView)findViewById(R.id.textViewQuickAddTeamsTop);
            cursor = db.rawQuery("SELECT t.team_name, u.lname FROM team t, users u " +
                                 "WHERE u.user_id = t.user_id " +
                                 "AND t.team_name = '" + teamName.toUpperCase() + "'"
                    , null);
            String displayString = "";
            if(cursor.moveToFirst()){
                do{
                    displayString = "Created: " +
                            cursor.getString(cursor.getColumnIndexOrThrow("team_name")) + ", " +
                            cursor.getString(cursor.getColumnIndexOrThrow("lname"));
                }while(cursor.moveToNext());
                textViewTop.setText(displayString);
            }else {
                throw new Exception("insert unsuccessful...");
            }
            cursor.close();

        }catch(Exception e){
            toastTeam = Toast.makeText(this, null, Toast.LENGTH_LONG);
            String msg = e.getMessage();
            toastTeam.setText(msg);
            toastTeam.show();

        }
        cursor.close();
    }

    public void quickAddCoach(View view){

        try {
            ccv = new ContentValues();
            fname = editTextFirst.getText().toString();
            lname = editTextLast.getText().toString();
            phone = Long.parseLong(editTextPhone.getText().toString());
            emerg = Long.parseLong(editTextEmergency.getText().toString());
            email = editTextCoachEmail.getText().toString();

            ccv.putNull("user_id");
            ccv.put("fname", fname.toUpperCase());
            ccv.put("lname", lname.toUpperCase());
            ccv.put("phone", phone);
            ccv.put("emergency", emerg);
            ccv.put("email", email.toUpperCase());
            ccv.put("user_type", "COACH");
            ccv.put("pass", "PASS");
            db.insert("users", null, ccv);
            textViewTop = (TextView)findViewById(R.id.textViewQuickAddTeamsTop);
            cursor = db.rawQuery("SELECT user_type, fname, lname FROM users WHERE email = '" + email.toUpperCase() + "';", null);
            String displayString = "";
            if(cursor.moveToFirst()){
                do{
                    displayString = "Created: " +
                                    cursor.getString(cursor.getColumnIndexOrThrow("user_type")) + " " +
                                    cursor.getString(cursor.getColumnIndexOrThrow("fname"))    + " "  +
                                    cursor.getString(cursor.getColumnIndexOrThrow("lname"));
                }while(cursor.moveToNext());
                textViewTop.setText(displayString);
            }else {
                throw new Exception("insert unsuccessful...");
            }
            cursor.close();
            loadSpinner();

        }catch(Exception e){

            toastCoach = Toast.makeText(this, null, Toast.LENGTH_LONG);
            String msg = e.getMessage();

            if(fname.length() < 1)
                msg = "Please fill in the first name...";
            else if(lname.length() < 1)
                msg = "Please fill in the last name...";
            else if((phone+"").length()< 1)
                msg = "Please provide a number where you can be reached...";
            else if((emerg+"").length() < 1)
                msg = "Please give the proper number to call in case of emergency...";
            else if(email.length()< 1)
                msg = "An email request is required...";

            toastCoach.setText(msg);
            toastCoach.show();
        }

    }
    /**
     * Removes all strings from edittexts
     * @param view
     */
    public void clearFormCoach(View view){

        editTextFirst.setText("");
        editTextLast.setText("");
        editTextPhone.setText("");
        editTextEmergency.setText("");
        editTextCoachEmail.setText("");
    }
}
