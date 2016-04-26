package beaconsoft.sycorowlayouts.SYCOServerAccess;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.StringReader;
import java.sql.SQLException;
import java.sql.Date;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;

import beaconsoft.sycorowlayouts.MySQLiteHelper;

/**
 * Created by Dave on 4/25/2016.
 */
public class SyncHelper  {
    private RemoteConnection remoteConnection;
    private SQLiteDatabase db;
    private MySQLiteHelper dbHelper;
    private RemoteSelectOperations selectOperations;
    private UpdateService updateServiceRef;

    public SyncHelper(MySQLiteHelper dbHelper, UpdateService updateServiceRef){
        this.dbHelper = dbHelper;
        this.remoteConnection = new RemoteConnection();
        this.selectOperations = new RemoteSelectOperations(remoteConnection);
        this.updateServiceRef = updateServiceRef;
    }

    public void sync() {

        //syncUsers(selectOperations.getListOfUsers());
        //syncPlayers(selectOperations.getListOfPlayers());
        //syncSports(selectOperations.getListOfSports());
        //syncLeagues(selectOperations.getListOfLeagues());
        //syncTeams(selectOperations.getListOfTeams());
        syncEnrollment(selectOperations.getListOfEnrollments());
        syncPlaces(selectOperations.getListOfPlaces());
        syncAttendance(selectOperations.getListOfAttendances());

    }

   private JsonArray getValidJSON(String json){
       StringReader stringReader = new StringReader(json);
       JsonReader jsonReader = null;
       JsonArray jsonArray = null;
       jsonReader = Json.createReader(stringReader);
       return (JsonArray) jsonReader.readArray();
   }

    private String stripPhoneNumber(String phone) {
        StringBuilder phoneBuilder = new StringBuilder();
        for(int i = 0; i < phone.length(); i++) {
            if(Character.isDigit(phone.charAt(i))){
                phoneBuilder.append(phone.charAt(i));
            }
        }
        return  phoneBuilder.toString();
    }

    private void syncUsers(String json){

        String new_fname;
        String new_lname;
        long new_phone;
        long new_emergency;
        String new_email;
        String new_user_type;
        String new_password;

        JsonArray jsonArray = getValidJSON(json);
        for(int i = 0; i < jsonArray.size(); i++) {
            new_fname = ((JsonObject) jsonArray.get(i)).getString("fname");
            new_lname = ((JsonObject) jsonArray.get(i)).getString("lname");
            new_phone = Long.parseLong(stripPhoneNumber(((JsonObject) jsonArray.get(i)).getString("phone")));
            new_emergency = Long.parseLong(stripPhoneNumber(((JsonObject) jsonArray.get(i)).getString("emergency")));
            new_email = ((JsonObject) jsonArray.get(i)).getString("email");
            new_user_type = ((JsonObject) jsonArray.get(i)).getString("user_type");
            new_password  = ((JsonObject) jsonArray.get(i)).getString("password");

            updateServiceRef.createUsers(new_fname, new_lname, new_phone , new_email, new_emergency, new_user_type, new_password);
        }
    }

    private void syncPlayers(String json) {
        String new_player_first;
        String new_player_last;
        int new_user_id;

        JsonArray jsonArray = getValidJSON(json);
        for(int i = 0; i < jsonArray.size(); i++) {

            new_player_first = ((JsonObject) jsonArray.get(i)).getString("fname");
            new_player_last  = ((JsonObject) jsonArray.get(i)).getString("lname");
            new_user_id      = Integer.parseInt(((JsonObject) jsonArray.get(i)).getString("user_id"));

            updateServiceRef.createPlayer(new_player_first, new_player_first, new_user_id);
        }
    }

    private void syncSports(String json) {
        String new_sport_name;

        JsonArray jsonArray = getValidJSON(json);
        for(int i = 0; i < jsonArray.size(); i++) {
            new_sport_name = ((JsonObject) jsonArray.get(i)).getString("sport_name");

            updateServiceRef.createSport(new_sport_name);
        }
    }

    /*
        wow
     */
    private long dateToLong(String badDate) {
        StringBuilder goodDate = new StringBuilder();
        for(int i = 0; i < badDate.length(); i++) {
            if(Character.isDigit(badDate.charAt(i))){
                goodDate.append(badDate.charAt(i));
            }
        }

        return Long.parseLong(goodDate.toString());
    }

    private void syncLeagues(String json) {
        int new_user_id = 13;
        int new_sport_id;
        String leagueName;
        int minAge;
        int maxAge;
        Date startDate;
        Date endDate;


        JsonArray jsonArray = getValidJSON(json);
        for(int i = 0; i < jsonArray.size(); i++) {
            //new_user_id      = Integer.parseInt(((JsonObject) jsonArray.get(i)).getString("user_id"));
            new_sport_id     = Integer.parseInt(((JsonObject) jsonArray.get(i)).getString("sport_id"));
            leagueName       =  ((JsonObject) jsonArray.get(i)).getString("league_name");
            minAge           = Integer.parseInt(((JsonObject) jsonArray.get(i)).getString("min_age"));
            maxAge           = Integer.parseInt(((JsonObject) jsonArray.get(i)).getString("max_age"));
            startDate        = new Date(dateToLong(((JsonObject) jsonArray.get(i)).getString("start_date")));
            endDate          = new Date(dateToLong(((JsonObject) jsonArray.get(i)).getString("end_date")));

            updateServiceRef.createLeague(new_user_id, new_sport_id, leagueName, minAge, maxAge, startDate, endDate);
        }

    }

    private void syncTeams(String json) {
        String teamName;
        int leagueID;
        int userID;

        //{"team_id":"1","0":"1","league_id":"1","1":"1","team_name":"YETIS","2":"YETIS","user_id":"15","3":"15"}
        JsonArray jsonArray = getValidJSON(json);
        for(int i = 0; i < jsonArray.size(); i++) {
            userID          = Integer.parseInt(((JsonObject) jsonArray.get(i)).getString("user_id"));
            leagueID        = Integer.parseInt(((JsonObject) jsonArray.get(i)).getString("league_id"));
            teamName        =  ((JsonObject) jsonArray.get(i)).getString("team_name");

            updateServiceRef.createTeam(teamName, leagueID, userID);
        }

    }

    private void syncEnrollment(String json) {
        int userID;
        int playerID;
        int leagueID;
        int teamID;
        Date enrollmentDate;
        Double fee;

        JsonArray jsonArray = getValidJSON(json);
        for(int i = 0; i < jsonArray.size(); i++) {
            userID              = Integer.parseInt(((JsonObject) jsonArray.get(i)).getString("user_id"));
            playerID            = Integer.parseInt(((JsonObject) jsonArray.get(i)).getString("player_id"));
            leagueID            = Integer.parseInt(((JsonObject) jsonArray.get(i)).getString("league_id"));
            teamID              =  Integer.parseInt(((JsonObject) jsonArray.get(i)).getString("team_id"));
            enrollmentDate      = new Date(dateToLong(((JsonObject) jsonArray.get(i)).getString("enrollment_date")));
            fee                 = Double.parseDouble(((JsonObject) jsonArray.get(i)).getString("fee"));

            updateServiceRef.createEnrollment(userID, playerID, leagueID, teamID, enrollmentDate, fee);
        }
    }

    private void syncPlaces(String json) {
        String placeName;
        String streetAddress;
        String city;
        String state;
        int zip;

        JsonArray jsonArray = getValidJSON(json);
        for(int i = 0; i < jsonArray.size(); i++) {
            placeName        =  ((JsonObject) jsonArray.get(i)).getString("place_name");
            streetAddress    =  ((JsonObject) jsonArray.get(i)).getString("street_address");
            city             =  ((JsonObject) jsonArray.get(i)).getString("city");
            state            =  ((JsonObject) jsonArray.get(i)).getString("state");
            zip              = Integer.parseInt(((JsonObject) jsonArray.get(i)).getString("zip"));
            updateServiceRef.createPlace(placeName, streetAddress, city, state, zip);
        }

    }

    private void syncAttendance(String json) {

        JsonArray jsonArray = getValidJSON(json);
    }

}
