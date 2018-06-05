/*
 * Copyright (C) 2017 zhengjun, fanwe (http://www.fanwe.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fanwe.lib.dialoger;

import android.animation.Animator;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;

public interface Dialoger
{
    /**
     * 设置是否调试模式
     *
     * @param debug
     */
    void setDebug(boolean debug);

    Context getContext();

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
     * <br>
     * 此方法需要传递按键事件后才有效{@link #onKeyDown(int, KeyEvent)}
     *
     * @param cancel
     */
    void setCancelable(boolean cancel);

    /**
     * 设置触摸到非内容view区域是否关闭窗口，默认false
     *
     * @param cancel
     */
    void setCanceledOnTouchOutside(boolean cancel);

    /**
     * 设置窗口内容view动画创建对象
     *
     * @param creater
     */
    void setAnimatorCreater(AnimatorCreater creater);

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
         * @param show
         * @param view
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
