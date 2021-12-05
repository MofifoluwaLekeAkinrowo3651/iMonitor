package ca.greenops.it.smartbuilding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {
    TextView textView1,textView2,textView3;
    String name,email,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        textView1 = (TextView) findViewById(R.id.profileUserName);
        textView2 = (TextView) findViewById(R.id.profileEmail);
        textView3 = (TextView) findViewById(R.id.profilePhone);

        name=MainActivity.getActivityInstance().getData();
        textView1.setText(new StringBuilder().append(getString(R.string.user1)).append(name));

        email=LoginActivity.getActivityInstance2().getEmail();
        textView2.setText(new StringBuilder().append(getString(R.string.email1)).append(email));

       // phone=ReviewActivity.getActivityInstance3().getPhone();
        textView3.setText(new StringBuilder().append(getString(R.string.phone1)).append(phone));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

}