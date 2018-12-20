package com.sd.lib.dialoger.animator;

import android.animation.Animator;
import android.view.View;

import com.sd.lib.dialoger.Dialoger;

/**
 * 在动画开始的时候修改view的锚点，动画结束后还原view的锚点
 */
public class PivotPercentCreater extends BaseAnimatorCreater
{
    private final float mPivotPercentX;
    private final float mPivotPercentY;

    private final PivotCreater mPivotCreater;

    /**
     * @param creater
     * @param pivotPercentX x方向锚点百分比[0-1]
     * @param pivotPercentY y方向锚点百分比[0-1]
     */
    public PivotPercentCreater(Dialoger.AnimatorCreater creater, float pivotPercentX, float pivotPercentY)
    {
        mPivotPercentX = pivotPercentX;
        mPivotPercentY = pivotPercentY;

        mPivotCreater = new PivotCreater(creater, new PivotCreater.PivotProvider()
        {
            @Override
            public float getPivot(boolean show, View view)
            {
                return mPivotPercentX * view.getWidth();
            }
        }, new PivotCreater.PivotProvider()
        {
            @Override
            public float getPivot(boolean show, View view)
            {
                return mPivotPercentY * view.getHeight();
            }
        });
    }

    @Override
    protected final Animator onCreateAnimator(boolean show, View view)
    {
        return mPivotCreater.createAnimator(show, view);
    }
}
