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

/**
 * 在动画开始的时候修改view的锚点，动画结束后还原view的锚点
 */
public class PivotCreater extends BaseAnimatorCreater
{
    private final Dialoger.AnimatorCreater mCreater;
    private final float mPivotXPercent;
    private final float mPivotYPercent;

    private PivotHolder mPivotHolder;

    public PivotCreater(Dialoger.AnimatorCreater creater, float pivotXPercent, float pivotYPercent)
    {
        if (creater == null)
            throw new NullPointerException("creater is null");

        mCreater = creater;
        mPivotXPercent = pivotXPercent;
        mPivotYPercent = pivotYPercent;
    }

    protected final PivotHolder getPivotHolder()
    {
        if (mPivotHolder == null)
            mPivotHolder = new PivotHolder();
        return mPivotHolder;
    }

    @Override
    protected final Animator onCreateAnimator(boolean show, View view)
    {
        return mCreater.createAnimator(show, view);
    }

    @Override
    protected void onAnimationStart(boolean show, View view)
    {
        super.onAnimationStart(show, view);
        getPivotHolder().setPivotXY(mPivotXPercent * view.getWidth(), mPivotYPercent * view.getHeight(), view);
    }

    @Override
    protected void onAnimationEnd(boolean show, View view)
    {
        super.onAnimationEnd(show, view);
        getPivotHolder().restore(view);
    }

    private static class PivotHolder
    {
        private final float[] mPivotXYOriginal = new float[2];

        public void setPivotXY(float pivotX, float pivotY, View view)
        {
            if (view == null)
                return;

            mPivotXYOriginal[0] = view.getPivotX();
            mPivotXYOriginal[1] = view.getPivotY();

            view.setPivotX(pivotX);
            view.setPivotY(pivotY);
        }

        public void restore(View view)
        {
            if (view == null)
                return;

            view.setPivotX(mPivotXYOriginal[0]);
            view.setPivotY(mPivotXYOriginal[1]);
        }
    }
}
