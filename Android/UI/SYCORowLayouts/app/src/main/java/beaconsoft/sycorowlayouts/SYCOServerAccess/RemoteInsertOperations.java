package beaconsoft.sycorowlayouts.SYCOServerAccess;


import beaconsoft.sycorowlayouts.dbobjects.*;

public class RemoteInsertOperations extends RemoteOperations {

    public RemoteInsertOperations(RemoteConnection remoteConnection) {
        super(remoteConnection);
    }

    /**
     * Create a sport.
     * <br>
     * @param sportName
     * @return The result of the operation.
     */
    public String createSport(String sportName) {
        command = SQLCommandMaps.InsertCommandMaps.adminCreateSport;
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

        command = SQLCommandMaps.InsertCommandMaps.adminCreateLeague;
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
        command = SQLCommandMaps.InsertCommandMaps.adminCreateUser;
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
        command = SQLCommandMaps.InsertCommandMaps.adminCreateTeam;
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

    public String createPlayer(String f_name, String l_name, int user_id) {
        command = SQLCommandMaps.InsertCommandMaps.adminCreatePlayer;
        String result = "";
        try
        {
            command.put("user_id", user_id + "");
            command.put("fname", f_name);
            command.put("lname", l_name);

            result = doOperation();
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
        command = SQLCommandMaps.InsertCommandMaps.adminCreateEnrollment;
        String result = "";
        try
        {
            command.put("user_id", "user_id");
            command.put("player_id", "player_id");
            command.put("team_id", "team_id");
            command.put("league_id", "league_id");
            command.put("enrollment_date", "enrollment_date");
            command.put("fee", "fee");

            result = doOperation();
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
        command = SQLCommandMaps.InsertCommandMaps.adminCreatePlace;
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
        command = SQLCommandMaps.InsertCommandMaps.adminCreateEvent;
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


    public int insertPOJO(Object pojo) {
        POJO_TYPE pojo_type = getPOJOtype(pojo);
        switch(pojo_type){
            case ATTENDANCE:
                Attendance attendanceRecord = (Attendance)pojo;
                //TODO implement createAttendance
                break;
            case ENROLLMENT:
                Enrollment enrollmentRecord = (Enrollment)pojo;
                createEnrollment(String.valueOf(enrollmentRecord.getUserID()),
                        String.valueOf(enrollmentRecord.getPlayerID()),
                        String.valueOf(enrollmentRecord.getTeamID()),
                        String.valueOf(enrollmentRecord.getLeagueID()),
                        String.valueOf(enrollmentRecord.getEnrollmentDate()),
                        String.valueOf(enrollmentRecord.getFee()));
                break;
            case EVENT:
                Event eventRecord = (Event)pojo;
                createEvent(eventRecord.getEventType(),
                        eventRecord.getStartDateTime().toString(),
                        String.valueOf(eventRecord.getPlaceID()),
                        String.valueOf(eventRecord.getHomeTeamID()),
                        String.valueOf(eventRecord.getAwayTeamID()));
                break;
            case LEAGUE:
                League leagueRecord = (League)pojo;
                createLeague(leagueRecord.getLeagueName(),
                        String.valueOf(leagueRecord.getSportID()),
                        String.valueOf(leagueRecord.getMinAge()),
                        String.valueOf(leagueRecord.getMaxAge()),
                        leagueRecord.getStartDate().toString(),
                        leagueRecord.getEndDate().toString());
                break;
            case PLACE:
                Place placeRecord = (Place)pojo;
                createPlace(placeRecord.getPlaceName(),
                        placeRecord.getStreetAddress(),
                        placeRecord.getCity(),
                        String.valueOf(placeRecord.getZip()));
                break;
            case PLAYER:
                Player playerRecord = (Player)pojo;
                //TODO implement create player
                createPlayer(playerRecord.getFname(),
                             playerRecord.getLname(),
                             playerRecord.getUserID());

                break;
            case SPORT:
                Sport sportRecord = (Sport)pojo;
                createSport(sportRecord.getSportName());
                break;
            case TEAM:
                Team teamRecord = (Team)pojo;
                createTeam(String.valueOf(teamRecord.getLeagueID()),
                        teamRecord.getTeamName(),
                        String.valueOf(teamRecord.getUserID()));
                break;
            case USERS:
                Users userRecord = (Users)pojo;
                createUser(userRecord.getFname(),
                        userRecord.getLname(),
                        String.valueOf(userRecord.getPhone()),
                        String.valueOf(userRecord.getEmergency()),
                        userRecord.getUser_type(),
                        userRecord.getEmail(),
                        userRecord.getPassword());
                break;
        }
        return 0;
    }
}
