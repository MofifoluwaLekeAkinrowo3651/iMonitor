package ca.greenops.it.smartbuilding;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReviewActivity extends AppCompatActivity implements View.OnClickListener{

    RatingBar ratingBar;
    Button button;
    TextView textView;

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
        String rating = String.valueOf(ratingBar.getRating());
        String comment = textView.getText().toString();

        textView.setText(getText(R.string.yourRating).toString() + rating);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.alert)
                .setTitle(R.string.confirmation)
                .setMessage(R.string.areYouSure)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String details = getText(R.string.yourRating) + rating + getText(R.string.comments) + comment;
                        ref.setValue(details);

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
}
