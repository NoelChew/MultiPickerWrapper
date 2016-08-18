package com.noelchew.multipickerwrapper.demo.utils;

import android.content.res.Resources;

/**
 * Created by noelchew on 18/08/2016.
 */
public class PixelUtil {
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
