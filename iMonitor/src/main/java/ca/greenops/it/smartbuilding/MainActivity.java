package ca.greenops.it.smartbuilding;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * iMonitor
 * https://github.com/MofifoluwaLekeAkinrowo3651/iMonitor
 * Created on 25-SEP-2021.
 * Created by : Team greenOps : Mofifoluwa Leke-Akinrowo (N01343651), Andrew Fraser(N01309442)
 */

//DESIGN PRINCIPLE INTERFACE SEGREGATION PRINCIPLE
public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    TextView welcome;
    RelativeLayout home_rl;
    private GoogleApiClient googleApiClient;
    String name;
    private TextView ulttv, bmpTextView;
    Button ultbtn;
    Button bmpBtn;

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

        ulttv = findViewById(R.id.UltRead);
        bmpTextView = findViewById(R.id.bmp280_textView);
        ultbtn = findViewById(R.id.ultsonic);
        bmpBtn = findViewById(R.id.bmp);

        home_rl = findViewById(R.id.home_rl);
        ImageButton setting_rl = findViewById(R.id.setting_rl);
        ImageButton review_rl = findViewById(R.id.review);

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

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("distance");


        ultbtn.setOnClickListener((View.OnClickListener) view -> {
            dbref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String value = snapshot.getValue().toString();
                        ulttv.setText("Distance: "+ value + " cm");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
        DatabaseReference bmpDbref = FirebaseDatabase.getInstance().getReference().child("Temperature and pressure");

        bmpBtn.setOnClickListener((View.OnClickListener) view -> {
            bmpDbref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String value = snapshot.getValue().toString();
                        bmpTextView.setText(value);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

                //Builder
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

        //Builder
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(R.string.exit_msg1)
                .setCancelable(false)
                .setPositiveButton(R.string.exit, (dialog, id) -> {
                        this.finish();
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
            //opr.setResultCallback(this::handleSignInResult);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            assert account != null;
            name = account.getDisplayName();
            welcome.setText(new StringBuilder().append(getString(R.string.greet)).append(" ").append(name).toString());
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}