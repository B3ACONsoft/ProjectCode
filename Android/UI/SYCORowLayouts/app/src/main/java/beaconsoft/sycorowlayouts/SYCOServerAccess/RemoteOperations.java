package beaconsoft.sycorowlayouts.SYCOServerAccess;
import java.util.HashMap;

import beaconsoft.sycorowlayouts.dbobjects.*;

/**
 * The parent class for a user types remote operation.
 * 
 * @author David Johnson
 *
 */
public abstract class RemoteOperations {
	
	
	protected RemoteConnection remoteConnection;
	protected HashMap<String, String> command;

    protected enum POJO_TYPE {
        ATTENDANCE,
        ENROLLMENT,
        EVENT,
        LEAGUE,
        PLACE,
        PLAYER,
        SPORT,
        TEAM,
        USERS,
        INVALID_POJO
    }

	public RemoteOperations(RemoteConnection remoteConnection) {
		this.remoteConnection = remoteConnection;
		this.command = new HashMap<String, String>();
	}

    /*
    This strips out unwanted characters from the server response upon
    doing a post request.
     */
    private String parseNetaiCrap(String netai_crap) {
        String retVal = netai_crap;

		/*
		 * We assume that server should return a vaild JSON string.
		 * So it should begin with a "["
		 * If not, then it really has returned crap.
		 */
        if(netai_crap.startsWith("[")) {
			/*
			 * We start at the end of the server response because
			 * in most cases there will be less data to get through.
			 */
            for(int i = netai_crap.length()-1; i >= 0; i--) {
                if(netai_crap.charAt(i) == ']') {
                    retVal = netai_crap.substring(0, i+1);
                    break;
                }

            }
        }

        return retVal;
    }

	protected String doOperation() {
		return parseNetaiCrap(remoteConnection.doPostRequest(command));
	}

    protected POJO_TYPE getPOJOtype(Object pojo) {
        if(pojo instanceof Attendance) {
            return POJO_TYPE.ATTENDANCE;
        }
        if(pojo instanceof Enrollment) {
            return POJO_TYPE.ENROLLMENT;
        }
        if(pojo instanceof Event) {
            return POJO_TYPE.EVENT;
        }
        if(pojo instanceof League) {
            return POJO_TYPE.LEAGUE;
        }
        if(pojo instanceof Place) {
            return POJO_TYPE.PLACE;
        }
        if(pojo instanceof Player) {
            return POJO_TYPE.PLAYER;
        }
        if(pojo instanceof Sport) {
            return POJO_TYPE.SPORT;
        }
        if(pojo instanceof Team) {
            return POJO_TYPE.TEAM;
        }
        if(pojo instanceof Users) {
            return POJO_TYPE.USERS;
        }
        return POJO_TYPE.INVALID_POJO;
    }
}
