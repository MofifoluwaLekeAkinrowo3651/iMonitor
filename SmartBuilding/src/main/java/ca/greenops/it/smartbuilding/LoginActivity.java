package ca.greenops.it.smartbuilding;

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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Smart Building
 * https://github.com/MofifoluwaLekeAkinrowo3651/SmartBuilding
 * Created on 25-SEP-2021.
 * Created by : Team greenOps : Mofifoluwa Leke-Akinrowo (N01343651), Andrew Fraser(N01309442), Bibek Dhakal(N01419953)
 */
public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    CheckBox rememberMe;
    SharedPreferences sharedPref;
    SharedPreferences.Editor edit;
    static final int RC_SIGN_IN = 0;

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

        String usernames = sharedPref.getString("username", "");
        String passwords = sharedPref.getString("passwords", "");

        username.setText(usernames);
        password.setText(passwords);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(view -> {
            switch (view.getId()) {
                case R.id.GoogleSignin:
                    signIn();
                    break;
            }

        if(rememberMe.isChecked()){
                edit.putString("username",username.getText().toString());
                edit.putString("password",password.getText().toString());
        }else{
                edit.putString("username","");
                edit.putString("password","");
        }
            edit.commit();
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
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
           startActivity(new Intent(getApplicationContext(), MainActivity.class));
       }
  }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(this, "Sign-in Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } catch (ApiException e) {
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null)
        {
            Toast.makeText(this, "User already Signed-in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
    }
}

