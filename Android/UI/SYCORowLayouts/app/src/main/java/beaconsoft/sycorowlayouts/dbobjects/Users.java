package beaconsoft.sycorowlayouts.dbobjects;

import java.util.EnumSet;

/**
 * Created by Patrick on 3/8/2016.
 */
public class Users {

    private int userID;
    private String fname;
    private String lname;
    private long phone;
    private String email;
    private long emergency;
    private String user_type;
    private String password;

    public int getUserID() {
        return userID;
    }

    public void setUser_id(int user_id) {
        this.userID = user_id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getEmergency() {
        return emergency;
    }

    public void setEmergency(long emergency) {
        this.emergency = emergency;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString(){
        String str = "";
        if(getUser_type().equals("COACH")){
            str += "Coach ";
        } else if (getUser_type().equals("ADMIN")){
            str += "Admin";
        }
        str += userID + " " + fname + " " + lname;
        return str;
    }
}
