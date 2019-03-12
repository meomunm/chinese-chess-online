package techkids.vn.chinesechessonline.activities;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.luolc.emojirain.EmojiRainLayout;

import techkids.vn.chinesechessonline.R;

public class EndGameFragment extends DialogFragment {
    Button btOk;
    EmojiRainLayout ed;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_endgame, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btOk = view.findViewById(R.id.bt_ok);

        ed = view.findViewById(R.id.emoji_rain_layout);
        ed.addEmoji(R.drawable.emoji_1_3);
        ed.addEmoji(R.drawable.emoji_2_3);
        ed.addEmoji(R.drawable.emoji_3_3);
        ed.addEmoji(R.drawable.emoji_4_3);
        ed.addEmoji(R.drawable.emoji_5_3);

        // set emojis per flow, default 6
        ed.setPer(10);

        // set total duration in milliseconds, default 8000
        ed.setDuration(7200);

        // set average drop duration in milliseconds, default 2400
        ed.setDropDuration(2400);

        // set drop frequency in milliseconds, default 500
        ed.setDropFrequency(500);

        ed.startDropping();

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed.stopDropping();
                getDialog().dismiss();

                //finish activity
                getActivity().finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }
}