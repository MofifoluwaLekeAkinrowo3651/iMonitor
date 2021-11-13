package ca.greenops.it.smartbuilding;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReviewActivity extends AppCompatActivity {

    RatingBar ratingBar;
    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ratingBar = findViewById(R.id.ratingBar);
        button = findViewById(R.id.submit_btn);
        textView = findViewById(R.id.ratingView);

        button.setOnClickListener(view -> textView.setText(getString(R.string.yourRating) + ratingBar.getRating()));
    }
}
