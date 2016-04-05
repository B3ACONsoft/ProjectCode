package beaconsoft.sycorowlayouts.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private static final String COACH_KEY = "beaconsoft.sycorowlayouts.COACH";
    private static final String EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private static final String NAME_KEY  = "beaconsoft.sycorowlayouts.NAME";

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
        textViewEmail.setText("Coach Email: " + email);
        TextView textViewName  = (TextView)findViewById(R.id.textViewCoachHomeName);
        textViewName.setText("Coach: " + name + "\n CoachId:" + coachId );
        textViewTeamName    = (TextView)findViewById(R.id.textViewCoachHomeTeamName);
        textViewPlayerCount = (TextView)findViewById(R.id.textViewCoachHomePlayerCount);
        textViewPlayerCount.setText("NUMBER");
        textViewGamesLeft   = (TextView)findViewById(R.id.textViewCoachHomeGamesLeft);
        textViewNextGame    = (TextView)findViewById(R.id.textViewCoachHomeNextGame);

        spinnerSports  = (Spinner)findViewById(R.id.spinnerCoachHomeSports);
        spinnerLeagues = (Spinner)findViewById(R.id.spinnerCoachHomeLeagues);
        
        spinnerSports.setOnItemSelectedListener(this);
        spinnerLeagues.setOnItemSelectedListener(this);
        
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
        currentSportId = currentSport.getSportID();
        
        arrayListLeagues.clear();
        arrayListLeagues.addAll(dataSource.getListOfLeaguesByCoachAndSport(coachId, currentSportId));
        adapterSpinnerLeagues = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayListLeagues);
        adapterSpinnerLeagues.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLeagues.setAdapter(adapterSpinnerLeagues);
        spinnerLeagues.setEnabled(true);
        currentLeague = (League)spinnerLeagues.getSelectedItem();
        currentLeagueId = currentLeague.getLeagueID();
        
        
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent == spinnerSports) {
            currentSport = (Sport) parent.getItemAtPosition(position);
            currentSportId = currentSport.getSportID();
            onSportSpinnerChange();
        }
        if(parent == spinnerLeagues) {
            currentLeague = (League) parent.getItemAtPosition(position);
            currentLeagueId = currentLeague.getLeagueID();
            arrayListTeams.clear();
            arrayListTeams.addAll(dataSource.getListOfTeamsCoachedByUser(coachId, currentLeagueId));
            if(arrayListTeams.size() == 1) {
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

            }else{
                try {
                    throw new Exception("Too many Teams for this coach. The Database is not atomic or the spinner's arraylist is not being emptied");
                } catch (Exception e) {
                    Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
                    toast.setText(e.getMessage());
                    toast.show();
                }
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

    public void goToRoster(View view){

    }
}
