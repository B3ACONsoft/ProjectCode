package beaconsoft.sycorowlayouts.dbobjects;

/**
 * Created by Patrick on 3/8/2016.
 */
public class Place {

    private int tempPlaceId;
    private int placeID;
    private String placeName;
    private String streetAddress;
    private String city;
    private String state;
    private int zip;

    public int getTempPlaceId(){ return tempPlaceId; }

    public void setTempPlaceId(int id) { tempPlaceId = id; }

    public int getPlaceID() {
        return placeID;
    }

    public void setPlaceID(int placeID) {
        this.placeID = placeID;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String toString(){
        return placeID + " " + placeName;
    }
}
