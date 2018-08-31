package com.sd.lib.dialoger.animator;

import android.view.View;

/**
 * 向左滑入，向右滑出
 */
public class SlideLeftRightCreater extends SlideHorizontalCreater
{
    private float mValueOriginal;

    @Override
    protected void beforeCreateAnimator(boolean show, View view)
    {
        super.beforeCreateAnimator(show, view);
        if (show)
            mValueOriginal = view.getTranslationX();
    }

    @Override
    protected float getValueHidden(View view)
    {
        return view.getWidth();
    }

    @Override
    protected float getValueShown(View view)
    {
        return mValueOriginal;
    }

    @Override
    protected void onAnimationEnd(boolean show, View view)
    {
        super.onAnimationEnd(show, view);
        if (!show)
            view.setTranslationX(mValueOriginal);
    }
}
