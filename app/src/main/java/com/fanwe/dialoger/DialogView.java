package com.fanwe.dialoger;

import android.app.Activity;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.fanwe.lib.dialoger.Dialoger;
import com.fanwe.lib.dialoger.FDialoger;

public class DialogView extends FrameLayout
{
    private final Dialoger mDialoger;

    public DialogView(Activity activity)
    {
        super(activity);
        mDialoger = new FDialoger(activity, this);
        mDialoger.setContentView(R.layout.dialog_view);
    }

    public final Dialoger getDialoger()
    {
        return mDialoger;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        getDialoger().onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        super.onTouchEvent(event);
        return getDialoger().onTouchEvent(event);
    }
}
