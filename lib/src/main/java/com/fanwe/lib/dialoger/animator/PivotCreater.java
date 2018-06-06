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
import android.view.View;

import com.fanwe.lib.dialoger.Dialoger;
import com.fanwe.lib.dialoger.utils.PivotHolder;

/**
 * 在动画开始的时候修改view的锚点，动画结束后还原view的锚点
 */
public class PivotCreater extends BaseAnimatorCreater
{
    private final Dialoger.AnimatorCreater mCreater;

    private PivotHolder mPivotHolder;
    private final PivotHolder.Position mPosition;

    public PivotCreater(Dialoger.AnimatorCreater creater, PivotHolder.Position position)
    {
        if (creater == null)
            throw new NullPointerException("creater is null");
        if (position == null)
            throw new NullPointerException("position is null");

        mCreater = creater;
        mPosition = position;
    }

    protected final PivotHolder getPivotHolder()
    {
        if (mPivotHolder == null)
            mPivotHolder = new PivotHolder();
        return mPivotHolder;
    }

    protected final PivotHolder.Position getPosition()
    {
        return mPosition;
    }

    @Override
    protected final Animator onCreateAnimator(boolean show, View view)
    {
        return mCreater.createAnimator(show, view);
    }

    @Override
    protected void onAnimationStart(boolean show, View view)
    {
        getPivotHolder().setPivotXY(mPosition, view);
    }

    @Override
    protected void onAnimationEnd(boolean show, View view)
    {
        getPivotHolder().restore(view);
    }
}
