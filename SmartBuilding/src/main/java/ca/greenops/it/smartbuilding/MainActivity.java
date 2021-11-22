package ca.greenops.it.smartbuilding;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.app.AlertDialog;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * Smart Building
 * https://github.com/MofifoluwaLekeAkinrowo3651/SmartBuilding
 * Created on 25-SEP-2021.
 * Created by : Team greenOps : Mofifoluwa Leke-Akinrowo (N01343651), Andrew Fraser(N01309442), Bibek Dhakal(N01419953)
 */

//DESIGN PRINCIPLE INTERFACE SEGREGATION PRINCIPLE
public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private final List<Room> roomList = new ArrayList<>();
    RecyclerView recyclerView;
    private RoomAdapter mAdapter;
    TextView welcome;
    RelativeLayout home_rl;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    Button logout;
    String name;

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
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Notification", getString(R.string.channelname), NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        logout = findViewById(R.id.logoutBtn);
        home_rl = findViewById(R.id.home_rl);
        ImageButton setting_rl = findViewById(R.id.setting_rl);
        ImageButton review_rl = findViewById(R.id.review);

        mAdapter = new RoomAdapter(roomList, getApplicationContext());
        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        prepareRoomData();

        welcome = findViewById(R.id.hiuser);
        Intent intent = getIntent();
        name = intent.getStringExtra("username");
        welcome.setText(new StringBuilder().append(getString(R.string.greet)).append(" ").append(name).append("\n").toString());
        home_rl.setOnClickListener(v -> home_rl.setBackgroundResource(0));

        setting_rl.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
        });

        review_rl.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, ReviewActivity.class);
            startActivity(i);
        });


        //Builder
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build();

        //Builder
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                    status -> {
                        if (status.isSuccess()) {
                            Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent1);
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.failedMsg), Toast.LENGTH_LONG).show();
                        }
                    });

//          DESIGN PATTERN CREATIONAL PATTERN - BUILDER
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, getString(R.string.builderid));
            builder.setContentTitle(getString(R.string.notiftitle));
            builder.setContentText(getString(R.string.notiftext));
            builder.setSmallIcon(R.drawable.logo);
            builder.setAutoCancel(true);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
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
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void prepareRoomData() {
        Room room1 = new Room("1", getString(R.string.bedroom));
        roomList.add(room1);
        Room room2 = new Room("2", getString(R.string.kitchen));
        roomList.add(room2);
        Room room3 = new Room("3", getString(R.string.hallway));
        roomList.add(room3);
        Room room4 = new Room("4", getString(R.string.lockDoord));
        roomList.add(room4);
        Room room5 = new Room("5", getString(R.string.solarPanels));
        roomList.add(room5);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(R.string.exit_msg1)
                .setCancelable(false)
                .setPositiveButton(R.string.exit, (dialog, id) -> {

                    CheckBox checkBox = findViewById(R.id.rememberme);
                    if (checkBox.isChecked()) {
                        finish();
                    } else if (!checkBox.isChecked()) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }

                })
                .setNegativeButton(R.string.stay, null)
                .show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result=opr.get();
            handleSignInResult(result);
        }else{
            opr.setResultCallback(this::handleSignInResult);
        }
    }
    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            name = account.getDisplayName();
            welcome.setText(getString(R.string.greet)+" "+ name);
        }else{

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}