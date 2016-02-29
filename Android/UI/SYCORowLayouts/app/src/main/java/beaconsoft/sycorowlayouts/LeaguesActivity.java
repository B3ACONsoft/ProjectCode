package beaconsoft.sycorowlayouts;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LeaguesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String  NAME_KEY = "beaconsoft.sycorowlayouts.NAME";
    private static final String ADMIN_KEY = "beaconsoft.sycorowlayouts.ADMIN";
    private static final String EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private DBHelper helper = new DBHelper(this);

/*push*/
    private TextView textViewAdminEmail;
    private String name;
    private String adminId;
    private String email;

    private TextView textViewLeaguesEmail;

    private int currentAdmin;
    private int currentSport;
    private int currentLeague;
    private int currentTeam;
    private int currentPlayer;

    private Cursor cursor;
    private SQLiteDatabase db;

    private HashMap<Integer, String> hashMapSports;
    private ArrayList<String> sportNameArrayList;
    private ArrayList<Integer> sportIdArrayList;
    private ArrayAdapter adapterSpinnerSports;
    private Spinner spinnerSports;

    private HashMap<Integer, String> hashMapleagues;
    private ArrayList<String> leagueNameArrayList;
    private ArrayList<Integer> leagueIdArrayList;
    private ArrayAdapter adapterSpinnerLeagues;
    private Spinner spinnerLeagues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leagues);
        /* Receive whats passed from Main and insert into appropriate variables, start database in this activity */
        Intent intent = getIntent();
        name = intent.getStringExtra(NAME_KEY);
        email = intent.getStringExtra(EMAIL_KEY);
        adminId = intent.getStringExtra(ADMIN_KEY);
        currentAdmin = Integer.parseInt(adminId);

        db = helper.getReadableDatabase();

        /*Set text in the headers for the administrator */
        textViewLeaguesEmail = (TextView) findViewById(R.id.textViewLeagueEmail);
        TextView textViewLeaguesAdmin = (TextView)findViewById(R.id.textViewPromptChooseSport);
        textViewLeaguesEmail.setText(email);
        textViewLeaguesAdmin.setText(name + ", please choose a sport...");

        /* Call the database and ask for the sports we have leagues for, insert into the first spinner
        * through a series of arraylists and a hashmap */

        cursor = db.rawQuery("SELECT sport_id, sport_name FROM sport", null);
        hashMapSports = new HashMap<>();
        sportNameArrayList = new ArrayList<String>();
        sportIdArrayList = new ArrayList<Integer>();
        if(cursor.moveToFirst()){
            do{
                sportIdArrayList.add(cursor.getInt(cursor.getColumnIndexOrThrow("sport_id")));
                sportNameArrayList.add(cursor.getString(cursor.getColumnIndexOrThrow("sport_name")));
                sportIdArrayList.add(cursor.getInt(cursor.getColumnIndexOrThrow("sport_id")));

                hashMapSports.put(cursor.getInt(cursor.getColumnIndexOrThrow("sport_id")), cursor.getString(cursor.getColumnIndexOrThrow("sport_name")));

            }while(cursor.moveToNext());
        }
        cursor.close();


        spinnerSports = (Spinner)findViewById(R.id.spinnerMyLeaguesSports);
        adapterSpinnerSports = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sportNameArrayList.toArray());
        adapterSpinnerSports.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSports.setAdapter(adapterSpinnerSports);
        spinnerSports.setOnItemSelectedListener(this);
        spinnerSports.setEnabled(true);
        spinnerSports.setSelection(0);

        /* Find the initial current sport and use it to load the next spinner, leagues */
        currentSport = sportIdArrayList.get(0);
        System.out.println("Sport:" + currentSport);
        //textViewLeaguesEmail.setText(new Integer(currentSport).toString());
        //
        leagueIdArrayList = new ArrayList<>();
        leagueNameArrayList = new ArrayList<>();
        cursor = db.rawQuery("SELECT league_id, league_name FROM league WHERE sport_id = " + currentSport, null);
        if(cursor.moveToFirst()){
            do{
                System.out.println(cursor.getCount());
                leagueIdArrayList.add(cursor.getInt(cursor.getColumnIndexOrThrow("league_id")));
                leagueNameArrayList.add(cursor.getString(cursor.getColumnIndexOrThrow("league_name")));
            }while(cursor.moveToNext());
        }


        spinnerLeagues = (Spinner)findViewById(R.id.spinnerMyLeaguesLeagues);
        adapterSpinnerLeagues = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, leagueNameArrayList);
        adapterSpinnerLeagues.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLeagues.setAdapter(adapterSpinnerLeagues);
        spinnerLeagues.setOnItemSelectedListener(this);
        spinnerLeagues.setEnabled(true);
        spinnerLeagues.setSelection(0);

        Spinner spinnerTeams = (Spinner)findViewById(R.id.spinnerMyLeaguesTeams);
        ArrayAdapter adapterSpinnerTeams = ArrayAdapter.createFromResource(getBaseContext(),
                R.array.dummy_teams, android.R.layout.simple_spinner_dropdown_item);
        adapterSpinnerTeams.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTeams.setAdapter(adapterSpinnerTeams);
        spinnerTeams.setOnItemSelectedListener(this);
        spinnerTeams.setEnabled(true);
        spinnerTeams.setSelection(0);



        Spinner spinnerPlayers = (Spinner)findViewById(R.id.spinnerMyLeaguesPlayers);
        ArrayAdapter adapterSpinnerPlayers = ArrayAdapter.createFromResource(getBaseContext(),
                R.array.dummy_players, android.R.layout.simple_spinner_dropdown_item);
        adapterSpinnerPlayers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlayers.setAdapter(adapterSpinnerPlayers);
        spinnerPlayers.setOnItemSelectedListener(this);
        spinnerPlayers.setEnabled(true);
        spinnerPlayers.setSelection(0);
    }

    private Cursor leaguesCursor;

    private class SelectionCall extends AsyncTask<Object, Object, Cursor>{
        SQLiteDatabase db = helper.getReadableDatabase();

        @Override
        protected Cursor doInBackground(Object ...params){
            Cursor cursor;
            String table = (String)params[0];
            String[]columns = (String[])params[1];
            String whereClause = (String)params[2];

                String query = new String("SELECT ");
                if(columns.length == 0){
                    query += "* ";
                } else {
                    for (int i = 0; i < columns.length; i++) {
                        if(i < columns.length -2) {
                            query += columns[i] + ", ";
                        } else {
                            query += columns[i] + " ";
                        }
                    }
                }
                if(whereClause != null) {
                    query += whereClause;
                }
                return db.rawQuery(query, null);
        }
        @Override
        protected void onPostExecute(Cursor result){
            db.close();
            cursor.close();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);
        if(parent == spinnerSports){

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void goToAddPlayerFromLeagues(View view){
        Intent intent = new Intent(getApplicationContext(), QuickAddPlayersActivity.class);
        intent.putExtra(NAME_KEY, name);
        startActivity(intent);
    }

    public void goToQuickAddTeams(View view){
        Intent intent = new Intent(getApplicationContext(), QuickAddTeamsActivity.class);
        intent.putExtra(NAME_KEY, name);
        startActivity(intent);
    }
}
