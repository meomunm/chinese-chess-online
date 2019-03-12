package techkids.vn.chinesechessonline.activities;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import techkids.vn.chinesechessonline.R;
import techkids.vn.chinesechessonline.models.User;

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
        btSignIn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etUsername.getText().toString()) || TextUtils.isEmpty(etPassword.getText().toString())){
                Snackbar.make(rootLayout, "Email và Password không được để trống!", Snackbar.LENGTH_LONG).show();
                return;
            }

            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(etUsername.getText().toString(), etPassword.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, String.format("%s -> onComplete: success", "debug"));
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                            progressDialog.dismiss();
                        } else {
                            Snackbar.make(rootLayout, "Sai tài khoản hoặc mật khẩu, hãy thử lại!", Snackbar.LENGTH_LONG).show();
                            Log.d(TAG, String.format("%s -> onComplete: fail", "debug"));
                            updateUI(null);
                            progressDialog.dismiss();
                        }
                    });
        });

        //clicked sign up (đăng ký account)
        btSignUp.setOnClickListener(v -> {
            showRegisterDiaglog();
            Snackbar.make(rootLayout, "LoginActivity.java/83", Snackbar.LENGTH_LONG).show();

        });
    }

    private void showRegisterDiaglog() {
        final AlertDialog.Builder registerDialog = new AlertDialog.Builder(this);
        registerDialog.setTitle("REGISTER ACCOUNT");
        registerDialog.setMessage("Please use mail to register");

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayoutInflate = layoutInflater.inflate(R.layout.layout_register, null);

        MaterialEditText edtName = viewLayoutInflate.findViewById(R.id.edt_register_name);
        MaterialEditText edtMail = viewLayoutInflate.findViewById(R.id.edt_register_email);
        MaterialEditText edtPassword = viewLayoutInflate.findViewById(R.id.edt_register_password);

        Log.d(TAG, String.format("showRegisterDiaglog: mail = %s \nname =%s\n pass = %s", edtMail, edtName, edtPassword));
        registerDialog.setView(viewLayoutInflate);
        registerDialog.setPositiveButton("REGISTER", ((dialog, which) -> {
            if (TextUtils.isEmpty(edtMail.getText().toString())) {
                Snackbar.make(rootLayout, "Please enter your mail!!", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (edtPassword.getText().length() < 6) {
                Snackbar.make(rootLayout, "Password too short!", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(edtName.getText().toString())) {
                Snackbar.make(rootLayout, "Please enter your name!", Snackbar.LENGTH_SHORT).show();
                return;
            }
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(edtMail.getText().toString(), edtPassword.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        User user = new User();
                        user.setId(FirebaseAuth.getInstance().getUid());
                        user.setName(edtName.getText().toString());

                        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user)
                                .addOnSuccessListener((aVoid) -> {
                                    Snackbar.make(rootLayout, "Register success fully", Snackbar.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                })
                                .addOnFailureListener((e) -> {
                                    Snackbar.make(rootLayout, "Register success fully", Snackbar.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                });

                    })
                    .addOnFailureListener(e -> {
                        Snackbar.make(rootLayout, "Failed " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    });
        }));

        registerDialog.setNegativeButton("Cancel", (dialog, which) -> {

        });

        registerDialog.show();
        Log.d(TAG, "showRegisterDiaglog: show dialog register.");
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

    //nếu đăng nhập rồi thì không cần phải đăng nhập lại nữa
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
        Log.d(TAG, String.format("%s -> onStart: ", "Debug"));
    }

    //chuyển màn hình nếu tồn tại tài khoản
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Snackbar.make(rootLayout, "Đăng nhập thành công!", Snackbar.LENGTH_LONG).show();

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
