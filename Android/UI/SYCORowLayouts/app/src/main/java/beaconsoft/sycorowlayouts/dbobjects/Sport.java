package beaconsoft.sycorowlayouts.dbobjects;

/**
 * Created by Patrick on 3/8/2016.
 */
public class Sport {

    private int tempSportId;
    private int sportID;
    private String sportName;

    public int getTempSportId() { return tempSportId; }

    public void setTempSportId(int id){ tempSportId = id; }

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

