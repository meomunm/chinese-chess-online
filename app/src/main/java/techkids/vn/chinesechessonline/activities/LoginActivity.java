package techkids.vn.chinesechessonline.activities;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import techkids.vn.chinesechessonline.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.toString();

    private FirebaseAuth firebaseAuth;

    private RelativeLayout rootLayout;
    private EditText etUsername;
    private EditText etPassword;
    private Button btSignUp;
    private Button btSignIn;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //auto hide statusbar
        removeStatusBarAndroid();
        setContentView(R.layout.activity_login);

        //code in here
        init();
        listener();
    }

    private void listener() {
        //clicked sign in
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(etUsername.getText().toString(), etPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, String.format("%s -> onComplete: success", "debug"));
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    updateUI(user);
                                    progressDialog.dismiss();
                                } else {
                                    Log.d(TAG, String.format("%s -> onComplete: ", "debug"));
                                    updateUI(null);
                                    Snackbar.make(rootLayout, "Wrong password or username, try again!", Snackbar.LENGTH_LONG);
                                    progressDialog.dismiss();
                                }
                            }
                        });
            }
        });

        //clicked sign up
//        btSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                firebaseAuth.createUserWithEmailAndPassword()
//            }
//        });
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();

        rootLayout = findViewById(R.id.root_layout_login);
        etPassword = findViewById(R.id.et_password);
        etUsername = findViewById(R.id.et_username);
        btSignIn = findViewById(R.id.bt_signin);
        btSignUp = findViewById(R.id.bt_signup);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Progressing...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
    }


    private void removeStatusBarAndroid() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY // hide status bar and nav bar after a short delay, or if the user interacts with the middle of the screen
        );
        ActionBar actionBar = (ActionBar) getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
        Log.d(TAG, String.format("%s -> onStart: ", "Debug"));
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Destroy activity A and not exist in Back stack
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        removeStatusBarAndroid();
        Log.d(TAG, String.format("%s -> onResume: ", "Debug"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, String.format("%s -> onDestroy: ", "Debug"));
    }
}
