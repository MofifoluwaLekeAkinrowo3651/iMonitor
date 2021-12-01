package ca.greenops.it.smartbuilding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    Button button;
    EditText email, pword;
    FirebaseAuth mAuth;
    Pattern pattern;
    final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&=!])(?=\\S+$).{4,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        button = findViewById(R.id.reg_button);
        email = findViewById(R.id.reg_email);
        pword = findViewById(R.id.reg_pass);
        mAuth = FirebaseAuth.getInstance();
        pattern = Pattern.compile(PASSWORD_PATTERN);

        button.setOnClickListener(view -> {
            String email_2 = email.getText().toString().trim();
            String password = pword.getText().toString().trim();

            if (email_2.isEmpty()) {
                email.setError("Email is empty");
                email.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email_2).matches()) {
                email.setError("Enter the valid email");
                email.requestFocus();
                return;
            }
            if (!pattern.matcher(password).matches()) {
                pword.setError("Enter the valid password");
                pword.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                pword.setError("Password is empty");
                pword.requestFocus();
                return;
            }
            if (password.length() < 8) {
                pword.setError("Length should be at least 8 characters");
                pword.requestFocus();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email_2, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(RegistrationActivity.this, "You are successfully Registered", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(RegistrationActivity.this, "You are not Registered! Try again", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }


}

