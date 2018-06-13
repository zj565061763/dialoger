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
package com.fanwe.lib.dialoger.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

import com.fanwe.lib.dialoger.Dialoger;

public abstract class BaseAnimatorCreater implements Dialoger.AnimatorCreater
{
    @Override
    public final Animator createAnimator(final boolean show, final View view)
    {
        beforeCreateAnimator(show, view);

        final Animator animator = onCreateAnimator(show, view);
        if (animator != null)
        {
            animator.addListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationStart(Animator animation)
                {
                    super.onAnimationStart(animation);
                    BaseAnimatorCreater.this.onAnimationStart(show, view);
                }

                @Override
                public void onAnimationEnd(Animator animation)
                {
                    super.onAnimationEnd(animation);
                    animation.removeListener(this);
                    BaseAnimatorCreater.this.onAnimationEnd(show, view);
                }
            });
            onAnimatorCreated(show, view, animator);
        }
        return animator;
    }

    /**
     * 在动画要创建之前回调
     *
     * @param show
     * @param view
     */
    protected void beforeCreateAnimator(boolean show, View view)
    {
    }

    /**
     * 创建动画
     *
     * @param show
     * @param view
     * @return
     */
    protected abstract Animator onCreateAnimator(boolean show, View view);


    /**
     * 动画被创建后回调
     *
     * @param show
     * @param view
     * @param animator
     */
    protected void onAnimatorCreated(boolean show, View view, Animator animator)
    {
    }

    /**
     * 动画开始回调
     *
     * @param show
     * @param view
     */
    protected void onAnimationStart(boolean show, View view)
    {
    }

    /**
     * 动画结束回调
     *
     * @param show
     * @param view
     */
    protected void onAnimationEnd(boolean show, View view)
    {
    }

    protected static long getScaledDuration(float deltaValue, float maxValue, long maxDuration)
    {
        if (maxDuration <= 0)
            return 0;
        if (maxValue == 0)
            return 0;
        if (deltaValue == 0)
            return 0;

        final float percent = Math.abs(deltaValue / maxValue);
        long duration = (long) (percent * maxDuration);
        if (duration > maxDuration)
            return maxDuration;

        return duration;
    }
}
