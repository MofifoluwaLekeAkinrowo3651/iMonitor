package ca.greenops.it.smartbuilding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Smart Building
 * https://github.com/MofifoluwaLekeAkinrowo3651/SmartBuilding
 * Created on 25-SEP-2021.
 * Created by : Team greenOps : Mofifoluwa Leke-Akinrowo (N01343651), Andrew Fraser(N01309442), Bibek Dhakal(N01419953)
 */

public class SettingsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button logout = findViewById(R.id.logoutBtn);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, (GoogleApiClient.OnConnectionFailedListener) this)
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

