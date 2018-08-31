package com.sd.lib.dialoger.animator;

import android.view.View;

/**
 * 透明度
 */
public class AlphaCreater extends ObjectAnimatorCreater
{
    private float mValueOriginal;

    @Override
    protected void beforeCreateAnimator(boolean show, View view)
    {
        super.beforeCreateAnimator(show, view);
        if (show)
            mValueOriginal = view.getAlpha();
    }

    @Override
    protected final String getPropertyName()
    {
        return View.ALPHA.getName();
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
        return view.getAlpha();
    }

    @Override
    protected void onAnimationEnd(boolean show, View view)
    {
        super.onAnimationEnd(show, view);
        if (!show)
            view.setAlpha(mValueOriginal);
    }
}
