package beaconsoft.sycorowlayouts.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import beaconsoft.sycorowlayouts.DataSource;
import beaconsoft.sycorowlayouts.R;
import beaconsoft.sycorowlayouts.dbobjects.League;
import beaconsoft.sycorowlayouts.dbobjects.Sport;
import beaconsoft.sycorowlayouts.dbobjects.Team;
import beaconsoft.sycorowlayouts.dbobjects.Users;

public class QuickAddTeamsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int currentLeague;
    private int currentUser;
    private DataSource dataSource;
    private static final String  EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private static final String LEAGUE_KEY = "beaconsoft.sycorowlayouts.LEAGUE";
    private ArrayList<Users> coachArrayList;
    private Spinner spinnerCoaches;
    private ArrayAdapter adapterSpinnerCoaches;
    private EditText editTextFirst;
    private EditText editTextLast;
    private EditText editTextPhone;
    private EditText editTextEmergency;
    private EditText editTextCoachEmail;
    private EditText editTeamName;
    private Toast toastCoach;
    private String fname;
    private String lname;
    private String teamName;
    private long phone;
    private long emerg;
    private String email;
    private TextView textViewTop;

    @Override
    public void onDestroy(){
        dataSource.close();
        super.onDestroy();
    }

    @Override
    public void onResume(){
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public void onPause(){
        dataSource.close();
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_add_teams);

        dataSource = new DataSource(this);
        try {
            dataSource.open();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        Intent intent = getIntent();
        email = intent.getStringExtra(EMAIL_KEY);
        currentLeague = intent.getIntExtra(LEAGUE_KEY, 0);
        editTextFirst     = (EditText)findViewById(R.id.editTextCoachFirst);
        editTextLast      = (EditText)findViewById(R.id.editTextCoachLast );
        editTextPhone     = (EditText)findViewById(R.id.editTextCoachPhone);
        editTextEmergency = (EditText)findViewById(R.id.editTextCoachEmergency);
        editTextCoachEmail= (EditText)findViewById(R.id.editTextCoachEmail);
        editTextFirst        .setText("El");
        editTextLast         .setText("Guapo");
        editTextCoachEmail   .setText("GoodLooking@ndAvailable.com");
        editTextPhone        .setText("1594658789");
        editTextEmergency    .setText("3626659856");
        coachArrayList = new ArrayList<>();
        loadSpinner();
    }

    public void loadSpinner() {

        coachArrayList.clear();
        coachArrayList.addAll(dataSource.getListOfUsersAvailableToCoach(currentLeague));
        spinnerCoaches = (Spinner) findViewById(R.id.spinnerCoachesQuickAddTeams);
        adapterSpinnerCoaches = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, coachArrayList.toArray());
        adapterSpinnerCoaches.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCoaches.setAdapter(adapterSpinnerCoaches);
        spinnerCoaches.setOnItemSelectedListener(this);
        spinnerCoaches.setEnabled(true);
        Users tempUser = (Users) spinnerCoaches.getSelectedItem();
        if (tempUser != null) {
            currentUser = tempUser.getUserID();
        }else{
            currentUser = 0;
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Users tempUser = (Users)parent.getItemAtPosition(position);
        currentUser = tempUser.getUserID();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        /* hmmm well */
    }

    public void createTeam(View view){

        editTeamName = (EditText) findViewById(R.id.editTextNewTeamName);
        teamName = editTeamName.getText().toString().toUpperCase();

        try {

            ArrayList<Team> allLeagueTeams = new ArrayList<>();
            allLeagueTeams.addAll(dataSource.getListOfTeamsByLeague(currentLeague));
            ArrayList<String> teamNames = new ArrayList<>();
            for(int i =0; i < allLeagueTeams.size(); i++){
                teamNames.add(allLeagueTeams.get(i).getTeamName());
            }
            ArrayList<Team> tempList = new ArrayList<>();
            tempList.addAll(dataSource.getListOfTeamsCoachedByUser(currentUser, currentLeague));
            if(tempList.isEmpty() && !teamNames.contains(teamName)){
                Team team = dataSource.createTeam(teamName, currentLeague, currentUser);
                dataSource.createEnrollment(currentUser, 0, currentLeague, team.getTeamID(),
                        new Date(), 1.99);
                toastCoach = Toast.makeText(this, null, Toast.LENGTH_LONG);
                String msg = "User Enrolled as Coach of the " + team.getTeamName();
                toastCoach.setText(msg);
                toastCoach.show();
                Intent resultIntent = new Intent();
                League league = dataSource.getLeagueById(currentLeague);
                resultIntent.putExtra("team_id", team.getTeamID());
                resultIntent.putExtra("league_id", team.getLeagueID());
                resultIntent.putExtra("sport_id", league.getSportID());
                setResult(RESULT_OK, resultIntent);
                finish();
            }else if(teamNames.contains(teamName)){
                throw new Exception(teamName + " taken...");
            }
            else
            {
                Team tempTeam = tempList.get(0);
                setResult(RESULT_CANCELED);
                throw new Exception("This Coach already coaches the " + tempTeam.getTeamName());

            }
        }catch(Exception e){
            toastCoach = Toast.makeText(this, null, Toast.LENGTH_LONG);
            String msg = e.getMessage();
            toastCoach.setText(msg);
            toastCoach.show();
        }
    }

    public void quickAddCoach(View view){

        try {
            fname = editTextFirst.getText().toString().toUpperCase();
            lname = editTextLast.getText().toString().toUpperCase();
            phone = Long.parseLong(editTextPhone.getText().toString());
            emerg = Long.parseLong(editTextEmergency.getText().toString());
            email = editTextCoachEmail.getText().toString().toUpperCase();
            Users testUser = dataSource.getUserByEmail(email);
            if(testUser == null && fname.length() > 1 && lname.length() > 1 &&
                    phone > 1000000000 && email.length() > 5 && emerg > 1000000000) {
                Users user = dataSource.createUsers(fname.toUpperCase(), lname.toUpperCase(),
                        phone, email.toUpperCase(), emerg, "COACH", "PASS");
                textViewTop = (TextView) findViewById(R.id.textViewQuickAddTeamsTop);
                textViewTop.setText(user.toString());
                loadSpinner();
            }
            else if(testUser != null) {
                throw new Exception("Email address taken...");
            }

        }catch(Exception e){

            toastCoach = Toast.makeText(this, null, Toast.LENGTH_LONG);
            String msg = e.getMessage();

            if(fname.length() < 1)
                msg = "Please fill in the first name...";
            else if(lname.length() < 1)
                msg = "Please fill in the last name...";
            else if((phone+"").length()< 1)
                msg = "Please provide a number where you can be reached...";
            else if((emerg+"").length() < 1)
                msg = "Please give the proper number to call in case of emergency...";
            else if(email.length()< 1)
                msg = "An email request is required...";

            toastCoach.setText(msg);
            toastCoach.show();
        }

    }
    /**
     * Removes all strings from edittexts
     * @param view
     */
    public void clearFormCoach(View view){

        editTextFirst.setText("");
        editTextLast.setText("");
        editTextPhone.setText("");
        editTextEmergency.setText("");
        editTextCoachEmail.setText("");
    }
}
