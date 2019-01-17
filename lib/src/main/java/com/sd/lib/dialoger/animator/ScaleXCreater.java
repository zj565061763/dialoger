package com.sd.lib.dialoger.animator;

import android.view.View;

/**
 * 缩放x
 */
public class ScaleXCreater extends ObjectAnimatorCreater
{
    @Override
    protected final String getPropertyName()
    {
        return View.SCALE_X.getName();
    }

    @Override
    protected float getValueHidden(View view)
    {
        return 0.0f;
    }

    @Override
    protected float getValueShown(View view)
    {
        return 1.0f;
    }

    @Override
    protected float getValueCurrent(View view)
    {
        return view.getScaleX();
    }
}