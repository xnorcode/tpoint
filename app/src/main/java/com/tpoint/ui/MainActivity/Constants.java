package com.tpoint.ui.MainActivity;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by xnorcode on 18/02/2018.
 */

public class Constants {

    // for the location settings request
    public static final int REQUEST_CODE_CHECK_LOCATION_SETTINGS = 800;

    // for button UI mode
    public static final int TAG_MODE_NORMAL = 0;
    public static final int TAG_MODE_ORANGE = 1;
    public static final int TAG_MODE_RED = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TAG_MODE_NORMAL, TAG_MODE_ORANGE, TAG_MODE_RED})
    public @interface SELECT_TAG_MODE {

    }
}
