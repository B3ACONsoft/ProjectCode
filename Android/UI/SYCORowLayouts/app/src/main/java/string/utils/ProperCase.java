package string.utils;

/**
 * Created by Patrick on 2/26/2016.
 */
public class ProperCase {

    public static String toProperCase(String properNoun){
        String pn = "";
        pn = properNoun.substring(0, 1) + properNoun.substring(1);

        return pn;
    }
}
