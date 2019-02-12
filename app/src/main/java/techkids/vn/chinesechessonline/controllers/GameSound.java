package techkids.vn.chinesechessonline.controllers;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import techkids.vn.chinesechessonline.R;

public class GameSound {
    private SoundPool soundPool;
    private int ID_SOUND_EAT;
    private int ID_SOUND_GO;

    GameSound(Context context) {
        //maxstream số lượng luồng phát âm thanh tại 1 thời điểm tối đa
        //streamType
        soundPool = new SoundPool.Builder().build();
        ID_SOUND_EAT = soundPool.load(context, R.raw.eat, 0);
        ID_SOUND_GO = soundPool.load(context, R.raw.go, 0);
    }


    public void playSoundGo() {
        soundPool.play(ID_SOUND_GO, 1, 1, 0, 0, 1);
    }

    public void playSoundEat() {
        soundPool.play(ID_SOUND_EAT, 1, 1, 0, 0, 1);
    }
}
