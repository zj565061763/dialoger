package com.sd.lib.dialoger;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public interface Dialoger
{
    /**
     * 设置调试模式，内部会输出日志，日志tag：Dialoger
     *
     * @param debug
     */
    void setDebug(boolean debug);

    Context getContext();

    Activity getOwnerActivity();

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
     * 设置窗口背景颜色
     *
     * @param color
     */
    void setBackgroundColor(int color);

    /**
     * 根据id查找view
     *
     * @param id
     * @param <T>
     * @return
     */
    <T extends View> T findViewById(int id);

    /**
     * 设置按返回键是否可以关闭窗口，默认true
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
     * 添加生命周期回调
     *
     * @param callback
     */
    void addLifecycleCallback(LifecycleCallback callback);

    /**
     * 移除生命周期回调
     *
     * @param callback
     */
    void removeLifecycleCallback(LifecycleCallback callback);

    /**
     * 设置窗口内容view动画创建对象
     *
     * @param creater
     */
    void setAnimatorCreater(AnimatorCreater creater);

    /**
     * 返回窗口内容view动画创建对象
     *
     * @return
     */
    AnimatorCreater getAnimatorCreater();

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
     * 设置上下左右间距
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    void setPadding(int left, int top, int right, int bottom);

    /**
     * 左边边距
     *
     * @return
     */
    int getPaddingLeft();

    /**
     * 顶部边距
     *
     * @return
     */
    int getPaddingTop();

    /**
     * 右边边距
     *
     * @return
     */
    int getPaddingRight();

    /**
     * 底部边距
     *
     * @return
     */
    int getPaddingBottom();

    /**
     * 窗口是否正在显示
     *
     * @return
     */
    boolean isShowing();

    /**
     * 显示窗口
     */
    void show();

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
     * 触摸事件回调
     *
     * @param event
     * @return
     */
    boolean onTouchEvent(MotionEvent event);

    /**
     * 按键事件回调
     */
    boolean onKeyDown(int keyCode, KeyEvent event);

    /**
     * 返回TargetDialoger对象
     *
     * @return
     */
    TargetDialoger target();

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
         * @param dialoger
         */
        void onShow(Dialoger dialoger);
    }

    /**
     * 动画创建接口
     */
    interface AnimatorCreater
    {
        /**
         * 创建动画
         * <br>
         * 注意：隐藏动画不能设置为无限循环，否则窗口将不能被移除
         *
         * @param show true-窗口显示，false-窗口隐藏
         * @param view 窗口内容view
         * @return
         */
        Animator createAnimator(boolean show, View view);
    }

    interface LifecycleCallback
    {
        /**
         * 窗口显示之前回调
         *
         * @param dialoger
         */
        void onStart(Dialoger dialoger);

        /**
         * 窗口关闭之前回调
         *
         * @param dialoger
         */
        void onStop(Dialoger dialoger);
    }
}
