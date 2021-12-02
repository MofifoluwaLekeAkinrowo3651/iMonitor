package ca.greenops.it.smartbuilding;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        submit = findViewById(R.id.submit_btn);
        textView = findViewById(R.id.ratingView);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = String.valueOf(ratingBar.getRating());
              //  comment = textView.getText().toString();
               // details = rating + comment;

                textView.setText(getString(R.string.yourRating) + rating);
               // textView.setText(getString(R.string.yourRating) + details);

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
