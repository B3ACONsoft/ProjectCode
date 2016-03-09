package beaconsoft.sycorowlayouts;

import android.provider.BaseColumns;

/**
 * Created by Dave on 2/17/2016.
 */
public final class DatabaseConstants {

    //global globals
    public static final String DATABASE_NAME = "bacon.db";
    public static final int DATABASE_VERSION = 1;
    //globals, kind of

    public static final String TEXT_TYPE = "TEXT";
    public static final String DELIM = ",";

    public static abstract class USER implements BaseColumns {
        public static final String TABLE_NAME = "USER";
        public static final String FIELD_ID = "USER_ID";                      //AUTOGEN PRIMARY KEY
        public static final String FIELD_FIRST_NAME = "FIRST_NAME";
        public static final String FIELD_LAST_NAME = "LAST_NAME";
        public static final String FIELD_EMAIL = "EMAIL_ADDRESS";
        public static final String FIELD_PHONE = "PHONE";                   //all users have a phone now
                                                                            //i dunno if logically this is the
                                                                            //correct thing to do
        public static final String FIELD_PASSWORD = "PASSWORD";
        public static final String FIELD_USER_TYPE = "USER_TYPE";           //CAN BE EITHER PARENT, COACH, ADMIN
        public static final String FIELD_CREATED_DATE = "DATE_CREATED";     //USED FOR EXPIRATION PURPOSES
    }

    public static abstract class PLAYER implements BaseColumns {
        public static final String TABLE_NAME = "PLAYER";
        public static final String FIELD_ID = "USER_ID";                    //primary key but not autogen
        public static final String FIELD_DOB = "DOB";
        public static final String FIELD_TEAM_ID = "TEAM_ID";
        public static final String FIELD_POSITION = "POSITION";             //do players need positions?
    }

    //differntiate on table name
    /*
        user)id, position, what else a plyer might have
     */

    /*

       SEASON
            SEASON_ID START_DATE END_DATE

       LEAGUE
            LEAGUE_ID SEASON_ID LEAGUE_NAME

       TEAM
            TEAM_ID TEAM_NAME

       ENROLLMENT
            MEMBER_ID TEAM_ID LEAGUE_ID SEASON_ID USER_ID

       USER
            USER_ID USER_TYPE EMAIL PHONEID

       ADDRESS
            ADDRESS_ID STREETADDRESS CITY ZIPCODE

       PHONE
            PHONE_ID PHONENUMBER

     */


    public static abstract class COACH  implements BaseColumns {
        public static final String TABLE_NAME = "COACH";
        public static final String FIELD_ID = "USER_ID";
        public static final String FIELD_TEAM_ID = "TEAM_ID";               //cant be null, no way
    }

    public static abstract class TEAMS implements BaseColumns {
        public static final String TABLE_NAME = "TEAMS";
        public static final String FIELD_ID = "TEAM_ID";                    //must be autogen
        public static final String FIELD_TEAM_NAME = "TEAM_NAME";
        public static final String FIELD_COACH_ID = "COACH_ID";
    }

    public static abstract class GAME_SCHEDULE implements BaseColumns {
        public static final String TABLE_NAME = "GAME_SCHEDULE";
        public static final String FIELD_ROW_ID = "ID";                     //autogen this
        public static final String FIELD_LOCATION_ID = "FIELD_ID";
        public static final String FIELD_TEAM_ID = "TEAM_ID";
        public static final String MEETING_TIME = "MEETING_TIME";           //do we need both dates, can't we just keep date/time in one string?
        public static final String MEETING_DATE = "MEETING_DATE";
    }

    public static abstract class LOCATION implements BaseColumns {
        public static final String TABLE_NAME = "LOCATION";
        public static final String FIELD_ID = "ID";
        public static final String FIELD_NAME = "NAME";
        public static final String FIELD_STREET = "STREET";
        public static final String FIELD_CITY = "STATE";
        public static final String FIELD_ZIP = "ZIP";
        public static final String FIELD_MAP_LINK = "MAP_LINK";
    }

    /*
    //admin table
    public static abstract class ADMINISTRATION implements BaseColumns {
        public static final String TABLE_NAME = "ADMINISTRATION";
        public static final String FIELD_ID = "ADMIN_ID";
        public static final String FIELD_ADMIN_TYPE = "ADMIN_TYPE";
        public static final String FIELD_EMAIL = "EMAIL_ADDRESS";
        public static final String FIELD_PASSWORD = "PASSWORD";
        public static final String FIELD_FIRST_NAME = "FIRST_NAME";
        public static final String FIELD_LAST_NAME = "LAST_NAME";
    }
     */

    //coach table
    /*
    public static abstract class COACH implements BaseColumns {
        public static final String TABLE_NAME = "COACH";
        public static final String FIELD_ID = "ID";
        public static final String FIELD_PHONE = "PHONE";
        public static final String FIELD_EMAIL = "EMAIL";
        public static final String FIELD_PASSWORD = "PASSWORD";
        public static final String FIELD_FIRST_NAME = "FIRST_NAME";
        public static final String FIELD_LAST_NAME = "LAST_NAME";
    }
     */



    //these are create table statements
    //that i have not tested
    public static abstract class CREATE_DB {

        //run this first?
        public static final String RESET =
                  "DROP TABLE USER;"
                + "DROP TABLE PLAYER;"
                + "DROP TABLE COACH;"
                + "DROP TABLE LOCATION;"
                + "DROP TABLE TEAMS;"
                + "DROP TABLE GAME_SCHEDULE;";

        /*
            So a user has:
                USER_ID, FIRST_NAME, LAST_NAME, USER_TYPE, PASSWORD, EMAIL, DATE_CREATED
         */
        public static final String CREATE_USER_TABLE =
                "CREATE TABLE" + USER.TABLE_NAME + "("
                        + USER.FIELD_ID + "INTEGER PRIMARY KEY AUTOINCREMENT" + DELIM       //expensive, but worth it?
                        + USER.FIELD_FIRST_NAME + TEXT_TYPE + "NOT NULL" + DELIM
                        + USER.FIELD_LAST_NAME + TEXT_TYPE + "NOT NULL" + DELIM
                        + USER.FIELD_USER_TYPE + "INTEGER NOT NULL"  + DELIM
                        + USER.FIELD_PASSWORD  + TEXT_TYPE + "NOT NULL" + DELIM
                        + USER.FIELD_EMAIL + TEXT_TYPE + "NOT NULL" + DELIM
                        + USER.FIELD_CREATED_DATE + "NOT NULL" + TEXT_TYPE                  //sqlite likes this as a string
                        + ")";

        /*
            Player just has an id, which is the user id and phonenumber
            What else?
         */
        public static final String CREATE_PLAYER_TABLE =
                  "CREATE TABLE" + PLAYER.TABLE_NAME + "("
                + PLAYER.FIELD_ID + "INTEGER PRIMARY KEY NOT NULL" + DELIM
                + PLAYER.FIELD_DOB + TEXT_TYPE + "NOT NULL" + DELIM
                + PLAYER.FIELD_TEAM_ID + "INTEGER NOT NULL" + DELIM
                + PLAYER.FIELD_POSITION + TEXT_TYPE + "NOT NULL"
                + ")";


        public static final String CREATE_COACH_TABLE =
                  "CREATE TABLE" + COACH.TABLE_NAME + "("
                + COACH.FIELD_ID +  "INTEGER PRIMARY KEY NOT NULL" + DELIM
                + COACH.FIELD_TEAM_ID + "INTEGER NOT NULL"
                + ")";

        public static final String CREATE_TEAMS_TABLE =
                  "CREATE TABLE" + TEAMS.TABLE_NAME + "("
                + TEAMS.FIELD_ID + "INTEGER PRIMARY KEY AUTOINCREMENT" + DELIM
                + TEAMS.FIELD_TEAM_NAME + TEXT_TYPE + "NOT NULL" + DELIM
                + TEAMS.FIELD_COACH_ID + "INTEGER NOT NULL"
                + ")";

        public static final String CREATE_LOCATION_TABLE =
                  "CREATE TABLE" + LOCATION.TABLE_NAME + "("
                + LOCATION.FIELD_ID + "INTEGER PRIMARY KEY AUTOINCREMENT" + DELIM
                + LOCATION.TABLE_NAME + TEXT_TYPE + "NOT NULL" + DELIM
                + LOCATION.FIELD_STREET + TEXT_TYPE + "NOT NULL" + DELIM
                + LOCATION.FIELD_CITY + TEXT_TYPE + "NOT NULL" + DELIM
                + LOCATION.FIELD_ZIP + TEXT_TYPE + "NOT NULL" + DELIM
                + LOCATION.FIELD_MAP_LINK + TEXT_TYPE + "NOT NULL" + DELIM
                + ")";

        public static final String CREATE_GAME_SCHEDULE_TABLE =
                  "CREATE TABLE" + GAME_SCHEDULE.TABLE_NAME + "("
                + GAME_SCHEDULE.FIELD_ROW_ID + "INTEGER PRIMARY KEY AUTOINCREMENT" + DELIM
                + GAME_SCHEDULE.FIELD_TEAM_ID + "INTEGER NOT NULL" + DELIM
                + GAME_SCHEDULE.FIELD_LOCATION_ID + "INTEGER NOT NULL" + DELIM
                + GAME_SCHEDULE.MEETING_DATE + TEXT_TYPE + "NOT NULL" + DELIM
                + GAME_SCHEDULE.MEETING_TIME + TEXT_TYPE + "NOT NULL" + DELIM
                + ")";
    }


}
