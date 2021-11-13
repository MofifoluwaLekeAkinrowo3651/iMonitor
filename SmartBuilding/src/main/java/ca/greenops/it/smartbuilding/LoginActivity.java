package ca.greenops.it.smartbuilding;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Smart Building
 * https://github.com/MofifoluwaLekeAkinrowo3651/SmartBuilding
 * Created on 25-SEP-2021.
 * Created by : Team greenOps : Mofifoluwa Leke-Akinrowo (N01343651), Andrew Fraser(N01309442), Bibek Dhakal(N01419953)
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    EditText username, password;
    SignInButton signInButton;
    GoogleApiClient googleApiClient;
    CheckBox rememberMe;
    SharedPreferences sharedPref;
    SharedPreferences.Editor edit;
    static final int RC_SIGN_IN = 0;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListen;
    private static final String EP = "EmailPassword";

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
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        rememberMe = findViewById(R.id.rememberme);
        sharedPref = getSharedPreferences("LoginPref", MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        authListen = firebaseAuth -> {

            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                Log.d(TAG, "onAuthStateChanged:signed_in:" + currentUser.getUid());
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
        };

        String usernames = sharedPref.getString(getString(R.string.user), "");
        String passwords = sharedPref.getString(getString(R.string.pass1), "");

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

        signInButton.setOnClickListener(view -> {
            signIn();
        });
    }

   public void onLoginClicked(View view) {
       FirebaseDatabase database = FirebaseDatabase.getInstance();
       DatabaseReference ref = database.getReference();
       String uname = username.getText().toString();
       String passWord = username.getText().toString();
       String details = getString(R.string.username) + uname + getString(R.string.pass) + passWord;

       ref.setValue(details);

       if (uname.isEmpty() && passWord.isEmpty()) {
           new AlertDialog.Builder(this)
                   .setIcon(R.drawable.alert)
                   .setTitle(R.string.wrongLoginTitle)
                   .setMessage(R.string.wrongLogin)
                   .setCancelable(false)
                   .setPositiveButton(R.string.ok,null)
                   .show();
       }
       if (uname.isEmpty() || passWord.isEmpty()) {
           new AlertDialog.Builder(this)
                   .setIcon(R.drawable.alert)
                   .setTitle(R.string.wrongLoginTitle)
                   .setMessage(R.string.wrongLogin)
                   .setCancelable(false)
                   .setPositiveButton(R.string.ok,null)
                   .show();
       } else {
           Intent i = new Intent(getApplicationContext(), MainActivity.class);
           i.putExtra("username", uname);
           startActivity(i);
       }
  }

    private void rememberMe() {
        if(rememberMe.isChecked()){
           edit.putString("username",username.getText().toString());
           edit.putString("password",password.getText().toString());
        }else{
           edit.putString("username","");
           edit.putString("password","");
        }
        edit.commit();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(EP, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void sendEmailVerification() {
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, task -> {
                    // Email sent
                });
    }

    private void reload() { }

    private void updateUI(FirebaseUser user) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult completedTask) {
        if (completedTask.isSuccess()) {
            GoogleSignInAccount account = completedTask.getSignInAccount();
            String idToken = account.getIdToken();
            String name = account.getDisplayName();
            String email = account.getEmail();
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
            firebaseAuthWithGoogle(credential);

            Toast.makeText(this, getString(R.string.successMsg), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Log.e(TAG, getString(R.string.loginMsg)+completedTask);
            Toast.makeText(this,getString( R.string.loginMsg1), Toast.LENGTH_SHORT).show();        }
    }

    private void firebaseAuthWithGoogle(AuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, getString(R.string.msg6) + task.isSuccessful());

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, getString(R.string.successLogin), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (authListen != null) {
            mAuth.getInstance().signOut();
        }
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
}

