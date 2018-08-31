package com.sd.lib.dialoger.animator;

import android.view.View;

/**
 * 缩放x
 */
public class ScaleXCreater extends ObjectAnimatorCreater
{
    private float mValueOriginal;

    @Override
    protected void beforeCreateAnimator(boolean show, View view)
    {
        super.beforeCreateAnimator(show, view);
        if (show)
            mValueOriginal = view.getScaleX();
    }

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
        return mValueOriginal;
    }

    @Override
    protected float getValueCurrent(View view)
    {
        return view.getScaleX();
    }

    @Override
    protected void onAnimationEnd(boolean show, View view)
    {
        super.onAnimationEnd(show, view);
        if (!show)
            view.setScaleX(mValueOriginal);
    }
}
