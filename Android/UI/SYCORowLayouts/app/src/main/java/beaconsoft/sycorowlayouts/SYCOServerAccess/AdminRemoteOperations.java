package beaconsoft.sycorowlayouts.SYCOServerAccess;

import beaconsoft.sycorowlayouts.TestRoutines.TestCommands;

/**
 * This class contains methods which send particular post requests to the 
 * server.
 * <br>
 * Inherits from RemoteOperations.
 * 
 * @author David Johnson
 *
 */
public class AdminRemoteOperations extends RemoteOperations {
	
	/**
	 * Constructor.
	 * Calls superclass constructor.
	 */
	public AdminRemoteOperations() {
		super();
	}
	
	
	/**
	 * Create a sport.
	 * <br>
	 * @param sportName 
	 * @return The result of the operation.
	 */
	public String createSport(String sportName) {
		command = TestCommands.TestAdminSync.adminCreateSport;
		String result = "";
		try
		{
			command.put("sport_name", sportName);
			
			result = doOperation();
		} catch(Exception e) {
			return e.getMessage();
		}
		return result;
	}
	
	/**
	 * Create a League.
	 * <br>
	 * @param league_name
	 * @param sport_id
	 * @param min_age
	 * @param max_age
	 * @param start_date
	 * @param end_date
	 * @return The result of the operation.
	 */
	public String createLeague(String league_name,
							   String sport_id,
							   String min_age,
							   String max_age,
							   String start_date,
							   String end_date) {
		
		command = TestCommands.TestAdminSync.adminCreateLeague;
		String result = "";
		try
		{
			command.put("league_name", league_name);
			command.put("sport_id", sport_id);
			command.put("min_age", min_age);
			command.put("max_age", max_age);
			command.put("start_date", start_date);
			command.put("end_date", end_date);
			
			result = doOperation();
		} catch(Exception e) {
			return e.getMessage();
		}
		return result;
	}
	
	/**
	 * Create a user.
	 * <br>
	 * @param fname
	 * @param lname
	 * @param phone
	 * @param emergency
	 * @param user_type
	 * @param email
	 * @param password
	 * @return The result of the operation.
	 */
	public String createUser(String fname,
							 String lname,
							 String phone,
							 String emergency,
							 String user_type,
							 String email,
							 String password) {
		command = TestCommands.TestAdminSync.adminCreateUser;
		String result = "";
		try
		{
			command.put("fname", fname);
			command.put("lname", lname);
			command.put("phone", phone);
			command.put("emergency", emergency);
			command.put("email", email);
			command.put("user_type", user_type);
			command.put("password", password);
			
			result = doOperation();
		} catch(Exception e) {
			return e.getMessage();
		}
		return result;
	}
	
	/**
	 * Create a team.
	 * 
	 * @param league_id
	 * @param team_name
	 * @param user_id
	 * @return The result of the operation.
	 */
	public String createTeam(String league_id,
						    String team_name,
						    String user_id) {
		command = TestCommands.TestAdminSync.adminCreateTeam;
		String result = "";
		try
		{
			command.put("league_id", league_id);
			command.put("team_name", team_name);
			command.put("user_id", user_id);
			
			doOperation();
		} catch(Exception e) {
			return e.getMessage();
		}
		return result;
	}
	
	/**
	 * Create an entry in the enrollment table.
	 * 
	 * @param user_id
	 * @param player_id
	 * @param team_id
	 * @param league_id
	 * @param enrollment_date
	 * @param fee
	 * @return The result of the operation.
	 */
	public String createEnrollment(String user_id,
								   String player_id,
								   String team_id,
								   String league_id,
								   String enrollment_date,
								   String fee) {
		command = TestCommands.TestAdminSync.adminCreateEnrollment;
		String result = "";
		try
		{
			command.put("user_id", "user_id");
			command.put("player_id", "player_id");
			command.put("team_id", "team_id");
			command.put("league_id", "league_id");
			command.put("enrollment_date", "enrollment_date");
			command.put("fee", "fee");
			
			doOperation();
		} catch(Exception e) {
			return e.getMessage();
		}
		return result;
	}
	
	/**
	 * Create a place.
	 * '
	 * @param place_name
	 * @param street_address
	 * @param city
	 * @param zip
	 * @return The result of the operation.
	 */
	public String createPlace(String place_name,
							  String street_address,
							  String city,
							  String zip) {
		command = TestCommands.TestAdminSync.adminCreatePlace;
		String result = "";
		try
		{
			command.put("place_name", "place_name");
			command.put("street_address", "street_address");
			command.put("city", "city");
			command.put("state", "state");
			command.put("zip", "zip");
			
			result = doOperation();
		} catch(Exception e) {
			return e.getMessage();
		}
		return result;
	}
	
	/**
	 * Create an event.
	 * 
	 * @param event_type
	 * @param start_date_time
	 * @param place_id
	 * @param home_team
	 * @param away_team
	 * @return The result of the operation.
	 */
	public String createEvent(String event_type,
							  String start_date_time,
							  String place_id,
							  String home_team,
							  String away_team) {
		command = TestCommands.TestAdminSync.adminCreateEvent;
		String result = "";
		try
		{
			command.put("event_type", "event_type");
			command.put("start_date_time", "start_date_time");
			command.put("place_id", "place_id");
			command.put("home_team_id", "home_team");
			command.put("away_team_id", "away_team");
			
			result = doOperation();
		} catch(Exception e) {
			return e.getMessage();
		}
		return result;
	}
}
