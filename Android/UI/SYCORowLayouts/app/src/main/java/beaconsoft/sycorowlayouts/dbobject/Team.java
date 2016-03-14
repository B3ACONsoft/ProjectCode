package beaconsoft.sycorowlayouts.dbobject;

/**
 * Created by Patrick on 3/8/2016.
 */
public class Team {

    private int teamID;
    private String teamName;
    private int leagueID;
    private int userID;


    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getLeagueID() {
        return leagueID;
    }

    public void setLeagueID(int leagueID) {
        this.leagueID = leagueID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String toString(){
        return teamID + " " + teamName;
    }
}
