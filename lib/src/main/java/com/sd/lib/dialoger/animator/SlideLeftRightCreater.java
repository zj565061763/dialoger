package com.sd.lib.dialoger.animator;

import android.view.View;

/**
 * 向左滑入，向右滑出
 */
public class SlideLeftRightCreater extends SlideHorizontalCreater
{
    @Override
    protected float getValueHidden(View view)
    {
        return view.getWidth();
    }

    @Override
    protected float getValueShown(View view)
    {
        return 0;
    }
}