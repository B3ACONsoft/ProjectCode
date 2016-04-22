package beaconsoft.sycorowlayouts.SYCOServerAccess;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import beaconsoft.sycorowlayouts.MySQLiteHelper;
import beaconsoft.sycorowlayouts.dbobjects.Attendance;
import beaconsoft.sycorowlayouts.dbobjects.Enrollment;
import beaconsoft.sycorowlayouts.dbobjects.Event;
import beaconsoft.sycorowlayouts.dbobjects.League;
import beaconsoft.sycorowlayouts.dbobjects.Place;
import beaconsoft.sycorowlayouts.dbobjects.Player;
import beaconsoft.sycorowlayouts.dbobjects.Sport;
import beaconsoft.sycorowlayouts.dbobjects.Team;
import beaconsoft.sycorowlayouts.dbobjects.Users;

/**
 * Created by Dave on 3/18/2016.
 *
 * We just clone data source.
 *
 */
public class UpdateService extends Service {

    private SQLiteDatabase db;
    private MySQLiteHelper dbHelper;
    private String[] columnsAttendance = {
            MySQLiteHelper.COLUMN_ATTENDANCE_ID,
            MySQLiteHelper.COLUMN_FK_ATTENDANCE_EVENT_ID,
            MySQLiteHelper.COLUMN_FK_ATTENDANCE_USER_ID,
            MySQLiteHelper.COLUMN_STATUS,
            MySQLiteHelper.COLUMN_MESSAGE
    };
    private String[] columnsEvent = {
            MySQLiteHelper.COLUMN_EVENT_ID,
            MySQLiteHelper.COLUMN_EVENT_TYPE,
            MySQLiteHelper.COLUMN_START_DATE_TIME,
            MySQLiteHelper.COLUMN_FK_EVENT_PLACE_ID,
            MySQLiteHelper.COLUMN_FK_EVENT_HOME_TEAM_ID,
            MySQLiteHelper.COLUMN_FK_EVENT_AWAY_TEAM_ID
    };
    private String[] columnsPlace = {
            MySQLiteHelper.COLUMN_PLACE_ID,
            MySQLiteHelper.COLUMN_PLACE_NAME,
            MySQLiteHelper.COLUMN_STREET_ADDRESS,
            MySQLiteHelper.COLUMN_CITY,
            MySQLiteHelper.COLUMN_STATE,
            MySQLiteHelper.COLUMN_ZIP
    };
    private String[] columnsEnrollment = {
            MySQLiteHelper.COLUMN_ENROLLMENT_ID,
            MySQLiteHelper.COLUMN_FK_ENROLLMENT_USER_ID,
            MySQLiteHelper.COLUMN_FK_ENROLLMENT_PLAYER_ID,
            MySQLiteHelper.COLUMN_FK_ENROLLMENT_LEAGUE_ID,
            MySQLiteHelper.COLUMN_FK_ENROLLMENT_TEAM_ID,
            MySQLiteHelper.COLUMN_ENROLLMENT_DATE,
            MySQLiteHelper.COLUMN_FEE
    };
    private String[] columnsPlayer = {
            MySQLiteHelper.COLUMN_PLAYER_ID,
            MySQLiteHelper.COLUMN_PLAYER_FIRST,
            MySQLiteHelper.COLUMN_PLAYER_LAST,
            MySQLiteHelper.COLUMN_FK_PLAYER_USER_ID
    };
    private String[] columnsTeam = {
            MySQLiteHelper.COLUMN_TEAM_ID,
            MySQLiteHelper.COLUMN_TEAM_NAME,
            MySQLiteHelper.COLUMN_FK_TEAM_LEAGUE_ID,
            MySQLiteHelper.COLUMN_FK_TEAM_USER_ID
    };
    private String[] columnsLeague = {
            MySQLiteHelper.COLUMN_LEAGUE_ID,
            MySQLiteHelper.COLUMN_FK_LEAGUE_USER_ID,
            MySQLiteHelper.COLUMN_FK_LEAGUE_SPORT_ID,
            MySQLiteHelper.COLUMN_LEAGUE_NAME,
            MySQLiteHelper.COLUMN_MIN_AGE,
            MySQLiteHelper.COLUMN_MAX_AGE,
            MySQLiteHelper.COLUMN_START_DATE,
            MySQLiteHelper.COLUMN_END_DATE
    };
    private String[] columnsSport = {
            MySQLiteHelper.COLUMN_SPORT_ID,
            MySQLiteHelper.COLUMN_SPORT_NAME
    };
    private String[] columnsUsers = {
            MySQLiteHelper.COLUMN_USER_ID,
            MySQLiteHelper.COLUMN_FIRST,
            MySQLiteHelper.COLUMN_LAST,
            MySQLiteHelper.COLUMN_PHONE,
            MySQLiteHelper.COLUMN_EMERGENCY,
            MySQLiteHelper.COLUMN_EMAIL,
            MySQLiteHelper.COLUMN_USER_TYPE,
            MySQLiteHelper.COLUMN_PASSWORD
    };

    private class ServerConnectionInterface {
        public boolean running;
        public Queue<Object> insertQueue;
        public Queue<Object> updateQueue;
        public Queue<Object> deleteQueue;

        public ServerConnectionInterface() {
            insertQueue = new LinkedList<Object>();
            updateQueue = new LinkedList<Object>();
            deleteQueue = new LinkedList<Object>();
        }

        /**
         * Check if any of the queue have data
         * @return
         */
        public boolean hasData() {
            if(this.insertQueue.size() > 0
                    || this.updateQueue.size() > 0
                    || this.deleteQueue.size() > 0) {
                return true;
            }
            return false;
        }

    }

    private class ServerConnectionProcess implements Runnable {

        private ServerConnectionInterface serverConnectionInterface;                    //a reference to the serverConnectionInterface
        private RemoteConnection remoteConnection;
        private RemoteInsertOperations insertOperations;
        private UpdateOperations updateOperations;
        private RemoteDeleteOperations deleteOperations;
        private RemoteSelectOperations selectOperations;
        private final int SYNC_RATE_MILLISECONDS = 10000;                               //the rate at which the sync operation cycles in milliseconds

        public ServerConnectionProcess(ServerConnectionInterface serverConnectionInterface) {
            this.remoteConnection = new RemoteConnection();
            this.insertOperations = new RemoteInsertOperations(remoteConnection);
            this.updateOperations = new UpdateOperations(remoteConnection);
            this.deleteOperations = new RemoteDeleteOperations(remoteConnection);
            this.selectOperations = new RemoteSelectOperations(remoteConnection);
            this.serverConnectionInterface = serverConnectionInterface;

        }
        
        /**
         * Starts executing the active part of the class' code. This method is
         * called when a thread is started that has been created with a class which
         * implements {@code Runnable}.
         */
        @Override
        public void run() {
            while(serverConnectionInterface.running) {
                //get all data from server
                //TODO implement get all

                //check to see if any data to upload to server
                if(serverConnectionInterface.hasData()) {
                    //do delete operations
                    while(serverConnectionInterface.deleteQueue.peek() != null) {
                        deleteOperations.deletePOJO(serverConnectionInterface.deleteQueue.poll());
                    }
                    //do insert operations
                    while(serverConnectionInterface.insertQueue.peek() != null) {
                        //poll the queue and do the the operation
                        insertOperations.insertPOJO(serverConnectionInterface.insertQueue.poll());
                    }
                    //do update operations
                    while(serverConnectionInterface.updateQueue.peek() != null) {
                        //poll the queue and do the the operation
                        updateOperations.updatePOJO(serverConnectionInterface.updateQueue.poll());
                    }
                }

                //sleep for SYNC_RATE_MILLISECONDS
                //is sleep the right thing to do?
                //is it too expensive
                try {
                    Thread.sleep(SYNC_RATE_MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private ServerConnectionInterface serverConnectionInterface;              //this the memory space that serverConnectionProcess can access
    private ServerConnectionProcess serverConnectionProcess;            //this the class which is run in the background connection thread
    private Thread serverConnectionThread;


    private final IBinder mBinder = new UpdateServiceBinder();

    /*
    This is class used for the client binder.
    It allows the public methods of the class the service
    to be called from the called activity.
     */
    public class UpdateServiceBinder extends Binder {
        public UpdateService getService() {
            // Return this instance of UpdateService so clients can call public methods
            return UpdateService.this;
        }
    }


    @Override
    public void onCreate() {
        //init serverConnectionInterface memory
        serverConnectionInterface = new ServerConnectionInterface();

        //init the SQL list database helper
        dbHelper = new MySQLiteHelper(this.getBaseContext());

        //start the serverConnectionThread
        startServerConnectionProcess();
    }

    /*
    Return an interface that activities can bind to.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //make the sure the thread is dead
    @Override
    public void onDestroy() {
        stopServerConnectionProcess();
    }

    /*
        Start the worker thread
     */
    public void startServerConnectionProcess() {
        serverConnectionInterface.running = true;
        serverConnectionProcess = new ServerConnectionProcess(serverConnectionInterface);
        serverConnectionThread = new Thread(serverConnectionProcess);
        serverConnectionThread.start();
    }
    /*
        Kill the worker thread.
     */
    public void stopServerConnectionProcess() {
        this.serverConnectionInterface.running = false;
    }


    /*
        Add an object to object to be inserted remotely
     */
    private void queueInsert(Object insert) {
        this.serverConnectionInterface.insertQueue.add(insert);
    }

    /*
        Add an object to be updated remotely
     */
    private void queueUpdate(Object update) {
        this.serverConnectionInterface.updateQueue.add(update);
    }

    /*
        Add and object to be deleted remotely
     */
    private void queueDelete(Object delete) {
        this.serverConnectionInterface.deleteQueue.add(delete);
    }

    private void open_db() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    private void close_db(){
        dbHelper.close();
    }



     /*                                'SPORT' OBJECT SECTION                                */
    /*ALL METHODS HEREIN DEAL WITH THE SELECTION, CREATING, UPDATE OR DELETION OF A 'SPORT'*/
    /*
    *                               MMM  MMMM   MMM  MMMM  MMMMM
    *                              M   M M   M M   M M   M   M
    *                              M     M   M M   M M   M   M
    *                                M   MMMM  M   M MMMM    M
    *                                  M M     M   M M M     M
    *                              M   M M     M   M M  M    M
    *                               MMM  M      MMM  M   M   M
    * */

    public Sport createSport(String sportName){
        try{
            open_db();
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.COLUMN_SPORT_NAME, sportName);
            long insertID = db.insert(MySQLiteHelper.TABLE_SPORT, null, values);
            Cursor cursor = db.query(MySQLiteHelper.TABLE_SPORT, columnsSport,
                    MySQLiteHelper.COLUMN_SPORT_ID + " = " + insertID, null, null, null, null);
            cursor.moveToFirst();
            Sport newSport = cursorToSport(cursor);
            queueInsert(newSport);
            return newSport;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    private Sport cursorToSport(Cursor cursor) {
        Sport newSport = new Sport();
        newSport.setSportID(cursor.getInt(0));
        newSport.setSportName(cursor.getString(1));
        return newSport;
    }

    public Collection<? extends Sport> getListOfSportsByCoach(int coachId) {
        try{
            open_db();
            List<Sport> sportsList = new ArrayList<>();
            Cursor cursor = db.rawQuery("SELECT s.sport_id, s.sport_name " +
                    "FROM sport s, league l, team t " +
                    "WHERE t." + MySQLiteHelper.COLUMN_FK_TEAM_USER_ID + " = " + coachId + " AND " +
                    "t." + MySQLiteHelper.COLUMN_FK_TEAM_LEAGUE_ID + " = " + "l." + MySQLiteHelper.COLUMN_LEAGUE_ID + " AND " +
                    "l." + MySQLiteHelper.COLUMN_FK_LEAGUE_SPORT_ID + " = " + "s." + MySQLiteHelper.COLUMN_SPORT_ID

                    , null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Sport tempSport = cursorToSport(cursor);
                sportsList.add(tempSport);
                cursor.moveToNext();
            }
            cursor.close();
            return sportsList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public Collection<? extends Sport> getListOfSportsByAdmin(int currentAdminId) {
        try{
            open_db();
            List<Sport> sportsList = new ArrayList<>();
            Cursor cursor = db.rawQuery("SELECT DISTINCT s.sport_id, s.sport_name FROM sport s, league l " +
                    " WHERE s.sport_id = l.sport_id AND l.user_id = " + currentAdminId, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Sport tempSport = cursorToSport(cursor);
                sportsList.add(tempSport);
                cursor.moveToNext();
            }
            cursor.close();
            return sportsList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public boolean checkForDuplicateSports(String tempName) {
        try{
            open_db();
            List<Sport> sportsList = new ArrayList<>();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_SPORT, columnsSport,
                    MySQLiteHelper.COLUMN_SPORT_NAME + " = '" + tempName + "';", null, null, null, null);
            cursor.moveToFirst();
            if(cursor.getCount() > 0){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        } finally {
            close_db();
        }

    }



    public List<Sport> getListOfSports(){
        try{
            open_db();
            List<Sport> sportsList = new ArrayList<>();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_SPORT, columnsSport,
                    null, null, null, null, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Sport tempSport = cursorToSport(cursor);
                sportsList.add(tempSport);
                cursor.moveToNext();
            }
            cursor.close();
            return sportsList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    /*                                'USERS' OBJECT SECTION                                */
    /*ALL METHODS HEREIN DEAL WITH THE SELECTION, CREATING, UPDATE OR DELETION OF A 'USERS'*/
    /*
    *                           M   M  MMM  MMMMM MMMM
    *                           M   M M   M M     M   M
    *                           M   M M     M     M   M
    *                           M   M   M   MMM   MMMM
    *                           M   M     M M     M M
    *                           M   M M   M M     M  M
    *                            MMM   MMM  MMMMM M   M
    * */

    public Users createUsers(String first, String last, long phone, String email,
                             long emergency, String userType, String password){
        try{
            open_db();
            ContentValues values = new ContentValues();
            values.putNull(MySQLiteHelper.COLUMN_USER_ID);
            values.put(MySQLiteHelper.COLUMN_FIRST, first);
            values.put(MySQLiteHelper.COLUMN_LAST, last);
            values.put(MySQLiteHelper.COLUMN_PHONE, phone);
            values.put(MySQLiteHelper.COLUMN_EMERGENCY, emergency);
            values.put(MySQLiteHelper.COLUMN_EMAIL, email);
            values.put(MySQLiteHelper.COLUMN_USER_TYPE, userType);
            values.put(MySQLiteHelper.COLUMN_PASSWORD, password);
            long insertID = db.insert(MySQLiteHelper.TABLE_USERS, null, values);
            Cursor cursor = db.query(MySQLiteHelper.TABLE_USERS, columnsUsers,
                    MySQLiteHelper.COLUMN_USER_ID + " = " + insertID, null, null, null, null);
            cursor.moveToFirst();
            Users newUser = cursorToUser(cursor);
            queueInsert(newUser);
            return newUser;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    private Users cursorToUser(Cursor cursor){
        Users newUser = new Users();
        newUser.setUser_id(cursor.getInt(0));
        newUser.setFname(cursor.getString(1));
        newUser.setLname(cursor.getString(2));
        newUser.setPhone(cursor.getLong(3));
        newUser.setEmergency(cursor.getLong(4));
        newUser.setEmail(cursor.getString(5));
        newUser.setUser_type(cursor.getString(6));
        newUser.setPassword(cursor.getString(7));
        return newUser;
    }

    public boolean checkForDuplicateEmail(String email) {
        try{
            open_db();
            Cursor cursor = db.query(
                    MySQLiteHelper.TABLE_USERS, columnsUsers,
                    MySQLiteHelper.COLUMN_EMAIL + " = " + email,
                    null, null, null, null);
            cursor.moveToFirst();
            if(cursor.getCount() >= 1){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        } finally {
            close_db();
        }

    }

    public Users updateUser(int currentUser, String first, String last, Long phone, String email, Long emergency) {
        try{
            open_db();
            ContentValues cv = new ContentValues();
            cv.put("fname", first);
            cv.put("lname", last);
            cv.put("phone", phone);
            cv.put("email", email);
            cv.put("emergency", emergency);
            db.beginTransaction();
            int rowsAltered = db.update(MySQLiteHelper.TABLE_USERS, cv,
                    MySQLiteHelper.COLUMN_USER_ID + " = ?",
                    new String[] { currentUser + "" });
            if(rowsAltered == 1) {
                db.setTransactionSuccessful();
                db.endTransaction();
                Users returVal = getUserById(currentUser);
                queueUpdate(returVal);
                return returVal;
            }else{
                db.execSQL("ROLLBACK");
                db.endTransaction();
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public Users getUserById(int currentUser) {
        try{
            open_db();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_USERS, columnsUsers,
                    MySQLiteHelper.COLUMN_USER_ID + " = " + currentUser,
                    null, null, null, null);
            cursor.moveToFirst();
            if(cursor.getCount() == 1) {
                return cursorToUser(cursor);
            }else {
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public boolean checkForUserByEmail(String email){
        try{
            open_db();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_USERS, columnsUsers,
                    MySQLiteHelper.COLUMN_EMAIL + " = '" + email.toUpperCase() + "';",
                    null,null,null,null);
            cursor.moveToFirst();
            if(cursor.getCount() == 1){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false;
            //TODO this is probably a bad thing to return i dunnooooo
        } finally {
            close_db();
        }

    }

    public List<Users> getListOfUsers(){
        try{
            open_db();
            List<Users> usersList = new ArrayList<>();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_USERS, columnsUsers,
                    null, null, null, null, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Users tempUser = cursorToUser(cursor);
                usersList.add(tempUser);
                cursor.moveToNext();
            }
            cursor.close();
            return usersList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public Users getUserByEmail(String email){
        try{
            open_db();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_USERS, columnsUsers,
                    MySQLiteHelper.COLUMN_EMAIL + " = '" + email.toUpperCase() + "'", null, null, null, null);
            cursor.moveToNext();
            if(cursor.getCount() == 1) {
                return cursorToUser(cursor);
            }else if(cursor.getCount() == 0){
                return null;
            }else{
                try {
                    throw new Exception("There is more than one user with that email. The database is not atomic...");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return new Users();
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public List<Users> getListOfUsersAvailableToCoach(int currentLeague){
        try{
            open_db();
            List<Users> usersList = new ArrayList<>();
            Cursor cursor = db.rawQuery(
                    "SELECT " +
                            " u." + MySQLiteHelper.COLUMN_USER_ID     + ", " +
                            " u." + MySQLiteHelper.COLUMN_FIRST       + ", " +
                            " u." + MySQLiteHelper.COLUMN_LAST        + ", " +
                            " u." + MySQLiteHelper.COLUMN_PHONE       + ", " +
                            " u." + MySQLiteHelper.COLUMN_EMERGENCY   + ", " +
                            " u." + MySQLiteHelper.COLUMN_EMAIL       + ", " +
                            " u." + MySQLiteHelper.COLUMN_USER_TYPE   + ", " +
                            " u." + MySQLiteHelper.COLUMN_PASSWORD    +
                            " FROM users u WHERE " + MySQLiteHelper.COLUMN_USER_TYPE + " = 'COACH'" +
                            " EXCEPT SELECT " +
                            " u." + MySQLiteHelper.COLUMN_USER_ID     + ", " +
                            " u." + MySQLiteHelper.COLUMN_FIRST       + ", " +
                            " u." + MySQLiteHelper.COLUMN_LAST        + ", " +
                            " u." + MySQLiteHelper.COLUMN_PHONE       + ", " +
                            " u." + MySQLiteHelper.COLUMN_EMERGENCY   + ", " +
                            " u." + MySQLiteHelper.COLUMN_EMAIL       + ", " +
                            " u." + MySQLiteHelper.COLUMN_USER_TYPE   + ", " +
                            " u." + MySQLiteHelper.COLUMN_PASSWORD    +
                            " FROM " + MySQLiteHelper.TABLE_USERS + " u, " + MySQLiteHelper.TABLE_TEAM + " t " +
                            " WHERE u." + MySQLiteHelper.COLUMN_USER_ID + " = t." + MySQLiteHelper.COLUMN_FK_TEAM_USER_ID +
                            " AND t." + MySQLiteHelper.COLUMN_FK_TEAM_LEAGUE_ID + " = " + currentLeague + ";"
                    , null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Users tempUser = cursorToUser(cursor);
                usersList.add(tempUser);
                cursor.moveToNext();
            }
            cursor.close();
            return usersList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public List<Users> getListOfUsersByTeam(int teamID){
        try{
            open_db();
            List<Users> usersList = new ArrayList<>();
            Cursor cursor = db.rawQuery("SELECT u.user_id, u.fname, u.lname, u.phone, u.emergency, u.email, u.user_type, u.pass " +
                    " FROM users u, enrollment e " +
                    " WHERE u.user_id = e.user_id " +
                    "  AND e.team_id = " + teamID, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Users tempUser = cursorToUser(cursor);
                usersList.add(tempUser);
                cursor.moveToNext();
            }
            cursor.close();
            return usersList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }


    /*                                'LEAGUE' OBJECT SECTION                                */
    /*ALL METHODS HEREIN DEAL WITH THE SELECTION, CREATING, UPDATE OR DELETION OF A 'LEAGUE'*/
    /*
    *                          M      MMMMM    M     MMM   M   M  MMMMM
    *                          M      M       M M   M   M  M   M  M
    *                          M      M      M   M  M      M   M  M
    *                          M      MMM    MMMMM  M MMM  M   M  MMM
    *                          M      M      M   M  M   M  M   M  M
    *                          M   M  M      M   M  M   M  M   M  M
    *                          MMMMM  MMMMM  M   M   MMM    MMM   MMMMM
    * */

    public League createLeague(int userID, int sportID, String leagueName,
                               int minAge, int maxAge, Date startDate, Date endDate){
        try{
            open_db();
            ContentValues values = new ContentValues();
            values.putNull(MySQLiteHelper.COLUMN_LEAGUE_ID);
            values.put(MySQLiteHelper.COLUMN_FK_LEAGUE_USER_ID, userID);
            values.put(MySQLiteHelper.COLUMN_FK_LEAGUE_SPORT_ID, sportID);
            values.put(MySQLiteHelper.COLUMN_LEAGUE_NAME, leagueName);
            values.put(MySQLiteHelper.COLUMN_MIN_AGE, minAge);
            values.put(MySQLiteHelper.COLUMN_MAX_AGE, maxAge);
            values.put(MySQLiteHelper.COLUMN_START_DATE, startDate.toString());
            values.put(MySQLiteHelper.COLUMN_END_DATE, endDate.toString());
            long insertID = db.insert(MySQLiteHelper.TABLE_LEAGUE, null, values);
            Cursor cursor = db.query(MySQLiteHelper.TABLE_LEAGUE, columnsLeague,
                    MySQLiteHelper.COLUMN_LEAGUE_ID + " = " + insertID, null, null, null, null);
            cursor.moveToFirst();
            League newLeague = cursorToLeague(cursor);
            queueInsert(newLeague);
            return newLeague;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    private League cursorToLeague(Cursor cursor) {
        League newLeague = new League();
        newLeague.setLeagueID(cursor.getInt(0));
        newLeague.setUserID(cursor.getInt(1));
        newLeague.setSportID(cursor.getInt(2));
        newLeague.setLeagueName(cursor.getString(3));
        newLeague.setMinAge(cursor.getInt(4));
        newLeague.setMaxAge(cursor.getInt(5));
        newLeague.setStartDate(new Date(cursor.getLong(6)));
        newLeague.setEndDate(new Date(cursor.getLong(7)));
        return newLeague;
    }

    public Collection<? extends League> getListOfLeaguesByCoachAndSport(int coachId, int sportId) {
        try{
            open_db();
            List<League> leaguesList = new ArrayList<>();
            Cursor cursor = db.rawQuery(
                    "SELECT l." + MySQLiteHelper.COLUMN_LEAGUE_ID           + ", " +
                            "l." + MySQLiteHelper.COLUMN_FK_LEAGUE_USER_ID   + ", " +
                            "l." + MySQLiteHelper.COLUMN_FK_LEAGUE_SPORT_ID  + ", " +
                            "l." + MySQLiteHelper.COLUMN_LEAGUE_NAME         + ", " +
                            "l." + MySQLiteHelper.COLUMN_MIN_AGE             + ", " +
                            "l." + MySQLiteHelper.COLUMN_MAX_AGE             + ", " +
                            "l." + MySQLiteHelper.COLUMN_START_DATE          + ", " +
                            "l." + MySQLiteHelper.COLUMN_END_DATE            + " " +
                            "FROM " +
                            MySQLiteHelper.TABLE_LEAGUE + " l, " +
                            MySQLiteHelper.TABLE_TEAM   + " t " +
                            "WHERE " +
                            " t." + MySQLiteHelper.COLUMN_FK_TEAM_USER_ID    + " = " + coachId   + " AND " +
                            " t." + MySQLiteHelper.COLUMN_FK_TEAM_LEAGUE_ID  + " = " +
                            " l." + MySQLiteHelper.COLUMN_LEAGUE_ID                              + " AND " +
                            " l." + MySQLiteHelper.COLUMN_FK_LEAGUE_SPORT_ID + " = " + sportId
                    , null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                League tempLeague = cursorToLeague(cursor);
                leaguesList.add(tempLeague);
                cursor.moveToNext();
            }
            cursor.close();
            return leaguesList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public League getLeagueById(int currentLeague) {
        try{
            open_db();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_LEAGUE, columnsLeague,
                    MySQLiteHelper.COLUMN_LEAGUE_ID + " = " + currentLeague,
                    null, null, null, null);
            cursor.moveToFirst();
            if(cursor.getCount() > 0){
                return cursorToLeague(cursor);
            }else{
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public Collection<? extends League> getListOfLeaguesByAdminIdAndSport(int currentAdminId, int currentSport) {
        try{
            open_db();
            List<League> leaguesList = new ArrayList<>();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_LEAGUE, columnsLeague,
                    MySQLiteHelper.COLUMN_FK_LEAGUE_USER_ID + " = " + currentAdminId + " AND " +
                            MySQLiteHelper.COLUMN_FK_LEAGUE_SPORT_ID + " = " + currentSport + ";",
                    null, null, null, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                League tempLeague = cursorToLeague(cursor);
                leaguesList.add(tempLeague);
                cursor.moveToNext();
            }
            cursor.close();
            return leaguesList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public List<League> getListOfLeagues(){
        try{
            open_db();
            List<League> leaguesList = new ArrayList<>();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_LEAGUE, columnsLeague,
                    null, null, null, null, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                League tempLeague = cursorToLeague(cursor);
                leaguesList.add(tempLeague);
                cursor.moveToNext();
            }
            cursor.close();
            return leaguesList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public List<League> getListOfLeaguesBySport(int currentSport){
        try{
            open_db();
            List<League> leaguesList = new ArrayList<>();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_LEAGUE, columnsLeague,
                    MySQLiteHelper.COLUMN_FK_LEAGUE_SPORT_ID + " = " + currentSport,
                    null, null, null, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                League tempLeague = cursorToLeague(cursor);
                leaguesList.add(tempLeague);
                cursor.moveToNext();
            }
            cursor.close();
            return leaguesList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

 /*                                'TEAM' OBJECT SECTION                                */
    /*ALL METHODS HEREIN DEAL WITH THE SELECTION, CREATING, UPDATE OR DELETION OF A 'TEAM'*/
    /*
    *                          MMMMM  MMMMM    M    M   M
    *                            M    M       M M   M   M
    *                            M    M      M   M  MM MM
    *                            M    MMM    MMMMM  M M M
    *                            M    M      M   M  M M M
    *                            M    M      M   M  M   M
    *                            M    MMMMM  M   M  M   M
    * */

    public Team createTeam(String teamName, int leagueID, int userID){
        try{
            open_db();
            ContentValues values = new ContentValues();
            values.putNull(MySQLiteHelper.COLUMN_TEAM_ID);
            values.put(MySQLiteHelper.COLUMN_TEAM_NAME, teamName);
            values.put(MySQLiteHelper.COLUMN_FK_TEAM_LEAGUE_ID, leagueID);
            values.put(MySQLiteHelper.COLUMN_FK_TEAM_USER_ID, userID);

            long insertID = db.insert(MySQLiteHelper.TABLE_TEAM, null, values);
            Cursor cursor = db.query(MySQLiteHelper.TABLE_TEAM, columnsTeam,
                    MySQLiteHelper.COLUMN_TEAM_ID + " = " + insertID, null, null, null, null);
            cursor.moveToFirst();
            Team newTeam = cursorToTeam(cursor);
            queueInsert(newTeam);
            return newTeam;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public Team getTeamById(int currentTeam) {
        try{
            open_db();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_TEAM, columnsTeam,
                    MySQLiteHelper.COLUMN_TEAM_ID + " = " + currentTeam,
                    null, null, null, null);
            cursor.moveToFirst();
            if(cursor.getCount() == 1){
                return cursorToTeam(cursor);
            }else{
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    private Team cursorToTeam(Cursor cursor) {
        Team newTeam = new Team();
        newTeam.setTeamID(cursor.getInt(0));
        newTeam.setTeamName(cursor.getString(1));
        newTeam.setLeagueID(cursor.getInt(2));
        newTeam.setUserID(cursor.getInt(3));
        return newTeam;
    }

    public Team updateTeamCoach(int currentTeamId, int currentCoachId){
        try{
            open_db();
            ContentValues cv = new ContentValues();
            cv.put(MySQLiteHelper.COLUMN_FK_TEAM_USER_ID, currentCoachId);
            db.beginTransaction();
            int rowsAltered = db.update(MySQLiteHelper.TABLE_TEAM, cv,
                    MySQLiteHelper.COLUMN_TEAM_ID + " = ?", new String[] { String.format("%d", currentTeamId) });
            if(rowsAltered == 1) {
                db.setTransactionSuccessful();
                db.endTransaction();
                return getTeamById(currentTeamId);
            }else{
                db.execSQL("ROLLBACK");
                db.endTransaction();
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public Team updateTeamName(Team team, String teamName, int leagueId, int coachUserId){
        try{
            open_db();
            ContentValues cv = new ContentValues();
            cv.put("team_name", teamName);
            cv.put("league_id", leagueId);
            cv.put("user_id", coachUserId);
            db.beginTransaction();
            int rowsAltered = db.update(MySQLiteHelper.TABLE_TEAM, cv,
                    MySQLiteHelper.COLUMN_TEAM_ID + " = ?", new String[] { team.getTeamID() + "" });
            if(rowsAltered == 1) {
                db.setTransactionSuccessful();
                db.endTransaction();
                return getTeamById(team.getTeamID());
            }else{
                db.execSQL("ROLLBACK");
                db.endTransaction();
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public List<Team> getListOfTeams(){
        try{
            open_db();
            List<Team> teamsList = new ArrayList<>();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_TEAM, columnsTeam,
                    null, null, null, null, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Team tempTeam = cursorToTeam(cursor);
                teamsList.add(tempTeam);
                cursor.moveToNext();
            }
            cursor.close();
            return teamsList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public List<Team> getListOfTeamsCoachedByUser(int currentUser, int currentLeague){
        try{
            open_db();
            List<Team> teamsList = new ArrayList<>();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_TEAM, columnsTeam,
                    MySQLiteHelper.COLUMN_FK_TEAM_USER_ID + " = " + currentUser + " AND " +
                            MySQLiteHelper.COLUMN_FK_TEAM_LEAGUE_ID + " = " + currentLeague + ";",
                    null, null, null, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Team tempTeam = cursorToTeam(cursor);
                teamsList.add(tempTeam);
                cursor.moveToNext();
            }
            cursor.close();
            return teamsList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public List<Team> getListOfTeamsByLeague(int currentLeague){
        try{
            open_db();
            List<Team> teamsList = new ArrayList<>();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_TEAM, columnsTeam,
                    MySQLiteHelper.COLUMN_FK_TEAM_LEAGUE_ID + " = " + currentLeague, null, null, null, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Team tempTeam = cursorToTeam(cursor);
                teamsList.add(tempTeam);
                cursor.moveToNext();
            }
            cursor.close();
            return teamsList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

/*                                'PLAYER' OBJECT SECTION                                */
    /*ALL METHODS HEREIN DEAL WITH THE SELECTION, CREATING, UPDATE OR DELETION OF A 'PLAYER'*/
    /*
    *                 MMMM   M       M   M   M MMMMM MMMM
    *                 M   M  M      M M  M   M M     M   M
    *                 M   M  M     M   M  M M  M     M   M
    *                 MMMM   M     M   M   M   MMM   MMMM
    *                 M      M     MMMMM   M   M     M M
    *                 M      M     M   M   M   M     M  M
    *                 M      MMMMM M   M   M   MMMMM M   M
    * */

    public Player createPlayer(String playerFirst, String playerLast, int userID){
        try{
            open_db();
            ContentValues values = new ContentValues();
            values.putNull(MySQLiteHelper.COLUMN_PLAYER_ID);
            values.put(MySQLiteHelper.COLUMN_PLAYER_FIRST, playerFirst);
            values.put(MySQLiteHelper.COLUMN_PLAYER_LAST, playerLast);
            values.put(MySQLiteHelper.COLUMN_FK_PLAYER_USER_ID, userID);

            long insertID = db.insert(MySQLiteHelper.TABLE_PLAYER, null, values);
            Cursor cursor = db.query(MySQLiteHelper.TABLE_PLAYER, columnsPlayer,
                    MySQLiteHelper.COLUMN_PLAYER_ID + " = " + insertID, null, null, null, null);
            cursor.moveToFirst();
            Player newPlayer = cursorToPlayer(cursor);

            //TODO: Make sure new players are added to attendance rolls
            queueInsert(newPlayer);
            return newPlayer;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public Player updatePlayer(int currentPlayerId, String fname, String lname, int currentUserId){
        try{
            open_db();
            ContentValues cv = new ContentValues();
            cv.put("player_id", currentPlayerId);
            cv.put("fname", fname);
            cv.put("lname", lname);
            cv.put("user_id", currentUserId);

            int rowsAltered = db.update(MySQLiteHelper.TABLE_PLAYER, cv,
                    MySQLiteHelper.COLUMN_PLAYER_ID + " = " + currentPlayerId, null);
            if(rowsAltered == 1) {
                Player returnVal = getPlayerById(currentPlayerId);
                queueUpdate(returnVal);
                return returnVal;
            }else
            {
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public Collection<? extends Player> getListOfPlayersByTeam(int currentTeam) {
        try{
            open_db();
            Cursor cursor = db.rawQuery("SELECT p." + MySQLiteHelper.COLUMN_PLAYER_ID    + ", " +
                    "p." + MySQLiteHelper.COLUMN_PLAYER_FIRST + ", " +
                    "p." + MySQLiteHelper.COLUMN_PLAYER_LAST  + ", "  +
                    "p." + MySQLiteHelper.COLUMN_FK_PLAYER_USER_ID + " " +
                    "FROM " + MySQLiteHelper.TABLE_PLAYER + " p, " +
                    MySQLiteHelper.TABLE_ENROLLMENT + " e " +
                    "WHERE p." + MySQLiteHelper.COLUMN_PLAYER_ID + " = " +
                    "e." + MySQLiteHelper.COLUMN_FK_ENROLLMENT_PLAYER_ID +
                    " AND e." + MySQLiteHelper.COLUMN_FK_ENROLLMENT_TEAM_ID + " = " + currentTeam + ";"
                    , null);
            List<Player> players = new ArrayList<>();

            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Player tempPlayer = cursorToPlayer(cursor);
                players.add(tempPlayer);
                cursor.moveToNext();
            }
            cursor.close();
            return players;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public Player getPlayerById(int currentPlayer) {
        try{
            open_db();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_PLAYER, columnsPlayer,
                    MySQLiteHelper.COLUMN_PLAYER_ID + " = " + currentPlayer,
                    null, null, null, null);
            cursor.moveToFirst();
            if(cursor.getCount() == 1){
                return cursorToPlayer(cursor);
            }else{
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public boolean checkForPlayerByFirstLastAndUserId(String first, String last, int userID){
        try{
            open_db();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_PLAYER, columnsPlayer,
                    MySQLiteHelper.COLUMN_FK_PLAYER_USER_ID + " = " + userID + " AND " +
                            MySQLiteHelper.COLUMN_FIRST + " = '" + first + "' AND " +
                            MySQLiteHelper.COLUMN_LAST + " = '" + last + "';"
                    , null, null, null, null);
            if(cursor.getCount() == 1){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false;
            //TODO does this break shit if things go wrong????
            //scary but whatever....
        } finally {
            close_db();
        }

    }

    public Player getPlayerByFirstLastAndUserId(String first, String last, int userID) {
        try{
            open_db();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_PLAYER, columnsPlayer,
                    MySQLiteHelper.COLUMN_FK_PLAYER_USER_ID + " = " + userID + " AND " +
                            MySQLiteHelper.COLUMN_FIRST + " = '" + first + "' AND " +
                            MySQLiteHelper.COLUMN_LAST + " = '" + last + "';"
                    , null, null, null, null);
            if(cursor.getCount() == 1){
                return cursorToPlayer(cursor);
            }else {
                if (cursor.getCount() == 0) {
                    return null;
                } else {
                    try {
                        throw new Exception("More than one record matches your search... that means the database is out of order...");
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }
            return new Player();
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    private Player cursorToPlayer(Cursor cursor) {
        Player newPlayer = new Player();
        newPlayer.setPlayerID(cursor.getInt(0));
        newPlayer.setFname(cursor.getString(1));
        newPlayer.setLname(cursor.getString(2));
        newPlayer.setUserID(cursor.getInt(3));
        return newPlayer;
    }

    public List<Player> getListOfPlayers(){
        try{
            open_db();
            List<Player> playersList = new ArrayList<>();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_PLAYER, columnsPlayer,
                    null, null, null, null, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Player tempPlayer = cursorToPlayer(cursor);
                playersList.add(tempPlayer);
                cursor.moveToNext();
            }
            cursor.close();
            return playersList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

/*                                'ENROLLMENT' OBJECT SECTION                                */
    /*ALL METHODS HEREIN DEAL WITH THE SELECTION, CREATING, UPDATE OR DELETION OF A 'ENROLLMENT'*/
    /*
    *                 MMMMM M   M MMMM   MMM  M     M     M   M MMMMM N   N MMMMM
    *                 M     M   M M   M M   M M     M     M   M M     N   N   M
    *                 M     MM  M M   M M   M M     M     MM MM M     NN  N   M
    *                 MMMM  M M M MMMM  M   M M     M     M M M MMM   N N N   M
    *                 M     M  MM M M   M   M M     M     M M M M     N  NN   M
    *                 M     M   M M  M  M   M M     M     M   M M     N   N   M
    *                 MMMMM M   M M   M  MMM  MMMMM MMMMM M   M MMMMM N   N   M
    * */

    public Enrollment createEnrollment(int userID, int playerID, int leagueID, int teamID,
                                       Date enrollmentDate, Double fee){
        try{
            open_db();
            ContentValues values = new ContentValues();
            values.putNull(MySQLiteHelper.COLUMN_ENROLLMENT_ID);
            values.put(MySQLiteHelper.COLUMN_FK_ENROLLMENT_USER_ID, userID);
            values.put(MySQLiteHelper.COLUMN_FK_ENROLLMENT_PLAYER_ID, playerID);
            values.put(MySQLiteHelper.COLUMN_FK_ENROLLMENT_LEAGUE_ID, leagueID);
            values.put(MySQLiteHelper.COLUMN_FK_ENROLLMENT_TEAM_ID, teamID);
            values.put(MySQLiteHelper.COLUMN_ENROLLMENT_DATE, enrollmentDate.toString());
            values.put(MySQLiteHelper.COLUMN_FEE, fee);

            long insertID = db.insert(MySQLiteHelper.TABLE_ENROLLMENT, null, values);
            Cursor cursor = db.query(MySQLiteHelper.TABLE_ENROLLMENT, columnsEnrollment,
                    MySQLiteHelper.COLUMN_ENROLLMENT_ID + " = " + insertID, null, null, null, null);
            cursor.moveToFirst();
            Enrollment newEnrollment = cursorToEnrollment(cursor);
            queueInsert(newEnrollment);
            return newEnrollment;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    private Enrollment cursorToEnrollment(Cursor cursor) {
        Enrollment newEnrollment = new Enrollment();
        newEnrollment.setEnrollmentID(cursor.getInt(0));
        newEnrollment.setUserID(cursor.getInt(1));
        newEnrollment.setPlayerID(cursor.getInt(2));
        newEnrollment.setLeagueID(cursor.getInt(3));
        newEnrollment.setTeamID(cursor.getInt(4));
        newEnrollment.setEnrollmentDate(new Date(cursor.getLong(5)));
        newEnrollment.setFee(cursor.getDouble(6));
        return newEnrollment;
    }

    public Enrollment updateCoachEnrollmentWithTeamIdAndLeagueId(int currentTeamId, int currentCoachUserId, int currentLeagueId) {
        try{
            open_db();
            ContentValues cv = new ContentValues();
            cv.put(MySQLiteHelper.COLUMN_FK_ENROLLMENT_USER_ID, currentCoachUserId);

            int rowsAltered = db.update(MySQLiteHelper.TABLE_ENROLLMENT, cv,
                    MySQLiteHelper.COLUMN_FK_ENROLLMENT_TEAM_ID + " = ?" + " AND " +
                            MySQLiteHelper.COLUMN_FK_ENROLLMENT_LEAGUE_ID + " = ?", new String[] {
                            "" + currentTeamId, "" + currentLeagueId
                    });
            if(rowsAltered == 1) {
                Enrollment returnVal = getEnrollmentByUserLeagueAndTeam(currentCoachUserId, currentTeamId, currentLeagueId);
                queueUpdate(returnVal);
                return returnVal;
            }else
            {
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }


    }

    private Enrollment getEnrollmentByTeamIdAndLeagueId(int currentTeamId, int currentLeagueId) {
        try{
            open_db();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_ENROLLMENT, columnsEnrollment,
                    MySQLiteHelper.COLUMN_FK_ENROLLMENT_TEAM_ID + " = " + currentTeamId + " AND " +
                            MySQLiteHelper.COLUMN_FK_ENROLLMENT_LEAGUE_ID + " = " + currentLeagueId, null,
                    null, null, null
            );

            return null;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public Enrollment getEnrollmentByUserLeagueAndTeam(int coachUserId, int leagueId, int teamId) {
        try{
            open_db();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_ENROLLMENT, columnsEnrollment,
                    MySQLiteHelper.COLUMN_FK_ENROLLMENT_USER_ID   + " = " + coachUserId + " AND " +
                            MySQLiteHelper.COLUMN_FK_ENROLLMENT_LEAGUE_ID + " = " +    leagueId + " AND " +
                            MySQLiteHelper.COLUMN_FK_ENROLLMENT_TEAM_ID   + " = " +      teamId,
                    null, null, null, null);
            if(cursor.getCount() == 0){
                return null;
            }else if(cursor.getCount() == 1){
                return cursorToEnrollment(cursor);
            }else
            {
                try {
                    throw new Exception("There is more than one record with these parameters. The database is not atomic...");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return new Enrollment();
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public Enrollment getEnrollmentByPlayerUserLeagueAndTeam(int playerID, int userID, int currentLeague, int currentTeam) {
        try{
            open_db();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_ENROLLMENT, columnsEnrollment,
                    MySQLiteHelper.COLUMN_PLAYER_ID + " = " + playerID + " AND " +
                            MySQLiteHelper.COLUMN_FK_ENROLLMENT_USER_ID + " = " + userID + " AND " +
                            MySQLiteHelper.COLUMN_FK_ENROLLMENT_LEAGUE_ID + " = " + currentLeague + " AND " +
                            MySQLiteHelper.COLUMN_FK_ENROLLMENT_TEAM_ID + " = " + currentTeam +";",
                    null, null, null, null);
            if(cursor.getCount() == 0){
                return null;
            }else if(cursor.getCount() == 1){
                return cursorToEnrollment(cursor);
            }else
            {
                try {
                    throw new Exception("There is more than one record with these parameters. The database is not atomic...");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return new Enrollment();
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public List<Enrollment> getListOfEnrollments(){
        try{
            open_db();
            List<Enrollment> enrollmentsList = new ArrayList<>();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_ENROLLMENT, columnsEnrollment,
                    null, null, null, null, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Enrollment tempEnrollment = cursorToEnrollment(cursor);
                enrollmentsList.add(tempEnrollment);
                cursor.moveToNext();
            }
            cursor.close();
            return enrollmentsList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

/*                                'PLACE' OBJECT SECTION                                */
    /*ALL METHODS HEREIN DEAL WITH THE SELECTION, CREATING, UPDATE OR DELETION OF A 'PLACE'*/
    /*
    *                 MMMM   M       M    MMM  MMMMM
    *                 M   M  M      M M  M   M M
    *                 M   M  M     M   M M     M
    *                 MMMM   M     M   M M     MMM
    *                 M      M     MMMMM M     M
    *                 M      M     M   M M   M M
    *                 M      MMMMM M   M  MMM  MMMMM
    * */

    public Place createPlace(String placeName, String streetAddress, String city, String state, int zip){
        try{
            open_db();
            ContentValues values = new ContentValues();
            values.putNull(MySQLiteHelper.COLUMN_PLACE_ID);
            values.put(MySQLiteHelper.COLUMN_PLACE_NAME, placeName);
            values.put(MySQLiteHelper.COLUMN_STREET_ADDRESS, streetAddress);
            values.put(MySQLiteHelper.COLUMN_CITY, city);
            values.put(MySQLiteHelper.COLUMN_STATE, state);
            values.put(MySQLiteHelper.COLUMN_ZIP, zip);

            long insertID = db.insert(MySQLiteHelper.TABLE_PLACE, null, values);
            Cursor cursor = db.query(MySQLiteHelper.TABLE_PLACE, columnsPlace,
                    MySQLiteHelper.COLUMN_PLACE_ID + " = " + insertID, null, null, null, null);
            cursor.moveToFirst();
            Place newPlace = cursorToPlace(cursor);
            queueInsert(newPlace);
            return newPlace;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    private Place cursorToPlace(Cursor cursor) {
        Place newPlace = new Place();
        newPlace.setPlaceID(cursor.getInt(0));
        newPlace.setPlaceName(cursor.getString(1));
        newPlace.setStreetAddress(cursor.getString(2));
        newPlace.setCity(cursor.getString(3));
        newPlace.setState(cursor.getString(4));
        newPlace.setZip(cursor.getInt(5));
        return newPlace;
    }

    public Place getPlaceById(int placeID) {
        try{
            open_db();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_PLACE, columnsPlace,
                    MySQLiteHelper.COLUMN_PLACE_ID + " = " + placeID, null, null, null, null);
            cursor.moveToFirst();
            if(cursor.getCount() == 1){
                return cursorToPlace(cursor);
            }else{
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public List<Place> getListOfPlaces(){
        try{
            open_db();
            List<Place> placesList = new ArrayList<>();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_PLACE, columnsPlace,
                    null, null, null, null, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Place tempPlace = cursorToPlace(cursor);
                placesList.add(tempPlace);
                cursor.moveToNext();
            }
            cursor.close();
            return placesList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

/*                                'EVENT' OBJECT SECTION                                */
    /*ALL METHODS HEREIN DEAL WITH THE SELECTION, CREATING, UPDATE OR DELETION OF A 'EVENT'*/
    /*
    *                           MMMMM M   M MMMMM M   M MMMMM
    *                           M     M   M M     M   M   M
    *                           M     M   M M     MM  M   M
    *                           MMMM   M M  MMM   M M M   M
    *                           M      M M  M     M  MM   M
    *                           M       M   M     M   M   M
    *                           MMMMM   M   MMMMM M   M   M
    * */

    public Event createEvent(String eventType, Date startDateTime, int placeID, int homeTeamID, int awayTeamID){

        try{
            open_db();
            ContentValues values = new ContentValues();
            values.putNull(MySQLiteHelper.COLUMN_EVENT_ID);
            values.put(MySQLiteHelper.COLUMN_EVENT_TYPE, eventType);
            values.put(MySQLiteHelper.COLUMN_START_DATE_TIME, startDateTime.toString());
            values.put(MySQLiteHelper.COLUMN_FK_EVENT_PLACE_ID, placeID);
            values.put(MySQLiteHelper.COLUMN_FK_EVENT_HOME_TEAM_ID, homeTeamID);

            if(eventType.equals("GAME")) {
                values.put(MySQLiteHelper.COLUMN_FK_EVENT_AWAY_TEAM_ID, awayTeamID);

            } else {
                values.putNull(MySQLiteHelper.COLUMN_FK_EVENT_AWAY_TEAM_ID);
            }

            long insertID = db.insert(MySQLiteHelper.TABLE_EVENT, null, values);
            Cursor cursor = db.query(MySQLiteHelper.TABLE_EVENT, columnsEvent,
                    MySQLiteHelper.COLUMN_EVENT_ID + " = " + insertID, null, null, null, null);
            cursor.moveToFirst();
            Event newEvent = cursorToEvent(cursor);

            if(newEvent != null) {
                ArrayList<Users> attendanceMakingList = new ArrayList<>();
                attendanceMakingList.addAll(getListOfUsersByTeam(homeTeamID));
                if(eventType.equals("GAME")) {
                    attendanceMakingList.addAll(getListOfUsersByTeam(awayTeamID));
                }
                for (Users u : attendanceMakingList) {
                    createAttendance(newEvent.getEventID(), u.getUserID(), "GOING", "");
                }
            }
            queueInsert(newEvent);
            return newEvent;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }
    }

    private Event cursorToEvent(Cursor cursor) {
        Event newEvent = new Event();

        newEvent.setEventID(cursor.getInt(0));
        newEvent.setEventType(cursor.getString(1));
        Calendar cal = new GregorianCalendar();
        Date date = new Date(cursor.getString(2));
        newEvent.setStartDateTime(date);
        newEvent.setPlaceID(cursor.getInt(3));
        newEvent.setHomeTeamID(cursor.getInt(4));
        newEvent.setAwayTeamID(cursor.getInt(5));
        return newEvent;
    }

    public Collection<? extends Event> getListOfEventsByTeam(Team currentTeam) {
        try{
            open_db();
            List<Event> eventsList = new ArrayList<>();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_EVENT, columnsEvent,
                    MySQLiteHelper.COLUMN_FK_EVENT_HOME_TEAM_ID + " = " + currentTeam.getTeamID() + " OR " +
                            MySQLiteHelper.COLUMN_FK_EVENT_AWAY_TEAM_ID + " = " + currentTeam.getTeamID(),
                    null, null, null, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Event tempEvent = cursorToEvent(cursor);
                eventsList.add(tempEvent);
                cursor.moveToNext();
            }
            cursor.close();
            return eventsList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }



    public List<Event> getListOfEventsById(int eventId){
        try{
            open_db();
            List<Event> eventsList = new ArrayList<>();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_EVENT, columnsEvent,
                    MySQLiteHelper.COLUMN_EVENT_ID + " = " + eventId, null, null, null, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Event tempEvent = cursorToEvent(cursor);
                eventsList.add(tempEvent);
                cursor.moveToNext();
            }
            cursor.close();
            return eventsList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }
    }

    public List<Event> getListOfEvents(){
        try{
            open_db();
            List<Event> eventsList = new ArrayList<>();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_EVENT, columnsEvent,
                    null, null, null, null, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Event tempEvent = cursorToEvent(cursor);
                eventsList.add(tempEvent);
                cursor.moveToNext();
            }
            cursor.close();
            return eventsList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    public Event updateEvent(int eventId, String eventType, Date startDateTime, int placeId, int homeTeam, int awayTeam ){
        try{
            open_db();
            ContentValues cv = new ContentValues();
            cv.put(MySQLiteHelper.COLUMN_EVENT_ID, eventId);
            cv.put(MySQLiteHelper.COLUMN_EVENT_TYPE, eventType);
            cv.put(MySQLiteHelper.COLUMN_START_DATE_TIME, startDateTime.toString());
            cv.put(MySQLiteHelper.COLUMN_FK_EVENT_PLACE_ID, placeId);
            cv.put(MySQLiteHelper.COLUMN_FK_EVENT_HOME_TEAM_ID, homeTeam);
            if(eventType.toString().equals("GAME")) {
                cv.put(MySQLiteHelper.COLUMN_FK_EVENT_AWAY_TEAM_ID, awayTeam);
            }else{
                cv.putNull(MySQLiteHelper.COLUMN_FK_EVENT_AWAY_TEAM_ID);
            }

            try {
                db.beginTransaction();
                int recordsChanged = db.update(MySQLiteHelper.TABLE_EVENT, cv,
                        MySQLiteHelper.COLUMN_EVENT_ID + " = ?", new String[]{eventId + ""});
                if(recordsChanged == 1){
                    db.setTransactionSuccessful();
                }else if(recordsChanged < 1) {
                    throw new Exception("No Records Changed");
                }else{
                    db.execSQL("ROLLBACK");
                }
                db.endTransaction();

                ArrayList<Event> eventsList = new ArrayList<>();
                eventsList.addAll(getListOfEventsById(eventId));
                if(eventsList.size() == 1){
                    return eventsList.get(0);
                }else{
                    throw new Exception("The Database is not atomic exception.");
                }
            } catch (Exception e) {
                Log.e("UPDATE EVENTS EXCEPTION", "..........." + e.getMessage());
            }
            return null;
        }catch(SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

 /*ALL METHODS HEREIN DEAL WITH THE SELECTION, CREATING, UPDATE OR DELETION OF A 'PLACE'*/
    /*
    *                   M    MMMMM MMMMM MMMMM N   N MMMM  MMMMM M   M  MMM  MMMMM
    *                  M M     M     M   M     N   M M   M M     M   M M   M M
    *                 M   M    M     M   M     MN  N M   M M     MM  M M     M
    *                 M   M    M     M   MMM   N N M M   M MMM   M M M M     MMM
    *                 MMMMM    M     M   M     M  MM M   M M     M  MM M     M
    *                 M   M    M     M   M     M   M M   M M     M   M M   M M
    *                 M   M    M     M   MMMMM M   M MMMM  MMMMM M   M  MMM  MMMMM
    * */

    public Attendance createAttendance(int eventID, int userID, String status, String message){
        try{
            open_db();
            ContentValues values = new ContentValues();
            values.putNull(MySQLiteHelper.COLUMN_ATTENDANCE_ID);
            values.put(MySQLiteHelper.COLUMN_FK_ATTENDANCE_EVENT_ID, eventID);
            values.put(MySQLiteHelper.COLUMN_FK_ATTENDANCE_USER_ID, userID);
            values.put(MySQLiteHelper.COLUMN_STATUS, status);
            values.put(MySQLiteHelper.COLUMN_MESSAGE, message);

            long insertID = db.insert(MySQLiteHelper.TABLE_ATTENDANCE, null, values);
            Cursor cursor = db.query(MySQLiteHelper.TABLE_ATTENDANCE, columnsAttendance,
                    MySQLiteHelper.COLUMN_ATTENDANCE_ID + " = " + insertID, null, null, null, null);
            cursor.moveToFirst();
            Attendance newAttendance = cursorToAttendance(cursor);
            queueInsert(newAttendance);
            return newAttendance;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }

    }

    private Attendance cursorToAttendance(Cursor cursor) {
        Attendance newAttendance = new Attendance();
        newAttendance.setAttendanceID(cursor.getInt(0));
        newAttendance.setEventID(cursor.getInt(1));
        newAttendance.setUserID(cursor.getInt(2));
        newAttendance.setStatus(cursor.getString(3));
        newAttendance.setMessage(cursor.getString(4));
        return newAttendance;
    }

    public List<Attendance> getListOfAttendances(){
        try{
            open_db();
            List<Attendance> attendanceList = new ArrayList<Attendance>();
            Cursor cursor = db.query(MySQLiteHelper.TABLE_ATTENDANCE, columnsAttendance,
                    null, null, null, null, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Attendance tempAttendance = cursorToAttendance(cursor);
                attendanceList.add(tempAttendance);
                cursor.moveToNext();
            }
            cursor.close();
            return attendanceList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close_db();
        }
    }
}
