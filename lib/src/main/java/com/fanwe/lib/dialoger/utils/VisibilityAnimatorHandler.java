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
package com.fanwe.lib.dialoger.utils;

import android.animation.Animator;

public final class VisibilityAnimatorHandler
{
    private Animator mShowAnimator;
    private Animator mHideAnimator;

    private final AnimatorListenerWrapper mShowAnimatorListener = new AnimatorListenerWrapper();
    private final AnimatorListenerWrapper mHideAnimatorListener = new AnimatorListenerWrapper();

    /**
     * 设置显示动画
     *
     * @param animator
     */
    public void setShowAnimator(Animator animator)
    {
        if (mShowAnimator != animator)
        {
            if (animator != null)
                animator.removeListener(mShowAnimatorListener);

            mShowAnimator = animator;

            if (mShowAnimator != null)
                mShowAnimator.addListener(mShowAnimatorListener);
        }
    }

    /**
     * 设置显示动画监听
     *
     * @param listener
     */
    public void setShowAnimatorListener(Animator.AnimatorListener listener)
    {
        mShowAnimatorListener.setOriginal(listener);
    }

    /**
     * 开始显示动画
     *
     * @return true-动画被执行
     */
    public boolean startShowAnimator()
    {
        if (mShowAnimator != null)
        {
            if (mShowAnimator.isStarted())
                return true;

            cancelHideAnimator();
            mShowAnimator.start();
            return true;
        }
        return false;
    }

    /**
     * 显示动画是否已经开始
     *
     * @return
     */
    public boolean isShowAnimatorStarted()
    {
        return mShowAnimator != null && mShowAnimator.isStarted();
    }

    /**
     * 取消显示动画
     */
    public void cancelShowAnimator()
    {
        if (isShowAnimatorStarted())
            mShowAnimator.cancel();
    }

    /**
     * 设置隐藏动画
     *
     * @param animator
     */
    public void setHideAnimator(Animator animator)
    {
        if (mHideAnimator != animator)
        {
            if (animator != null)
                animator.removeListener(mHideAnimatorListener);

            mHideAnimator = animator;

            if (mHideAnimator != null)
                mHideAnimator.addListener(mHideAnimatorListener);
        }
    }

    /**
     * 设置隐藏动画监听
     *
     * @param listener
     */
    public void setHideAnimatorListener(Animator.AnimatorListener listener)
    {
        mHideAnimatorListener.setOriginal(listener);
    }

    /**
     * 开始隐藏动画
     *
     * @return true-动画被执行
     */
    public boolean startHideAnimator()
    {
        if (mHideAnimator != null)
        {
            if (mHideAnimator.isStarted())
                return true;

            cancelShowAnimator();
            mHideAnimator.start();
            return true;
        }
        return false;
    }

    /**
     * 隐藏动画是否已经开始执行
     *
     * @return
     */
    public boolean isHideAnimatorStarted()
    {
        return mHideAnimator != null && mHideAnimator.isStarted();
    }

    /**
     * 取消隐藏动画
     */
    public void cancelHideAnimator()
    {
        if (isHideAnimatorStarted())
            mHideAnimator.cancel();
    }

    private static class AnimatorListenerWrapper implements Animator.AnimatorListener
    {
        private Animator.AnimatorListener mOriginal;

        public void setOriginal(Animator.AnimatorListener original)
        {
            mOriginal = original;
        }

        @Override
        public void onAnimationStart(Animator animation)
        {
            if (mOriginal != null)
                mOriginal.onAnimationStart(animation);
        }

        @Override
        public void onAnimationEnd(Animator animation)
        {
            if (mOriginal != null)
                mOriginal.onAnimationEnd(animation);
        }

        @Override
        public void onAnimationCancel(Animator animation)
        {
            if (mOriginal != null)
                mOriginal.onAnimationCancel(animation);
        }

        @Override
        public void onAnimationRepeat(Animator animation)
        {
            if (mOriginal != null)
                mOriginal.onAnimationRepeat(animation);
        }
    }
}
