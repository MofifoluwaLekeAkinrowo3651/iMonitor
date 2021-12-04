package ca.greenops.it.smartbuilding;

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
    RatingBar ratingBar;
    Button submit;
    TextView textView;
    ProgressBar progressBar;
    EditText name,phoneNum,email,cmnt;
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rating = String.valueOf(ratingBar.getRating());
                comment = cmnt.getText().toString();
                details = getText(R.string.yourRating) + rating + getText(R.string.comments) + comment;

                textView.setText(getString(R.string.yourRating)+ rating + getString(R.string.commentString) + comment);

                name.getText().clear();
                phoneNum.getText().clear();
                email.getText().clear();
                cmnt.getText().clear();
                ratingBar.setRating(0);

                prog();
            }

        });
    }

    public void prog()
    {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

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
