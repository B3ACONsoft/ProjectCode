package beaconsoft.sycorowlayouts.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import beaconsoft.sycorowlayouts.DataSource;
import beaconsoft.sycorowlayouts.R;
import beaconsoft.sycorowlayouts.dbobjects.League;
import beaconsoft.sycorowlayouts.dbobjects.Sport;

public class EditLeaguesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Toolbar toolbar;
    private TextView textViewEditLeaguesTopPrompt;
    private EditText editTextEditLeaguesLeagueName;
    private EditText editTextEditLeaguesSportName;
    private Spinner spinnerEditLeaguesChooseSport;
    private Spinner spinnerEditLeaguesChooseLeague;
    private Button buttonCreateLeague;
    private Button buttonCreateSport;
    private Button buttonDeleteLeague;
    private Button buttonDeleteSport;
    private ArrayList<League> arrayListLeagues;
    private ArrayAdapter arrayAdapterSpinnerLeagues;
    private ArrayList<Sport> arrayListSports;
    private ArrayAdapter arrayAdapterSpinnerSports;
    private Intent intentIncoming;
    private int currentAdminId;
    private int currentLeague;
    private String currentAdminEmail;
    private int currentSport;
    private final String EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private final String LEAGUE_KEY = "beaconsoft.sycorowlayouts.LEAGUE";
    private final String ADMIN_KEY = "beaconsoft.sycorowlayouts.ADMIN";
    Toast toast;
    private DataSource dataSource;

    @Override
    public void onResume()  {
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onResume();
        loadSportSpinner();
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
    public boolean onKeyDown(int keyCode, KeyEvent event){
        //Changes 'back' button action
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            onBackPressed();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra("sport_id", currentSport);
        intent.putExtra("league_id", currentLeague);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_leagues);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toast = Toast.makeText(this, null, Toast.LENGTH_LONG);

        dataSource = new DataSource(this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        intentIncoming = getIntent();
        currentAdminEmail = intentIncoming.getStringExtra(EMAIL_KEY);
        currentAdminId = intentIncoming.getIntExtra(ADMIN_KEY, 0);
        currentLeague = intentIncoming.getIntExtra(LEAGUE_KEY, 0);
        currentSport = 0;

        textViewEditLeaguesTopPrompt    = (TextView) findViewById(R.id.textViewEditLeaguesTop);

        editTextEditLeaguesSportName    = (EditText) findViewById(R.id.editTextEditLeaguesSportName);
        editTextEditLeaguesLeagueName   = (EditText) findViewById(R.id.editTextEditLeaguesLeagueName);

        spinnerEditLeaguesChooseSport   = (Spinner)  findViewById(R.id.spinnerEditLeaguesAddSport);
        spinnerEditLeaguesChooseLeague  = (Spinner)  findViewById(R.id.spinnerEditLeaguesChooseLeague);

        buttonCreateLeague              = (Button)   findViewById(R.id.buttonEditLeaguesAddLeague);
        buttonCreateSport               = (Button)   findViewById(R.id.buttonEditLeaguesAddSport);
        buttonDeleteLeague              = (Button)   findViewById(R.id.buttonEditLeaguesDeleteLeague);
        buttonDeleteSport               = (Button)   findViewById(R.id.buttonEditLeaguesDeleteSport);

        textViewEditLeaguesTopPrompt.setText(currentAdminEmail + " AID:" + currentAdminId + " LID:" + currentLeague);
        arrayListSports = new ArrayList<>();
        arrayListLeagues = new ArrayList<>();
        loadSportSpinner();

    }

    private void loadSportSpinner() {

        try {
            activateView(spinnerEditLeaguesChooseSport);
            activateView(spinnerEditLeaguesChooseLeague);
            activateView(buttonCreateLeague);
            activateView(buttonDeleteLeague);
            activateView(buttonDeleteSport);
            activateView(buttonCreateSport);
            arrayListSports.clear();
            arrayListSports.addAll(dataSource.getListOfSports());
            if (arrayListSports.size() > 0) {
                arrayAdapterSpinnerSports = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, arrayListSports);
                spinnerEditLeaguesChooseSport.setAdapter(arrayAdapterSpinnerSports);
                spinnerEditLeaguesChooseSport.setOnItemSelectedListener(this);
                currentSport = ((Sport) spinnerEditLeaguesChooseSport.getSelectedItem()).getSportID();
                loadLeagueSpinner();
            } else {
                currentSport = 0;
                deactivateView(spinnerEditLeaguesChooseSport);
                deactivateView(spinnerEditLeaguesChooseLeague);
                deactivateView(buttonCreateLeague);
                deactivateView(buttonDeleteLeague);
                deactivateView(buttonDeleteSport);
            }
        }catch(Exception e){
            textViewEditLeaguesTopPrompt.setText("No Sports to Display...");
        }
    }

    private void activateView(View parent){
        parent.setEnabled(true);
        for(View lol : parent.getTouchables()){
            lol.setEnabled(true);
        }
    }

    private void deactivateView(View parent){
        for(View lol : parent.getTouchables()){
            lol.setEnabled(false);
        }
    }

    private void loadLeagueSpinner(){
        try {
            arrayListLeagues.clear();
            arrayListLeagues.addAll(dataSource.getListOfLeaguesByAdminIdAndSport(currentAdminId, currentSport));
            activateView(spinnerEditLeaguesChooseLeague);
            activateView(buttonDeleteLeague);
            activateView(buttonCreateLeague);
            if(arrayListLeagues.size() > 0){
                arrayAdapterSpinnerLeagues =
                        new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, arrayListLeagues);
                spinnerEditLeaguesChooseLeague.setAdapter(arrayAdapterSpinnerLeagues);
                spinnerEditLeaguesChooseLeague.setOnItemSelectedListener(this);
                currentLeague = ((League)spinnerEditLeaguesChooseLeague.getSelectedItem()).getLeagueID();
            } else {
                currentLeague = 0;
                deactivateView(spinnerEditLeaguesChooseLeague);
                deactivateView(buttonDeleteLeague);
            }
        }catch(Exception e){
            textViewEditLeaguesTopPrompt.setText(e.getMessage().toString());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String choice = parent.getItemAtPosition(position).toString();

        if (parent == spinnerEditLeaguesChooseSport) {
            onSpinnerSportsChange(choice);
        }
        if (parent == spinnerEditLeaguesChooseLeague) {
            onSpinnerLeaguesChange(choice);
        }
    }

    private void onSpinnerLeaguesChange(String choice) {
        currentLeague = ((League)spinnerEditLeaguesChooseLeague.getSelectedItem()).getLeagueID();
        textViewEditLeaguesTopPrompt.setText(choice);
    }

    private void onSpinnerSportsChange(String choice) {
        currentSport = ((Sport)spinnerEditLeaguesChooseSport.getSelectedItem()).getSportID();
        textViewEditLeaguesTopPrompt.setText(choice);
        loadLeagueSpinner();
    }

    public void addLeague(View view){
        try {
            Date startDate = new Date();
            Date endDate   = new Date();
            String name = editTextEditLeaguesLeagueName.getText().toString().toUpperCase();
            if(name.length() < 2){
                throw new Exception("Please Enter the Full Sport Name");
            }
            League league = dataSource.createLeague(currentAdminId, currentSport, name, 0, 0,
                    startDate, endDate);

            textViewEditLeaguesTopPrompt.setText("New League: " + name + ", "
                    + league.getStartDate());
            currentLeague = league.getLeagueID();
            loadLeagueSpinner();
        }catch(Exception e){
            textViewEditLeaguesTopPrompt.setText(e.getMessage().toString());
        }
    }

    public void addSport(View view) {
        try {
            Sport sport;
            String tempName = editTextEditLeaguesSportName.getText().toString().toUpperCase();
            if(tempName.length() < 2){
                throw new Exception("Please Enter the Full Sport Name");
            }

            if(!dataSource.checkForDuplicateSports(tempName)) {

                sport = dataSource.createSport(tempName);

            }else{
                throw new Exception("There is already a sport named " + tempName);
            }

            currentSport = sport.getSportID();
            loadSportSpinner();

        }catch(Exception e){
            textViewEditLeaguesTopPrompt.setText(e.getMessage().toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        parent.setSelection(parent.getCount()-1);
    }


}