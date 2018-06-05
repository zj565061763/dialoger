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
        }
        return animator;
    }

    protected abstract Animator onCreateAnimator(boolean show, View view);

    protected abstract void onAnimationStart(boolean show, View view);

    protected abstract void onAnimationEnd(boolean show, View view);
}
