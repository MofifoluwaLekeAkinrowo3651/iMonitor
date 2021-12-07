package ca.greenops.it.smartbuilding;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.runner.RunWith;

import java.util.Timer;
import java.util.TimerTask;

public class ReviewActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();

    String rating, comment, details;
    static boolean btnPressed = false;
    RatingBar ratingBar;
    Button submit;
    TextView textView;
    ProgressBar progressBar;
    static EditText name,phoneNum,email,cmnt;
    int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        submit = findViewById(R.id.submit_btn);
        textView = findViewById(R.id.ratingView);
        name = findViewById(R.id.fullName);
        phoneNum = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        cmnt = findViewById(R.id.comment);

        //when button is pressed
        submit.setOnClickListener(view -> {

            //if text fields are empty
            if (ratingBar.getRating() == 0)
            {
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.alert)
                        .setTitle(R.string.error)
                        .setMessage(R.string.enterRating)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, null)
                        .show();
            }
            else if (cmnt.getText().toString().isEmpty())
            {
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.alert)
                        .setTitle(R.string.error)
                        .setMessage(R.string.enterComment)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, null)
                        .show();
            }
            else if (name.getText().toString().isEmpty())
            {
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.alert)
                        .setTitle(R.string.error)
                        .setMessage(R.string.enterName)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, null)
                        .show();
            }

            else if (phoneNum.getText().toString().isEmpty())
            {
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.alert)
                        .setTitle(R.string.error)
                        .setMessage(R.string.enterPhone)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, null)
                        .show();
            }

            else if (email.getText().toString().isEmpty())
            {
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.alert)
                        .setTitle(R.string.error)
                        .setMessage(R.string.enterEmail)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, null)
                        .show();
            }
            btnPressed = true;
            rating = String.valueOf(ratingBar.getRating());
            comment = cmnt.getText().toString();
            details = getText(R.string.yourRating) + rating + getText(R.string.comments) + comment;

            textView.setText(getString(R.string.yourRating)+ rating );

            prog();

            name.getText().clear();
            phoneNum.getText().clear();
            email.getText().clear();
            cmnt.getText().clear();
            ratingBar.setRating(0);
        });
    }

    public void prog()
    {
        progressBar = findViewById(R.id.progressBar);

        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                ref.setValue(details);
                counter =100;
            progressBar.setProgress(counter);
            if(counter != 0)
            {
                t.cancel();
            }
            }
        };
        t.schedule(tt,0,100);
    }
}
