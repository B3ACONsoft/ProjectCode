package beaconsoft.sycorowlayouts;

/**
 * Created by Patrick on 3/8/2016.
 */
public class Sport {

    private int sportID;
    private String sportName;

    public int getSportID() {
        return sportID;
    }

    public void setSportID(int sportID) {
        this.sportID = sportID;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String toString(){
        return sportID + " " + sportName;
    }
}

