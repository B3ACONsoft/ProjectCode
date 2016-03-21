package beaconsoft.sycorowlayouts.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import beaconsoft.sycorowlayouts.DataSource;
import beaconsoft.sycorowlayouts.R;
import beaconsoft.sycorowlayouts.dbobjects.Enrollment;
import beaconsoft.sycorowlayouts.dbobjects.Player;
import beaconsoft.sycorowlayouts.dbobjects.Users;

public class QuickAddPlayersActivity extends AppCompatActivity {

    /* Set up private fields so that each method can call them. Getters and setters for another day... */

    private CheckBox kidBox;
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private EditText et5;
    private EditText et6;
    private EditText et7;
    private static final String   NAME_KEY = "beaconsoft.sycorowlayouts.NAME";
    private static final String  ADMIN_KEY = "beaconsoft.sycorowlayouts.ADMIN";
    private static final String  EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private static final String   TEAM_KEY = "beaconsoft.sycorowlayouts.TEAM";
    private static final String LEAGUE_KEY = "beaconsoft.sycorowlayouts.LEAGUE";
    private DataSource dataSource;
    private String email;
    private String name;
    private int currentLeague;
    private int currentTeam;
    private int currentAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_add_players);
        dataSource = new DataSource(this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /**
         * After setting the XML file in it's place as UI, get the intent and empty its contents into
         * the "current" variables.
         */
        Intent intent = getIntent();
        name = intent.getStringExtra(NAME_KEY);
        email = intent.getStringExtra(EMAIL_KEY);
        currentLeague = intent.getIntExtra(LEAGUE_KEY, 0);
        currentTeam   = intent.getIntExtra(TEAM_KEY, 0);
        currentAdmin  = intent.getIntExtra(ADMIN_KEY, 0);

        /**
         * get the email and check box into a variable for alterations
         */
        TextView textViewAdminEmail = (TextView)findViewById(R.id.textViewQAAdminEmail);
        kidBox = (CheckBox)findViewById(R.id.checkBoxPlayerIsKid);

        /**
         * The same for edittexts....
         */
        et1 = (EditText) findViewById(R.id.editTextContactFirst);
        et2 = (EditText) findViewById(R.id.editTextContactLast);
        et3 = (EditText) findViewById(R.id.editTextChildFirst);
        et4 = (EditText) findViewById(R.id.editTextChildLast);
        et5 = (EditText) findViewById(R.id.editTextPhone);
        et6 = (EditText) findViewById(R.id.editTextEmail);
        et7 = (EditText) findViewById(R.id.editTextEmergencyPhone);

        /* We will assume that the user is a parent, check the box */
        kidBox.setChecked(true);

        /**
         * Test script
         */
        et1.setText("Paddy");
        et2.setText("Currin");
        et3.setText("John");
        et4.setText("Currin");
        et5.setText("1234567891");
        et6.setText("fearth@yeti.com");
        et7.setText("9876543210");
        /**
         * End test script
         */

        /**
         * Advertise the current user's email at the top of the screen so that he knows which
         * administrator will be adding things
         */
        textViewAdminEmail.setText(email);

    }

    /**
     * changes availability of input objects on checkbox click
     * @param view
     */
    public void onClickCheckBox(View view){
        if(!kidBox.isChecked()){
            kidBox.setSelected(false);
            et3.setText("");
            et3.setEnabled(false);
            et4.setText("");
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
            et3.setEnabled(true);
            et4.setText("");
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

    /**
     * Opens a database and adds information into the USERS and PLAYERS tables before creating a record
     * in the ENROLLMENT table. The order is important and as stated above.
     *
     * First we insert into USERS:
     *   dbw.insert("users", null, insertValues); [user_id=null, fname, lname, phone, email, emergency, user_type, pass]
     *
     * Then we query USERS for the user_id, which was assigned automatically on insert, using the email, which is unique
     *   cursor = dbw.rawQuery("SELECT user_id FROM users WHERE email = '" + email + "';", null);
     *
     * Then we insert into the PLAYERS table, either the first and last name of the USER's kid
     * or the first and last name of the USER(s).
     *   dbw.insert("player", null, playerValues); [player_id=null, fname, lname, user_id]
     *
     * Then we query PLAYER for the player_id assigned on insert.
     *   cursor = dbw.rawQuery("SELECT player_id FROM player WHERE fname = '" + childFirst + "' AND lname = '" + childLast + "' AND user_id = " + userId, null);
     *
     * This allows us to insert into the ENROLLMENT table and charge the client while making a record.
     * [enrollment_id=null, user_id, player_id, team_id, league_id, enrollment_date, fee]
     *    dbw.insert("enrollment", null, enrollmentValues);
     *
     * Testing insertion into USERS with a select statement:
     *
     *         cursor = dbw.rawQuery("SELECT user_id, fname, lname FROM users WHERE user_id = " + userId, null);
     *
     * brings back a number and a name, which displays in a hidden textview at the bottom of the screen.
     *
     * @param view
     */
    public void quickAddPlayer(View view){

//        try {
        /**
         * taking in strings from edittexts
         */

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

            boolean haveKid = kidBox.isChecked();

            /**
             * Exception handling takes care of improper information entry, first, with a checkbox for entering the child's name.
             * If there is no child, the user will need to be entered into the PLAYER table in their stead.
             */
//            if(first.length() < 1 || last.length() < 1 || (haveKid && childFirst.length() < 1) || (haveKid && childLast.length() < 1))
//            {
//                throw new Exception("There are appropriate name fields left to fill...");
//            }

            /**
             * if there is no surrogate relationship, the the names of the user are added to the PLAYER table
             */
            if (!haveKid) {
                childFirst = first;
                childLast = last;
            }

            /**
             * query for a user_id necessary to adding any PLAYER
             */
        Users user;
            Player player;
            if(dataSource.checkForUserByEmail(email)){
                user = dataSource.getUserByEmail(email.toUpperCase());
                if(dataSource.checkForPlayerByFirstLastAndUserId(childFirst, childLast, user.getUserID())){
                    player = dataSource.getPlayerByFirstLastAndUserId(childFirst, childLast, user.getUserID());
                }else{
                    player = dataSource.createPlayer(childFirst, childLast, user.getUserID());
                }
            }else{
                user = dataSource.createUsers(first, last, phone, email, emergency, "USER", "PASS");
                if(dataSource.checkForPlayerByFirstLastAndUserId(childFirst, childLast, user.getUserID())){
                    player = dataSource.getPlayerByFirstLastAndUserId(childFirst, childLast, user.getUserID());
                }else{
                    player = dataSource.createPlayer(childFirst, childLast, user.getUserID());
                }
            }

            /**
             * clear content values and close the cursor
             */
            dataSource.createEnrollment(user.getUserID(), player.getPlayerID(), currentLeague, currentTeam, new Date(), 1.99);

            //clearForm(findViewById(R.id.buttonQuickAddPlayerAddPlayer));

            /**
             * A successful ENROLLMENT will requre successful inserts into the USER and PLAYER table. There is a hidden textview
             * beneathe the last buttons that will show the last record inserted.
             */
            Enrollment enrollment = dataSource.getEnrollmentByPlayerUserLeagueAndTeam(player.getPlayerID(), player.getUserID(), currentLeague, currentTeam);


            int successfulEID = 0;
            int successfulUID = 0;
            int successfulPID = 0;
            int successfulLID = 0;
            int successfulTID = 0;

//            if (enrollment != null) {

                    successfulEID = enrollment.getEnrollmentID();
                    successfulUID = enrollment.getUserID();
                    successfulPID = enrollment.getPlayerID();
                    successfulLID = enrollment.getLeagueID();
                    successfulTID = enrollment.getTeamID();
//            }else{
//                throw new Exception("Bad Player Creation...please debug");
//            }

            TextView textViewSuccess = (TextView) findViewById(R.id.textViewSuccessfulInsert);
            textViewSuccess.setText("EID: " + successfulEID + " UID: " + successfulUID + " PID: "
                    + successfulPID + " LID:" + successfulLID + " TID: " + successfulTID);

            /**
             * All Exceptions are caught and displayed here, with a toast, and inside of the catch
             * block, every entry point is tested for minumum input first.
              */
//        }catch(Exception e){
//
//            Toast toast = Toast.makeText(this, null, Toast.LENGTH_LONG);
//            String msg = e.getMessage();
//            if(et1.getText().toString().length() < 1){
//                msg = "Please fill in your first name";
//            }
//            if(et2.getText().toString().length() < 1){
//                msg = "Please fill in your last name";
//            }
//            if(et3.getText().toString().length() < 1 && kidBox.isChecked()){
//                msg = "Is your child the player? Please fill in their first name";
//            }
//            if(et4.getText().toString().length() < 1 && kidBox.isChecked()){
//                msg = "Is your child the player? Please fill in their last name";
//            }
//            if(et5.getText().toString().length() < 1){
//                msg = "Please fill in your phone number";
//            }
//            if(et6.getText().toString().length() < 1){
//                msg = "You must enter a valid email address";
//            }
//            if(et7.getText().toString().length() < 1){
//                msg = "Please give an emergency number";
//            }
//            toast.setText(msg);
//            toast.show();
//        }
    }

    /**
     * Removes all strings from edittexts
     * @param view
     */
    public void clearForm(View view){

        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et5.setText("");
        et7.setText("");
        et6.setText("");
    }
}
