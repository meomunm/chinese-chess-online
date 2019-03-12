package techkids.vn.chinesechessonline.utils;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Util {
    private static final String TAG = Util.class.toString();
    public static final String cloudfunctionURL = "https://us-central1-chinese-chess-online.cloudfunctions.net";

    //save to firebase database
    public static void savePushToken(String refreshToken, String userID){
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(userID)
                .child("pushID")
                .setValue(refreshToken);
        Log.d(TAG, "savePushToken: luu token");
    }

    public static String getCurrentUserID(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null || currentUser.isAnonymous()){
            return "";
        }else {
            return currentUser.getUid();
        }
    }
}
