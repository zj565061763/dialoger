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
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * 透明度
 */
public class AlphaCreater extends BaseAnimatorCreater
{
    @Override
    protected Animator onCreateAnimator(boolean show, View view)
    {
        final ObjectAnimator animator = new ObjectAnimator();
        animator.setPropertyName(View.ALPHA.getName());

        final float[] values = show ? new float[]{0, 1.0f} : new float[]{view.getAlpha(), 0};
        animator.setFloatValues(values);

        final long duration = getScaledDuration(values[0], values[1], 1.0f, 300);
        animator.setDuration(duration);

        animator.setTarget(view);
        return animator;
    }

    @Override
    protected void onAnimationStart(boolean show, View view)
    {

    }

    @Override
    protected void onAnimationEnd(boolean show, View view)
    {
        if (!show)
            view.setAlpha(1.0f);
    }
}
