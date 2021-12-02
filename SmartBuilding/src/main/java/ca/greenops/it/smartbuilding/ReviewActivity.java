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

public class ReviewActivity extends AppCompatActivity implements View.OnClickListener{
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();

    String rating;
    String comment;
    String details;

    RatingBar ratingBar;
    Button button;
    TextView textView;
    ProgressBar progressBar;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        button = findViewById(R.id.submit_btn);
        textView = findViewById(R.id.ratingView);
        //button.setOnClickListener(view -> textView.setText(getString(R.string.yourRating) + ratingBar.getRating()));
    }


    @Override
    public void onClick(View view) {


        textView.setText(getText(R.string.yourRating).toString() + rating);


        rating = String.valueOf(ratingBar.getRating());
        comment = textView.getText().toString();
        details = getText(R.string.yourRating) + rating + getText(R.string.comments) + comment;


        new AlertDialog.Builder(this)
                .setIcon(R.drawable.alert)
                .setTitle(R.string.confirmation)
                .setMessage(R.string.areYouSure)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        prog();

                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();




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
