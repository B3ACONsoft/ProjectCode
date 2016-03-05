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
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class QuickAddTeamsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String email;
    private String name;
    private int currentLeague;
    private int team;
    private int adminId;
    private static final String   NAME_KEY = "beaconsoft.sycorowlayouts.NAME";
    private static final String  ADMIN_KEY = "beaconsoft.sycorowlayouts.ADMIN";
    private static final String  EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private static final String   TEAM_KEY = "beaconsoft.sycorowlayouts.TEAM";
    private static final String LEAGUE_KEY = "beaconsoft.sycorowlayouts.LEAGUE";
    private DBHelper helper = new DBHelper(this);
    private ArrayList<String> arrayListCoachNames = new ArrayList<>();
    private ArrayList<Integer>arrayListCoachIds   = new ArrayList<>();
    private HashMap<String, Integer> hashMapCoaches = new HashMap<>();
    private SQLiteDatabase db;
    private Spinner spinnerCoaches;
    private ArrayAdapter adapterSpinnerCoaches;
    private Integer currentCoach;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_add_teams);

        db = helper.getWritableDatabase();

        Intent intent = getIntent();
        email         = intent.getStringExtra(EMAIL_KEY    );
        adminId       = intent.getIntExtra   (ADMIN_KEY , 0);
        currentLeague = intent.getIntExtra   (LEAGUE_KEY, 0);
        team          = intent.getIntExtra   (TEAM_KEY  , 0);
        name          = intent.getStringExtra(NAME_KEY     );

        Cursor cursor = db.rawQuery(
                        "SELECT e.user_id, u.fname, u.lname " +
                        "  FROM enrollment e, users u       " +
                        " WHERE e.user_id = u.user_id     " +
                        "   AND u.user_type =    'COACH'    " +
                        "   AND e.league_id != " + currentLeague     + ";", null);

        if(cursor.moveToFirst() && (cursor != null && cursor.getCount()>0)){
            arrayListCoachIds.clear();
            arrayListCoachNames.clear();
            hashMapCoaches.clear();
            do{
                int tempId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                arrayListCoachIds.add(tempId);
                String tempFirstAndLastName = cursor.getString(cursor.getColumnIndexOrThrow("fname")) +
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

        EditText editTeamName = (EditText)findViewById(R.id.editTextNewTeamName);
        String teamName = editTeamName.getText().toString();
        //currentCoach...
        ContentValues cv = new ContentValues(4);
        cv.put("team_name", teamName);
        cv.put("league_id", currentLeague);
        cv.put("null", "none");
        cv.put("user_id", currentCoach);

        db.insert("team", null, cv);
        //insert into TEAM table

    }
}
