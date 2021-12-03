package ca.greenops.it.smartbuilding;

import static org.junit.Assert.assertEquals;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginUnitTest {

    FirebaseAuth mAuth;

    @Test
    public void bademail_goodpass () {
        String uname = "bademail";
        String passWord = "ceng322";

        Task<AuthResult> task = mAuth.signInWithEmailAndPassword(uname, passWord);
        String result = task.getResult().toString().trim();
        assertEquals(result, "Successful");
    }

    @Test
    public void goodemail_badpass () {
        String uname = "test@test.com";
        String passWord = "badpass";

        Task<AuthResult> task = mAuth.signInWithEmailAndPassword(uname, passWord);
        String result = task.getResult().toString().trim();
        assertEquals(result, "Successful");
    }

    @Test
    public void bademail_badpass () {
        String uname = "bademail";
        String passWord = "badpass";

        Task<AuthResult> task = mAuth.signInWithEmailAndPassword(uname, passWord);
        String result = task.getResult().toString().trim();
        assertEquals(result, "Successful");
    }

    @Test
    public void goodemail_goodpass () {
        String uname = "test@test.com";
        String passWord = "test111";

        Task<AuthResult> task = mAuth.signInWithEmailAndPassword(uname, passWord);
        String result = task.getResult().toString().trim();
        assertEquals(result, "Successful");
    }
}
