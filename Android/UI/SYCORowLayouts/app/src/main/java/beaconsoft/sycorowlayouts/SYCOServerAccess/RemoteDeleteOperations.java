package beaconsoft.sycorowlayouts.SYCOServerAccess;

import beaconsoft.sycorowlayouts.dbobjects.*;

public class RemoteDeleteOperations extends RemoteOperations {

    public RemoteDeleteOperations(RemoteConnection remoteConnection) {
        super(remoteConnection);
    }

    public int deletePOJO(Object pojo) {
        POJO_TYPE pojo_type = getPOJOtype(pojo);
        switch(pojo_type){
            case ATTENDANCE:
                Attendance attendanceRecord = (Attendance)pojo;
                //TODO implement delete attendance
                break;
            case ENROLLMENT:
                Enrollment enrollmentRecord = (Enrollment)pojo;
                //TODO implement delete enrollment
                break;
            case EVENT:
                Event eventRecord = (Event)pojo;
                //TODO implement delete event
                break;
            case LEAGUE:
                League leagueRecord = (League)pojo;
                //TODO implement delete league
                break;
            case PLACE:
                Place placeRecord = (Place)pojo;
                //TODO implement delete place
                break;
            case PLAYER:
                Player playerRecord = (Player)pojo;
                //TODO implement delete player
                break;
            case SPORT:
                Sport sportRecord = (Sport)pojo;
                //TODO implement delete sport
                break;
            case TEAM:
                Team teamRecord = (Team)pojo;
                //TODO implement delete team
                break;
            case USERS:
                Users userRecord = (Users)pojo;
                //TODO implement delete user
                break;
        }
        return 0;
    }
}
