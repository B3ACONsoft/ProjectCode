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
import android.view.MenuItem;
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
import beaconsoft.sycorowlayouts.SYCOServerAccess.UpdateService;
import beaconsoft.sycorowlayouts.dbobjects.Enrollment;
import beaconsoft.sycorowlayouts.dbobjects.League;
import beaconsoft.sycorowlayouts.dbobjects.Sport;
import beaconsoft.sycorowlayouts.dbobjects.Team;
import beaconsoft.sycorowlayouts.dbobjects.Users;

public class EditTeamsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int currentLeagueId;
    private int currentCoachUserId;
    private int currentTeamId;

    private static final String  EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private static final String LEAGUE_KEY = "beaconsoft.sycorowlayouts.LEAGUE";
    private static final String   TEAM_KEY = "beaconsoft.sycorowlayouts.TEAM";
    private ArrayList<Users> coachArrayList;
    private Spinner spinnerCoaches;
    private ArrayAdapter adapterSpinnerCoaches;
    private EditText editTextFirst;
    private EditText editTextLast;
    private EditText editTextPhone;
    private EditText editTextEmergency;
    private EditText editTextCoachEmail;
    private EditText editTextTeamName;
    private Toast toastCoach;
    private String fname = "";
    private String lname = "";
    private String teamName = "";
    private long phone = 0;
    private long emerg = 0;
    private String email = "";
    private TextView textViewTop;

    private Team currentTeam;
    private Users coach;
    private Toast toastTeam;
    UpdateService updateService;        //reference to the update service
    boolean mBound = false;             //to bind or not to bind...


    /**
     *
     * Defines callbacks for service binding, passed to bindService()
     *
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            // We've bound to LocalService, cast the IBinder and get LocalService instance
            beaconsoft.sycorowlayouts.SYCOServerAccess.UpdateService.UpdateServiceBinder binder = (beaconsoft.sycorowlayouts.SYCOServerAccess.UpdateService.UpdateServiceBinder) service;

            updateService = binder.getService();

            mBound = true;

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
                UpdateService.class), mConnection, Context.BIND_AUTO_CREATE);

        if(mBound) {



            editTextTeamName  = (EditText)findViewById(R.id.editTextEditTeamsTeamName);
            editTextFirst     = (EditText)findViewById(R.id.editTextEditTeamsCoachFirst);
            editTextLast      = (EditText)findViewById(R.id.editTextEditTeamsCoachLast );
            editTextPhone     = (EditText)findViewById(R.id.editTextEditTeamsCoachPhone);
            editTextEmergency = (EditText)findViewById(R.id.editTextEditTeamsCoachEmergency);
            editTextCoachEmail= (EditText)findViewById(R.id.editTextEditTeamsCoachEmail);
            editTextCoachEmail.setEnabled(false);
            editTextCoachEmail.setActivated(false);
            spinnerCoaches = (Spinner) findViewById(R.id.spinnerCoachesEditTeams);
            coachArrayList = new ArrayList<>();

            Intent intent = getIntent();
            email = intent.getStringExtra(EMAIL_KEY);
            currentLeagueId = intent.getIntExtra(LEAGUE_KEY, 0);
            currentTeamId = intent.getIntExtra(TEAM_KEY, 0);
            currentTeam = updateService.getTeamById(currentTeamId);
            currentCoachUserId = currentTeam.getUserID();
            coach = updateService.getUserById(currentCoachUserId);

            loadSpinner();

            for(int i = 0; i < coachArrayList.size(); i++){
                if(coachArrayList.get(i).getUserID() == currentCoachUserId){
                    spinnerCoaches.setSelection(i);
                }
            }
            editTextFirst        .setText(coach.getFname());
            editTextLast.setText(coach.getLname());

            editTextCoachEmail.setText(coach.getEmail());
            editTextPhone.setText(String.format(coach.getPhone() + ""));
            editTextEmergency.setText(String.format(coach.getEmergency() + ""));

            editTextTeamName.setText(currentTeam.getTeamName());
        }

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
    public void onDestroy(){

        super.onDestroy();
    }

    @Override
    public void onResume(){

        super.onResume();
    }

    @Override
    public void onPause(){

        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("OPTS ITEM SELECTED BACK", "................HIT BACK ON TOOLBAR");
        if (item.getItemId() == android.R.id.home) {

            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed(){

        Log.e("ON BACK PRESSED", "...............QUICK ADD TEAMS ON BACK PRESSED");
        Intent intent = new Intent();
        intent.putExtra("team_id", currentTeamId);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_teams);


    }

    public void loadSpinner() {

        coachArrayList.clear();
        coachArrayList.addAll(updateService.getListOfUsersAvailableToCoach(currentLeagueId));
        coachArrayList.add(coach);
        adapterSpinnerCoaches = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, coachArrayList);
        adapterSpinnerCoaches.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCoaches.setAdapter(adapterSpinnerCoaches);
        spinnerCoaches.setOnItemSelectedListener(this);
        spinnerCoaches.setEnabled(true);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        coach = (Users)parent.getItemAtPosition(position);
        currentCoachUserId = coach.getUserID();

        editTextCoachEmail.setText(coach.getEmail());
        editTextEmergency.setText( String.format("%d", coach.getEmergency() ));
        editTextFirst.setText(coach.getFname());
        editTextLast.setText(coach.getLname());
        editTextPhone.setText( String.format("%d", coach.getPhone()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        /* hmmm well */
    }

    public void updateTeam(View view){

        teamName = editTextTeamName.getText().toString().toUpperCase();

        try {

            ArrayList<Team> allLeagueTeams = new ArrayList<>();
            allLeagueTeams.addAll(updateService.getListOfTeamsByLeague(currentLeagueId));
            ArrayList<String> teamNames = new ArrayList<>();
            for(int i =0; i < allLeagueTeams.size(); i++){
                teamNames.add(allLeagueTeams.get(i).getTeamName());
            }

            ArrayList<Users>tempAvailableCoaches = new ArrayList<>();
            tempAvailableCoaches.addAll(updateService.getListOfUsersAvailableToCoach(currentLeagueId));

            if(!teamNames.contains(teamName)) {
                currentTeam = updateService.updateTeamName(currentTeam, teamName.toUpperCase(), currentLeagueId, coach.getUserID());
                editTextTeamName.setText(currentTeam.getTeamName());
            }
            if(tempAvailableCoaches.contains(coach)) {

                currentTeam = updateService.updateTeamCoach(currentTeamId, currentCoachUserId);
                Users testCoach = updateService.getUserById(currentTeam.getUserID());

                toastTeam = Toast.makeText(this, null, Toast.LENGTH_LONG);
                String msg = "TeamName: " + currentTeam.getTeamName() + "\n" +
                             "Coach: " + testCoach.getFname() + " " + testCoach.getLname();
                toastTeam.setText(msg);
                toastTeam.show();


//                Intent resultIntent = new Intent();
//                League league = dataSource.getLeagueById(currentLeagueId);
//                resultIntent.putExtra("team_id", testTeam.getTeamID());
//                resultIntent.putExtra("league_id", testTeam.getLeagueID());
//                resultIntent.putExtra("sport_id", league.getSportID());
//                setResult(RESULT_OK, resultIntent);
//                finish();
            }

        }catch(Exception e){
            toastCoach = Toast.makeText(this, null, Toast.LENGTH_LONG);
            String msg = e.getMessage();
            toastCoach.setText(msg);
            toastCoach.show();
        }
    }

    public void updateCoach(View view){

        try {

            email = editTextCoachEmail.getText().toString().toUpperCase();
            fname = editTextFirst.getText().toString().toUpperCase();
            lname = editTextLast.getText().toString().toUpperCase();
            phone = Long.parseLong(editTextPhone.getText().toString());
            emerg = Long.parseLong(editTextEmergency.getText().toString());

            Users userToUpdate = updateService.getUserByEmail(email);
            if (userToUpdate != null && fname.length() > 1 && lname.length() > 1 &&
                    phone > 1000000000 && email.length() > 5 && emerg > 1000000000) {
                Users user = updateService.updateUser(userToUpdate.getUserID(), fname.toUpperCase(), lname.toUpperCase(),
                        phone, email.toUpperCase(), emerg);
                textViewTop = (TextView) findViewById(R.id.textViewEditTeamsTop);
                textViewTop.setText(user.toString());
                editTextFirst.setText(user.getFname());
                editTextLast.setText(user.getLname());
                editTextPhone.setText(String.format("%d", user.getPhone()));
                editTextEmergency.setText(String.format("%d", user.getEmergency()));
                editTextCoachEmail.setText(user.getEmail());
                editTextTeamName.setText(currentTeam.getTeamName());
                coach = user;
                currentCoachUserId = coach.getUserID();
                int presentSpinnerPosition = spinnerCoaches.getSelectedItemPosition();
                coachArrayList.set(presentSpinnerPosition, coach);
                adapterSpinnerCoaches = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, coachArrayList);
                spinnerCoaches.setAdapter(adapterSpinnerCoaches);
                spinnerCoaches.setSelection(presentSpinnerPosition);
            }
            else{
                Toast toast = Toast.makeText(this, "This is not the coach email", Toast.LENGTH_LONG);
                toast.show();
            }
        }
        catch(Exception e){

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
    public void clearFormEditTeamsCoach(View view){

        editTextFirst.setText("");
        editTextLast.setText("");
        editTextPhone.setText("");
        editTextEmergency.setText("");
        editTextCoachEmail.setText("");
    }
}
