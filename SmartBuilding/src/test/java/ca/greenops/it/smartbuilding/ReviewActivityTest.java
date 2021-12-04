package ca.greenops.it.smartbuilding;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;


@RunWith(RobolectricTestRunner.class)
public class ReviewActivityTest{

    @Test
    public void clickingSubmit()
    {
        ReviewActivity activity = Robolectric.setupActivity(ReviewActivity.class);
        activity.findViewById(R.id.submit_btn).performClick();


    }

}
