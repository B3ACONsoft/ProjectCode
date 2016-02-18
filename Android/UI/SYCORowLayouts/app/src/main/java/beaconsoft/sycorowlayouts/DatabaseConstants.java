package beaconsoft.sycorowlayouts;

import android.provider.BaseColumns;

/**
 * Created by AnalEmbargo on 2/17/2016. Someone's screwy. That's for sure. BACON!
 */
public final class DatabaseConstants {

    //Keep someone from accidentally instantiating the class?
    public DatabaseConstants() {}

    //user table
    public static abstract class USER implements BaseColumns {
        public static final String TABLE_NAME = "USER";
        public static final String FIELD_ID = "USER_ID";                      //AUTOGEN PRIMARY KEY
        public static final String FIELD_FIRST_NAME = "FIRST_NAME";
        public static final String FIELD_LAST_NAME = "LAST_NAME";
        public static final String FIELD_PHONE = "PHONE";
        public static final String FIELD_EMAIL = "EMAIL_ADDRESS";
        public static final String FIELD_PASSWORD = "PASSWORD";
        public static final String FIELD_USER_TYPE = "USER_TYPE";           //CAN BE EITHER PARENT, COACH, ADMIN
        public static final String FIELD_CREATED_DATE = "DATE_CREATED";     //USED FOR EXPIRATION PURPOSES
    }

    //coaches could be a view based on user table
    //players/ parents could be a view based on user table
    //admin table could be a view based on user table

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

    //coach table
    public static abstract class COACH implements BaseColumns {
        public static final String TABLE_NAME = "COACH";
        public static final String FIELD_ID = "ID";
        public static final String FIELD_PHONE = "PHONE";
        public static final String FIELD_EMAIL = "EMAIL";
        public static final String FIELD_PASSWORD = "PASSWORD";
        public static final String FIELD_FIRST_NAME = "FIRST_NAME";
        public static final String FIELD_LAST_NAME = "LAST_NAME";
    }

    public static abstract class GAME_SCHEDULE implements BaseColumns {
        public static final String TABLE_NAME = "GAME_SCHEDULE";
        public static final String FIELD_ID = "ID";
        //need datetime beginning and end

    }
}
