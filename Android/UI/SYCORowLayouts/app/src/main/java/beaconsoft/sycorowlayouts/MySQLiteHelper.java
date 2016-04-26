package beaconsoft.sycorowlayouts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by Patrick on 3/8/2016.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_USERS                    =           "users";
    public static final String COLUMN_USER_ID                 =         "user_id";
    public static final String COLUMN_FIRST                   =           "fname";
    public static final String COLUMN_LAST                    =           "lname";
    public static final String COLUMN_PHONE                   =           "phone";
    public static final String COLUMN_EMAIL                   =           "email";
    public static final String COLUMN_EMERGENCY               =       "emergency";
    public static final String COLUMN_USER_TYPE               =       "user_type";
    public static final String COLUMN_PASSWORD                =            "pass";

    public static final String TABLE_SPORT                    =           "sport";
    public static final String COLUMN_SPORT_ID                =        "sport_id";
    public static final String COLUMN_SPORT_NAME              =      "sport_name";

    public static final String TABLE_LEAGUE                   =          "league";
    public static final String COLUMN_LEAGUE_ID               =       "league_id";
    public static final String COLUMN_FK_LEAGUE_USER_ID       =         "user_id";
    public static final String COLUMN_FK_LEAGUE_SPORT_ID      =        "sport_id";
    public static final String COLUMN_LEAGUE_NAME             =     "league_name";
    public static final String COLUMN_MIN_AGE                 =         "min_age";
    public static final String COLUMN_MAX_AGE                 =         "max_age";
    public static final String COLUMN_START_DATE              =      "start_date";
    public static final String COLUMN_END_DATE                =        "end_date";

    public static final String TABLE_TEAM                     =            "team";
    public static final String COLUMN_TEAM_ID                 =         "team_id";
    public static final String COLUMN_FK_TEAM_LEAGUE_ID       =       "league_id";
    public static final String COLUMN_TEAM_NAME               =       "team_name";
    public static final String COLUMN_FK_TEAM_USER_ID         =         "user_id";

    public static final String TABLE_PLAYER                   =          "player";
    public static final String COLUMN_PLAYER_ID               =       "player_id";
    public static final String COLUMN_PLAYER_FIRST            =           "fname";
    public static final String COLUMN_PLAYER_LAST             =           "lname";
    public static final String COLUMN_FK_PLAYER_USER_ID       =         "user_id";

    public static final String TABLE_ENROLLMENT               =      "enrollment";
    public static final String COLUMN_ENROLLMENT_ID           =   "enrollment_id";
    public static final String COLUMN_FK_ENROLLMENT_USER_ID   =         "user_id";
    public static final String COLUMN_FK_ENROLLMENT_PLAYER_ID =       "player_id";
    public static final String COLUMN_FK_ENROLLMENT_LEAGUE_ID =       "league_id";
    public static final String COLUMN_FK_ENROLLMENT_TEAM_ID   =         "team_id";
    public static final String COLUMN_ENROLLMENT_DATE         = "enrollment_date";
    public static final String COLUMN_FEE                     =             "fee";

    public static final String TABLE_PLACE                   =            "place";
    public static final String COLUMN_PLACE_ID               =         "place_id";
    public static final String COLUMN_PLACE_NAME             =       "place_name";
    public static final String COLUMN_STREET_ADDRESS         =   "street_address";
    public static final String COLUMN_CITY                   =             "city";
    public static final String COLUMN_STATE                  =            "state";
    public static final String COLUMN_ZIP                    =              "zip";

    public static final String TABLE_EVENT                   =            "event";
    public static final String COLUMN_EVENT_ID               =         "event_id";
    public static final String COLUMN_EVENT_TYPE             =       "event_type";
    public static final String COLUMN_START_DATE_TIME        =  "start_date_time";
    public static final String COLUMN_FK_EVENT_PLACE_ID      =         "place_id";
    public static final String COLUMN_FK_EVENT_HOME_TEAM_ID  =     "home_team_id";
    public static final String COLUMN_FK_EVENT_AWAY_TEAM_ID  =     "away_team_id";

    public static final String TABLE_ATTENDANCE              =       "attendance";
    public static final String COLUMN_ATTENDANCE_ID          =    "attendance_id";
    public static final String COLUMN_FK_ATTENDANCE_EVENT_ID =         "event_id";
    public static final String COLUMN_FK_ATTENDANCE_USER_ID  =          "user_id";
    public static final String COLUMN_STATUS                 =           "status";
    public static final String COLUMN_MESSAGE                =          "message";


    public static final String[] CREATE_DATABASE = {

            " CREATE TABLE " + TABLE_USERS + " ( " +
                    COLUMN_USER_ID         + " INTEGER PRIMARY KEY , " +
                    COLUMN_FIRST           + " TEXT        ,      " +
                    COLUMN_LAST            + " TEXT        ,      " +
                    COLUMN_PHONE           + " INTEGER     ,      " +
                    COLUMN_EMERGENCY       + " INTEGER     ,      " +
                    COLUMN_EMAIL           + " TEXT        ,      " +
                    COLUMN_USER_TYPE       + " TEXT        ,      " +
                    COLUMN_PASSWORD        + " TEXT ) ; ",

            " CREATE TABLE " + TABLE_SPORT + " ( "                             +
                    COLUMN_SPORT_ID   + " INTEGER PRIMARY KEY       ,        " +
                    COLUMN_SPORT_NAME + " TEXT ) ; ",

            " CREATE TABLE " + TABLE_LEAGUE + " ( "                            +
                    COLUMN_LEAGUE_ID              + " INTEGER PRIMARY KEY ,  " +
                    COLUMN_FK_LEAGUE_USER_ID      + " INTEGER       ,        " +
                    COLUMN_LEAGUE_NAME            + " TEXT          ,        " +
                    COLUMN_FK_LEAGUE_SPORT_ID     + " INTEGER       ,        " +
                    COLUMN_MIN_AGE                + " INTEGER       ,        " +
                    COLUMN_MAX_AGE                + " INTEGER       ,        " +
                    COLUMN_START_DATE             + " TEXT          ,        " +
                    COLUMN_END_DATE               + " TEXT          ,        " +
                    " FOREIGN KEY( " + COLUMN_FK_LEAGUE_USER_ID + " ) REFERENCES " +
                        TABLE_USERS + " ( " + COLUMN_USER_ID + " ) ) ; ",

            " CREATE TABLE " + TABLE_TEAM + " ( " +
                    COLUMN_TEAM_ID                + " INTEGER PRIMARY KEY , " +
                    COLUMN_FK_TEAM_LEAGUE_ID      + " INTEGER     ,  " +
                    COLUMN_TEAM_NAME              + " TEXT        ,  " +
                    COLUMN_FK_TEAM_USER_ID        + " INTEGER     ,  " +
                    " FOREIGN KEY ( " + COLUMN_FK_TEAM_LEAGUE_ID + " ) REFERENCES " +
                        TABLE_LEAGUE + " ("+ COLUMN_LEAGUE_ID +" ), " +
                    " FOREIGN KEY ( " + COLUMN_FK_TEAM_USER_ID + " ) REFERENCES " +
                        TABLE_USERS  + " ( " + COLUMN_USER_ID + " ) ) ; ",

            " CREATE TABLE " + TABLE_PLAYER + " ( " +
                    COLUMN_PLAYER_ID            + " INTEGER PRIMARY KEY , " +
                    COLUMN_PLAYER_FIRST         + " TEXT       ,       " +
                    COLUMN_PLAYER_LAST          + " TEXT       ,       " +
                    COLUMN_FK_PLAYER_USER_ID    + " INTEGER    ,       " +
                    " FOREIGN KEY ( " + COLUMN_FK_PLAYER_USER_ID + " ) REFERENCES " +
                    TABLE_USERS + " ( "+ COLUMN_USER_ID + " ) ) ; ",


            " CREATE TABLE " + TABLE_ENROLLMENT + " ( " +
                    COLUMN_ENROLLMENT_ID             + " INTEGER PRIMARY KEY   ,  " +
                    COLUMN_FK_ENROLLMENT_USER_ID     + " INTEGER  ,               " +
                    COLUMN_FK_ENROLLMENT_PLAYER_ID   + " INTEGER  ,               " +
                    COLUMN_FK_ENROLLMENT_TEAM_ID     + " INTEGER  ,               " +
                    COLUMN_FK_ENROLLMENT_LEAGUE_ID   + " INTEGER  ,               " +
                    COLUMN_ENROLLMENT_DATE           + " TEXT     ,               " +
                    COLUMN_FEE                       + " REAL     ,               " +
                    " FOREIGN KEY ( " + COLUMN_FK_ENROLLMENT_USER_ID + " ) REFERENCES " +
                        TABLE_USERS + " ( " + COLUMN_USER_ID + " ) , " +
                    " FOREIGN KEY ( " + COLUMN_FK_ENROLLMENT_PLAYER_ID + " ) REFERENCES " +
                        TABLE_PLAYER + " ( " + COLUMN_PLAYER_ID + " ) , " +
                    " FOREIGN KEY ( " + COLUMN_FK_ENROLLMENT_TEAM_ID + " ) REFERENCES " +
                        TABLE_TEAM + " ( " + COLUMN_FK_ENROLLMENT_TEAM_ID + " ) , " +
                    " FOREIGN KEY ( " + COLUMN_FK_ENROLLMENT_LEAGUE_ID + " ) REFERENCES " +
                        TABLE_LEAGUE + " ( " + COLUMN_LEAGUE_ID + " ) ) ; ",

            " CREATE TABLE " + TABLE_PLACE + " ( " +
                    COLUMN_PLACE_ID                + " INTEGER PRIMARY KEY , " +
                    COLUMN_PLACE_NAME              + " TEXT     ,   " +
                    COLUMN_STREET_ADDRESS          + " TEXT     ,   " +
                    COLUMN_CITY                    + " TEXT     ,   " +
                    COLUMN_STATE                   + " TEXT     ,   " +
                    COLUMN_ZIP                     + " INTEGER ) ;  ",


             " CREATE TABLE " + TABLE_EVENT + " ( " +
                    COLUMN_EVENT_ID                + " INTEGER PRIMARY KEY ,  " +
                    COLUMN_EVENT_TYPE              + " TEXT    ,  " +
                    COLUMN_START_DATE_TIME         + " TEXT    ,  " +
                    COLUMN_FK_EVENT_PLACE_ID       + " INTEGER ,  " +
                    COLUMN_FK_EVENT_HOME_TEAM_ID   + " INTEGER ,  " +
                    COLUMN_FK_EVENT_AWAY_TEAM_ID   + " INTEGER ,  " +
                    " FOREIGN KEY ( " + COLUMN_FK_EVENT_PLACE_ID + " ) REFERENCES " +
                    TABLE_PLACE + " ( " + COLUMN_PLACE_ID + " ) ) ; ",

             " CREATE TABLE " + TABLE_ATTENDANCE + " ( " +
                    COLUMN_ATTENDANCE_ID                + " INTEGER PRIMARY KEY , " +
                    COLUMN_FK_ATTENDANCE_EVENT_ID       + " INTEGER ,  " +
                    COLUMN_FK_ATTENDANCE_USER_ID        + " INTEGER ,  " +
                    COLUMN_STATUS                       + " TEXT    ,  " +
                    COLUMN_MESSAGE                      + " TEXT    ,  " +
                    " FOREIGN KEY ( " + COLUMN_FK_ATTENDANCE_EVENT_ID + " ) REFERENCES " +
                        TABLE_EVENT + " ( " + COLUMN_EVENT_ID + " ) , " +
                    " FOREIGN KEY ( " + COLUMN_FK_ATTENDANCE_USER_ID + " ) REFERENCES " +
                    TABLE_USERS + " ( " + COLUMN_USER_ID + " ) ) ; "};

    public static MySQLiteHelper sInstance;

    public static synchronized MySQLiteHelper getInstance(Context context){
        if(sInstance == null){
            sInstance = new MySQLiteHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    private static final String DATABASE_NAME = "baconator.db";
    private static final int DATABASE_VERSION = 1;



    private MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        /*
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(json != null){
            Log.e(MySQLiteHelper.class.getName(), json);
        }
*/
        db.beginTransaction();
        for(String q : CREATE_DATABASE){
            db.execSQL(q);
        }

        /*
        for(String q : DatabaseDummyLoader.boom){
            db.execSQL(q);
        }
        */
        db.setTransactionSuccessful();
        db.endTransaction();

    }

    public void onOpen(SQLiteDatabase db){
        Log.w(MySQLiteHelper.class.getName(), "The Database appears to be open...version: " + db.getVersion());
    }

    public void killAndRemake(SQLiteDatabase db) {
        db.beginTransaction();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENROLLMENT   + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE   + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT        + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACE        + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER       + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM         + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS        + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEAGUE       + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPORT        + ";");

        for(String q : CREATE_DATABASE){
            db.execSQL(q);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
            "Upgrading... Old Version: " + oldVersion + "\n" +
                    "New Version: " + newVersion + ", which destroys all old data...");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENROLLMENT   + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE   + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT        + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACE        + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER       + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM         + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS        + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEAGUE       + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPORT        + ";");
        onCreate(db);
    }
}
