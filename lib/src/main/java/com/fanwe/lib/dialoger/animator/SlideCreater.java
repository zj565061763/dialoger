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
 * 滑动
 */
abstract class SlideCreater extends BaseAnimatorCreater
{
    @Override
    protected final Animator onCreateAnimator(boolean show, View view)
    {
        final ObjectAnimator animator = new ObjectAnimator();
        final String propertyName = getPropertyName();

        if (View.TRANSLATION_Y.getName().equals(propertyName) || View.TRANSLATION_X.getName().equals(propertyName))
        {
            animator.setPropertyName(propertyName);
            animator.setFloatValues(getFloatValues(show, view));
            animator.setTarget(view);
            return animator;
        } else
        {
            throw new RuntimeException("Illegal property name:" + propertyName);
        }
    }

    protected abstract String getPropertyName();

    protected abstract float[] getFloatValues(boolean show, View view);
}
