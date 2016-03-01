package beaconsoft.sycorowlayouts;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class QuickAddPlayersActivity extends AppCompatActivity {

    DBHelper helper = new DBHelper(this);
    private CheckBox kidBox;
    private HashMap<String, String> hashMapQuickAddPlayer;
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private EditText et5;
    private EditText et6;
    private EditText et7;
    private static final String   NAME_KEY = "beaconsoft.sycorowlayouts.NAME";
    private static final String  ADMIN_KEY = "beaconsoft.sycorowlayouts.ADMIN";
    private static final String  EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private static final String   TEAM_KEY = "beaconsoft.sycorowlayouts.TEAM";
    private static final String LEAGUE_KEY = "beaconsoft.sycorowlayouts.LEAGUE";

    private String email;
    private String name;
    private int league;
    private int team;
    private int adminId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_add_players);

        TextView textViewAdminEmail = (TextView)findViewById(R.id.textViewQAAdminEmail);
        kidBox = (CheckBox)findViewById(R.id.checkBoxPlayerIsKid);
        hashMapQuickAddPlayer = new HashMap<>();

        et1 = (EditText) findViewById(R.id.editTextContactFirst);
        et2 = (EditText) findViewById(R.id.editTextContactLast);
        et3 = (EditText) findViewById(R.id.editTextChildFirst);
        et4 = (EditText) findViewById(R.id.editTextChildLast);
        et5 = (EditText) findViewById(R.id.editTextPhone);
        et6 = (EditText) findViewById(R.id.editTextEmail);
        et7 = (EditText) findViewById(R.id.editTextEmergencyPhone);

        Intent intent = getIntent();
        email = intent.getStringExtra(EMAIL_KEY);
        adminId = Integer.parseInt(intent.getStringExtra(ADMIN_KEY));
        league  = Integer.parseInt(intent.getStringExtra(LEAGUE_KEY));
        team    = Integer.parseInt(intent.getStringExtra(TEAM_KEY));
        name = intent.getStringExtra(NAME_KEY);

        textViewAdminEmail.setText(email);
        kidBox.setSelected(false);

    }

    public void onClickCheckBox(View view){
        if(!kidBox.isChecked()){
            kidBox.setSelected(false);
            et3.setText(" ");
            et3.setEnabled(false);
            et4.setText(" ");
            et4.setEnabled(false);
            for(View lol: et3.getTouchables()){
                lol.setEnabled(false);
                lol.setClickable(false);
            }
            for(View lol: et4.getTouchables()){
                lol.setEnabled(false);
                lol.setClickable(false);
            }
        }else{
            kidBox.setSelected(true);
            et3.setText("first");
            et3.setEnabled(true);
            et4.setText("last");
            et4.setEnabled(true);

            for(View lol: et3.getTouchables()){
                lol.setEnabled(true);
                lol.setClickable(true);
            }
            for(View lol: et4.getTouchables()){
                lol.setEnabled(true);
                lol.setClickable(true);
            }
        }
    }

    public void quickAddPlayer(View view){


        hashMapQuickAddPlayer.put("keyContactFirst"  , et1.toString());
        hashMapQuickAddPlayer.put("keyContactLast"   , et2.toString());
        hashMapQuickAddPlayer.put("keyChildFirst"    , et3.toString());
        hashMapQuickAddPlayer.put("keyChildLast"     , et4.toString());
        hashMapQuickAddPlayer.put("keyPhone"         , et5.toString());
        hashMapQuickAddPlayer.put("keyEmail"         , et6.toString());
        hashMapQuickAddPlayer.put("keyEmergencyPhone", et7.toString());


    }

    public void clearForm(View view){

        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et5.setText("");
        et7.setText("");
        et6.setText("dontBark@theYeti.bad");
    }
}
