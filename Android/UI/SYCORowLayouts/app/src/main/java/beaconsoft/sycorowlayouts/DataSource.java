package beaconsoft.sycorowlayouts;

import android.database.sqlite.SQLiteDatabase;

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
            MySQLiteHelper.COLUMN_EMAIL,
            MySQLiteHelper.COLUMN_EMERGENCY,
            MySQLiteHelper.COLUMN_USER_TYPE,
            MySQLiteHelper.COLUMN_PASSWORD
    };

}
