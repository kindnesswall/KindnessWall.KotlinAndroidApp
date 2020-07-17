package ir.kindnesswall.utils.transformer

import android.view.View
import androidx.viewpager.widget.ViewPager

/**
 * Created by Javad Vatan on 5/10/2020
 * Sites: http://Jvatan.ir && http://JavadVatan.ir
 */
public class ParallaxPageTransformer : ViewPager.PageTransformer {
    override fun transformPage(view: View, position: Float) {
/*
        View dummyImageView = view.findViewById(R.id.ivImage);
        View tvTitle = view.findViewById(R.id.tvIntroTitle);
        View tvDescription = view.findViewById(R.id.tvIntroDescription);
        View tvAction = view.findViewById(R.id.tvAction);
        int pageWidth = view.getWidth();


        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(1);

        } else if (position <= 1) { // [-1,1]
            tvTitle.setTranslationX((position) * (pageWidth));
            tvDescription.setTranslationX((position) * (pageWidth / 3));
            tvAction.setTranslationX(-position * (pageWidth/2)); //Half the normal speed

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(1);
        }

*/
    }
}