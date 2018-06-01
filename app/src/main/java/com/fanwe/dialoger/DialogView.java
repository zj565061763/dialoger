package com.fanwe.dialoger;

import android.app.Activity;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.fanwe.lib.dialoger.Dialoger;
import com.fanwe.lib.dialoger.FDialoger;

public class DialogView extends LinearLayout
{
    private final Dialoger mDialoger;

    public DialogView(Activity activity)
    {
        super(activity);
        mDialoger = new FDialoger(activity, this)
        {
            @Override
            protected void onStart()
            {
                super.onStart();
                DialogView.this.setGravity(mDialoger.getGravity());
            }
        };
    }

    public final Dialoger getDialoger()
    {
        return mDialoger;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        // 传递事件
        getDialoger().onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        super.onTouchEvent(event);
        // 传递事件
        return getDialoger().onTouchEvent(event);
    }
}
