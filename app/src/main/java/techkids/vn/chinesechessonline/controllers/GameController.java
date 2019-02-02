package techkids.vn.chinesechessonline.controllers;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Display;
import android.view.View;

import techkids.vn.chinesechessonline.activities.GameActivity;
import techkids.vn.chinesechessonline.models.GameSound;
import techkids.vn.chinesechessonline.views.DialogCustomLayout;
import techkids.vn.chinesechessonline.views.GameView;


public class GameController {
    private final String TAG = GameController.class.toString();

    GameView gameView;
    Display screenSize;
    Context context;
    GameSound soundManager;

    public GameController(Context context, Display screenSize) {
        this.screenSize = screenSize;
        this.context = context;
        this.gameView = new GameView(context, this);
        this.soundManager = new GameSound(context);
    }


    public void startNewGame() {
        this.chooseSide();
        Log.d(TAG, "debug -> startNewGame: ");
    }

    private void chooseSide() {
        DialogCustomLayout dialogCustomLayout = new DialogCustomLayout();
        dialogCustomLayout.showDialog(context,this);
    }

    public void setSide(boolean isRed) {
        gameView.initChess();
        gameView.setFirstGo(isRed);
        Log.d(TAG, "debug -> setSide: ok in setside");
    }

    public int getScreenWidth(){
        return screenSize.getWidth();
    }

    public int getScreenHeight(){
        return  screenSize.getHeight();
    }

    public GameView getGameView(){
        return gameView;
    }
}
