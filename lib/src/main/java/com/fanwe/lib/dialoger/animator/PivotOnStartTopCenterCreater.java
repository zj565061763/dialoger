package com.fanwe.lib.dialoger.animator;

import android.view.View;

import com.fanwe.lib.dialoger.Dialoger;

/**
 * 动画开始的时候设置锚点：顶部，左右居中
 */
public class PivotOnStartTopCenterCreater extends PivotOnStartCreater
{
    public PivotOnStartTopCenterCreater(Dialoger.AnimatorCreater creater)
    {
        super(creater);
    }

    @Override
    protected void getPivot(boolean show, View view, float[] pivotXY)
    {
        pivotXY[0] = view.getWidth() / 2;
        pivotXY[1] = view.getHeight();
    }
}
