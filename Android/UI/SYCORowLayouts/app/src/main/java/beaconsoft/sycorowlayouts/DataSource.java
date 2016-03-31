package beaconsoft.sycorowlayouts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

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
 * Created by Patrick on 3/8/2016.
 */
public class DataSource {

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

    public DataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
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
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_SPORT_NAME, sportName);
        long insertID = db.insert(MySQLiteHelper.TABLE_SPORT, null, values);
        Cursor cursor = db.query(MySQLiteHelper.TABLE_SPORT, columnsSport,
                MySQLiteHelper.COLUMN_SPORT_ID + " = " + insertID, null, null, null, null);
        cursor.moveToFirst();
        Sport newSport = cursorToSport(cursor);
        return newSport;
    }

    private Sport cursorToSport(Cursor cursor) {
        Sport newSport = new Sport();
        newSport.setSportID(cursor.getInt(0));
        newSport.setSportName(cursor.getString(1));
        return newSport;
    }

    public Collection<? extends Sport> getListOfSportsByAdmin(int currentAdminId) {
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
    }

    public boolean checkForDuplicateSports(String tempName) {
        List<Sport> sportsList = new ArrayList<>();
        Cursor cursor = db.query(MySQLiteHelper.TABLE_SPORT, columnsSport,
                MySQLiteHelper.COLUMN_SPORT_NAME + " = '" + tempName + "';", null, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }



    public List<Sport> getListOfSports(){
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
        return newUser;
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

    public Users updateUser(int currentUser, String first, String last, Long phone, String email, Long emergency) {
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
            return getUserById(currentUser);
        }else{
            db.execSQL("ROLLBACK");
            db.endTransaction();
            return null;
        }
    }

    public Users getUserById(int currentUser) {
        Cursor cursor = db.query(MySQLiteHelper.TABLE_USERS, columnsUsers,
                MySQLiteHelper.COLUMN_USER_ID + " = " + currentUser,
                null, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount() == 1) {
            return cursorToUser(cursor);
        }else {
            return null;
        }
    }

    public boolean checkForUserByEmail(String email){
        Cursor cursor = db.query(MySQLiteHelper.TABLE_USERS, columnsUsers,
                MySQLiteHelper.COLUMN_EMAIL + " = '" + email.toUpperCase() + "';",
                null,null,null,null);
        cursor.moveToFirst();
        if(cursor.getCount() == 1){
            return true;
        }else{
            return false;
        }
    }

    public List<Users> getListOfUsers(){
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
    }

    public Users getUserByEmail(String email){
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
    }

    public List<Users> getListOfUsersAvailableToCoach(int currentLeague){
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
    }

    public List<Users> getListOfUsersByTeam(int teamID){
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
        return newLeague;
    }

    private League cursorToLeague(Cursor cursor) {
        League newLeague = new League();
        newLeague.setLeagueID(cursor.getInt(0));
        newLeague.setUserID(cursor.getInt(1));
        newLeague.setSportID(cursor.getInt(2));
        newLeague.setLeagueName(cursor.getString(3));
        newLeague.setMinAge(cursor.getInt(4));
        newLeague.setMaxAge(cursor.getInt(5));
        newLeague.setStartDate(new Date(cursor.getString(6)));
        newLeague.setEndDate(new Date(cursor.getString(7)));
        return newLeague;
    }

    public League getLeagueById(int currentLeague) {
        Cursor cursor = db.query(MySQLiteHelper.TABLE_LEAGUE, columnsLeague,
                MySQLiteHelper.COLUMN_LEAGUE_ID + " = " + currentLeague,
                null, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            return cursorToLeague(cursor);
        }else{
            return null;
        }
    }

    public Collection<? extends League> getListOfLeaguesByAdminIdAndSport(int currentAdminId, int currentSport) {
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
    }

    public List<League> getListOfLeagues(){
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
    }

    public List<League> getListOfLeaguesBySport(int currentSport){
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
        return newTeam;
    }

    public Team getTeamById(int currentTeam) {
        Cursor cursor = db.query(MySQLiteHelper.TABLE_TEAM, columnsTeam,
                MySQLiteHelper.COLUMN_TEAM_ID + " = " + currentTeam,
                null, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount() == 1){
            return cursorToTeam(cursor);
        }else{
            return null;
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

    public List<Team> getListOfTeams(){
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
    }

    public List<Team> getListOfTeamsCoachedByUser(int currentUser, int currentLeague){
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
    }

    public List<Team> getListOfTeamsByLeague(int currentLeague){
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
        return newPlayer;
    }

    public Player updatePlayer(int currentPlayerId, String fname, String lname, int currentUserId){
        ContentValues cv = new ContentValues();
        cv.put("player_id", currentPlayerId);
        cv.put("fname", fname);
        cv.put("lname", lname);
        cv.put("user_id", currentUserId);

        int rowsAltered = db.update(MySQLiteHelper.TABLE_PLAYER, cv,
                MySQLiteHelper.COLUMN_PLAYER_ID + " = " + currentPlayerId, null);
        if(rowsAltered == 1) {
            return getPlayerById(currentPlayerId);
        }else
        {
            return null;
        }
    }

    public Collection<? extends Player> getListOfPlayersByTeam(int currentTeam) {
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
    }

    public Player getPlayerById(int currentPlayer) {
        Cursor cursor = db.query(MySQLiteHelper.TABLE_PLAYER, columnsPlayer,
                MySQLiteHelper.COLUMN_PLAYER_ID + " = " + currentPlayer,
                null, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount() == 1){
            return cursorToPlayer(cursor);
        }else{
            return null;
        }
    }

    public boolean checkForPlayerByFirstLastAndUserId(String first, String last, int userID){
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
    }

    public Player getPlayerByFirstLastAndUserId(String first, String last, int userID) {
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
        return newEnrollment;
    }

    private Enrollment cursorToEnrollment(Cursor cursor) {
        Enrollment newEnrollment = new Enrollment();
        newEnrollment.setEnrollmentID(cursor.getInt(0));
        newEnrollment.setUserID(cursor.getInt(1));
        newEnrollment.setPlayerID(cursor.getInt(2));
        newEnrollment.setLeagueID(cursor.getInt(3));
        newEnrollment.setTeamID(cursor.getInt(4));
        newEnrollment.setEnrollmentDate(new Date(cursor.getString(5)));
        newEnrollment.setFee(cursor.getDouble(6));
        return newEnrollment;
    }

    public Enrollment getEnrollmentByPlayerUserLeagueAndTeam(int playerID, int userID, int currentLeague, int currentTeam) {
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
    }

    public List<Enrollment> getListOfEnrollments(){
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
        return newPlace;
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
        Cursor cursor = db.query(MySQLiteHelper.TABLE_PLACE, columnsPlace,
                MySQLiteHelper.COLUMN_PLACE_ID + " = " + placeID, null, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount() == 1){
            return cursorToPlace(cursor);
        }else{
            return null;
        }
    }

    public List<Place> getListOfPlaces(){
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
        return newEvent;
    }

    private Event cursorToEvent(Cursor cursor) {
        Event newEvent = new Event();

        newEvent.setEventID(cursor.getInt(0));
        newEvent.setEventType(cursor.getString(1));

        /* What could go wrong? */
        SimpleDateFormat format = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss");
        Date date = new Date();
        try {
            date = format.parse(cursor.getString(2));
        } catch (ParseException e) {
            Log.e("DATE EXCEPTION THROWN", "..................DATE DATE DATE EXC EXC EXC" + e.getMessage());
        }
        newEvent.setStartDateTime(date);

        newEvent.setPlaceID(cursor.getInt(3));
        newEvent.setHomeTeamID(cursor.getInt(4));
        newEvent.setAwayTeamID(cursor.getInt(5));
        return newEvent;
    }

    public Collection<? extends Event> getListOfEventsByHomeTeam(Team currentHomeTeam) {
        List<Event> eventsList = new ArrayList<>();
        Cursor cursor = db.query(MySQLiteHelper.TABLE_EVENT, columnsEvent,
                MySQLiteHelper.COLUMN_FK_EVENT_HOME_TEAM_ID + " = " + currentHomeTeam.getTeamID(),
                null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Event tempEvent = cursorToEvent(cursor);
            eventsList.add(tempEvent);
            cursor.moveToNext();
        }
        cursor.close();
        return eventsList;
    }

    public List<Event> getListOfEventsById(int eventId){
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
    }

    public List<Event> getListOfEvents(){
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
        return newAttendance;
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
    }


}
