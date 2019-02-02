package techkids.vn.chinesechessonline.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import techkids.vn.chinesechessonline.R;

public class HomeActivity extends AppCompatActivity {
    private final String TAG = HomeActivity.class.toString();

    ImageButton ibFindMatch;
    ImageButton ibUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        listenner();

        Log.d(TAG, "debug -> onCreate: dm vcl day");
    }

    private void init() {
        ibFindMatch = findViewById(R.id.ib_findMatch);
        ibUser = findViewById(R.id.ib_user);
    }

    private void listenner() {
        ibFindMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, GameActivity.class);
                startActivity(intent);
                //co the finish hay ko?
                //finish();
            }
        });

        ibUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
