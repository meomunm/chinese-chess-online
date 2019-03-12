package techkids.vn.chinesechessonline.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import techkids.vn.chinesechessonline.models.User;
import techkids.vn.chinesechessonline.utils.Util;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = MyReceiver.class.toString();


    @Override
    public void onReceive(Context context, final Intent intent) {
        Log.d(TAG, "onReceive: " + intent.getAction());

        FirebaseDatabase.getInstance().getReference().child("users")
                .child(Util.getCurrentUserID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //used okhttpclient instead retrofit
                        User me = dataSnapshot.getValue(User.class);
                        OkHttpClient okHttpClient = new OkHttpClient();

                        String to = intent.getExtras().getString("to");
                        String withId = intent.getExtras().getString("withId");

                        String urlApi = String.format("%s/sendNoti?to=%s&fromId=%s&fromPushId=%s&fromName=%s&type=%s"
                                , Util.cloudfunctionURL
                                , to
                                , me.getId()
                                , Util.getCurrentUserID()
                                , me.getName()
                                , intent.getAction());

                        Log.d(TAG, "onDataChange: " + urlApi);
                        Request request = new Request.Builder().url(urlApi).build();

                        //push noti
                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d(TAG, "onFailure: onsuccess");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.d(TAG, "onResponse: onfail -> "+response.message());
                            }
                        });

                        //create room and push to database
                        if (intent.getAction().equals("accept")) {
                            String gameId = withId + "-" + Util.getCurrentUserID();
                            FirebaseDatabase.getInstance().getReference().child("games")
                                    .child(gameId)
                                    .setValue(null);

                            // TODO: 3/2/2019 start activity
                        }
                    }

                    // TODO: 3/6/2019 check lai noti tren 2 thiet bi, done phan noti -> set luot xem ai choi truoc
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }


}
