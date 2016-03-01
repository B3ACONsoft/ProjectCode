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
    private int currentUser;

    private Cursor cursor;
    private SQLiteDatabase db;

    private HashMap<String, Integer> hashMapSports;
    private ArrayList<String> sportNameArrayList;
    private ArrayList<Integer> sportIdArrayList;
    private ArrayAdapter adapterSpinnerSports;
    private Spinner spinnerSports;

    private HashMap<String, Integer> hashMapLeagues;
    private ArrayList<String> leagueNameArrayList;
    private ArrayList<Integer> leagueIdArrayList;
    private ArrayAdapter adapterSpinnerLeagues;
    private Spinner spinnerLeagues;

    private HashMap<String, Integer> hashMapTeams;
    private ArrayList<String> teamNameArrayList;
    private ArrayList<Integer> teamIdArrayList;
    private ArrayAdapter adapterSpinnerTeams;
    private Spinner spinnerTeams;

    private HashMap<String, Integer> hashMapUsers;
    private ArrayList<String> userNameArrayList;
    private ArrayList<Integer> userIdArrayList;
    private ArrayAdapter adapterSpinnerUsers;
    private Spinner spinnerUsers;

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
        TextView textViewLeaguesAdmin = (TextView) findViewById(R.id.textViewPromptChooseSport);
        textViewLeaguesEmail.setText(email);
        textViewLeaguesAdmin.setText(name + ", please choose a sport...");

        /* Call the database and ask for the sports we have leagues for, insert into the first spinner
        * through a series of arraylists and a hashmap */


        hashMapSports = new HashMap<>();
        sportNameArrayList = new ArrayList<String>();
        sportIdArrayList = new ArrayList<Integer>();
        cursor = db.rawQuery("SELECT sport_id, sport_name FROM sport", null);
        if (!(cursor == null) && (cursor.moveToFirst())) {
            do {
                sportIdArrayList.add(cursor.getInt(cursor.getColumnIndexOrThrow("sport_id")));
                sportNameArrayList.add(cursor.getString(cursor.getColumnIndexOrThrow("sport_name")));
                hashMapSports.put(cursor.getString(cursor.getColumnIndexOrThrow("sport_name")), cursor.getInt(cursor.getColumnIndexOrThrow("sport_id")));
            } while (cursor.moveToNext());
        }else{
            for(View tchbls : spinnerLeagues.getTouchables() ) {
                tchbls.setEnabled(false);
            }
            for(View tchbls : spinnerTeams.getTouchables() ) {
                tchbls.setEnabled(false);
            }
            for(View tchbls : spinnerUsers.getTouchables() ) {
                tchbls.setEnabled(false);
            }
        }
        cursor.close();


        spinnerSports = (Spinner) findViewById(R.id.spinnerMyLeaguesSports);
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
        hashMapLeagues = new HashMap<>();
        cursor = db.rawQuery("SELECT league_id, league_name FROM league WHERE sport_id = " + currentSport + " AND user_id = " + adminId, null);
        if (cursor.moveToFirst()) {
            do {
                leagueIdArrayList.clear();
                leagueNameArrayList.clear();
                hashMapLeagues.clear();
                hashMapLeagues.put(cursor.getString(cursor.getColumnIndexOrThrow("league_name")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("league_id")));
                leagueIdArrayList.add(cursor.getInt(cursor.getColumnIndexOrThrow("league_id")));
                leagueNameArrayList.add(cursor.getString(cursor.getColumnIndexOrThrow("league_name")));
            } while (cursor.moveToNext());
        }

        currentLeague = leagueIdArrayList.get(0);
        System.out.println(currentLeague);
        cursor.close();

        spinnerLeagues = (Spinner) findViewById(R.id.spinnerMyLeaguesLeagues);
        adapterSpinnerLeagues = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, leagueNameArrayList);
        adapterSpinnerLeagues.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLeagues.setAdapter(adapterSpinnerLeagues);
        spinnerLeagues.setOnItemSelectedListener(this);
        spinnerLeagues.setEnabled(true);
        spinnerLeagues.setSelection(0);

        teamIdArrayList = new ArrayList<>();
        teamNameArrayList = new ArrayList<>();
        hashMapTeams = new HashMap<>();
        cursor = db.rawQuery("SELECT team_id, team_name FROM team WHERE league_id = " + currentLeague + ";", null);
        if (cursor.moveToFirst()) {
            do {
                hashMapTeams.put(cursor.getString(cursor.getColumnIndexOrThrow("team_name")), cursor.getInt(cursor.getColumnIndexOrThrow("team_id")));
                teamIdArrayList.add(cursor.getInt(cursor.getColumnIndexOrThrow("team_id")));
                teamNameArrayList.add(cursor.getString(cursor.getColumnIndexOrThrow("team_name")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        currentTeam = teamIdArrayList.get(0);

        spinnerTeams = (Spinner) findViewById(R.id.spinnerMyLeaguesTeams);
        adapterSpinnerTeams = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, teamNameArrayList);
        adapterSpinnerTeams.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTeams.setAdapter(adapterSpinnerTeams);
        spinnerTeams.setOnItemSelectedListener(this);
        spinnerTeams.setEnabled(true);
        spinnerTeams.setSelection(0);

        cursor = db.rawQuery("SELECT e.user_id, u.fname, u.lname FROM enrollment e, users u " +
                             " WHERE e.team_id = " + currentTeam +
                             "   AND e.user_id = u.user_id;", null);
        userIdArrayList = new ArrayList<>();
        userNameArrayList = new ArrayList<>();
        hashMapUsers = new HashMap<>();
        if (cursor.moveToFirst()){
            do {
                hashMapUsers.put(cursor.getString(cursor.getColumnIndexOrThrow("fname")) + " " + cursor.getString(cursor.getColumnIndexOrThrow("lname")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
                userIdArrayList.add(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
                userNameArrayList.add(cursor.getString(cursor.getColumnIndexOrThrow("fname")) + " " +
                        cursor.getString(cursor.getColumnIndexOrThrow("lname")));
            } while (cursor.moveToNext());
        }
        cursor.close();

        spinnerUsers = (Spinner) findViewById(R.id.spinnerMyLeaguesPlayers);
        adapterSpinnerUsers = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                userNameArrayList);
        adapterSpinnerUsers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUsers.setAdapter(adapterSpinnerUsers);
        spinnerUsers.setOnItemSelectedListener(this);
        spinnerUsers.setEnabled(true);
        spinnerUsers.setSelection(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String choice = parent.getItemAtPosition(position).toString();


        if(parent == spinnerSports){
            onSpinnerSportsChange(choice);
        }else if(parent == spinnerLeagues){
            onSpinnerLeaguesChange(choice);
        }else if(parent == spinnerTeams){
            onSpinnerTeamsChange(choice);
        }else if(parent == spinnerUsers){
            onSpinnerUsersChange(choice);
        }
    }

    private void onSpinnerSportsChange(String choiceSportName){
        currentSport = hashMapSports.get(choiceSportName);
        cursor = db.rawQuery("SELECT league_id, league_name FROM league WHERE sport_id = " + currentSport + " AND user_id = " + adminId, null);
        if ((cursor.getCount() > 0) && cursor.moveToFirst()) {
            leagueIdArrayList.clear();
            leagueNameArrayList.clear();
            hashMapLeagues.clear();
            do {
                leagueIdArrayList.add(cursor.getInt(cursor.getColumnIndexOrThrow("league_id")));
                leagueNameArrayList.add(cursor.getString(cursor.getColumnIndexOrThrow("league_name")));
                hashMapLeagues.put(cursor.getString(cursor.getColumnIndexOrThrow("league_name")), cursor.getInt(cursor.getColumnIndexOrThrow("league_id")));

            } while (cursor.moveToNext());
            spinnerLeagues.setEnabled(true);
            adapterSpinnerLeagues = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, leagueNameArrayList.toArray());
            spinnerLeagues.setAdapter(adapterSpinnerLeagues);
            spinnerLeagues.setSelection(0);
            currentLeague = hashMapLeagues.get(leagueNameArrayList.get(0));
        } else {
            adapterSpinnerSports = ArrayAdapter.createFromResource(getBaseContext(), R.array.noLeague, android.R.layout.simple_spinner_dropdown_item);
            spinnerLeagues.setClickable(false);
            spinnerTeams.setClickable(false);
            spinnerUsers.setClickable(false);
        }
        cursor.close();
    }

    private void onSpinnerLeaguesChange(String choiceLeagueName) {
        currentLeague = hashMapLeagues.get(choiceLeagueName);
        cursor = db.rawQuery("SELECT team_id, team_name FROM team WHERE league_id = " + currentLeague, null);
        for(View lol: spinnerTeams.getTouchables()) {
            lol.setEnabled(true);
            lol.setClickable(true);
        }
        if (cursor.moveToFirst() && ((cursor != null) && (cursor.getCount()>0))) {
            teamIdArrayList.clear();
            teamNameArrayList.clear();
            hashMapTeams.clear();
            do{
                teamIdArrayList.add(cursor.getInt(cursor.getColumnIndexOrThrow("team_id")));
                teamNameArrayList.add(cursor.getString(cursor.getColumnIndexOrThrow("team_name")));
                hashMapTeams.put(cursor.getString(cursor.getColumnIndexOrThrow("team_name")), cursor.getInt(cursor.getColumnIndexOrThrow("team_id")));
            } while (cursor.moveToNext());

            adapterSpinnerTeams = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, teamNameArrayList.toArray());
            spinnerTeams.setAdapter(adapterSpinnerTeams);
            spinnerTeams.setSelection(0);
            currentTeam = hashMapTeams.get(teamNameArrayList.get(0));
        }else {
            for(View lol: spinnerTeams.getTouchables()) {
                lol.setEnabled(false);
                lol.setClickable(false);
            }
        }
        cursor.close();
    }

    private void onSpinnerTeamsChange(String choiceTeamName){
        currentTeam = hashMapTeams.get(choiceTeamName);
        for(View lol: spinnerUsers.getTouchables()){
            lol.setEnabled(true);
            lol.setClickable(true);
        }
        cursor = db.rawQuery("SELECT e.user_id, u.fname, u.lname FROM enrollment e, users u WHERE e.team_id= " + currentTeam + " AND e.user_id = u.user_id;", null);
        for(View lol: spinnerUsers.getTouchables()) {
            lol.setEnabled(true);
            lol.setClickable(true);
        }
        if(cursor.moveToFirst() && ((cursor != null) && (cursor.getCount()>0))){
            userIdArrayList.clear();
            userNameArrayList.clear();
            hashMapUsers.clear();
            do{
                userIdArrayList.add(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
                userNameArrayList.add(cursor.getString(cursor.getColumnIndexOrThrow("fname")) + " " + cursor.getString(cursor.getColumnIndexOrThrow("lname")));
                hashMapUsers.put(cursor.getString(cursor.getColumnIndexOrThrow("fname")) + " " + cursor.getString(cursor.getColumnIndexOrThrow("lname")),
                       cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
            }while(cursor.moveToNext());

            adapterSpinnerUsers = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, userNameArrayList.toArray());
            spinnerUsers.setAdapter(adapterSpinnerUsers);
            spinnerUsers.setSelection(0);
            currentUser = userIdArrayList.get(0);
        }else {
            for(View lol: spinnerUsers.getTouchables()) {
                lol.setEnabled(false);
                lol.setClickable(false);
            }
        }
        cursor.close();
    }

    private void onSpinnerUsersChange(String choiceUser){
        currentUser = hashMapUsers.get(choiceUser);
    }

    private ArrayList<String> insertEmptyArrayList(){
        ArrayList<String> empty = new ArrayList<>();
        empty.add("...please choose...");
        return empty;
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
