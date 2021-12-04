package ca.greenops.it.smartbuilding;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import org.robolectric.shadows.ShadowToast;

import android.app.Activity;

import android.widget.Button;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;



@RunWith(RobolectricTestRunner.class)
public class ReviewActivityTest{
    Activity activity;

    @Before public void init()
    {
        ReviewActivity activity = Robolectric.setupActivity(ReviewActivity.class);
    }


    @Test
    public void nameTest()
    {
        EditText result = (EditText) activity.findViewById (R.id.fullName);
        String resultsText = result.getText().toString();
        assertThat(resultsText, equalTo(ReviewActivity.name));
    }

    @Test
    public void emailTest()
    {
        EditText result = (EditText) activity.findViewById (R.id.email);
        String resultsText = result.getText().toString();
        assertThat(resultsText, equalTo(ReviewActivity.email));
    }

    @Test
    public void phoneNumTest()
    {
        EditText result = (EditText) activity.findViewById (R.id.phone);
        String resultsText = result.getText().toString();
        assertThat(resultsText, equalTo(ReviewActivity.phoneNum));
    }

    @Test
    public void commentTest()
    {
        EditText result = (EditText) activity.findViewById (R.id.comment);
        String resultsText = result.getText().toString();
        assertThat(resultsText, equalTo(ReviewActivity.cmnt));
    }

}
