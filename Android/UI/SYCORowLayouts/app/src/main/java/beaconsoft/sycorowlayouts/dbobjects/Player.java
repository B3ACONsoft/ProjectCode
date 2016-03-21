package beaconsoft.sycorowlayouts.dbobjects;

/**
 * Created by Patrick on 3/8/2016.
 */
public class Player {

    private int playerID;
    private String fname;
    private String lname;
    private int userID;


    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String toString(){
        return playerID + " " + fname + " " + lname;
    }
}
