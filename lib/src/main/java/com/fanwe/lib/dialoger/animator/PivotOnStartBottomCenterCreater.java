package com.fanwe.lib.dialoger.animator;

import android.view.View;

import com.fanwe.lib.dialoger.Dialoger;

/**
 * 动画开始的时候设置锚点：底部，左右居中
 */
public class PivotOnStartBottomCenterCreater extends PivotOnStartCreater
{
    public PivotOnStartBottomCenterCreater(Dialoger.AnimatorCreater creater)
    {
        super(creater);
    }

    @Override
    protected void getPivot(boolean show, View view, float[] pivotXY)
    {
        pivotXY[0] = view.getWidth() / 2;
        pivotXY[1] = 0.0f;
    }
}
