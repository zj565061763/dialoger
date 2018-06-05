package com.fanwe.lib.dialoger.utils;

import android.view.View;

public class PivotHolder
{
    private final float[] mPivotXYOriginal = new float[2];

    public void setPivotXY(float pivotX, float pivotY, View view)
    {
        if (view == null)
            return;

        mPivotXYOriginal[0] = view.getPivotX();
        mPivotXYOriginal[1] = view.getPivotY();

        view.setPivotX(pivotX);
        view.setPivotY(pivotY);
    }

    public void restore(View view)
    {
        if (view == null)
            return;

        view.setPivotX(mPivotXYOriginal[0]);
        view.setPivotY(mPivotXYOriginal[1]);
    }
}
