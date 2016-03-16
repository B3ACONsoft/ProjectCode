package beaconsoft.sycorowlayouts.activities;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import java.sql.SQLException;
import java.util.ArrayList;

import beaconsoft.sycorowlayouts.DataSource;
import beaconsoft.sycorowlayouts.dbobjects.League;
import beaconsoft.sycorowlayouts.R;
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
    private String name;
    private String email;
    private TextView textViewLeaguesEmail;
    private int currentAdmin;
    private int currentSport;
    private int currentLeague;
    private int currentTeam;
    private int currentUser;
    private ArrayAdapter adapterSpinnerSports;
    private Spinner spinnerSports;
    private ArrayList<Sport> sportsArrayList;
    private ArrayAdapter adapterSpinnerLeagues;
    private Spinner spinnerLeagues;
    private ArrayList<League> leaguesArrayList;
    private ArrayAdapter adapterSpinnerTeams;
    private Spinner spinnerTeams;
    private ArrayList<Team> teamsArrayList;
    private ArrayAdapter adapterSpinnerUsers;
    private Spinner spinnerUsers;
    private ArrayList<Users> usersArrayList;
    private Button buttonStartLeague;
    private Button buttonCalendar;
    private Button buttonAddTeams;
    private Button buttonAddPlayers;
    private Button buttonEditTeam;
    private Button buttonEditPlayer;
    private DataSource dataSource;

    @Override
    public void onResume()  {
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onResume();
        loadSpinners();
    }
    @Override
    public void onPause() {
        dataSource.close();
        super.onPause();
    }
    @Override
    public void onDestroy(){
        dataSource.close();
        super.onDestroy();
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
        currentSport = 0;
        currentLeague = 0;
        currentTeam = 0;
        currentUser = 0;
        spinnerSports = (Spinner) findViewById(R.id.spinnerMyLeaguesSports);
        spinnerLeagues = (Spinner) findViewById(R.id.spinnerMyLeaguesLeagues);
        spinnerTeams = (Spinner) findViewById(R.id.spinnerMyLeaguesTeams);
        spinnerUsers = (Spinner) findViewById(R.id.spinnerMyLeaguesPlayers);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        int resultInt1 = 2;
        int resultInt2 = 2;
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                currentSport = resultInt1 = data.getIntExtra("sport_id", 0);
                currentLeague = resultInt2 = data.getIntExtra("league_id", 0);
//                int sportsPointer = -1;
//                for(int i = 0; i < sportsArrayList.size(); i++){
//                    if(sportsArrayList.get(i).getSportID() == currentSport){
//                        sportsPointer = i;
//                    }
//                }
//                spinnerSports.setSelection(sportsPointer);
//                int leaguesPointer = -1;
//                for(int i = 0; i < leaguesArrayList.size(); i++){
//                    if(leaguesArrayList.get(i).getLeagueID() == currentLeague){
//                        leaguesPointer = i;
//                    }
//                }
//                spinnerLeagues.setSelection(leaguesPointer);
                textViewLeaguesEmail.setText(resultInt1 + " " + resultInt2);
            }
        }
    }

    private void deactivateView(View parent){
        for(View foo: parent.getTouchables()){
            foo.setClickable(false);
            foo.setEnabled(false);
        }
        parent.setEnabled(false);
    }

    private void activateView(View parent){
        for(View foo: spinnerUsers.getTouchables()){
            foo.setClickable(true);
            foo.setEnabled(true);
        }
        parent.setEnabled(true);
    }

        /* Call the database and ask for the sports we have leagues for, insert into the first spinner
        * through a series of arraylists and a hashmap */
    public void loadSpinners(){

        activateView(spinnerSports);
        activateView(spinnerLeagues);
        activateView(spinnerTeams);
        activateView(spinnerUsers);
        activateView(buttonStartLeague);
        activateView(buttonCalendar);
        activateView(buttonAddTeams);
        activateView(buttonEditTeam);
        activateView(buttonAddPlayers);
        activateView(buttonEditPlayer);

        /*LOAD THE SPORTS SPINNER AND ADAPTER*/
        sportsArrayList = new ArrayList<>();
        sportsArrayList.addAll(dataSource.getListOfSports());
        if(sportsArrayList.isEmpty()){
            deactivateView(spinnerSports);
            deactivateView(spinnerLeagues);
            deactivateView(spinnerSports);
            deactivateView(spinnerUsers);
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
            activateView(spinnerUsers);
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
        leaguesArrayList.addAll(dataSource.getListOfLeaguesBySport(currentSport));
        if (leaguesArrayList.isEmpty()) {
            deactivateView(spinnerLeagues);
            deactivateView(spinnerTeams);
            deactivateView(spinnerUsers);
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
        teamsArrayList.addAll(dataSource.getListOfTeamsByLeague(currentLeague));
        if(teamsArrayList.isEmpty()){
            deactivateView(spinnerTeams);
            deactivateView(spinnerUsers);
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

        usersArrayList = new ArrayList<>();
        usersArrayList.addAll(dataSource.getListOfUsersByTeam(currentTeam));
        if(usersArrayList.isEmpty()){
            deactivateView(spinnerUsers);
            deactivateView(buttonEditPlayer);
        }else {
            activateView(spinnerUsers);
            activateView(buttonAddPlayers);
            activateView(buttonEditPlayer);
            adapterSpinnerUsers = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, usersArrayList);
            adapterSpinnerUsers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerUsers.setAdapter(adapterSpinnerUsers);
            spinnerUsers.setOnItemSelectedListener(this);
            spinnerUsers.setEnabled(true);
            Users user = (Users)spinnerUsers.getSelectedItem();
            currentUser = user.getUserID();
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
        if(parent == spinnerUsers){
            onSpinnerUsersChange(choice);
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
            activateView(spinnerUsers);
            activateView(buttonStartLeague);
            activateView(buttonCalendar);
            activateView(buttonAddTeams);
            activateView(buttonEditTeam);
            activateView(buttonAddPlayers);
            activateView(buttonEditPlayer);
            adapterSpinnerLeagues = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, leaguesArrayList.toArray());
            spinnerLeagues.setAdapter(adapterSpinnerLeagues);
            spinnerLeagues.setOnItemSelectedListener(this);
            spinnerLeagues.setEnabled(true);
            League listLeague = (League)spinnerLeagues.getSelectedItem();
            currentLeague = listLeague.getLeagueID();

        } else {
            deactivateView(spinnerLeagues);
            deactivateView(spinnerTeams);
            deactivateView(spinnerUsers);
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
            activateView(spinnerUsers);
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
            deactivateView(spinnerUsers);
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
        activateView(spinnerUsers);
        usersArrayList.clear();
        usersArrayList.addAll(dataSource.getListOfUsersByTeam(currentTeam));

        if(usersArrayList.isEmpty()){
            deactivateView(spinnerUsers);
            currentUser = 0;
        }else {
            Users listUser = (Users) spinnerUsers.getSelectedItem();
            currentUser = listUser.getUserID();
            adapterSpinnerUsers = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, usersArrayList.toArray());
            spinnerUsers.setAdapter(adapterSpinnerUsers);
            spinnerUsers.setOnItemSelectedListener(this);
            Users user = (Users)spinnerUsers.getSelectedItem();
            currentUser = user.getUserID();
        }
        statusChangeDisplay();
    }

    private void onSpinnerUsersChange(String choiceUser){

        Users tempUser = (Users)spinnerUsers.getSelectedItem();
        currentUser = tempUser.getUserID();
        statusChangeDisplay();
    }

    private void statusChangeDisplay(){
        textViewLeaguesEmail.setText("AID:" + currentAdmin + " SID:" + currentSport + " LID:" + currentLeague
                + " TID" + currentTeam + " UID" + currentUser);
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
        startActivityForResult(intent, 1);
    }

    public void goToEditLeagues(View view){
        Intent intent = new Intent(getApplicationContext(), EditLeaguesActivity.class);
        intent.putExtra(  NAME_KEY, name);
        intent.putExtra( ADMIN_KEY, currentAdmin);
        intent.putExtra(LEAGUE_KEY, currentLeague);
        intent.putExtra(  TEAM_KEY, currentTeam);
        intent.putExtra(EMAIL_KEY, email);
        startActivityForResult(intent, 1);
    }
}
