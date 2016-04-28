package beaconsoft.sycorowlayouts.SYCOServerAccess;


import beaconsoft.sycorowlayouts.dbobjects.Team;

public class RemoteSelectOperations extends RemoteOperations {
    public RemoteSelectOperations(RemoteConnection remoteConnection) {
        super(remoteConnection);
    }

    /*
        USER
     */
    public String getListOfUsers() {
        command = SQLCommandMaps.SelectAllCommandMaps.adminGetsAllUsers;
        String result = "";
        try
        {
            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }

    public String getUserByEmail(String email) {
        command = SQLCommandMaps.SelectAllCommandMaps.adminGetsAllUsers;
        String result = "";
        try
        {
            command.put("option", "get_user_by_email");
            command.put("email", email);

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;

    }

    public String getListOfUsersAvailableToCoach(int currentLeague) {
        command = SQLCommandMaps.SelectAllCommandMaps.adminGetsAllUsers;
        String result = "";
        try
        {
            command.put("option", "get_users_by_coach");
            command.put("current_league_id", currentLeague + "");

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;

    }

    public String getListOfUsersByTeam(int teamID) {
        command = SQLCommandMaps.SelectAllCommandMaps.adminGetsAllUsers;
        String result = "";
        try
        {
            command.put("option", "get_users_by_team");
            command.put("current_team_id", teamID + "");

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;

    }
    /*
        TEAM
     */
    public String getListOfTeams() {
        command = SQLCommandMaps.SelectAllCommandMaps.adminGetsAllTeams;
        String result = "";
        try
        {
            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }
/*
    public String getListOfTeamsByUserEmail(String email) {
        command = SQLCommandMaps.SelectAllCommandMaps.adminGetsAllTeams;
        String result = "";
        try
        {
            command.put("option", "get_team_by_id");
            command.put("sport_name", sportName);

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }

    public String getListOfTeamsCoachedByUser(int currentUser, int currentLeague) {
        command = SQLCommandMaps.SelectAllCommandMaps.adminGetsAllTeams;
        String result = "";
        try
        {
            command.put("option", "get_team_by_coach");
            command.put("sport_name", sportName);

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }

    public String getListOfTeamsByLeague(int currentLeague) {
        command = SQLCommandMaps.SelectAllCommandMaps.adminGetsAllTeams;
        String result = "";
        try
        {
            command.put("option", "get_team_by_league");
            command.put("sport_name", sportName);

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }
    */

    /*
        LEAGUE
     */
    /*
    public String getListOfLeaguesByCoachAndSport(int coachId, int sportId) {
        command = SQLCommandMaps.InsertCommandMaps.adminCreateSport;
        String result = "";
        try
        {
            command.put("option", "get_leagues_by_coach_and_sport");
            command.put("sport_name", sportName);

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }

    public String getLeagueById(int currentLeague) {
        command = SQLCommandMaps.InsertCommandMaps.adminCreateSport;
        String result = "";
        try
        {
            command.put("option", "get_league_by_id");
            command.put("sport_name", sportName);

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }

    public String getListOfLeaguesByAdminIdAndSport(int currentAdminId, int currentSport) {
        command = SQLCommandMaps.InsertCommandMaps.adminCreateSport;
        String result = "";
        try
        {
            command.put("option", "get_leagues_by_adminid_and_sportid");
            command.put("sport_name", sportName);

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }
*/
    public String getListOfLeagues() {
        command = SQLCommandMaps.SelectAllCommandMaps.adminGetsAllLeagues;
        String result = "";
        try
        {

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }
/*
    public String getListOfLeaguesBySport(int currentSport) {
        command = SQLCommandMaps.InsertCommandMaps.adminCreateSport;
        String result = "";
        try
        {
            command.put("option", "get_leagues_by_sportid");
            command.put("sport_name", sportName);

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }
    */
    /*
        PLAYER
     */
    /*
    public String getListOfPlayersByTeam(int currentTeam) {
        command = SQLCommandMaps.InsertCommandMaps.adminCreateSport;
        String result = "";
        try
        {
            command.put("option", "get_players_by_team");
            command.put("sport_name", sportName);

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }

    public String getPlayerById(int currentPlayer) {
        command = SQLCommandMaps.InsertCommandMaps.adminCreateSport;
        String result = "";
        try
        {
            command.put("option", "get_player_by_id");
            command.put("sport_name", sportName);

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }

    public String checkForPlayerByFirstLastAndUserId(String first, String last, int userID) {
        command = SQLCommandMaps.InsertCommandMaps.adminCreateSport;
        String result = "";
        try
        {
            command.put("option", "get_player_by_first_last_userid");
            command.put("sport_name", sportName);

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }

    public String getPlayerByFirstLastAndUserId(String first, String last, int userID) {
        command = SQLCommandMaps.InsertCommandMaps.adminCreateSport;
        String result = "";
        try
        {
            command.put("option", "get_player_by_first_last_userid");
            command.put("sport_name", sportName);

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }
*/
    public String getListOfPlayers() {
        command = SQLCommandMaps.SelectAllCommandMaps.adminGetsAllPlayers;
        String result = "";
        try
        {

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }
    /*
        SPORT
     */
    /*
    public String getListOfSportsByCoach(int coachId) {
        command = SQLCommandMaps.InsertCommandMaps.adminCreateSport;
        String result = "";
        try
        {
            command.put("option", "get_sports_by_coach");
            command.put("sport_name", sportName);

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }

    public String getListOfSportsByAdmin(int currentAdminId) {
        command = SQLCommandMaps.InsertCommandMaps.adminCreateSport;
        String result = "";
        try
        {
            command.put("option", "get_sports_by_admin");
            command.put("sport_name", sportName);

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }
*/
    public String getListOfSports() {
        command = SQLCommandMaps.SelectAllCommandMaps.adminGetsAllSports;
        String result = "";
        try
        {

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }
/*
    public String getSportByName(String sport_name) {
        command = SQLCommandMaps.InsertCommandMaps.adminCreateSport;
        String result = "";
        try
        {
            command.put("option", "get_sports_by_name");
            command.put("sport_name", sportName);

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }
    */
    /*
        ENROLLMENT
     */
    /*
    //TODO !!!!!!!!!!!!!!!
    public String getEnrollmentByTeamIdAndLeagueId(int currentTeamId, int currentLeagueId) {
        command = SQLCommandMaps.InsertCommandMaps.adminCreateSport;
        String result = "";
        try
        {
            command.put("option", "get_sports_by_name");
            command.put("sport_name", sportName);

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }

    public String getEnrollmentByUserLeagueAndTeam(int coachUserId, int leagueId, int teamId) {
        command = SQLCommandMaps.InsertCommandMaps.adminCreateSport;
        String result = "";
        try
        {
            command.put("option", "get_enrollment_by_userid_leagueid_teamid");
            command.put("sport_name", sportName);

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }

    public String getEnrollmentByPlayerUserLeagueAndTeam(int playerID, int userID, int currentLeague, int currentTeam) {
        command = SQLCommandMaps.InsertCommandMaps.adminCreateSport;
        String result = "";
        try
        {
            command.put("option", "get_enrollment_by_playerid_userid_currentleague_currentteam");
            command.put("sport_name", sportName);

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }
*/
    public String getListOfEnrollments() {
        command = SQLCommandMaps.SelectAllCommandMaps.adminGetsAllEnrollment;
        String result = "";
        try
        {
            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }
    /*
        PLACE
     */
    public String getListOfPlaces() {
        command = SQLCommandMaps.SelectAllCommandMaps.adminGetsAllPlaces;
        String result = "";
        try
        {

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }
/*
    public String getPlaceById(int id) {
        command = SQLCommandMaps.InsertCommandMaps.adminCreateSport;
        String result = "";
        try
        {
            command.put("option", "get_place_by_id");
            command.put("sport_name", sportName);

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }
    /*
        EVENTS
     */
    public String getAllEvents() {
        command = SQLCommandMaps.SelectAllCommandMaps.adminGetsAllEvents;
        String result = "";
        try
        {
            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }
    /*
    public String getListOfEventsByTeam(Team currentTeam) {
        command = SQLCommandMaps.InsertCommandMaps.adminCreateSport;
        String result = "";
        try
        {
            command.put("option", "get_events_by_team");
            command.put("sport_name", sportName);

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }

    public String getListOfEventsById(int eventId) {
        command = SQLCommandMaps.InsertCommandMaps.adminCreateSport;
        String result = "";
        try
        {
            command.put("option", "get_events_by_placeid");
            command.put("sport_name", sportName);

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }
    */
    /*
        ATTENDANCE
     */
    public String getListOfAttendances() {
        command = SQLCommandMaps.SelectAllCommandMaps.adminGetsAllAttendance;
        String result = "";
        try
        {

            result = doOperation();
        } catch(Exception e) {
            return e.getMessage();
        }
        return result;
    }

}
