package beaconsoft.sycorowlayouts.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import beaconsoft.sycorowlayouts.R;
import beaconsoft.sycorowlayouts.SYCOServerAccess.UpdateService;
import beaconsoft.sycorowlayouts.dbobjects.Event;
import beaconsoft.sycorowlayouts.dbobjects.League;
import beaconsoft.sycorowlayouts.dbobjects.Player;
import beaconsoft.sycorowlayouts.dbobjects.Sport;
import beaconsoft.sycorowlayouts.dbobjects.Team;



public class CoachHomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String  SPORT_KEY = "beaconsoft.sycorowlayouts.SPORT";
    private static final String  ADMIN_KEY = "beaconsoft.sycorowlayouts.ADMIN";
    private static final String  COACH_KEY = "beaconsoft.sycorowlayouts.COACH";
    private static final String  EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private static final String   NAME_KEY = "beaconsoft.sycorowlayouts.NAME";
    private static final String   TEAM_KEY = "beaconsoft.sycorowlayouts.TEAM";
    private static final String LEAGUE_KEY = "beaconsoft.sycorowlayouts.LEAGUE";
    private static final String   USER_KEY = "beaconsoft.sycorowlayouts.USER"  ;
    private static final String PLAYER_KEY = "beaconsoft.sycorowlayouts.PLAYER";
    private static final int INTENT_REQUEST_CODE_QUICK_ADD_PLAYER = 1;
    private static final int INTENT_REQUEST_CODE_QUICK_EDIT_PLAYER = 2;
    private static final int INTENT_REQUEST_CODE_CALENDAR_ACTIVITY = 3;
    private static boolean hasStarted = false;

    private String name;
    private String email;
    private int coachId;

    private ArrayList<Sport> arrayListSports;
    private ArrayList<League> arrayListLeagues;
    private ArrayList<Team> arrayListTeams;

    private Spinner spinnerSports;
    private ArrayAdapter adapterSpinnerSports;
    private Sport currentSport;
    private int currentSportId;
    private ArrayAdapter adapterSpinnerLeagues;
    private Spinner spinnerLeagues;
    private League currentLeague;
    private ArrayAdapter adapterSpinnerPlayers;
    private int currentLeagueId;
    private Team currentTeam;
    private TextView textViewTeamName;
    private TextView textViewPlayerCount;
    private TextView textViewGamesLeft;
    private TextView textViewNextGame;
    private ArrayList<Player> arrayListPlayers;
    private Spinner spinnerPlayers;
    private Player currentPlayer;
    private int currentPlayerId;
    private int currentTeamId;
    private UpdateService updateService;  //reference to the update service
    boolean mBound = false;             //to bind or not to bind...

    //TODO Write a broadcast intent which we will call "RELOAD", on broadcast store current information and hit the reload button


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void initializeActivityViews(){
        arrayListSports.clear();
        arrayListSports.addAll(updateService.getListOfSportsByCoach(coachId));
        adapterSpinnerSports = new ArrayAdapter(CoachHomeActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayListSports);
        adapterSpinnerSports.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSports.setAdapter(adapterSpinnerSports);
        spinnerSports.setEnabled(true);
        currentSport = (Sport) spinnerSports.getSelectedItem();
        if(currentSport != null) {
            currentSportId = currentSport.getSportID();
        }
        arrayListLeagues.clear();
        arrayListLeagues.addAll(updateService.getListOfLeaguesByCoachAndSport(coachId, currentSportId));
        adapterSpinnerLeagues = new ArrayAdapter(CoachHomeActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayListLeagues);
        adapterSpinnerLeagues.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLeagues.setAdapter(adapterSpinnerLeagues);
        spinnerLeagues.setEnabled(true);
        currentLeague = (League)spinnerLeagues.getSelectedItem();
        if(currentLeague != null) {
            currentLeagueId = currentLeague.getLeagueID();
            arrayListTeams.addAll(updateService.getListOfTeamsCoachedByUser(coachId, currentLeagueId));
            if(arrayListTeams.size() == 1){
                currentTeam = arrayListTeams.get(0);
                currentTeamId = currentTeam.getTeamID();
            }else{
                try {
                    throw new Exception("Coach leads more than 1 Team Exception");
                } catch (Exception e) {
                    Log.e("EXCEPTION ", "......" + e.getMessage());
                }
            }
        }

        arrayListPlayers.clear();
        arrayListPlayers.addAll(updateService.getListOfPlayersByTeam(currentTeamId));
        ArrayAdapter adapterSpinnerPlayers = new ArrayAdapter(CoachHomeActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayListPlayers);
        spinnerPlayers.setAdapter(adapterSpinnerPlayers);
        currentPlayer = (Player) spinnerPlayers.getSelectedItem();
        if (currentPlayer != null) {
            currentPlayerId = currentPlayer.getPlayerID();
        }
    }


    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            // We've bound to LocalService, cast the IBinder and get LocalService instance
            beaconsoft.sycorowlayouts.SYCOServerAccess.UpdateService.UpdateServiceBinder binder = (beaconsoft.sycorowlayouts.SYCOServerAccess.UpdateService.UpdateServiceBinder) service;

            updateService = binder.getService();

            mBound = true;
            if(!hasStarted) {
                initializeActivityViews();
            }
            hasStarted = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this,
                beaconsoft.sycorowlayouts.SYCOServerAccess.UpdateService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_home);

        Intent intent = getIntent();
        name    = intent.getStringExtra(NAME_KEY);
        email   = intent.getStringExtra(EMAIL_KEY);
        coachId = intent.getIntExtra(COACH_KEY, 0);

        TextView textViewEmail = (TextView)findViewById(R.id.textViewCoachHomeEmail);
        textViewEmail.setText(email);
        TextView textViewName  = (TextView)findViewById(R.id.textViewCoachHomeName);
        textViewName.setText("Coach: " + name);
        textViewTeamName    = (TextView)findViewById(R.id.textViewCoachHomeTeamName);
        textViewPlayerCount = (TextView)findViewById(R.id.textViewCoachHomePlayerCount);
        textViewPlayerCount.setText("NUMBER");
        textViewGamesLeft   = (TextView)findViewById(R.id.textViewCoachHomeGamesLeft);
        textViewNextGame    = (TextView)findViewById(R.id.textViewCoachHomeNextGame);

        spinnerSports  = (Spinner)findViewById(R.id.spinnerCoachHomeSports);
        spinnerLeagues = (Spinner)findViewById(R.id.spinnerCoachHomeLeagues);
        spinnerPlayers = (Spinner)findViewById(R.id.spinnerCoachHomePlayers);
        
        spinnerSports.setOnItemSelectedListener(this);
        spinnerLeagues.setOnItemSelectedListener(this);
        spinnerPlayers.setOnItemSelectedListener(this);
        
        arrayListSports  = new ArrayList<Sport>();
        arrayListLeagues = new ArrayList<League>();
        arrayListTeams   = new ArrayList<Team>();
        arrayListPlayers = new ArrayList<Player>();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == spinnerSports) {
            currentSport = (Sport) parent.getItemAtPosition(position);
            currentSportId = currentSport.getSportID();
            onSportSpinnerChange();
        }
        if (parent == spinnerLeagues) {
            currentLeague = (League) parent.getItemAtPosition(position);
            currentLeagueId = currentLeague.getLeagueID();
            onSpinnerLeaguesChange();
        }
        if (parent == spinnerPlayers) {
            currentPlayer = (Player) parent.getItemAtPosition(position);
            currentPlayerId = currentPlayer.getPlayerID();
        }
    }

        public void onSpinnerLeaguesChange()
        {
            arrayListTeams.clear();
            arrayListTeams.addAll(updateService.getListOfTeamsCoachedByUser(coachId, currentLeagueId));
            if (arrayListTeams.size() == 1) {
                currentTeam = arrayListTeams.get(0);
                textViewTeamName.setText(currentTeam.getTeamName());

                /*Find the number of players on the team and print it to the textViewCoachHomePlayerCount view*/

                arrayListPlayers.clear();
                arrayListPlayers.addAll(updateService.getListOfPlayersByTeam(currentTeam.getTeamID()));
                int playerCount = arrayListPlayers.size();
                textViewPlayerCount.setEnabled(true);
                textViewPlayerCount.setText("" + playerCount);

                /* Calculate the number of games left in the season and display in textViewCoachHomeGamesLeft */
                ArrayList<Event> arrayListEvents = new ArrayList<>();
                ArrayList<Event> arrayListEventsLeft = new ArrayList<>();
                arrayListEvents.addAll(updateService.getListOfEventsByTeam(currentTeam));
                int gamesLeftCount = 0;
                Calendar cal = new GregorianCalendar();

                Date today = cal.getTime();
                Event present = new Event();
                present.setStartDateTime(today);
                for(Event e: arrayListEvents){
                    if(present.compareTo(e) > 0){
                        gamesLeftCount++;
                        arrayListEventsLeft.add(e);
                    }
                }


                //TODO: Calculate the number of games left in the season and display in textViewCoachHomeGamesLeft
                textViewGamesLeft.setText("" + gamesLeftCount);

                //TODO: Find the next game and display it's date
                if(arrayListEventsLeft.isEmpty()){
                    textViewNextGame.setText("n/a");
                }else {
                    Collections.sort(arrayListEventsLeft);
                    Event event = arrayListEventsLeft.get(0);
                    String[] timeStrings = event.getStartDateTime().toString().split(" ");
                    String nextGameString = timeStrings[1] + " " + timeStrings[2] + " " + timeStrings[5];
                    if(event.getAwayTeamID() != 0){
                        Team awayTeam = updateService.getTeamById(event.getAwayTeamID());
                        nextGameString += " " + awayTeam.getTeamName();
                    }

                    textViewNextGame.setText("" + nextGameString);
                }
                if(playerCount > 0) {
                    arrayListPlayers.clear();
                    arrayListPlayers.addAll(updateService.getListOfPlayersByTeam(currentTeam.getTeamID()));
                    adapterSpinnerPlayers = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayListPlayers);
                    spinnerPlayers.setAdapter(adapterSpinnerPlayers);
                    currentPlayer = (Player) spinnerPlayers.getSelectedItem();
                }
            } else {
                try {
                    throw new Exception("Too many Teams for this coach. The Database is not atomic or the spinner's arraylist is not being emptied");
                } catch (Exception e) {
                    Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
                    toast.setText(e.getMessage());
                    toast.show();
                }
            }
        }

    private void onSportSpinnerChange() {

        arrayListLeagues.clear();
        arrayListLeagues.addAll(updateService.getListOfLeaguesByCoachAndSport(coachId, currentSportId));
        adapterSpinnerLeagues = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayListLeagues);
        spinnerLeagues.setAdapter(adapterSpinnerLeagues);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        
    }

    public void goToCalendar(View view){

    }

    public void goToAddPlayers(View view){
        Intent intent = new Intent(this, QuickAddPlayersActivity.class);
        intent.putExtra(COACH_KEY, coachId);
        intent.putExtra(LEAGUE_KEY, currentLeagueId);
        intent.putExtra(EMAIL_KEY, email);
        intent.putExtra(TEAM_KEY, currentTeamId);
        intent.putExtra(NAME_KEY, name);
        startActivityForResult(intent, INTENT_REQUEST_CODE_QUICK_ADD_PLAYER);
    }

    public void goToEditPlayer(View view){
        Intent intent = new Intent(this, QuickEditPlayerActivity.class);
        intent.putExtra(COACH_KEY, coachId);
        intent.putExtra(SPORT_KEY, currentSport.getSportID());
        intent.putExtra(LEAGUE_KEY, currentLeagueId);
        intent.putExtra(EMAIL_KEY, email);
        intent.putExtra(TEAM_KEY, currentTeamId);
        intent.putExtra(NAME_KEY, name);
        intent.putExtra(PLAYER_KEY, currentPlayerId);
        intent.putExtra(USER_KEY, currentPlayer.getUserID());
        startActivityForResult(intent, INTENT_REQUEST_CODE_QUICK_EDIT_PLAYER);
    }

    public void goToUserRosterActivity(View view){
        Intent intent = new Intent(this, UserRosterActivity.class);
        intent.putExtra(TEAM_KEY, currentTeamId);
        startActivity(intent);
    }

    public void goToCalendarActivity(View view){
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra(NAME_KEY, name);
        intent.putExtra(ADMIN_KEY, coachId);
        intent.putExtra(EMAIL_KEY, email);
        intent.putExtra(TEAM_KEY, currentTeamId);
        intent.putExtra(LEAGUE_KEY, currentLeagueId);
        startActivityForResult(intent, INTENT_REQUEST_CODE_CALENDAR_ACTIVITY);
    }

    @Override
    protected void onResume(){
        super.onResume();

        Log.e(" ", " ");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == INTENT_REQUEST_CODE_QUICK_ADD_PLAYER || requestCode == INTENT_REQUEST_CODE_QUICK_EDIT_PLAYER)
                && data != null) {
            if (resultCode == Activity.RESULT_OK) {

                int resultPlayer = data.getIntExtra("player_id", 0);
                int resultSport  = data.getIntExtra("sport_id" , 0);
                int resultLeague = data.getIntExtra("league_id", 0);

                for(int i = 0; i < arrayListSports.size(); i++){
                    if(arrayListSports.get(i).getSportID() == resultSport){
                        spinnerSports.setSelection(i);
                        currentSport = (Sport) spinnerSports.getSelectedItem();
                        currentSportId = currentSport.getSportID();
                        break;
                    }
                }

                for(int i = 0; i < arrayListLeagues.size(); i++){
                    if(arrayListLeagues.get(i).getLeagueID() == resultLeague){
                        spinnerLeagues.setSelection(i);
                        currentLeague = (League)spinnerLeagues.getSelectedItem();
                        currentLeagueId = currentLeague.getLeagueID();
                        break;
                    }
                }

                arrayListPlayers.clear();
                arrayListPlayers.addAll(updateService.getListOfPlayersByTeam(currentTeamId));
                adapterSpinnerPlayers = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayListPlayers);
                spinnerPlayers.setAdapter(adapterSpinnerPlayers);

                for(int i = 0; i < arrayListPlayers.size(); i++){
                    if(arrayListPlayers.get(i).getPlayerID() == resultPlayer){
                        spinnerPlayers.setSelection(i);
                        currentPlayer = (Player)spinnerPlayers.getSelectedItem();
                        currentPlayerId = currentPlayer.getPlayerID();
                        break;
                    }
                }
            }
        }
    }
}
