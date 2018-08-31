package com.sd.lib.dialoger.animator;

import android.animation.Animator;
import android.view.View;

import com.sd.lib.dialoger.Dialoger;

/**
 * 在动画开始的时候修改view的锚点，动画结束后还原view的锚点
 */
public class PivotCreater extends BaseAnimatorCreater
{
    private final Dialoger.AnimatorCreater mCreater;
    private final float mPivotXPercent;
    private final float mPivotYPercent;

    private PivotHolder mPivotHolder;

    /**
     * @param creater
     * @param pivotXPercent x方向锚点百分比[0-1]
     * @param pivotYPercent y方向锚点百分比[0-1]
     */
    public PivotCreater(Dialoger.AnimatorCreater creater, float pivotXPercent, float pivotYPercent)
    {
        if (creater == null)
            throw new NullPointerException("creater is null");

        mCreater = creater;
        mPivotXPercent = pivotXPercent;
        mPivotYPercent = pivotYPercent;
    }

    protected final PivotHolder getPivotHolder()
    {
        if (mPivotHolder == null)
            mPivotHolder = new PivotHolder();
        return mPivotHolder;
    }

    @Override
    protected final Animator onCreateAnimator(boolean show, View view)
    {
        return mCreater.createAnimator(show, view);
    }

    @Override
    protected void onAnimationStart(boolean show, View view)
    {
        super.onAnimationStart(show, view);
        getPivotHolder().setPivotXY(mPivotXPercent * view.getWidth(), mPivotYPercent * view.getHeight(), view);
    }

    @Override
    protected void onAnimationEnd(boolean show, View view)
    {
        super.onAnimationEnd(show, view);
        getPivotHolder().restore(view);
    }

    private static class PivotHolder
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
}
