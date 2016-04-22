package beaconsoft.sycorowlayouts.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import beaconsoft.sycorowlayouts.DataSource;
import beaconsoft.sycorowlayouts.MySQLiteHelper;
import beaconsoft.sycorowlayouts.R;
import beaconsoft.sycorowlayouts.SYCOServerAccess.UpdateService;
import beaconsoft.sycorowlayouts.dbobjects.Enrollment;
import beaconsoft.sycorowlayouts.dbobjects.League;
import beaconsoft.sycorowlayouts.dbobjects.Player;
import beaconsoft.sycorowlayouts.dbobjects.Team;
import beaconsoft.sycorowlayouts.dbobjects.Users;

public class QuickEditPlayerActivity extends AppCompatActivity {

    private CheckBox kidBox;
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private EditText et5;
    private EditText et6;
    private EditText et7;
    private TextView et8;
    private TextView textViewTeamName;
    private static final String   NAME_KEY = "beaconsoft.sycorowlayouts.NAME"  ;
    private static final String  ADMIN_KEY = "beaconsoft.sycorowlayouts.ADMIN" ;
    private static final String  EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL" ;
    private static final String   TEAM_KEY = "beaconsoft.sycorowlayouts.TEAM"  ;
    private static final String LEAGUE_KEY = "beaconsoft.sycorowlayouts.LEAGUE";
    private static final String   USER_KEY = "beaconsoft.sycorowlayouts.USER"  ;
    private static final String PLAYER_KEY = "beaconsoft.sycorowlayouts.PLAYER";

    private String currentAdminEmail;
    private String currentAdminName;
    private int currentLeague;
    private int currentTeam;
    private int currentAdminId;
    private int currentUser;
    private int currentPlayer;
    private Player player;
    private Users user;
    private TextView textViewLeagueName;
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


            Team team = updateService.getTeamById(currentTeam);
            textViewTeamName.setText("League: " + team.getTeamName());
            League league = updateService.getLeagueById(currentLeague);
            textViewLeagueName.setText("Team: " + league.getLeagueName());



            user = updateService.getUserById(currentUser);
            player = updateService.getPlayerById(currentPlayer);

            if(user != null && player != null)
            {
                if(player.getFname().equals(user.getFname()) && player.getLname().equals(user.getLname())){
                    kidBox.setChecked(false);
                    et3.setEnabled(false);
                    et4.setEnabled(false);
                }else{
                    kidBox.setChecked(true);
                }

                et1.setText(user.getFname());
                et2.setText(user.getLname());
                et3.setText(player.getFname());
                et4.setText(player.getLname());
                et5.setText(user.getPhone() + "");
                et6.setText(user.getEmail());
                et7.setText(user.getEmergency() + "");
            }
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
        if(currentPlayer > 0) {
            Intent intent = new Intent();
            intent.putExtra("player_id", currentPlayer);
            setResult(Activity.RESULT_OK, intent);
        }else{
            setResult(Activity.RESULT_CANCELED);
        }
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_edit_player);

        TextView textViewAdminEmail = (TextView)findViewById(R.id.textViewQEAdminEmail);
        kidBox = (CheckBox)findViewById(R.id.checkBoxPlayerIsKid);

        et1 = (EditText) findViewById(R.id.editTextPlayerEditContactFirst);
        et2 = (EditText) findViewById(R.id.editTextPlayerEditContactLast);
        et3 = (EditText) findViewById(R.id.editTextPlayerEditChildFirst);
        et4 = (EditText) findViewById(R.id.editTextPlayerEditChildLast);
        et5 = (EditText) findViewById(R.id.editTextPlayerEditPhone);
        et6 = (EditText) findViewById(R.id.editTextPlayerEditEmail);
        et7 = (EditText) findViewById(R.id.editTextPlayerEditEmergencyPhone);
        et8 = (TextView) findViewById(R.id.textViewPlayerEditContactID);
        textViewTeamName = (TextView) findViewById(R.id.textViewEditPlayersPromptTeamName);
        textViewLeagueName = (TextView) findViewById(R.id.textViewEditPlayersLeagueName);

        Intent intent = getIntent();
        currentAdminEmail = intent.getStringExtra(EMAIL_KEY);
        currentAdminName  = intent.getStringExtra(NAME_KEY);
        currentAdminId    = intent.getIntExtra(ADMIN_KEY, 0);
        currentLeague     = intent.getIntExtra(LEAGUE_KEY, 0);
        currentTeam       = intent.getIntExtra(TEAM_KEY, 0);
        currentUser       = intent.getIntExtra(USER_KEY, 0);
        currentPlayer     = intent.getIntExtra(PLAYER_KEY, 0);

        et8.setText(currentAdminName);
        textViewAdminEmail.setText(currentAdminEmail);

    }

    public void onClickCheckBox(View view){
        if(!kidBox.isChecked()){
            kidBox.setSelected(false);
            et3.setText(" ");
            et3.setEnabled(false);
            et4.setText(" ");
            et4.setEnabled(false);
            for(View lol: et3.getTouchables()){
                lol.setEnabled(false);
                lol.setClickable(false);
            }
            for(View lol: et4.getTouchables()){
                lol.setEnabled(false);
                lol.setClickable(false);
            }
        }else{
            kidBox.setSelected(true);
            et3.setText("");
            et3.setHint("First");
            et3.setEnabled(true);
            et4.setText("");
            et4.setHint("Last");
            et4.setEnabled(true);

            for(View lol: et3.getTouchables()){
                lol.setEnabled(true);
                lol.setClickable(true);
            }
            for(View lol: et4.getTouchables()){
                lol.setEnabled(true);
                lol.setClickable(true);
            }
        }
    }

    public void quickEditPlayer(View view){

        /**
         * taking in strings from edittexts
         */
        ContentValues insertValues = new ContentValues();
        String  first      =                  et1.getText().toString().toUpperCase();
        String  last       =                  et2.getText().toString().toUpperCase();
        String  childFirst =                  et3.getText().toString().toUpperCase();
        String  childLast  =                  et4.getText().toString().toUpperCase();
        Long phone         =                Long.parseLong(et5.getText().toString());
        String  email      =                  et6.getText().toString().toUpperCase();
        Long emergency     =                Long.parseLong(et7.getText().toString());

        /**
         * loading the contentvalues for insert into user
         */
        try {

            boolean haveKid = kidBox.isChecked();

            /**
             * Exception handling takes care of improper information entry, first, with a checkbox for entering the child's name.
             * If there is no child, the user will need to be entered into the PLAYER table in their stead.
             */
            if(first.length() < 1 || last.length() < 1 || (haveKid && childFirst.length() < 1) || (haveKid && childLast.length() < 1))
            {
                throw new Exception("There are appropriate name fields left to fill...");
            }

            /**
             * if there is no surrogate relationship, the the names of the user are added to the PLAYER table
             */
            if (!haveKid) {
                childFirst = first;
                childLast = last;
            }

            /***
             * update the player and user tables. There should be no change to enrollment since only personal information changes
             */
            player = updateService.updatePlayer(currentPlayer, childFirst, childLast, currentUser);
            if(player != null){
                user = updateService.updateUser(currentUser, first, last, phone, email, emergency);
                user = updateService.getUserById(currentUser);
                player = updateService.getPlayerById(currentPlayer);

                if(user != null && player != null)
                {
                    kidBox.setChecked(true);
                    et3.setEnabled(true);
                    et4.setEnabled(true);

                    et1.setText(user.getFname());
                    et2.setText(user.getLname());
                    et3.setText(player.getFname());
                    et4.setText(player.getLname());
                    et5.setText(user.getPhone() + "");
                    et6.setText(user.getEmail());
                    et7.setText(user.getEmergency() + "");

                    Toast toast = Toast.makeText(this,
                            "User  (" + user.getFname() + " " + user.getLname()     + ") and\n" +
                                    "Player (" + player.getFname() + " " + player.getLname() + ")" + "UPDATED",
                            Toast.LENGTH_LONG);
                    toast.show();
                }
            }else{
                throw new Exception("Bad Player Update...please debug");
            }

            /**
             * All Exceptions are caught and displayed here, with a toast, and inside of the catch
             * block, every entry point is tested for minumum input first.
             */
        }catch(Exception e){

            Toast toast = Toast.makeText(this, null, Toast.LENGTH_LONG);
            String msg = e.getMessage();
            if(et1.getText().toString().length() < 1){
                msg = "Please fill in your first name";
            }
            if(et2.getText().toString().length() < 1){
                msg = "Please fill in your last name";
            }
            if(et3.getText().toString().length() < 1 && kidBox.isChecked()){
                msg = "Is your child the player? Please fill in their first name";
            }
            if(et4.getText().toString().length() < 1 && kidBox.isChecked()){
                msg = "Is your child the player? Please fill in their last name";
            }
            if(et5.getText().toString().length() < 1){
                msg = "Please fill in your phone number";
            }
            if(et6.getText().toString().length() < 1){
                msg = "You must enter a valid email address";
            }
            if(et7.getText().toString().length() < 1){
                msg = "Please give an emergency number";
            }
            toast.setText(msg);
            toast.show();
        }
    }

    public void clearForm(View view){

        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et5.setText("");
        et7.setText("");
        et6.setText("dontBark@theYeti.bad");
    }
}

