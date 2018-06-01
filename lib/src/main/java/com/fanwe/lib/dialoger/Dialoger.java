package com.fanwe.lib.dialoger;

import android.animation.Animator;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public interface Dialoger
{
    View getDialogView();

    View getContentView();

    void setContentView(int layoutId);

    void setContentView(View view);

    void setCancelable(boolean cancel);

    void setCanceledOnTouchOutside(boolean cancel);

    void setDialogAnimatorCreater(AnimatorCreater creater);

    void setContentAnimatorCreater(AnimatorCreater creater);

    void setOnDismissListener(OnDismissListener listener);

    void show();

    void dismiss();

    boolean isShowing();

    void onLayout(boolean changed, int left, int top, int right, int bottom);

    boolean onTouchEvent(MotionEvent event);

    boolean onKeyDown(int keyCode, KeyEvent event);

    interface OnDismissListener
    {
        void onDismiss(Dialoger dialogView);
    }

    interface AnimatorCreater
    {
        Animator createAnimator(boolean show, View view);
    }
}
