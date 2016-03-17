package beaconsoft.sycorowlayouts.SYCOServerAccess;
import java.util.HashMap;

/**
 * The parent class for a user types remote operation.
 * 
 * @author David Johnson
 *
 */
public abstract class RemoteOperations {
	
	
	protected RemoteConnection remoteConnection;
	protected HashMap<String, String> command;
	
	//FIXME: This Remote Connection instance is not the static one that application thinks
	//it is using!!!!!!
	//CHANGE FOR PRODUCTION
	public RemoteOperations() {
		this.remoteConnection = new RemoteConnection();
		this.command = new HashMap<String, String>();
	}
	
	protected String doOperation() {
		return remoteConnection.doPostRequest(command);
	}
	
	
}
