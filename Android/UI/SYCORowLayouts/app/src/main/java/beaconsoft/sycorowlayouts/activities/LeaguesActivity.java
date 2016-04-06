package beaconsoft.sycorowlayouts.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

import beaconsoft.sycorowlayouts.DataSource;
import beaconsoft.sycorowlayouts.UpdateService;
import beaconsoft.sycorowlayouts.dbobjects.League;
import beaconsoft.sycorowlayouts.R;
import beaconsoft.sycorowlayouts.dbobjects.Player;
import beaconsoft.sycorowlayouts.dbobjects.Sport;
import beaconsoft.sycorowlayouts.dbobjects.Team;
import beaconsoft.sycorowlayouts.dbobjects.Users;

public class LeaguesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String   NAME_KEY = "beaconsoft.sycorowlayouts.NAME";
    private static final String  ADMIN_KEY = "beaconsoft.sycorowlayouts.ADMIN";
    private static final String  EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private static final String   TEAM_KEY = "beaconsoft.sycorowlayouts.TEAM";
    private static final String LEAGUE_KEY = "beaconsoft.sycorowlayouts.LEAGUE";
    private static final String   USER_KEY = "beaconsoft.sycorowlayouts.USER";
    private static final String SERVICEKEY = "beaconsoft.sycorowlayouts.SERVICEKEY";
    private static final String PLAYER_KEY = "beaconsoft.sycorowlayouts.PLAYER";
    private static final int INTENT_REQUEST_CODE_EDIT_LEAGUES = 1;
    private static final int INTENT_REQUEST_CODE_ADD_TEAMS = 2;
    private static final int INTENT_REQUEST_CODE_EDIT_TEAMS = 3;
    private String name;
    private String email;
    private TextView textViewLeaguesEmail;
    private int currentAdmin;
    private int currentSport;
    private int currentLeague;
    private int currentTeam;
    private int currentUser;
    private int currentPlayer;
    private ArrayAdapter adapterSpinnerSports;
    private Spinner spinnerSports;
    private ArrayList<Sport> sportsArrayList;
    private ArrayAdapter adapterSpinnerLeagues;
    private Spinner spinnerLeagues;
    private ArrayList<League> leaguesArrayList;
    private ArrayAdapter adapterSpinnerTeams;
    private Spinner spinnerTeams;
    private ArrayList<Team> teamsArrayList;
    private ArrayAdapter adapterSpinnerPlayers;
    private Spinner spinnerPlayers;
    private ArrayList<Player> playersArrayList;
    private Button buttonStartLeague;
    private Button buttonCalendar;
    private Button buttonAddTeams;
    private Button buttonAddPlayers;
    private Button buttonEditTeam;
    private Button buttonEditPlayer;
    private DataSource dataSource;

    @Override
    public void onResume() {
        Log.e("ON_RESUME", ".....................................................ON_RESUME_BEGIN");

        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e("ON_PAUSE", ".....................................................ON_PAUSE");
        dataSource.close();
        super.onPause();
    }
    @Override
    public void onDestroy(){
        Log.e("ON_DESTROY", ".....................................................ON_DESTROY");
        dataSource.close();
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leagues);

        Intent serviceIntent = new Intent(this, UpdateService.class);
        int localInteger = 100000000;
        serviceIntent.putExtra(SERVICEKEY, localInteger);
        startService(serviceIntent);

        Log.e("ON_CREATE", ".....................................................ON_CREATE");

        /* Receive whats passed from Main and insert into appropriate variables, start database in this activity */
        Intent intent = getIntent();
        name = intent.getStringExtra(NAME_KEY);
        email = intent.getStringExtra(EMAIL_KEY);
        currentAdmin = intent.getIntExtra(ADMIN_KEY, 0);
        currentSport = 0;
        currentLeague = 0;
        currentTeam = 0;
        currentUser = 0;
        currentPlayer = 0;
        spinnerSports = (Spinner) findViewById(R.id.spinnerMyLeaguesSports);
        spinnerLeagues = (Spinner) findViewById(R.id.spinnerMyLeaguesLeagues);
        spinnerTeams = (Spinner) findViewById(R.id.spinnerMyLeaguesTeams);
        spinnerPlayers = (Spinner) findViewById(R.id.spinnerMyLeaguesPlayers);
        buttonStartLeague = (Button)findViewById(R.id.buttonMyLeaguesAddLeague);
        buttonCalendar    = (Button)findViewById(R.id.buttonMyLeaguesCalendar);
        buttonAddTeams    = (Button)findViewById(R.id.buttonMyLeaguesAddTeam);
        buttonAddPlayers  = (Button)findViewById(R.id.buttonMyLeaguesAddPlayer);
        buttonEditTeam    = (Button)findViewById(R.id.buttonMyLeaguesEditTeam);
        buttonEditPlayer  = (Button)findViewById(R.id.buttonMyLeaguesEditPlayer);
        dataSource = new DataSource(this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*Set text in the headers for the administrator */
        textViewLeaguesEmail = (TextView) findViewById(R.id.textViewLeagueEmail);
        TextView textViewLeaguesAdmin = (TextView) findViewById(R.id.textViewPromptChooseSport);
        textViewLeaguesEmail.setText(email);
        textViewLeaguesAdmin.setText(name + ", please choose a sport...");

        loadSpinners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(requestCode == INTENT_REQUEST_CODE_EDIT_LEAGUES && data != null){
            if(resultCode == Activity.RESULT_OK) {
                Log.e("REQUEST CODE", "................................................REQUEST CODE " + requestCode);

                loadSpinners();

                int resultSport = data.getIntExtra("sport_id", 0);
                int resultLeague = data.getIntExtra("league_id", 0);

                for (int i = 0; i < sportsArrayList.size(); i++) {
                    if (sportsArrayList.get(i).getSportID() == resultSport) {
                        spinnerSports.setSelection(i);
                        onSpinnerSportsChange();
                        break;
                    }
                }

                for (int i = 0; i < leaguesArrayList.size(); i++) {
                    if (leaguesArrayList.get(i).getLeagueID() == resultLeague) {
                        spinnerLeagues.setSelection(i);
                        onSpinnerLeaguesChange();
                        break;
                    }
                }

                Log.e("INT EXTRAS", ".........sport_id: " + resultSport +
                        ".....league_id: " + resultLeague);
                Log.e("CURRENT SPORT/LEAGUE", ".........sport: " + currentSport + " ....league: " + currentLeague );
            } else{
                Toast toast = Toast.makeText(this, "BAD RESULT", Toast.LENGTH_LONG);
            }
        }else if(requestCode == INTENT_REQUEST_CODE_ADD_TEAMS && data != null){
            if(resultCode == Activity.RESULT_OK){
                Log.e("REQUEST CODE", "................................................REQUEST CODE " + requestCode);
                int resultTeam = data.getIntExtra("team_id", 0);

                teamsArrayList.clear();
                teamsArrayList.addAll(dataSource.getListOfTeamsByLeague(currentLeague));
                adapterSpinnerTeams = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, teamsArrayList);
                spinnerTeams.setAdapter(adapterSpinnerTeams);
                if(!teamsArrayList.isEmpty()){

                }else{

                }

                for(int i = 0; i < teamsArrayList.size(); i++){
                    if(teamsArrayList.get(i).getTeamID() == resultTeam){
                        spinnerTeams.setSelection(i);
                        onSpinnerTeamsChange();
                        break;
                    }
                }

                activateView(spinnerTeams);
                activateView(buttonEditTeam);
                activateView(buttonCalendar);
                activateView(buttonAddPlayers);

                Log.e("INT EXTRAS", "........team_id: " + resultTeam);
                Log.e("Current TEAM", "................TEAM: " + currentTeam);
            }else{
                if(teamsArrayList.isEmpty()){
                    deactivateView(buttonEditTeam);
                }else{
                    activateView(buttonEditTeam);
                }
            }
        }else if(requestCode == INTENT_REQUEST_CODE_EDIT_TEAMS ){
            if(resultCode == Activity.RESULT_OK){

                Log.e("REQUEST CODE", "................................................REQUEST CODE " + requestCode);
                int resultTeam = data.getIntExtra("team_id", 0);

                teamsArrayList.clear();
                teamsArrayList.addAll(dataSource.getListOfTeamsByLeague(currentLeague));
                adapterSpinnerTeams = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, teamsArrayList);
                spinnerTeams.setAdapter(adapterSpinnerTeams);
                if(!teamsArrayList.isEmpty()){

                }else{

                }

                for(int i = 0; i < teamsArrayList.size(); i++){
                    if(teamsArrayList.get(i).getTeamID() == resultTeam){
                        spinnerTeams.setSelection(i);
                        onSpinnerTeamsChange();
                        break;
                    }
                }

            }else{

            }
        }else{
            Toast toast = Toast.makeText(this, "UNRECOGNIZED REQUEST CODE", Toast.LENGTH_LONG);
        }
    }

    private void deactivateView(View parent){
        for(View foo: parent.getTouchables()){
            foo.setClickable(false);
            foo.setEnabled(false);
        }
        parent.setEnabled(false);
        parent.setClickable(false);
    }

    private void activateView(View parent){
        for(View foo: parent.getTouchables()){
            foo.setClickable(true);
            foo.setEnabled(true);
        }
        parent.setEnabled(true);
        parent.setClickable(true);
    }

        /* Call the database and ask for the sports we have leagues for, insert into the first spinner
        * through a series of arraylists and a hashmap */
    public void loadSpinners(){

        activateView(spinnerSports);
        activateView(spinnerLeagues);
        activateView(spinnerTeams);
        activateView( spinnerPlayers);
        activateView(buttonStartLeague);
        activateView(buttonCalendar);
        activateView(buttonAddTeams);
        activateView(buttonEditTeam);
        activateView(buttonAddPlayers);
        activateView(buttonEditPlayer);

        /*LOAD THE SPORTS SPINNER AND ADAPTER*/
        sportsArrayList = new ArrayList<>();
        sportsArrayList.clear();
        sportsArrayList.addAll(dataSource.getListOfSports());
        if(sportsArrayList.isEmpty()){
            deactivateView(spinnerSports);
            deactivateView(spinnerLeagues);
            deactivateView(spinnerSports);
            deactivateView(spinnerPlayers);
            deactivateView(buttonStartLeague);
            deactivateView(buttonCalendar);
            deactivateView(buttonAddTeams);
            deactivateView(buttonEditTeam);
            deactivateView(buttonAddPlayers);
            deactivateView(buttonEditPlayer);
            currentSport = 0;
            currentLeague = 0;
            currentTeam = 0;
            currentUser = 0;
        }else {
            activateView(spinnerLeagues);
            activateView(spinnerTeams);
            activateView(spinnerPlayers);
            activateView(buttonStartLeague);
            activateView(buttonCalendar);
            activateView(buttonAddTeams);
            activateView(buttonEditTeam);
            activateView(buttonAddPlayers);
            activateView(buttonEditPlayer);
            adapterSpinnerSports = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sportsArrayList.toArray());
            adapterSpinnerSports.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSports.setAdapter(adapterSpinnerSports);
            spinnerSports.setOnItemSelectedListener(this);
            spinnerSports.setEnabled(true);
            Sport tempSport = (Sport) spinnerSports.getSelectedItem();
            currentSport = tempSport.getSportID();
        }

        leaguesArrayList = new ArrayList<>();
        leaguesArrayList.clear();
        leaguesArrayList.addAll(dataSource.getListOfLeaguesBySport(currentSport));
        if (leaguesArrayList.isEmpty()) {
            deactivateView(spinnerLeagues);
            deactivateView(spinnerTeams);
            deactivateView( spinnerPlayers);
            deactivateView(buttonCalendar);
            deactivateView(buttonAddTeams);
            deactivateView(buttonEditTeam);
            deactivateView(buttonAddPlayers);
            deactivateView(buttonEditPlayer);
            currentLeague = 0;
            currentTeam = 0;
            currentUser = 0;
        }else {
            activateView(buttonStartLeague);
            activateView(buttonCalendar);
            activateView(buttonAddTeams);
            activateView(buttonEditTeam);
            activateView(buttonAddPlayers);
            activateView(buttonEditPlayer);
            adapterSpinnerLeagues = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, leaguesArrayList.toArray());
            adapterSpinnerLeagues.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerLeagues.setAdapter(adapterSpinnerLeagues);
            spinnerLeagues.setOnItemSelectedListener(this);
            spinnerLeagues.setEnabled(true);
            League tempLeague = (League) spinnerLeagues.getSelectedItem();
            currentLeague = tempLeague.getLeagueID();
        }


        teamsArrayList = new ArrayList<>();
        teamsArrayList.clear();
        teamsArrayList.addAll(dataSource.getListOfTeamsByLeague(currentLeague));
        if(teamsArrayList.isEmpty()){
            deactivateView(spinnerTeams);
            deactivateView( spinnerPlayers);
            deactivateView(buttonAddPlayers);
            deactivateView(buttonEditPlayer);
            currentTeam = 0;
            currentUser = 0;
        }else {
            adapterSpinnerTeams = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, teamsArrayList);
            adapterSpinnerTeams.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTeams.setAdapter(adapterSpinnerTeams);
            spinnerTeams.setOnItemSelectedListener(this);
            spinnerTeams.setEnabled(true);
            Team listTeam = (Team) spinnerTeams.getSelectedItem();
            currentTeam = listTeam.getTeamID();
        }

        playersArrayList = new ArrayList<>();
        playersArrayList.clear();
        playersArrayList.addAll(dataSource.getListOfPlayersByTeam(currentTeam));
        if(playersArrayList.isEmpty()){
            deactivateView( spinnerPlayers);
            deactivateView(buttonEditPlayer);
        }else {
            activateView( spinnerPlayers);
            activateView(buttonAddPlayers);
            activateView(buttonEditPlayer);
            adapterSpinnerPlayers = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, playersArrayList);
            adapterSpinnerPlayers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPlayers.setAdapter(adapterSpinnerPlayers);
            spinnerPlayers.setOnItemSelectedListener(this);
            spinnerPlayers.setEnabled(true);
            Player player = (Player) spinnerPlayers.getSelectedItem();
            currentPlayer = player.getPlayerID();
        }
        statusChangeDisplay();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String choice = parent.getItemAtPosition(position).toString();


        if(parent == spinnerSports ){
            onSpinnerSportsChange();
        }
        if(parent == spinnerLeagues){
            onSpinnerLeaguesChange();
        }
        if(parent == spinnerTeams){
            onSpinnerTeamsChange();
        }
        if(parent ==  spinnerPlayers){
            onSpinnerPlayersChange(choice);
        }
    }

    private void onSpinnerSportsChange(){

        Sport tempSport = (Sport)spinnerSports.getSelectedItem();
        currentSport = tempSport.getSportID();

        leaguesArrayList.clear();
        leaguesArrayList.addAll(dataSource.getListOfLeaguesBySport(currentSport));

        if(!leaguesArrayList.isEmpty()){

            activateView(spinnerLeagues);
            activateView(spinnerTeams);
            activateView( spinnerPlayers);
            activateView(buttonStartLeague);
            //activateView(buttonCalendar);
            activateView(buttonAddTeams);
            activateView(buttonEditTeam);
            activateView(buttonAddPlayers);
            activateView(buttonEditPlayer);
            adapterSpinnerLeagues = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, leaguesArrayList);
            spinnerLeagues.setAdapter(adapterSpinnerLeagues);
            spinnerLeagues.setOnItemSelectedListener(this);
            spinnerLeagues.setEnabled(true);

            boolean foundLeague = false;
            for(int i = 0; i < leaguesArrayList.size(); i++){
                if(leaguesArrayList.get(i).getLeagueID() == currentLeague){
                    spinnerLeagues.setSelection(i);
                    onSpinnerLeaguesChange();
                    foundLeague = true;
                    break;
                }
            }
            if(!foundLeague){
                League league = (League)spinnerLeagues.getSelectedItem();
                currentLeague = league.getLeagueID();
            }

        } else {
            deactivateView(spinnerLeagues);
            deactivateView(spinnerTeams);
            deactivateView(spinnerPlayers);
            deactivateView(buttonCalendar);
            deactivateView(buttonEditPlayer);
            deactivateView(buttonEditTeam);
            deactivateView(buttonAddPlayers);
            deactivateView(buttonAddTeams);
            currentLeague = 0;
            currentTeam = 0;
            currentUser = 0;
        }
        statusChangeDisplay();
    }

    private void onSpinnerLeaguesChange() {

        League tempLeague = (League)spinnerLeagues.getSelectedItem();
        currentLeague = tempLeague.getLeagueID();

        teamsArrayList.clear();
        teamsArrayList.addAll(dataSource.getListOfTeamsByLeague(currentLeague));

        if(!teamsArrayList.isEmpty()){

            activateView(spinnerTeams);
            activateView(spinnerPlayers);
            activateView(buttonCalendar);
            activateView(buttonEditTeam);
            activateView(buttonAddTeams);
            activateView(buttonAddPlayers);
            activateView(buttonEditPlayer);
            adapterSpinnerTeams = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, teamsArrayList.toArray());
            spinnerTeams.setAdapter(adapterSpinnerTeams);
            spinnerTeams.setOnItemSelectedListener(this);
            spinnerTeams.setEnabled(true);

            Team listTeam = (Team)spinnerTeams.getSelectedItem();
            currentTeam = listTeam.getTeamID();

        }else{
            deactivateView(spinnerTeams);
            deactivateView(spinnerPlayers);
            deactivateView(buttonCalendar);
            deactivateView(buttonEditTeam);
            deactivateView(buttonAddPlayers);
            deactivateView(buttonEditPlayer);
            currentTeam = 0;
            currentUser = 0;
        }
        statusChangeDisplay();
    }

    private void onSpinnerTeamsChange(){

        Team tempTeam = (Team)spinnerTeams.getSelectedItem();
        currentTeam = tempTeam.getTeamID();
        activateView(spinnerPlayers);
        playersArrayList.clear();
        playersArrayList.addAll(dataSource.getListOfPlayersByTeam(currentTeam));

        if(playersArrayList.isEmpty()){
            deactivateView(spinnerPlayers);
            currentUser = 0;
            currentPlayer = 0;
        }else {
            adapterSpinnerPlayers = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, playersArrayList.toArray());
            spinnerPlayers.setAdapter(adapterSpinnerPlayers);
            spinnerPlayers.setOnItemSelectedListener(this);
            Player player = (Player) spinnerPlayers.getSelectedItem();
            currentPlayer = player.getPlayerID();
            currentUser   = player.getUserID();
        }
        statusChangeDisplay();
    }

    private void onSpinnerPlayersChange(String choiceUser){

        Player player = (Player) spinnerPlayers.getSelectedItem();
        currentPlayer = player.getPlayerID();
        currentUser   = player.getUserID();
        statusChangeDisplay();
    }

    private void statusChangeDisplay(){
        textViewLeaguesEmail.setText("AID:" + currentAdmin + " SID:" + currentSport + " LID:" + currentLeague
                + " TID" + currentTeam + " UID:" + currentUser + " PID:" + currentPlayer);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        for(View foo: parent.getTouchables()){
            foo.setEnabled(false);
            foo.setClickable(false);
        }
    }

    public void goToEditPlayerFromLeagues(View view){
        Intent intent = new Intent(getApplicationContext(), QuickEditPlayerActivity.class);
        intent.putExtra(NAME_KEY, name);
        intent.putExtra(ADMIN_KEY, currentAdmin);
        intent.putExtra(LEAGUE_KEY, currentLeague);
        intent.putExtra(TEAM_KEY, currentTeam);
        intent.putExtra(EMAIL_KEY, email);
        intent.putExtra(USER_KEY, currentUser);
        intent.putExtra(PLAYER_KEY, currentPlayer);

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
        startActivityForResult(intent, INTENT_REQUEST_CODE_ADD_TEAMS);
    }

    public void goToEditTeams(View view){
        Intent intent = new Intent(getApplicationContext(), EditTeamsActivity.class);
        intent.putExtra(  NAME_KEY, name);
        intent.putExtra( ADMIN_KEY, currentAdmin);
        intent.putExtra(LEAGUE_KEY, currentLeague);
        intent.putExtra(  TEAM_KEY, currentTeam);
        intent.putExtra(EMAIL_KEY, email);
        startActivityForResult(intent, INTENT_REQUEST_CODE_EDIT_TEAMS);
    }

    public void goToEditLeagues(View view){
        Intent intent = new Intent(getApplicationContext(), EditLeaguesActivity.class);
        intent.putExtra(  NAME_KEY, name);
        intent.putExtra( ADMIN_KEY, currentAdmin);
        intent.putExtra(LEAGUE_KEY, currentLeague);
        intent.putExtra(  TEAM_KEY, currentTeam);
        intent.putExtra(EMAIL_KEY, email);
        intent.putExtra( USER_KEY, currentUser);
        startActivityForResult(intent, INTENT_REQUEST_CODE_EDIT_LEAGUES);
    }
}
