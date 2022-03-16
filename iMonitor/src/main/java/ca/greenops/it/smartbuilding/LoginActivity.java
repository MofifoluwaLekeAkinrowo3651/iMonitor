package ca.greenops.it.smartbuilding;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
/**
 * iMonitor
 * https://github.com/MofifoluwaLekeAkinrowo3651/iMonitor
 * Created on 25-SEP-2021.
 * Created by : Team greenOps : Mofifoluwa Leke-Akinrowo (N01343651), Andrew Fraser(N01309442)
 */

//DESIGN PRINCIPLE INTERFACE SEGREGATION PRINCIPLE
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    EditText username, password;
    SignInButton signInButton;
    GoogleApiClient googleApiClient;
    CheckBox rememberMe;
    SharedPreferences sharedPref;
    SharedPreferences.Editor edit;
    Button register;
    String usernames;
    ProgressDialog progressDialog;
    static final int RC_SIGN_IN = 0;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListen;

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signInButton = findViewById(R.id.GoogleSignin);
        signInButton.setSize(SignInButton.SIZE_ICON_ONLY);
        rememberMe = findViewById(R.id.rememberme);
        register = findViewById(R.id.reg_btn);
        sharedPref = getSharedPreferences(getString(R.string.checkbox), MODE_PRIVATE);
        String check = sharedPref.getString("remember", "");

        if (check.equals("true")) {
            newIntent();
        }

        progressDialog = new ProgressDialog(LoginActivity.this);
        mAuth = FirebaseAuth.getInstance();
        authListen = firebaseAuth -> {

            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                Log.d(TAG, getString(R.string.usernull) + currentUser.getUid());
            } else {
                // User is signed out
                Log.d(TAG, getString(R.string.userelse));
            }
        };

        usernames = sharedPref.getString(getString(R.string.user), "").trim();
        String passwords = sharedPref.getString(getString(R.string.pass1), "").trim();

        username.setText(usernames);
        password.setText(passwords);

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        rememberMe.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isChecked()) {
                sharedPref = getSharedPreferences(getString(R.string.sharedprefname), MODE_PRIVATE);
                edit = sharedPref.edit();
                edit.putString(getString(R.string.remember), "true");
                edit.apply();
            } else if (!compoundButton.isChecked()) {
                sharedPref = getSharedPreferences(getString(R.string.checkbox), MODE_PRIVATE);
                edit = sharedPref.edit();
                edit.putString(getString(R.string.remember), "false");
                edit.apply();
            }
        });

        signInButton.setOnClickListener(view -> {
            signIn();
        });

        register.setOnClickListener(view -> {
            Intent reg = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(reg);
        });
    }

    private void newIntent() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onLoginClicked(View view) {
       FirebaseDatabase database = FirebaseDatabase.getInstance();
       DatabaseReference ref = database.getReference();
       String uname = username.getText().toString();
       String passWord = password.getText().toString();

       if (uname.isEmpty() || !uname.contains("@")){
           new AlertDialog.Builder(this)
                   .setIcon(R.drawable.alert)
                   .setTitle(R.string.wrongLoginTitle)
                   .setMessage(R.string.wrongLogin)
                   .setCancelable(false)
                   .setPositiveButton(R.string.ok,null)
                   .show();
       }
       else if (passWord.isEmpty() || passWord.length()<6) {
           new AlertDialog.Builder(this)
                   .setIcon(R.drawable.alert)
                   .setTitle(R.string.wrongLoginTitle)
                   .setMessage(R.string.wrongLogin)
                   .setCancelable(false)
                   .setPositiveButton(R.string.ok,null)
                   .show();
       } else {
           progressDialog.setTitle(getString(R.string.dialogtitle));
           progressDialog.setMessage(getString(R.string.dialogmessage));
           progressDialog.setCanceledOnTouchOutside(false);
           progressDialog.show();

           mAuth.signInWithEmailAndPassword(uname, passWord)
           .addOnCompleteListener(task -> {
               if (task.isSuccessful()) {
                   FirebaseUser user = mAuth.getCurrentUser();
                   updateUI(user);
                   String details = getString(R.string.username) + uname + getString(R.string.pass) + passWord;
                   ref.setValue(details);
                   progressDialog.dismiss();

                   Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                   intent.putExtra("username", uname);
                   startActivity(intent);
               } else {
                   updateUI(null);
                   progressDialog.dismiss();
                   Snackbar.make(view, "Couldn't Log in. Try again later", BaseTransientBottomBar.LENGTH_SHORT)
                           .show();
               }
           });
       }
  }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(FirebaseUser user) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            assert result != null;
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult completedTask) {
        if (completedTask.isSuccess()) {
            GoogleSignInAccount account = completedTask.getSignInAccount();
            assert account != null;
            String idToken = account.getIdToken();
            String name = account.getDisplayName();
            String email = account.getEmail();
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
            firebaseAuthWithGoogle(credential);
        } else {
            Log.e(TAG, getString(R.string.loginMsg)+completedTask);
            Toast.makeText(this,getString( R.string.loginMsg1), Toast.LENGTH_SHORT).show();        }
    }

    private void firebaseAuthWithGoogle(AuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    Log.d(TAG, getString(R.string.msg6) + task.isSuccessful());

                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, getString(R.string.successLogin), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authListen != null) {
            FirebaseAuth.getInstance().signOut();
        }
        assert authListen != null;
        mAuth.addAuthStateListener(authListen);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authListen != null){
            mAuth.removeAuthStateListener(authListen);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(R.string.exit_msg1)
                .setCancelable(false)
                .setPositiveButton(R.string.exit, (dialog, id) -> {
                    this.finish();
                })
                .setNegativeButton(R.string.stay, null)
                .show();
    }
}

