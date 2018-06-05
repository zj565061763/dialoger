package com.fanwe.lib.dialoger.animator;

import android.view.View;

import com.fanwe.lib.dialoger.Dialoger;

/**
 * 动画开始的时候设置锚点：左边，上下居中
 */
public class PivotOnStartLeftCenterCreater extends PivotOnStartCreater
{
    public PivotOnStartLeftCenterCreater(Dialoger.AnimatorCreater creater)
    {
        super(creater);
    }

    @Override
    protected void getPivot(boolean show, View view, float[] pivotXY)
    {
        pivotXY[0] = 0.0f;
        pivotXY[1] = view.getHeight() / 2;
    }
}
