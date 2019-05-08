package com.info.charith.ogocardview;

import android.content.Context;
import android.graphics.Color;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class Utils {
    //Custom progress bar
    public static ACProgressFlower showProgressDialog(Context context) {
        ACProgressFlower dialog = new ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.RED)
                .fadeColor(Color.WHITE).build();

        return dialog;
    }
}
