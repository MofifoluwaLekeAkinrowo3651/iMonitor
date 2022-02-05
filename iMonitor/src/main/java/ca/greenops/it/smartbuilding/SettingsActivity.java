package ca.greenops.it.smartbuilding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

/**
 * iMonitor
 * https://github.com/MofifoluwaLekeAkinrowo3651/iMonitor
 * Created on 25-SEP-2021.
 * Created by : Team greenOps : Mofifoluwa Leke-Akinrowo (N01343651), Andrew Fraser(N01309442)
 */

public class SettingsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    FloatingActionButton fab;
    Switch pSwitch;
    Button profileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button logout = findViewById(R.id.logoutBtn);
        fab = findViewById(R.id.fab);
        pSwitch = findViewById(R.id.switch1);
        profileBtn = findViewById(R.id.profile);


        Toast toast= Toast.makeText(this, R.string.potrait, Toast.LENGTH_LONG);
        pSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                toast.show();
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
        });

        profileBtn.setOnClickListener(view -> {
                Intent intent = new Intent(SettingsActivity.this,ProfileActivity.class);
                startActivity(intent);
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "smartbuilding@greenops.com" });
            intent.putExtra(Intent.EXTRA_SUBJECT, "Customer Support:Device#A0o16C9IeX");
            intent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(intent, ""));
        });

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                    status -> {
                        if (status.isSuccess()) {
                            Intent intent1 = new Intent(SettingsActivity.this, LoginActivity.class);
                            startActivity(intent1);
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.failedMsg), Toast.LENGTH_LONG).show();
                        }
                    });

//          DESIGN PATTERN CREATIONAL PATTERN - BUILDER
            NotificationCompat.Builder builder = new NotificationCompat.Builder(SettingsActivity.this, getString(R.string.builderid));
            builder.setContentTitle(getString(R.string.notiftitle));
            builder.setContentText(getString(R.string.notiftext));
            builder.setSmallIcon(R.drawable.logo);
            builder.setAutoCancel(true);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(SettingsActivity.this);
            managerCompat.notify(1, builder.build());

            newIntent();
            SharedPreferences preferences = getSharedPreferences(getString(R.string.prefname), MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("remember", "false");
            editor.apply();
            finish();
        });
    }

    private void newIntent() {
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}

