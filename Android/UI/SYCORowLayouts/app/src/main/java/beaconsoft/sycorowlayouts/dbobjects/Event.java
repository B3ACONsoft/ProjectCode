package beaconsoft.sycorowlayouts.dbobjects;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by Patrick on 3/8/2016.
 */
public class Event implements Comparable {

    private int eventID;
    private String eventType;
    private Date startDateTime;
    private int placeID;
    private int homeTeamID;
    private int awayTeamID;

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public int getPlaceID() {
        return placeID;
    }

    public void setPlaceID(int placeID) {
        this.placeID = placeID;
    }

    public int getHomeTeamID() {
        return homeTeamID;
    }

    public void setHomeTeamID(int homeTeamID) {this.homeTeamID = homeTeamID;}

    public int getAwayTeamID() {
        return awayTeamID;
    }

    public void setAwayTeamID(int awayTeamID) {
        this.awayTeamID = awayTeamID;
    }

    public String toString(){
        return eventID + " " + startDateTime + " " + eventType;
    }

    @Override
    public boolean equals(Object object){
        if(this == object){
            return true;
        }else if(this.getEventID() == ((Event)object).getEventID()){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int compareTo(Object another) {
        Event otherEvent = (Event)another;
        long lhsTime = this.getStartDateTime().getTime();
        long rhsTime = otherEvent.getStartDateTime().getTime();
        if(lhsTime > rhsTime){
            return -1;
        }else if(lhsTime < rhsTime){
            return 1;
        }else {
            return 0;
        }
    }
}
