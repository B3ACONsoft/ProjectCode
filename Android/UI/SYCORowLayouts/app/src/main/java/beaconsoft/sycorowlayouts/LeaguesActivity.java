package beaconsoft.sycorowlayouts;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LeaguesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String   NAME_KEY = "beaconsoft.sycorowlayouts.NAME";
    private static final String  ADMIN_KEY = "beaconsoft.sycorowlayouts.ADMIN";
    private static final String  EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private static final String   TEAM_KEY = "beaconsoft.sycorowlayouts.TEAM";
    private static final String LEAGUE_KEY = "beaconsoft.sycorowlayouts.LEAGUE";
    private static final String   USER_KEY = "beaconsoft.sycorowlayouts.USER";

    private DBHelper helper = new DBHelper(this);
    private String name;
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
    public void onResume(){
        super.onResume();
        loadSpinners();
//        onSpinnerSportsChange (selectChoiceBuilder(currentSport , "sport" ,  "sport_name"));
//        onSpinnerLeaguesChange(selectChoiceBuilder(currentLeague, "league", "league_name"));
//        onSpinnerTeamsChange  (selectChoiceBuilder(currentTeam  , "team"  ,   "team_name"));
//        onSpinnerUsersChange  (selectChoiceBuilder(currentUser  , "users" ,   "user_name"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leagues);
        /* Receive whats passed from Main and insert into appropriate variables, start database in this activity */
        Intent intent = getIntent();
        name = intent.getStringExtra(NAME_KEY);
        email = intent.getStringExtra(EMAIL_KEY);
        currentAdmin = intent.getIntExtra(ADMIN_KEY, 0);
        db = helper.getReadableDatabase();

        /*Set text in the headers for the administrator */
        textViewLeaguesEmail = (TextView) findViewById(R.id.textViewLeagueEmail);
        TextView textViewLeaguesAdmin = (TextView) findViewById(R.id.textViewPromptChooseSport);
        textViewLeaguesEmail.setText(email);
        textViewLeaguesAdmin.setText(name + ", please choose a sport...");

        loadSpinners();
    }
        /* Call the database and ask for the sports we have leagues for, insert into the first spinner
        * through a series of arraylists and a hashmap */
    public void loadSpinners(){

        spinnerSports = (Spinner) findViewById(R.id.spinnerMyLeaguesSports);
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
        }
        cursor.close();

        adapterSpinnerSports = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sportNameArrayList.toArray());
        adapterSpinnerSports.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSports.setAdapter(adapterSpinnerSports);
        spinnerSports.setOnItemSelectedListener(this);
        spinnerSports.setEnabled(true);
        spinnerSports.setSelection(0);

        /* Find the initial current sport and use it to load the next spinner, leagues */
        currentSport = sportIdArrayList.get(0);

        spinnerLeagues = (Spinner) findViewById(R.id.spinnerMyLeaguesLeagues);

        leagueIdArrayList = new ArrayList<>();
        leagueNameArrayList = new ArrayList<>();
        hashMapLeagues = new HashMap<>();
        cursor = db.rawQuery("SELECT league_id, league_name FROM league WHERE sport_id = " + currentSport + " AND user_id = " + currentAdmin, null);
        if (cursor.moveToFirst() && ((cursor != null) && (cursor.getCount()>0))) {
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
        cursor.close();

        adapterSpinnerLeagues = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, leagueNameArrayList);
        adapterSpinnerLeagues.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLeagues.setAdapter(adapterSpinnerLeagues);
        spinnerLeagues.setOnItemSelectedListener(this);
        spinnerLeagues.setEnabled(true);
        spinnerLeagues.setSelection(0);

        teamIdArrayList = new ArrayList<>();
        teamNameArrayList = new ArrayList<>();
        hashMapTeams = new HashMap<>();

        cursor = db.rawQuery("SELECT team_id, team_name " +
                              " FROM team " + " " +
                             " WHERE league_id = " + currentLeague + ";", null);
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
        spinnerUsers = (Spinner) findViewById(R.id.spinnerMyLeaguesPlayers);
        userIdArrayList = new ArrayList<>();
        userNameArrayList = new ArrayList<>();
        hashMapUsers = new HashMap<>();

        if (cursor.moveToFirst() && ((cursor != null) && (cursor.getCount()>0)) ){
            do {
                hashMapUsers.put(cursor.getString(cursor.getColumnIndexOrThrow("fname")) + " " + cursor.getString(cursor.getColumnIndexOrThrow("lname")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
                userIdArrayList.add(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
                userNameArrayList.add(cursor.getString(cursor.getColumnIndexOrThrow("fname")) + " " +
                        cursor.getString(cursor.getColumnIndexOrThrow("lname")));
            } while (cursor.moveToNext());
            currentUser = userIdArrayList.get(0);
            adapterSpinnerUsers = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                    userNameArrayList);
        }
        cursor.close();

        adapterSpinnerUsers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUsers.setAdapter(adapterSpinnerUsers);
        spinnerUsers.setOnItemSelectedListener(this);
        spinnerUsers.setEnabled(true);
        spinnerUsers.setSelection(0);
    }

    private void unlockViews(){
        for(View foo: spinnerUsers.getTouchables()){
            foo.setEnabled(true);
            foo.setClickable(true);
        }
        for(View foo: spinnerTeams.getTouchables()) {
            foo.setEnabled(true);
            foo.setClickable(true);
        }
        for(View foo: spinnerLeagues.getTouchables()) {
            foo.setEnabled(true);
            foo.setClickable(true);
        }
    }

//    public String selectChoiceBuilder(int i, String table, String column){
//        if(table.equals("users"))
//            cursor = db.rawQuery("SELECT " + column + " FROM users  WHERE   user_id = " + i + ";", null);
//        else if(table.equals("team"))
//            cursor = db.rawQuery("SELECT " + column + " FROM team   WHERE   team_id = " + i + ";", null);
//        else if(table.equals("league"))
//            cursor = db.rawQuery("SELECT " + column + " FROM league WHERE league_id = " + i + ";", null);
//        else
//            cursor = db.rawQuery("SELECT " + column + " FROM sport  WHERE  sport_id = " + i + ";", null);
//
//        if(cursor.moveToFirst()){
//            do{
//                return cursor.getString(cursor.getColumnIndexOrThrow(column));
//            }while(cursor.moveToNext());
//        }
//
//        cursor.close();
//        return null;
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String choice = parent.getItemAtPosition(position).toString();


        if(parent == spinnerSports ){
            for(View foo: spinnerLeagues.getTouchables()) {
                foo.setEnabled(true);
                foo.setClickable(true);
            }
            onSpinnerSportsChange(choice);
        }
        if(parent == spinnerLeagues){

            for(View foo: spinnerTeams.getTouchables()) {
                foo.setEnabled(true);
                foo.setClickable(true);
            }
            onSpinnerLeaguesChange(choice);
        }
        if(parent == spinnerTeams){
            for(View foo: spinnerUsers.getTouchables()) {
                foo.setEnabled(true);
                foo.setClickable(true);
            }
            onSpinnerTeamsChange(choice);
        }
        if(parent == spinnerUsers){
            onSpinnerUsersChange(choice);
        }
    }

    private void onSpinnerSportsChange(String choiceSportName){
        currentSport = hashMapSports.get(choiceSportName);
        cursor = db.rawQuery("SELECT l.league_id, l.league_name FROM league l, sport s WHERE s.sport_id = " + currentSport + " AND l.user_id = " + currentAdmin + " AND s.sport_id = l.sport_id;", null);
        for(View foo: spinnerUsers.getTouchables()){
            foo.setClickable(true);
            foo.setEnabled(true);
        }
        for(View foo: spinnerTeams.getTouchables()){
            foo.setClickable(true);
            foo.setEnabled(true);
        }
        for(View foo: spinnerLeagues.getTouchables()){
            foo.setClickable(true);
            foo.setEnabled(true);
        }
        if (cursor.moveToFirst() && ((cursor != null) && (cursor.getCount()>0))) {

            leagueIdArrayList.clear();
            leagueNameArrayList.clear();
            hashMapLeagues.clear();
            do {
                leagueIdArrayList.add(cursor.getInt(cursor.getColumnIndexOrThrow("league_id")));
                leagueNameArrayList.add(cursor.getString(cursor.getColumnIndexOrThrow("league_name")));
                hashMapLeagues.put(cursor.getString(cursor.getColumnIndexOrThrow("league_name")), cursor.getInt(cursor.getColumnIndexOrThrow("league_id")));

            } while (cursor.moveToNext());
            adapterSpinnerLeagues = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, leagueNameArrayList.toArray());

            currentLeague = hashMapLeagues.get(leagueNameArrayList.get(0));
            spinnerLeagues.setAdapter(adapterSpinnerLeagues);
            spinnerLeagues.setOnItemSelectedListener(this);
            spinnerLeagues.setEnabled(true);
            spinnerLeagues.setSelection(0);

        } else {
            for(View foo: spinnerLeagues.getTouchables()){
                foo.setEnabled(false);
                foo.setClickable(false);
            }
            for(View foo: spinnerTeams.getTouchables()){
                foo.setEnabled(false);
                foo.setClickable(false);
            }
            for(View foo: spinnerUsers.getTouchables()){
                foo.setEnabled(false);
                foo.setClickable(false);
            }
            spinnerLeagues.setEnabled(false);
            spinnerTeams.setEnabled(false);
            spinnerUsers.setEnabled(false);
        }

        cursor.close();
    }

    private void onSpinnerLeaguesChange(String choiceLeagueName) {

        currentLeague = hashMapLeagues.get(choiceLeagueName);

        cursor = db.rawQuery("SELECT team_id, team_name FROM team WHERE league_id = " + currentLeague, null);
        for(View foo: spinnerUsers.getTouchables()){
            foo.setClickable(true);
            foo.setEnabled(true);
        }
        for(View foo: spinnerTeams.getTouchables()){
            foo.setClickable(true);
            foo.setEnabled(true);
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

            currentTeam = hashMapTeams.get(teamNameArrayList.get(0));
            spinnerTeams.setAdapter(adapterSpinnerTeams);
            spinnerTeams.setOnItemSelectedListener(this);
            spinnerTeams.setEnabled(true);
            spinnerTeams.setSelection(0);

        }else{
            for(View foo: spinnerTeams.getTouchables()){
                foo.setEnabled(false);
                foo.setClickable(false);
            }
            for(View foo: spinnerUsers.getTouchables()){
                foo.setEnabled(false);
                foo.setClickable(false);
            }
            spinnerTeams.setEnabled(false);
            spinnerUsers.setEnabled(false);
        }
        cursor.close();

    }

    private void onSpinnerTeamsChange(String choiceTeamName){

        currentTeam = hashMapTeams.get(choiceTeamName);

        cursor = db.rawQuery("SELECT e.user_id, u.fname, u.lname FROM enrollment e, users u WHERE e.team_id= " + currentTeam + " AND e.user_id = u.user_id;", null);
        for(View foo: spinnerUsers.getTouchables()){
            foo.setClickable(true);
            foo.setEnabled(true);
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
            currentUser = userIdArrayList.get(0);
            adapterSpinnerUsers = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, userNameArrayList.toArray());
            spinnerUsers.setAdapter(adapterSpinnerUsers);
            spinnerUsers.setOnItemSelectedListener(this);
            spinnerUsers.setEnabled(true);
            spinnerUsers.setSelection(0);


        }else{
            for(View foo: spinnerUsers.getTouchables()){
                foo.setEnabled(false);
                foo.setClickable(false);
            }
            spinnerUsers.setEnabled(false);
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
        for(View foo: parent.getTouchables()){
            foo.setEnabled(false);
            foo.setClickable(false);
        }
    }

    public void goToEditPlayerFromLeagues(View view){
        Intent intent = new Intent(getApplicationContext(), QuickAddPlayersActivity.class);
        intent.putExtra(NAME_KEY, name);
        intent.putExtra(ADMIN_KEY, currentAdmin);
        intent.putExtra(LEAGUE_KEY, currentLeague);
        intent.putExtra(TEAM_KEY, currentTeam + "");
        intent.putExtra(EMAIL_KEY, email);
        intent.putExtra(USER_KEY, currentUser);

        PendingIntent pendingIntent = TaskStackBuilder.create(this).addNextIntentWithParentStack(getIntent())
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(pendingIntent);

        startActivity(intent);

    }

    public void goToAddPlayerFromLeagues(View view){
        Intent intent = new Intent(getApplicationContext(), QuickAddPlayersActivity.class);
        intent.putExtra(NAME_KEY, name);
        intent.putExtra(ADMIN_KEY, currentAdmin);
        intent.putExtra(LEAGUE_KEY, currentLeague);
        intent.putExtra(TEAM_KEY, currentTeam);
        intent.putExtra(EMAIL_KEY, email);

        PendingIntent pendingIntent = TaskStackBuilder.create(this).addNextIntentWithParentStack(getIntent())
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(pendingIntent);

        startActivity(intent);

    }

    public void goToCalendar(View view){
        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
        intent.putExtra(NAME_KEY, name);
        intent.putExtra(ADMIN_KEY, currentAdmin);
        intent.putExtra(LEAGUE_KEY, currentLeague);
        intent.putExtra(TEAM_KEY, currentTeam);
        intent.putExtra(EMAIL_KEY, email);
        startActivity(intent);
    }

    public void goToQuickAddTeams(View view){
        Intent intent = new Intent(getApplicationContext(), QuickAddTeamsActivity.class);
        intent.putExtra(  NAME_KEY, name);
        intent.putExtra( ADMIN_KEY, currentAdmin);
        intent.putExtra(LEAGUE_KEY, currentLeague);
        intent.putExtra(  TEAM_KEY, currentTeam);
        intent.putExtra(EMAIL_KEY, email);
        startActivity(intent);

    }
}
