package com.fanwe.lib.dialoger;

import android.animation.Animator;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;

public interface Dialoger
{

    Context getContext();

    /**
     * 返回窗口的view
     *
     * @return
     */
    View getDialogerView();

    /**
     * 返回窗口的内容view
     *
     * @return
     */
    View getContentView();

    /**
     * 设置窗口的内容view布局id
     *
     * @param layoutId
     */
    void setContentView(int layoutId);

    /**
     * 设置窗口的内容view
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
     * 设置触摸到非内容view区域是否关闭窗口，默认true
     *
     * @param cancel
     */
    void setCanceledOnTouchOutside(boolean cancel);

    /**
     * 设置窗口的view动画创建对象
     *
     * @param creater
     */
    void setDialogAnimatorCreater(AnimatorCreater creater);

    /**
     * 设置窗口的内容view动画创建对象
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
     * 设置窗口显示监听
     *
     * @param listener
     */
    void setOnShowListener(OnShowListener listener);

    /**
     * 设置重力属性{@link android.view.Gravity}
     *
     * @param gravity
     */
    void setGravity(int gravity);

    /**
     * 设置左边间距
     *
     * @param padding
     */
    void paddingLeft(int padding);

    /**
     * 设置顶部间距
     *
     * @param padding
     */
    void paddingTop(int padding);

    /**
     * 设置右边间距
     *
     * @param padding
     */
    void paddingRight(int padding);

    /**
     * 设置底部间距
     *
     * @param padding
     */
    void paddingBottom(int padding);

    /**
     * 设置上下左右间距
     *
     * @param paddings
     */
    void paddings(int paddings);

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
     * Activity需要调用此方法，如果此方法返回true的话，Activity那边的重写方法也要返回true
     */
    boolean onKeyDown(int keyCode, KeyEvent event);

    /**
     * 关闭监听
     */
    interface OnDismissListener
    {
        /**
         * 消失后回调
         *
         * @param dialoger
         */
        void onDismiss(Dialoger dialoger);
    }

    /**
     * 显示监听
     */
    interface OnShowListener
    {
        /**
         * 显示后回调
         *
         * @param dialog
         */
        void onShow(Dialoger dialog);
    }

    /**
     * 动画创建接口
     */
    interface AnimatorCreater
    {
        Animator createAnimator(boolean show, View view);
    }
}
