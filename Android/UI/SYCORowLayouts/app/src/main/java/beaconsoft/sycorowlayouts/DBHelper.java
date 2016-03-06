package beaconsoft.sycorowlayouts;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Patrick on 2/20/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "bacon";

    static final String ENROLLMENT_TABLE = "enrollment";
    static final String      EVENT_TABLE = "event";
    static final String      PLACE_TABLE = "place";
    static final String ATTENDANCE_TABLE = "attendance";
    static final String     PLAYER_TABLE = "player";
    static final String       TEAM_TABLE = "team";
    static final String      USERS_TABLE = "users";
    static final String     LEAGUE_TABLE = "league";
    static final String      SPORT_TABLE = "sport";

    static final String TAG = "HERE";

    public DBHelper(Context applicationContext){
        super(applicationContext, DB_NAME + ".db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        Log.v(TAG, "BLaaaaaam!!!");

        db.execSQL("DROP TABLE IF EXISTS " + ENROLLMENT_TABLE  + ";");
        db.execSQL("DROP TABLE IF EXISTS " + ATTENDANCE_TABLE  + ";");
        db.execSQL("DROP TABLE IF EXISTS " +      EVENT_TABLE  + ";");
        db.execSQL("DROP TABLE IF EXISTS " +      PLACE_TABLE  + ";");
        db.execSQL("DROP TABLE IF EXISTS " +     PLAYER_TABLE  + ";");
        db.execSQL("DROP TABLE IF EXISTS " +       TEAM_TABLE  + ";");
        db.execSQL("DROP TABLE IF EXISTS " +      USERS_TABLE  + ";");
        db.execSQL("DROP TABLE IF EXISTS " +     LEAGUE_TABLE  + ";");
        db.execSQL("DROP TABLE IF EXISTS " +      SPORT_TABLE  + ";");

        db.execSQL("CREATE TABLE " + SPORT_TABLE + " (" +
                        "  sport_id    INTEGER PRIMARY KEY," +
                        "  sport_name  TEXT);");

        db.execSQL("CREATE TABLE " + LEAGUE_TABLE + " (" +
                        "  league_id   INTEGER," +
                        "  user_id     INTEGER," +
                        "  league_name TEXT," +
                        "  sport_id    INTEGER," +
                        "  min_age     INTEGER," +
                        "  max_age     INTEGER," +
                        "  start_date  TEXT," +
                        "  end_date    TEXT," +
                        "  PRIMARY KEY(league_id)," +
                        "  FOREIGN KEY(user_id) REFERENCES users(user_id));");


        db.execSQL("CREATE TABLE " + TEAM_TABLE + " (" +
                        "  team_id     INTEGER," +
                        "  league_id   INTEGER," +
                        "  team_name   TEXT," +
                        "  user_id     INTEGER," +
                        "  PRIMARY KEY (TEAM_ID)," +
                        "  FOREIGN KEY (league_id) REFERENCES league(league_id)," +
                        "  FOREIGN KEY (user_id) REFERENCES users(user_id));");


        db.execSQL("CREATE TABLE " + USERS_TABLE + " (" +
                        "  user_id   INTEGER PRIMARY KEY," +
                        "  fname     TEXT," +
                        "  lname     TEXT," +
                        "  phone     INTEGER," +
                        "  emergency INTEGER," +
                        "  email     TEXT," +
                        "  user_type TEXT," +
                        "  pass      TEXT," +
                        "  FOREIGN KEY (user_id) REFERENCES users(user_id));");


        db.execSQL("CREATE TABLE " + PLAYER_TABLE + "(" +
                        "  player_id INTEGER PRIMARY KEY," +
                        "  fname     TEXT," +
                        "  lname     TEXT," +
                        "  user_id   INTEGER," +
                        "  FOREIGN KEY(user_id) REFERENCES users(user_id));");

        db.execSQL("CREATE TABLE " + ENROLLMENT_TABLE + " (" +
                        "  enrollment_id   INTEGER PRIMARY KEY," +
                        "  user_id         INTEGER," +
                        "  player_id       INTEGER," +
                        "  team_id         INTEGER," +
                        "  league_id       INTEGER," +
                        "  enrollment_date TEXT," +
                        "  fee             INTEGER," +
                        "  FOREIGN KEY (user_id) REFERENCES users(user_id)," +
                        "  FOREIGN KEY (player_id) REFERENCES player(player_id)," +
                        "  FOREIGN KEY (team_id) REFERENCES team(team_id)," +
                        "  FOREIGN KEY (league_id) REFERENCES league(league_id));");

        db.execSQL("CREATE TABLE " + PLACE_TABLE + "(" +
                        "  place_id       INTEGER PRIMARY KEY," +
                        "  place_name     TEXT," +
                        "  street_address TEXT," +
                        "  city           TEXT," +
                        "  state          TEXT," +
                        "  zip            INTEGER);");


        db.execSQL("CREATE TABLE " + EVENT_TABLE + "(" +
                        "  event_id        INTEGER PRIMARY KEY," +
                        "  event_type      TEXT," +
                        "  start_date_time TEXT," +
                        "  place_id        INTEGER," +
                        "  home_team_id    INTEGER," +
                        "  away_team_id    INTEGER)," +
                        "  FOREIGN KEY (place_id) REFERENCES place(place_id));");

        db.execSQL("CREATE TABLE " + ATTENDANCE_TABLE + "(" +
                        "  attendance_id INTEGER PRIMARY KEY," +
                        "  event_id INTEGER," +
                        "  user_id  INTEGER," +
                        "  status   TEXT," +
                        "  message  TEXT," +
                        "  FOREIGN KEY (event_id) REFERENCES event(event_id)," +
                        "  FOREIGN KEY (user_id) REFERENCES users(user_id));");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }

    public void insertIntoUsers(HashMap<String, String> usersList){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", usersList.get("user_id"));
        values.put("fname", usersList.get("fname"));
        values.put("lname", usersList.get("lname"));
        values.put("phone", usersList.get("phone"));
        values.put("emergency", usersList.get("emergency"));
        values.put("email", usersList.get("email"));
        values.put("user_type", usersList.get("user_type"));
        db.insert("users", null, values);
        db.close();
    }

    // Update User table
    public void updateIntoUsers(HashMap<String, String> usersList){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", usersList.get("user_id"));
        values.put("fname", usersList.get("fname"));
        values.put("lname", usersList.get("lname"));
        values.put("phone", usersList.get("phone"));
        values.put("emergency", usersList.get("emergency"));
        values.put("email", usersList.get("email"));
        values.put("user_type", usersList.get("user_type"));
        db.update("users", values, DatabaseConstants.USER.FIELD_ID+"="+"user_id",null);
        db.close();
    }

    public ArrayList<HashMap<String, String>> getSQLiteUsers(){
        String query = "";

        query = "SELECT * FROM users";


        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<HashMap<String,String>> usersList = new ArrayList<HashMap<String, String>>();
        if(cursor.moveToFirst()){
            do{
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("user_id",   cursor.getString(0));
                hashMap.put("fname",    cursor.getString(1));
                hashMap.put("lname",   cursor.getString(2));
                hashMap.put("phone", cursor.getString(3));
                hashMap.put("emergency", cursor.getString(4));
                hashMap.put("email", cursor.getString(5));
                hashMap.put("user_type", cursor.getString(6));
                usersList.add(hashMap);
            }while(cursor.moveToNext());
        }
        db.close();
        return usersList;
    }
}
