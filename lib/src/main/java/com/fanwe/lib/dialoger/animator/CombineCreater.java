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
import android.animation.AnimatorSet;
import android.view.View;

import com.fanwe.lib.dialoger.Dialoger;

public class CombineCreater extends BaseAnimatorCreater
{
    private final Dialoger.AnimatorCreater[] mCreaters;

    public CombineCreater(Dialoger.AnimatorCreater... creaters)
    {
        if (creaters == null || creaters.length <= 0)
            throw new IllegalArgumentException("creaters is null or empty");

        for (Dialoger.AnimatorCreater item : creaters)
        {
            if (item == null)
                throw new NullPointerException("creaters array contains null item");
        }

        mCreaters = creaters;
    }

    protected final Dialoger.AnimatorCreater[] getCreaters()
    {
        return mCreaters;
    }

    private Animator getAnimator(boolean show, View view)
    {
        final Dialoger.AnimatorCreater[] creaters = getCreaters();
        final AnimatorSet animatorSet = new AnimatorSet();

        Animator mLast = null;
        for (int i = 0; i < creaters.length; i++)
        {
            final Animator animator = creaters[i].createAnimator(show, view);
            if (animator == null)
                continue;

            if (mLast == null)
                animatorSet.play(animator);
            else
                animatorSet.play(mLast).with(animator);

            mLast = animator;
        }

        if (mLast == null)
            return null;

        return animatorSet;
    }

    @Override
    protected Animator onCreateAnimator(boolean show, View view)
    {
        return getAnimator(show, view);
    }
}
