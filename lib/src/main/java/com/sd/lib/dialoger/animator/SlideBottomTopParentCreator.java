package com.sd.lib.dialoger.animator;

import android.view.View;

/**
 * 向下滑入，向上滑出相对于内容View父容器）
 */
public class SlideBottomTopParentCreator extends SlideVerticalCreator
{
    @Override
    protected float getValueHidden(View view)
    {
        return -(view.getHeight() + view.getTop());
    }

    @Override
    protected float getValueShown(View view)
    {
        return 0;
    }
}
