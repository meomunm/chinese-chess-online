package techkids.vn.chinesechessonline.services;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import techkids.vn.chinesechessonline.utils.Util;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private final String TAG = MyFirebaseInstanceIDService.class.toString();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null || currentUser.isAnonymous()){
            return;
        }
        Log.d(TAG, "onTokenRefresh: ");
        Util.savePushToken(refreshToken, currentUser.getUid());
    }
}
