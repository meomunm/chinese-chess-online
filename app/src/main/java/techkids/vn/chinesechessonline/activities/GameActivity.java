package techkids.vn.chinesechessonline.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import techkids.vn.chinesechessonline.controllers.GameController;

public class GameActivity extends AppCompatActivity {
    private final String TAG = GameActivity.class.toString();

    GameController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        controller = new GameController(this, display);

        setContentView(controller.getGameView());

        controller.startNewGame();
        Log.d(TAG, "onCreate: oi that la vcl");
    }
}
