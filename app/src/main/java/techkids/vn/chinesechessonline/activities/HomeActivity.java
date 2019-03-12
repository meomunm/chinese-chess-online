package techkids.vn.chinesechessonline.activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

import techkids.vn.chinesechessonline.R;
import techkids.vn.chinesechessonline.models.Game;
import techkids.vn.chinesechessonline.models.User;
import techkids.vn.chinesechessonline.utils.Util;

public class HomeActivity extends AppCompatActivity {
    private final String TAG = HomeActivity.class.toString();
    private String chanel_id;
    private String chanel_name;

    ImageButton ibFindMatch;
    ImageButton ibUser;

    private final String UID_USER = FirebaseAuth.getInstance().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        getPushToken();
        Util.savePushToken(FirebaseInstanceId.getInstance().getToken(), FirebaseAuth.getInstance().getCurrentUser().getUid());


//        createNotificationChannel();
        Log.d(TAG, String.format("onCreate: idpush() => %s", FirebaseInstanceId.getInstance().getToken()));

        //test subcribe topic
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
        firebaseMessaging.subscribeToTopic("games");
        firebaseMessaging.subscribeToTopic("all");

        init();
        listenner();
    }


    //create chanel notification id -> used in manifest



    private void init() {
        ibFindMatch = findViewById(R.id.ib_findMatch);
        ibUser = findViewById(R.id.ib_user);
    }

    private void listenner() {
        ibFindMatch.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, GameActivity.class);
            startActivity(intent);
        });

        ibUser.setOnClickListener(v -> {
//                addUser();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
//            testPushUserToFirebase();
        });
    }

    public boolean isNetworkAvailable(Context con) {
        try {
            ConnectivityManager cm = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private void logoutAccount(){
        FirebaseAuth.getInstance().signOut();
    }

    //add user thực lúc lúc đăng ký tài khoản thành công
    public void addToAvailable() {
        String uid_user = FirebaseAuth.getInstance().getUid();
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        DatabaseReference reference = instance.getReference("/available/users");

        Map<String, String> user = new HashMap<>();
        user.put("uid_user", uid_user);

        reference.child(uid_user).setValue(user);
    }


    private void testPushUserToFirebase() {
        User user = new User();
        user.setName("MeoMunm");
        user.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());

        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user);
    }

    private void getPushToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        Log.d(TAG, token);
                    }
                });

    }
}
