package ca.greenops.it.smartbuilding;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

import ca.greenops.it.smartbuilding.R;

/**
 * Smart Building
 * https://github.com/MofifoluwaLekeAkinrowo3651/SmartBuilding
 * Created on 25-SEP-2021.
 * Created by : Team greenOps : Mofifoluwa Leke-Akinrowo (N01343651), Andrew Fraser(N01309442), Bibek Dhakal(N01419953)
 */

class OnboardingPageTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View page, float position) {

        int pagePosition = (int) page.getTag();


        int pageWidth = page.getWidth();
        float pageWidthTimesPosition = pageWidth * position;
        float absPosition = Math.abs(position);


        if (position <= -1.0f || position >= 1.0f) {



        } else if (position == 0.0f) {


        } else {

            View title = page.findViewById(R.id.textView7);
            title.setAlpha(1.0f - absPosition);

            View description = page.findViewById(R.id.textView8);
            description.setTranslationY(-pageWidthTimesPosition / 2f);
            description.setAlpha(1.0f - absPosition);


            View computer = page.findViewById(R.id.imageView3);


            if (computer != null) {
                computer.setAlpha(1.0f - absPosition);
                computer.setTranslationX(-pageWidthTimesPosition * 1.5f);
//                computer.setTranslationY(-pageWidthTimesPosition / 2f);
            }


            if (position < 0) {
                // Create your out animation here
            } else {
                // Create your in animation here
            }
        }
    }

}
