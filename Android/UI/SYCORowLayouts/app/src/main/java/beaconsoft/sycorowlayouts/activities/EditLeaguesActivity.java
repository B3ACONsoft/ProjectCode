package beaconsoft.sycorowlayouts.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import beaconsoft.sycorowlayouts.R;

public class EditLeaguesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Toolbar toolbar;
    private TextView textViewEditLeaguesTopPrompt;
    private EditText editTextEditLeaguesLeagueName;
    private EditText editTextEditLeaguesSportName;
    private Spinner spinnerEditLeaguesChooseSport;
    private Spinner spinnerEditLeaguesChooseLeague;
    private Button buttonCreateLeague;
    private Button buttonCreateSport;
    private Button buttonDeleteLeague;
    private Button buttonDeleteSport;
    private ArrayList<String> arrayListLeagueNames;
    private ArrayList<Integer> arrayListLeagueIDs;
    private HashMap<String, Integer> hashMapLeagues;
    private ArrayAdapter arrayAdapterSpinnerLeagues;
    private ArrayList<String> arrayListSportNames;
    private ArrayList<Integer> arrayListSportIDs;
    private HashMap<String, Integer> hashMapSports;
    private ArrayAdapter arrayAdapterSpinnerSports;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Intent intentIncoming;
    private int currentAdminId;
    private int currentLeague;
    private String currentAdminEmail;
    private int currentSport;
    private final String EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private final String LEAGUE_KEY = "beaconsoft.sycorowlayouts.LEAGUE";
    private final String ADMIN_KEY = "beaconsoft.sycorowlayouts.ADMIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_leagues);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intentIncoming = getIntent();
        currentAdminEmail = intentIncoming.getStringExtra(EMAIL_KEY);
        currentAdminId = intentIncoming.getIntExtra(ADMIN_KEY, 0);
        currentLeague = intentIncoming.getIntExtra(LEAGUE_KEY, 0);
        currentSport = 0;

//        helper = new DBHelper(this);
//        db = helper.getWritableDatabase();

        textViewEditLeaguesTopPrompt    = (TextView) findViewById(R.id.textViewEditLeaguesTop);

        editTextEditLeaguesSportName    = (EditText) findViewById(R.id.editTextEditLeaguesSportName);
        editTextEditLeaguesLeagueName   = (EditText) findViewById(R.id.editTextEditLeaguesLeagueName);

        spinnerEditLeaguesChooseSport   = (Spinner)  findViewById(R.id.spinnerEditLeaguesAddSport);
        spinnerEditLeaguesChooseLeague  = (Spinner)  findViewById(R.id.spinnerEditLeaguesChooseLeague);

        buttonCreateLeague              = (Button)   findViewById(R.id.buttonEditLeaguesAddLeague);
        buttonCreateSport               = (Button)   findViewById(R.id.buttonEditLeaguesAddSport);
        buttonDeleteLeague              = (Button)   findViewById(R.id.buttonEditLeaguesDeleteLeague);
        buttonDeleteSport               = (Button)   findViewById(R.id.buttonEditLeaguesDeleteSport);

        arrayListLeagueIDs              = new ArrayList<>();
        arrayListLeagueNames            = new ArrayList<>();
        hashMapLeagues                  = new   HashMap<>();

        arrayListSportIDs               = new ArrayList<>();
        arrayListSportNames             = new ArrayList<>();
        hashMapSports                   = new   HashMap<>();

        textViewEditLeaguesTopPrompt.setText(currentAdminEmail);

        loadSportSpinner();
        loadLeagueSpinner();

    }

    private void loadSportSpinner() {

        try {
            cursor = db.rawQuery("SELECT sport_id, sport_name FROM sport", null);
            if (cursor.moveToFirst()) {
                arrayListSportIDs.clear();
                arrayListSportNames.clear();
                hashMapSports.clear();
            /* iterate through and get the ids and names into two arraylists and a hashmap */
                do {
                    int tempID = cursor.getInt(cursor.getColumnIndexOrThrow("sport_id"));
                    arrayListSportIDs.add(tempID);
                    String tempName = cursor.getString(cursor.getColumnIndexOrThrow("sport_name"));
                    arrayListSportNames.add(tempName);
                    hashMapSports.put(tempName, tempID);
                } while (cursor.moveToNext());
            }

            arrayAdapterSpinnerSports = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, arrayListSportNames);
            spinnerEditLeaguesChooseSport.setAdapter(arrayAdapterSpinnerSports);
            spinnerEditLeaguesChooseSport.setOnItemSelectedListener(this);
            cursor.close();

        }catch(Exception e){
            textViewEditLeaguesTopPrompt.setText("No Sports to Display...");
        }
    }

//    private void turnOnTouchables(View parent){
//        parent.setEnabled(true);
//        for(View lol : parent.getTouchables()){
//            lol.setEnabled(true);
//        }
//    }
//
//    private void turnOffTouchbles(View parent){
//        for(View lol : parent.getTouchables()){
//            lol.setEnabled(false);
//        }
//    }

    private void loadLeagueSpinner(){
        try {
            cursor = db.rawQuery("SELECT league_name, league_id FROM league WHERE user_id = " + currentAdminId, null);

            if (cursor.moveToFirst()) {

                arrayListLeagueNames.clear();
                arrayListLeagueIDs.clear();
                hashMapLeagues.clear();

                do {
                    int tempID = cursor.getInt(cursor.getColumnIndexOrThrow("league_id"));
                    arrayListLeagueIDs.add(tempID);
                    String tempName = cursor.getString(cursor.getColumnIndexOrThrow("league_name"));
                    arrayListLeagueNames.add(tempName);
                    hashMapLeagues.put(tempName, tempID);
                } while (cursor.moveToNext());

                arrayAdapterSpinnerLeagues = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, arrayListLeagueNames);
                spinnerEditLeaguesChooseLeague.setAdapter(arrayAdapterSpinnerLeagues);
                spinnerEditLeaguesChooseLeague.setOnItemSelectedListener(this);
                cursor.close();
            } else {
                throw new Exception("No Leagues to Display");
            }
        }catch(Exception e){
            textViewEditLeaguesTopPrompt.setText(e.getMessage().toString());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String choice = parent.getItemAtPosition(position).toString();

        if (parent == spinnerEditLeaguesChooseSport) {
            onSpinnerSportsChange(choice);
        }
        if (parent == spinnerEditLeaguesChooseLeague) {
            onSpinnerLeaguesChange(choice);
        }
    }

    private void onSpinnerLeaguesChange(String choice) {
        currentLeague = hashMapLeagues.get(choice);
        textViewEditLeaguesTopPrompt.setText(choice);
    }

    private void onSpinnerSportsChange(String choice) {
        currentSport = hashMapSports.get(choice);
        textViewEditLeaguesTopPrompt.setText(choice);

    }

    public void addLeague(View view){
        try {
            Date startDate = new Date();
            Date endDate   = new Date();
            ContentValues cv = new ContentValues();
            String tempName = editTextEditLeaguesLeagueName.getText().toString().toUpperCase();
            if(tempName.length() > 3) {
                cv.putNull("league_id");
                cv.put("league_name", tempName);
                cv.put("user_id", currentAdminId);
                cv.put("sport_id", currentSport);
                cv.put("min_age", 0);
                cv.put("max_age", 100);
                cv.put("start_date", startDate.toString().toUpperCase());
                cv.put("end_date", endDate.toString().toUpperCase());
                db.insert("league", null, cv);
            }else{
                throw new Exception("League Name too short");
            }

            String leagueName = "";
            int leagueID = 0;
            String startDateString = "";
            cursor = db.rawQuery("SELECT league_name, league_id, start_date FROM league WHERE user_id = " + currentAdminId + " AND league_name = '" + tempName + "'", null);
            if (cursor.moveToFirst()) {
                do {
                    startDateString = cursor.getString(cursor.getColumnIndexOrThrow("start_date" ));
                    leagueID   = cursor.getInt   (cursor.getColumnIndexOrThrow("league_id"  ));
                    leagueName = cursor.getString(cursor.getColumnIndexOrThrow("league_name"));
                } while (cursor.moveToNext());
            }else{
                throw new Exception("Improper League Insert");
            }
            textViewEditLeaguesTopPrompt.setText("New League: " + leagueName + ", " + startDateString);
            currentLeague = leagueID;
            loadLeagueSpinner();
        }catch(Exception e){
            textViewEditLeaguesTopPrompt.setText(e.getMessage().toString());
        }
    }

    public void addSport(View view) {
        try {
            ContentValues cv = new ContentValues();
            String tempName = editTextEditLeaguesSportName.getText().toString().toUpperCase();
            if(tempName.length() < 2){
                throw new Exception("Please Enter the Full Sport Name");
            }

            if(checkForDuplicateSports(tempName)) {

                cv.putNull("sport_id");
                cv.put("sport_name", tempName);
                db.insert("sport", null, cv);

            }else{
                throw new Exception("There is already a sport named " + tempName);
            }

            String sportName = "";
            int sport_id = 0;
            cursor = db.rawQuery("SELECT sport_name, sport_id FROM sport WHERE sport_name = '" + tempName + "'", null);
            if (cursor.moveToFirst() && cursor.getCount() > 0) {
                do {
                    sportName = cursor.getString(cursor.getColumnIndexOrThrow("sport_name"));
                    sport_id  = cursor.getInt(cursor.getColumnIndexOrThrow("sport_id"));
                } while (cursor.moveToNext());
                textViewEditLeaguesTopPrompt.setText("New Sport: " + sportName);
            } else {
                throw new Exception("Insert failed on sport table");
            }
            currentSport = sport_id;
            loadSportSpinner();

        }catch(Exception e){
            textViewEditLeaguesTopPrompt.setText(e.getMessage().toString());
        }
    }

    private boolean checkForDuplicateSports(String tempName) {
        cursor = db.rawQuery("SELECT sport_name FROM sport WHERE sport_name = '" + tempName + "';", null);
        if (cursor.getCount() == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        parent.setSelection(parent.getCount()-1);
    }


}