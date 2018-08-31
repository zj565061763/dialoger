package com.sd.lib.dialoger.animator;

import android.view.View;

/**
 * 缩放y
 */
public class ScaleYCreater extends ObjectAnimatorCreater
{
    private float mValueOriginal;

    @Override
    protected void beforeCreateAnimator(boolean show, View view)
    {
        super.beforeCreateAnimator(show, view);
        if (show)
            mValueOriginal = view.getScaleY();
    }

    @Override
    protected final String getPropertyName()
    {
        return View.SCALE_Y.getName();
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
        return view.getScaleY();
    }

    @Override
    protected void onAnimationEnd(boolean show, View view)
    {
        super.onAnimationEnd(show, view);
        if (!show)
            view.setScaleY(mValueOriginal);
    }
}
