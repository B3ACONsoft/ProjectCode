package beaconsoft.sycorowlayouts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class QuickAddPlayersActivity extends AppCompatActivity {

    private static final String ADMIN_KEY =  "beaconsoft.sycorowlayouts.ADMIN_NAME_PASSED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_add_players);

        TextView textViewAdminEmail = (TextView)findViewById(R.id.textViewQAAdminEmail);
        Intent intent = getIntent();
        String email = intent.getStringExtra(ADMIN_KEY);
        textViewAdminEmail.setText(email);

    }

    public void quickAddPlayer(View view){
        HashMap<String, String> hashMapQuickAddPlayer = new HashMap<>();
        EditText et1 = (EditText) findViewById(R.id.editTextChildFirst);
        hashMapQuickAddPlayer.put("keyChildFirst", et1.toString());
        EditText et2 = (EditText) findViewById(R.id.editTextChildLast);
        hashMapQuickAddPlayer.put("keyChildLast", et2.toString());
        EditText et3 = (EditText) findViewById(R.id.editTextContactFirst);
        hashMapQuickAddPlayer.put("keyContactFirst", et3.toString());
        EditText et4 = (EditText) findViewById(R.id.editTextContactLast);
        hashMapQuickAddPlayer.put("keyContactLast", et4.toString());
        EditText et5 = (EditText) findViewById(R.id.editTextPhone);
        hashMapQuickAddPlayer.put("keyPhone", et5.toString());
        EditText et6 = (EditText) findViewById(R.id.editTextEmail);
        hashMapQuickAddPlayer.put("keyEmail", et6.toString());
        EditText et7 = (EditText) findViewById(R.id.editTextEmergencyPhone);
        hashMapQuickAddPlayer.put("keyEmergencyPhone", et7.toString());

        // doneso hashmapQuickAddPlayer, ready to ship off to be bound and tabled
    }

    public void clearForm(View view){
        EditText et1 = (EditText)findViewById(R.id.editTextChildFirst);
        EditText et2 = (EditText)findViewById(R.id.editTextChildLast);
        EditText et3 = (EditText) findViewById(R.id.editTextContactFirst);
        EditText et4 = (EditText) findViewById(R.id.editTextContactLast);
        EditText et5 = (EditText) findViewById(R.id.editTextPhone);
        EditText et6 = (EditText) findViewById(R.id.editTextEmail);
        EditText et7 = (EditText) findViewById(R.id.editTextEmergencyPhone);
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et5.setText("");
        et7.setText("");
        et6.setText("dontBark@theYeti.bad");
    }
}
