package techkids.vn.chinesechessonline.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import techkids.vn.chinesechessonline.R;
import techkids.vn.chinesechessonline.controllers.GameController;

public class DialogCustomLayout{
    private final String TAG = DialogCustomLayout.class.toString();

    public void showDialog(Context context, final GameController gameController){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_layout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        FrameLayout frmButtonBlack = dialog.findViewById(R.id.frmBlack);
        frmButtonBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameController.setSide(false);
                Log.d(TAG, "debug -> onClick: clicked black");
                dialog.dismiss();
            }
        });

        FrameLayout frmButtonRed = dialog.findViewById(R.id.frmRed);
        frmButtonRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameController.setSide(true);
                Log.d(TAG, "debug -> onClick: clicked Red");
                dialog.cancel();
            }
        });

        dialog.show();
    }
}
