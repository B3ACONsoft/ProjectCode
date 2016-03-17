package beaconsoft.sycorowlayouts.TestRoutines;
import beaconsoft.sycorowlayouts.RandomNameGenerator.RandomNameGenerator;
import beaconsoft.sycorowlayouts.SYCOServerAccess.RemoteConnection;

import java.io.StringReader;

import javax.json.*;

/**
 * The puppyStuffer is a means to populate the test datbase with semi-random values.
 * 
 * @author David Johnson
 *
 */
public class PuppyStuffer {
	/**
	 * This is an array of partialy formed excuses used my terrible parents and players.
	 * 
	 */
	private final String[] excuses = {
			"I can't play because my _ _ed my _.",
			"My child won't attend an event where the _er's are in attendance.",
			"...Today I should also like to show you the drawing room, but today, you see, I have the fear.",
			"The _son's poor breeding has given us the fear.",
			"Quick smart my dear boy, I sense seepage!",
			"I've just gone cold turkey after a year long heroine fueled bender.",
			"My bowels are in a somewhat weakened state.",
			"Donald Trump _ed at least ten times."
	};
	
	/**
	 * The random value generator.
	 * This is NOT ours.
	 * See https://github.com/kohsuke/wordnet-random-name
	 */
	RandomNameGenerator rand = new RandomNameGenerator();
	
	/**
	 * @return Returns a random name.
	 */
	public String getRandomName() {
		return rand.next();
	}
	
	/**
	 * @return Returns a random team name.
	 */
	public String getRandomTeamName() {
		String name = getRandomName();
		String output = "The_" + name;
		if(!name.endsWith("s")) {
			output += "s";
		}
		return output;
	}
	
	/**
	 * @return Returns a random league name.
	 */
	public String getRandomLeagueName() {
		String name = getRandomName();
		String output = "League_of_" + name;
		if(!name.endsWith("s")) {
			output += "s";
		}
		return output;
	}
	
	/**
	 * 
	 * @return Returns a random sport name.
	 */
	public String getRandomSportName() {
	    String name = getRandomName();
	    String output = name + "ing";
		return output;
	}
	
	public String getYesOrNo() {
		int minimum = 0;
		int maximum = 1;
		int randomNum = minimum + (int)(Math.random() * (maximum + 1));
		
		if(randomNum == 0) 
			return "NO";
		else 
			return "YES";
	}
	
	public String getExcuse() {
		int minimum = 0;
		int maximum = this.excuses.length;
		int randomNum = minimum + (int)(Math.random() * (maximum));
		
		String excuse = this.excuses[randomNum];
		
		//check if we have replacement to do
		//if we do then we replace all "_" with random words :)
		if(excuse.contains("_")) {
			StringBuilder excuseBuilder = new StringBuilder();
			int lastSegEnd = 0;
			for(int i = 0; i < excuse.length();i++) {
				//we have a "_"
				if(excuse.charAt(i) == '_') {
					//append the string before the blank
					excuseBuilder.append(excuse.substring(lastSegEnd, i));
					//append the random word
					excuseBuilder.append(this.getRandomName());
					//set lastSegEnd
					lastSegEnd = i + 1;
				}
			}
			//take care of the end
			excuseBuilder.append(excuse.substring(lastSegEnd, excuse.length()));
			excuse = excuseBuilder.toString();
		}
		
		return excuse;
	}
	
	/**
	 * @return Returns a random phone number in (xxx) xxx-xxx format.
	 */
	public String getRandomPhoneNumber() {
		int minimum = 0;
		int maximum= 9;
		int randomNum;
		StringBuilder phoneNumber = new StringBuilder();
		for(int i = 1; i <= 14; i++) {
			randomNum = minimum + (int)(Math.random() * maximum);
			switch(i) {
			case 1:
				phoneNumber.append("(");
			break;
			case 2:
			case 3:
			case 4:
				phoneNumber.append(String.valueOf(randomNum));
			break;
			case 5:
				phoneNumber.append(")");
				break;
			case 6:
				phoneNumber.append("-");
				break;
			case 7:
			case 8:
			case 9:
				phoneNumber.append(String.valueOf(randomNum));
				break;
			case 10:
				phoneNumber.append("-");
				break;
			case 11:
			case 12:
			case 13:
			case 14:
				phoneNumber.append(String.valueOf(randomNum));
				break;
				
			}
		}
		return phoneNumber.toString(); 
	}
	
	/**
	 * Helper method for doFullPuppyStuff()
	 * Get JSONArray from a string();
	 * 
	 * http://www.tutorialspoint.com/json/json_java_example.htm
	 * 
	 * @param jsonString
	 * @return
	 */
	public JsonArray getJSONArray(String jsonString) {

		/*JsonReader jsonReader = Json.createParser();
		JsonObject object = jsonReader.readObject();
		jsonReader.close();*/
		return null;  
	}
	
	/**
	 * An instruction set for an amature taxidermist.
	 * Would you just look at those creep eyes!!!
	 * 
	 * Fully populates the database with dummy data.
	 * Also sees what the deal is with the JSON stuff...
	 * 
	 *  The database should be EMPTY before this call is run!!!
	 */
	public void doFullPuppyStuff() {
		
		/*
		 * create adminTests class, we need those calls
		 */
		AdminTests adminTests = new AdminTests();
		
		/*
		 * Create 10 sports
		 */
		adminTests.testAdminCreateSports(10);
		
		/*
		 * Get the current sports out of database
		 * Create a league for each.
		 */
		String sportRecords = 
				RemoteConnection.doPostRequest(
						TestCommands.TestAdminSync.adminGetsAllSports);
		
		JsonArray jsArr_sports = getJSONArray(adminTests.testAdminGetAllSports());
		
		int i = 0;
		
		
		//create users.
		//some admin, coach, and user
		
		//for each coach, make a team
		
		//for each user divide them evenly,
		//create a player and 
		//assign them to the teams
		
		//create an attendance table for each team
		
		//create some places
		
		//create some events
		
		//do the enrollement table
		
	}
}
