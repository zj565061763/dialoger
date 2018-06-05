package com.fanwe.lib.dialoger.animator;

import android.view.View;

import com.fanwe.lib.dialoger.Dialoger;

/**
 * 动画开始的时候设置锚点：右边，上下居中
 */
public class PivotOnStartRightCenterCreater extends PivotOnStartCreater
{
    public PivotOnStartRightCenterCreater(Dialoger.AnimatorCreater creater)
    {
        super(creater);
    }

    @Override
    protected void getPivot(boolean show, View view, float[] pivotXY)
    {
        pivotXY[0] = view.getWidth();
        pivotXY[1] = view.getHeight() / 2;
    }
}
