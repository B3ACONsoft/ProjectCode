package SYCOServerAccess;

import java.util.HashMap;

/**
 * This final class contains final classes which contain HashMaps
 * which define the contract that various commands must abide by.
 * <br>
 * The ordering is:
 * 		testSQLCommandMaps.<CommandCategory>.<CommandName>
 * 
 * @author David Johnson
 *
 */
public final class SQLCommandMaps {

	public static final class SelectAllCommandMaps {
		
		public static final HashMap<String, String> adminGetsAllSports;
		static {
			adminGetsAllSports = new HashMap<String, String>(); 
			{
				adminGetsAllSports.put("user_id", "1");
				adminGetsAllSports.put("password", "PASSWORD");
				adminGetsAllSports.put("command", "SELECT");
				adminGetsAllSports.put("table", "SPORTS");
			}
		};
		
		public static final HashMap<String, String> adminGetsAllLeagues;
		static {
			adminGetsAllLeagues = new HashMap<String, String>(); 
			{
				adminGetsAllLeagues.put("user_id", "1");
				adminGetsAllLeagues.put("password", "PASSWORD");
				adminGetsAllLeagues.put("command", "SELECT");
				adminGetsAllLeagues.put("table", "LEAGUE");
			}
		};
		
		public static final HashMap<String, String> adminGetsAllUsers;
		static {
			adminGetsAllUsers = new HashMap<String, String>(); 
			{
				adminGetsAllUsers.put("user_id", "1");
				adminGetsAllUsers.put("password", "PASSWORD");
				adminGetsAllUsers.put("command", "SELECT");
				adminGetsAllUsers.put("table", "USERS");
			}
		};
		
		
		public static final HashMap<String, String> adminGetsAllTeams;
		static {
			adminGetsAllTeams = new HashMap<String, String>(); 
			{
				adminGetsAllTeams.put("user_id", "1");
				adminGetsAllTeams.put("password", "PASSWORD");
				adminGetsAllTeams.put("command", "SELECT");
				adminGetsAllTeams.put("table", "TEAM");
			}
		};
		
		public static final HashMap<String, String> adminGetsAllEnrollment;
		static {
			adminGetsAllEnrollment = new HashMap<String, String>(); 
			{
				adminGetsAllEnrollment.put("user_id", "1");
				adminGetsAllEnrollment.put("password", "PASSWORD");
				adminGetsAllEnrollment.put("command", "SELECT");
				adminGetsAllEnrollment.put("table", "ENROLLMENT");
			}
		};
		
		public static final HashMap<String, String> adminGetsAllPlaces;
		static {
			adminGetsAllPlaces = new HashMap<String, String>(); 
			{
				adminGetsAllPlaces.put("user_id", "1");
				adminGetsAllPlaces.put("password", "PASSWORD");
				adminGetsAllPlaces.put("command", "SELECT");
				adminGetsAllPlaces.put("table", "PLACE");
			}
		};
		
		public static final HashMap<String, String> adminGetsAllEvents;
		static {
			adminGetsAllEvents = new HashMap<String, String>(); 
			{
				adminGetsAllEvents.put("user_id", "1");
				adminGetsAllEvents.put("password", "PASSWORD");
				adminGetsAllEvents.put("command", "SELECT");
				adminGetsAllEvents.put("table", "EVENTS");
			}
		};
		
		
		

	}

    public static final class InsertCommandMaps {
        public static final HashMap<String, String> adminCreateSport;
        static {
            adminCreateSport = new HashMap<String, String>();
            {
                adminCreateSport.put("user_id", "1");
                adminCreateSport.put("password", "PASSWORD");
                adminCreateSport.put("command", "INSERT");
                adminCreateSport.put("table_name", "SPORTS");
                adminCreateSport.put("sport_name", "");
                //adminCreateSport.put("last_edited", "");
            }
        };

        public static final HashMap<String, String> adminCreateLeague;
        static {
            adminCreateLeague = new HashMap<String, String>();
            {
                adminCreateLeague.put("user_id", "1");
                adminCreateLeague.put("password", "PASSWORD");
                adminCreateLeague.put("command", "INSERT");
                adminCreateLeague.put("table_name", "LEAGUE");
                adminCreateLeague.put("league_name", "");
                adminCreateLeague.put("sport_id", "");
                adminCreateLeague.put("min_age", "");
                adminCreateLeague.put("max_age", "");
                adminCreateLeague.put("start_date", "");
                adminCreateLeague.put("end_date", "");
                //adminCreateLeague.put("last_edited", "");
            }
        };

        public static final HashMap<String, String> adminCreateUser;
        static {
            adminCreateUser = new HashMap<String, String>();
            {
                adminCreateUser.put("user_id", "1");
                adminCreateUser.put("password", "PASSWORD");
                adminCreateUser.put("command", "INSERT");
                adminCreateUser.put("table_name", "USERS");
                adminCreateUser.put("fname", "");
                adminCreateUser.put("lname", "");
                adminCreateUser.put("phone", "");
                adminCreateUser.put("emergency", "");
                adminCreateUser.put("email", "");
                adminCreateUser.put("user_type", "");
                adminCreateUser.put("password", "");
                //adminCreateUser.put("last_edited", "");
            }
        };

        public static final HashMap<String, String> adminCreateTeam;
        static {
            adminCreateTeam = new HashMap<String, String>();
            {
                adminCreateTeam.put("user_id", "1");
                adminCreateTeam.put("password", "PASSWORD");
                adminCreateTeam.put("command", "INSERT");
                adminCreateTeam.put("table_name", "TEAM");
                adminCreateTeam.put("league_id", "");
                adminCreateTeam.put("team_name", "");
                adminCreateTeam.put("user_id", "");
                //adminCreateTeam.put("last_edited", "");
            }
        };

        public static final HashMap<String, String> adminCreateEnrollment;
        static {
            adminCreateEnrollment = new HashMap<String, String>();
            {
                adminCreateEnrollment.put("user_id", "1");
                adminCreateEnrollment.put("password", "PASSWORD");
                adminCreateEnrollment.put("command", "INSERT");
                adminCreateEnrollment.put("table_name", "ENROLLMENT");
                adminCreateEnrollment.put("user_id", "");
                adminCreateEnrollment.put("player_id", "");
                adminCreateEnrollment.put("team_id", "");
                adminCreateEnrollment.put("league_id", "");
                adminCreateEnrollment.put("enrollment_date", "");
                adminCreateEnrollment.put("fee", "");
                //adminCreateEnrollment.put("last_edited", "");
            }
        };

        public static final HashMap<String, String> adminCreatePlace;
        static {
            adminCreatePlace = new HashMap<String, String>();
            {
                adminCreatePlace.put("user_id", "1");
                adminCreatePlace.put("password", "PASSWORD");
                adminCreatePlace.put("command", "INSERT");
                adminCreatePlace.put("table_name", "PLACE");
                adminCreatePlace.put("place_name", "");
                adminCreatePlace.put("street_address", "");
                adminCreatePlace.put("city", "");
                adminCreatePlace.put("state", "");
                adminCreatePlace.put("zip", "");
                //adminCreatePlace.put("last_edited", "");
            }
        };

        public static final HashMap<String, String> adminCreateEvent;
        static {
            adminCreateEvent = new HashMap<String, String>();
            {
                adminCreateEvent.put("user_id", "1");
                adminCreateEvent.put("password", "PASSWORD");
                adminCreateEvent.put("command", "INSERT");
                adminCreateEvent.put("table_name", "EVENTS");
                adminCreateEvent.put("event_type", "");
                adminCreateEvent.put("start_date_time", "");
                adminCreateEvent.put("place_id", "");
                adminCreateEvent.put("home_team_id", "");
                adminCreateEvent.put("away_team_id", "");
                //adminCreateEvent.put("last_edited", "");
            }
        };
    }
	public static final class UpdateCommandMaps {

    }

    public static final class DeleteCommandMaps {

    }
}
