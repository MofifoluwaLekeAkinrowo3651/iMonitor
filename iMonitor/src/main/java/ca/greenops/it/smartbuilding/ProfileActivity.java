package ca.greenops.it.smartbuilding;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * iMonitor
 * https://github.com/MofifoluwaLekeAkinrowo3651/iMonitor
 * Created on 25-SEP-2021.
 * Created by : Team greenOps : Mofifoluwa Leke-Akinrowo (N01343651), Andrew Fraser(N01309442)
 */

public class ProfileActivity extends AppCompatActivity {
    TextView textView1,textView2,textView3,textView4;

    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textView1 = findViewById(R.id.profileUserName);
        textView2 = findViewById(R.id.profileEmail);
        textView3 = findViewById(R.id.profilePhone);
        textView4 = findViewById(R.id.profileUID);

        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        textView1.setText(new StringBuilder().append(getString(R.string.un)).append(currentUser.getDisplayName()));
        textView2.setText(new StringBuilder().append(getString(R.string.Email)).append(currentUser.getEmail()));
        textView3.setText(new StringBuilder().append(getString(R.string.Phone)).append(currentUser.getPhoneNumber()));
        textView4.setText(new StringBuilder().append(getString(R.string.UID)).append(currentUser.getUid()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

}