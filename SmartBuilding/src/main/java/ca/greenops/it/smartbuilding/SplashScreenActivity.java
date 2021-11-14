package ca.greenops.it.smartbuilding;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * Smart Building
 * https://github.com/MofifoluwaLekeAkinrowo3651/SmartBuilding
 * Created on 25-SEP-2021.
 * Created by : Team greenOps : Mofifoluwa Leke-Akinrowo (N01343651), Andrew Fraser(N01309442), Bibek Dhakal(N01419953)
 */

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.backgroundColor));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);


        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashScreenActivity.this, OnboardActivity.class);
            startActivity(i);
            finish();
            }, 3000);


    }
}
