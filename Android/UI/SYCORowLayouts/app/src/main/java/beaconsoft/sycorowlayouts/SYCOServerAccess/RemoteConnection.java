package beaconsoft.sycorowlayouts.SYCOServerAccess;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**This class allows connection to a remove location via
 * a POST request.
 * <br>
 * SERVER_URL specifies the location to which one posts.
 * 
 * @author David Johnson
 *
 */
public class RemoteConnection {

    private final static String         LOGIN_URL   = "http://capstone1.netai.net/devDave/login.php";
	//private final static String 		SERVER_URL = "http://localhost/SYCOsync/sync.php";	
	private final static String 		SERVER_URL = "http://capstone1.netai.net/devDave/sync.php";
	//private final static String			SERVER_URL = "http://172.20.23.103/SYCOsync/sync.php";
	private static HttpURLConnection 	conn;             //http connection
    private static URL 					url;   
    
    /*
     * 
     */
    
    /**
     * Converts a hash map into a post body.
     * <br>
     * NOTE ON DEBUG SETTINGS:
     * 	http://stackoverflow.com/questions/17998869/netbeans-how-to-debug-a-post-request-php
     *  https://www.devside.net/wamp-server/configure-netbeans-8-with-xdebug-connection-on-wamp
     *  
     * We append xdebug information in this function so that the localhost/<pagename>
     * knows to connect to our netbeans instance.
     * NOTE:
     * 	THIS WILL NOT WORK REMOTELY, MUST BE LOCAL HOST
     * 			as far as i can tell...
     * @param paramsMap Key value pairs which are the params of the post request.
     * @return
     * @throws Exception
     */
    private static String parsePostParams(HashMap<String, String> paramsMap) throws Exception
    {
        StringBuilder paramsBuilder = new StringBuilder();
        for(String key: paramsMap.keySet()) {
            try {

                paramsBuilder.append(key);
                paramsBuilder.append("=");
                paramsBuilder.append(
                        URLEncoder.encode(paramsMap.get(key), "UTF-8"));            //we must use this encoding
                                                                                    //on the value only
                paramsBuilder.append("&");
                
            } catch (UnsupportedEncodingException e)
            {
                throw new Exception("param Parsing error: " + e.getMessage());
            }
        }
        
        //php debugging settings
        if(DebugFLAGS.PHP_DEBUG == DebugFLAGS.SERVER_DEBUG_FLAGS.DEBUG_ON) {
        	//append the xdebug information
            //remove before production
            if(paramsBuilder.toString().endsWith("&")) {
            	paramsBuilder.append("XDEBUG_SESSION_START=netbeans-xdebug");
            } else {
            	paramsBuilder.append("&XDEBUG_SESSION_START=netbeans-xdebug");
            }
        }
        
        return paramsBuilder.toString();
    }
    
	
    /**
     * Make a post request to the SERVER_URL with the given parameters.
     * 
     * @param params The post parameters in string form.
     * @return The server response in string form.
     * @throws Exception
     */
    private static String postRequest(String params) throws Exception {
        StringBuilder responseBuffer = new StringBuilder();
        try
        {
            byte[] bytes = params.getBytes();   //convert the data to bytes

            url = new URL(SERVER_URL);                                                      //
            conn = (HttpURLConnection) url.openConnection();                               //init the connection objects
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");                                                      //set request method
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");       //set the attributes of the HTTP header
            conn.setRequestProperty("charset", "utf-8");                                        //more attributes...
            conn.setRequestProperty("Content-Length", Integer.toString(bytes.length));          //more attributes...
            conn.setConnectTimeout(10000);                                                      //set the timeout if things break

            DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());   //write data across the socket
            dataOutputStream.write(bytes);
            dataOutputStream.flush();
            dataOutputStream.close();


            InputStreamReader reader = new InputStreamReader(conn.getInputStream());            //get the response
            int input;
            while((input = reader.read()) != -1)
            {
                responseBuffer.append((char)input);
            }
            reader.close();

        } catch (IOException e) {
            throw new Exception("Error making post Request, message: " + e.getMessage());
        } finally {
            conn.disconnect();
        }
        return responseBuffer.toString();                                                       //pass the response to onPostExecute()
    }

    /**
     * post requst to login url...
     *
     * @param params The post parameters in string form.
     * @return The server response in string form.
     * @throws Exception
     */
    private static String loginRequest(String params) throws Exception {
        StringBuilder responseBuffer = new StringBuilder();
        try
        {
            byte[] bytes = params.getBytes();   //convert the data to bytes

            url = new URL(LOGIN_URL);                                                      //
            conn = (HttpURLConnection) url.openConnection();                               //init the connection objects
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");                                                      //set request method
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");       //set the attributes of the HTTP header
            conn.setRequestProperty("charset", "utf-8");                                        //more attributes...
            conn.setRequestProperty("Content-Length", Integer.toString(bytes.length));          //more attributes...
            conn.setConnectTimeout(10000);                                                      //set the timeout if things break

            DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());   //write data across the socket
            dataOutputStream.write(bytes);
            dataOutputStream.flush();
            dataOutputStream.close();


            InputStreamReader reader = new InputStreamReader(conn.getInputStream());            //get the response
            int input;
            while((input = reader.read()) != -1)
            {
                responseBuffer.append((char)input);
            }
            reader.close();

        } catch (IOException e) {
            throw new Exception("Error making post Request, message: " + e.getMessage());
        } finally {
            conn.disconnect();
        }
        return responseBuffer.toString();                                                       //pass the response to onPostExecute()
    }
    public static String doPostRequest(HashMap<String, String> paramsMap) {
    	try {
			return postRequest(parsePostParams(paramsMap));
		} catch (Exception e) {
			return e.getMessage();
		}
    }

    public static String login(HashMap<String, String> paramsMap){
        try {
            return loginRequest(parsePostParams(paramsMap));
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
