package beaconsoft.sycorowlayouts.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

import beaconsoft.sycorowlayouts.DataSource;
import beaconsoft.sycorowlayouts.R;
import beaconsoft.sycorowlayouts.dbobjects.League;
import beaconsoft.sycorowlayouts.dbobjects.Player;
import beaconsoft.sycorowlayouts.dbobjects.Sport;
import beaconsoft.sycorowlayouts.dbobjects.Team;

public class CoachHomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String  COACH_KEY = "beaconsoft.sycorowlayouts.COACH";
    private static final String  EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private static final String   NAME_KEY = "beaconsoft.sycorowlayouts.NAME";
    private static final String   TEAM_KEY = "beaconsoft.sycorowlayouts.TEAM";
    private static final String LEAGUE_KEY = "beaconsoft.sycorowlayouts.LEAGUE";
    private static final String   USER_KEY = "beaconsoft.sycorowlayouts.USER"  ;
    private static final String PLAYER_KEY = "beaconsoft.sycorowlayouts.PLAYER";
    private static final int INTENT_REQUEST_CODE_QUICK_ADD_PLAYER = 1;
    private static final int INTENT_REQUEST_CODE_QUICK_EDIT_PLAYER = 1;

    private String name;
    private String email;
    private int coachId;

    private DataSource dataSource;
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

    @Override
    protected void onDestroy(){
        dataSource.close();
        super.onDestroy();
    }

    @Override
    protected void onPause(){
        dataSource.close();
        super.onPause();
    }

    @Override
    protected void onResume(){
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_home);

        dataSource = new DataSource(this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

        arrayListSports.clear();
        arrayListSports.addAll(dataSource.getListOfSportsByCoach(coachId));
        adapterSpinnerSports = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayListSports);
        adapterSpinnerSports.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSports.setAdapter(adapterSpinnerSports);
        spinnerSports.setEnabled(true);
        currentSport = (Sport) spinnerSports.getSelectedItem();
        if(currentSport != null) {
            currentSportId = currentSport.getSportID();
        }
        arrayListLeagues.clear();
        arrayListLeagues.addAll(dataSource.getListOfLeaguesByCoachAndSport(coachId, currentSportId));
        adapterSpinnerLeagues = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayListLeagues);
        adapterSpinnerLeagues.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLeagues.setAdapter(adapterSpinnerLeagues);
        spinnerLeagues.setEnabled(true);
        currentLeague = (League)spinnerLeagues.getSelectedItem();
        if(currentLeague != null) {
            currentLeagueId = currentLeague.getLeagueID();
            arrayListTeams.addAll(dataSource.getListOfTeamsCoachedByUser(coachId, currentLeagueId));
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
        arrayListPlayers.addAll(dataSource.getListOfPlayersByTeam(currentTeamId));
        ArrayAdapter adapterSpinnerPlayers = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayListPlayers);
        spinnerPlayers.setAdapter(adapterSpinnerPlayers);
        currentPlayer = (Player) spinnerPlayers.getSelectedItem();
        if (currentPlayer != null) {
            currentPlayerId = currentPlayer.getPlayerID();
        }
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
            arrayListTeams.addAll(dataSource.getListOfTeamsCoachedByUser(coachId, currentLeagueId));
            if (arrayListTeams.size() == 1) {
                currentTeam = arrayListTeams.get(0);
                textViewTeamName.setText(currentTeam.getTeamName());

                /*Find the number of players on the team and print it to the textViewCoachHomePlayerCount view*/

                arrayListPlayers.clear();
                arrayListPlayers.addAll(dataSource.getListOfPlayersByTeam(currentTeam.getTeamID()));
                int playerCount = arrayListPlayers.size();
                textViewPlayerCount.setEnabled(true);
                textViewPlayerCount.setText("" + playerCount);

                /* Calculate the number of games left in the season and display in textViewCoachHomeGamesLeft */

                //TODO: Calculate the number of games left in the season and display in textViewCoachHomeGamesLeft
                textViewGamesLeft.setText("TODO");

                //TODO: Find the next game and display it's date

                if(playerCount > 0) {
                    arrayListPlayers.clear();
                    arrayListPlayers.addAll(dataSource.getListOfPlayersByTeam(currentTeam.getTeamID()));
                    ArrayAdapter adapterSpinnerPlayers = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayListPlayers);
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
        arrayListLeagues.addAll(dataSource.getListOfLeaguesByCoachAndSport(coachId, currentSportId));
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
        startActivityForResult(intent, 1);
    }

    public void goToEditPlayer(View view){
        Intent intent = new Intent(this, QuickEditPlayerActivity.class);
        intent.putExtra(COACH_KEY, coachId);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (requestCode == INTENT_REQUEST_CODE_QUICK_ADD_PLAYER && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                onSpinnerLeaguesChange();
                int resultPlayer = data.getIntExtra("player_id", 0);
                for(int i = 0; i < arrayListPlayers.size(); i++){
                    if(arrayListPlayers.get(i).getPlayerID() == resultPlayer){
                        spinnerPlayers.setSelection(i);
                        break;
                    }
                }
            }
        }
    }
}
