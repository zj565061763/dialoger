package com.fanwe.lib.dialoger.animator;

import android.animation.Animator;
import android.view.View;

import com.fanwe.lib.dialoger.Dialoger;
import com.fanwe.lib.dialoger.utils.PivotHolder;

public abstract class PivotOnStartCreater extends BaseAnimatorCreater
{
    private final float[] mPivotXY = new float[2];
    private final PivotHolder mPivotHolder = new PivotHolder();
    private Dialoger.AnimatorCreater mCreater;

    public PivotOnStartCreater(Dialoger.AnimatorCreater creater)
    {
        if (creater == null)
            throw new NullPointerException("creater is null");
        mCreater = creater;
    }

    @Override
    protected Animator onCreateAnimator(boolean show, View view)
    {
        return mCreater.createAnimator(show, view);
    }

    @Override
    protected void onAnimationStart(boolean show, View view)
    {
        getPivot(show, view, mPivotXY);
        mPivotHolder.setPivotXY(mPivotXY[0], mPivotXY[1], view);
    }

    @Override
    protected void onAnimationEnd(boolean show, View view)
    {
        mPivotHolder.restore(view);
    }

    protected abstract void getPivot(boolean show, View view, float[] pivotXY);
}
