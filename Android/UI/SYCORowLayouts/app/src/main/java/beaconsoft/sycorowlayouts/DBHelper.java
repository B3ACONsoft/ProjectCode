package beaconsoft.sycorowlayouts;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Patrick on 2/20/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context applicationContext){
        super(applicationContext, "user.db", null, 1);
    }

    public void onCreate(SQLiteDatabase database){

        String query = "IF EXISTS DROP TABLE users";
        database.execSQL(query);

        query = "CREATE TABLE users ( user_id TEXT, fname TEXT, lname TEXT, phone TEXT, emergency TEXT, email TEXT, user_type TEXT);";
        database.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS users";
        db.execSQL(query);
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
