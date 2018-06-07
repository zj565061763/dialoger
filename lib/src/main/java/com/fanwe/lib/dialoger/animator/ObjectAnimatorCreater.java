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

public abstract class ObjectAnimatorCreater extends BaseAnimatorCreater
{
    @Override
    protected final Animator onCreateAnimator(boolean show, View view)
    {
        final ObjectAnimator animator = new ObjectAnimator();
        animator.setPropertyName(getPropertyName());

        final float valueShown = getValueShown(view);
        final float valueHidden = getValueHidden(view);
        final float[] values = show ? new float[]{valueHidden, valueShown} : new float[]{getValueCurrent(view), valueHidden};
        animator.setFloatValues(values);

        final long duration = getScaledDuration(values[0] - values[1], valueShown - valueHidden, getDuration());
        animator.setDuration(duration);

        animator.setTarget(view);
        return animator;
    }

    protected abstract String getPropertyName();

    /**
     * 返回动画执行到于显示状态的值
     *
     * @param view
     * @return
     */
    protected abstract float getValueShown(View view);

    /**
     * 返回动画执行到于隐藏状态的值
     *
     * @param view
     * @return
     */
    protected abstract float getValueHidden(View view);

    /**
     * 返回当前的值
     *
     * @param view
     * @return
     */
    protected abstract float getValueCurrent(View view);

    /**
     * 返回动画时长，默认300毫秒
     *
     * @return
     */
    protected long getDuration()
    {
        return 300;
    }
}
