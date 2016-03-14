package beaconsoft.sycorowlayouts.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import beaconsoft.sycorowlayouts.R;

public class UserHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
    }

    public void sendToUserRoster(View view){
        Intent intent = new Intent(this, UserRosterActivity.class);
        startActivity(intent);
    }
}
