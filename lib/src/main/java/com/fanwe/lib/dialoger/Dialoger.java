package com.fanwe.lib.dialoger;

import android.animation.Animator;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public interface Dialoger
{
    /**
     * 返回窗口view
     *
     * @return
     */
    View getDialogView();

    /**
     * 返回内容view
     *
     * @return
     */
    View getContentView();

    /**
     * 设置内容view布局id
     *
     * @param layoutId
     */
    void setContentView(int layoutId);

    /**
     * 设置内容view
     *
     * @param view
     */
    void setContentView(View view);

    /**
     * 设置按返回键是否可以关闭窗口，默认true
     * <br>
     * 此方法需要传递按键事件后才有效{@link #onKeyDown(int, KeyEvent)}
     *
     * @param cancel
     */
    void setCancelable(boolean cancel);

    /**
     * 设置触摸到非内容区域是否关闭窗口，默认true
     * <br>
     * 此方法需要窗口view传递触摸事件后才有效{@link #onTouchEvent(MotionEvent)}
     *
     * @param cancel
     */
    void setCanceledOnTouchOutside(boolean cancel);

    /**
     * 设置窗口view的动画创建对象
     *
     * @param creater
     */
    void setDialogAnimatorCreater(AnimatorCreater creater);

    /**
     * 设置内容view的动画创建对象
     *
     * @param creater
     */
    void setContentAnimatorCreater(AnimatorCreater creater);

    /**
     * 设置窗口关闭监听
     *
     * @param listener
     */
    void setOnDismissListener(OnDismissListener listener);

    /**
     * 设置重力属性{@link android.view.Gravity}
     *
     * @param gravity
     */
    void setGravity(int gravity);

    /**
     * 返回当前的重力属性
     *
     * @return
     */
    int getGravity();

    /**
     * 显示窗口
     */
    void show();

    /**
     * 窗口是否正在显示
     *
     * @return
     */
    boolean isShowing();

    /**
     * 关闭窗口
     */
    void dismiss();

    /**
     * 延迟多久后关闭dialog
     *
     * @param delay （毫秒）
     */
    void startDismissRunnable(long delay);

    /**
     * 停止延迟关闭任务
     */
    void stopDismissRunnable();

    /**
     * 窗口view需要调用此方法
     */
    void onLayout(boolean changed, int left, int top, int right, int bottom);

    /**
     * 窗口view需要调用此方法
     */
    boolean onTouchEvent(MotionEvent event);

    /**
     * Activity需要调用此方法，如果此方法返回true的话，Activity那边的重写方法也要返回true
     */
    boolean onKeyDown(int keyCode, KeyEvent event);

    /**
     * 关闭监听
     */
    interface OnDismissListener
    {
        void onDismiss(Dialoger dialoger);
    }

    /**
     * 动画创建接口
     */
    interface AnimatorCreater
    {
        Animator createAnimator(boolean show, View view);
    }
}
