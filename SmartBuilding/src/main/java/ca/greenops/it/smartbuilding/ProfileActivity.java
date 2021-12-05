package ca.greenops.it.smartbuilding;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    TextView textView1,textView2,textView3,textView4;

    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textView1 = (TextView) findViewById(R.id.profileUserName);
        textView2 = (TextView) findViewById(R.id.profileEmail);
        textView3 = (TextView) findViewById(R.id.profilePhone);
        textView4 = (TextView) findViewById(R.id.profileUID);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
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