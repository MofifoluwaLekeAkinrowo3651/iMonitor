package ca.greenops.it.smartbuilding;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);


        final RatingBar ratingbar = (RatingBar) findViewById(R.id.ratingBar);
        Button button = (Button) findViewById(R.id.submit_btn);
        final TextView textView = (TextView) findViewById(R.id.ratingView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 textView.setText(getString(R.string.yourRating) + ratingbar.getRating());

            }
        });
    }
}
