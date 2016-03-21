package SYCOServerAccess;

/**
 * These are the flags which set debugging options throughout
 * the test program.
 * 
 * @author David Johnson
 *
 */
public final class DebugFLAGS {
	
	public enum SERVER_DEBUG_FLAGS {
		DEBUG_ON,
		DEBUG_OFF
	}
	
	
	public enum LOCAL_DEBUG_FLAGS {
		DEBUG_ON,
		DEBUG_OFF
	}
	
	/**
	 * This flag controls if messages which control Xdebug are
	 * added to the end of all post requests.
	 *
	 */
	public static SERVER_DEBUG_FLAGS PHP_DEBUG;
	/**
	 * This flag controls weather the program prints 
	 * debugging messages to the console.
	 *
	 */
	public static LOCAL_DEBUG_FLAGS LOCAL_DEBUG;
}
