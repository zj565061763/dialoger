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

import android.view.View;

/**
 * 向左滑入，向右滑出
 */
public class SlideLeftRightCreater extends SlideHorizontalCreater
{
    @Override
    protected float[] getFloatValues(boolean show, View view)
    {
        if (show)
        {
            return new float[]{view.getWidth(), 0};
        } else
        {
            return new float[]{view.getTranslationX(), view.getWidth()};
        }
    }

    @Override
    protected void onAnimationStart(boolean show, View view)
    {

    }

    @Override
    protected void onAnimationEnd(boolean show, View view)
    {
        if (!show)
            view.setTranslationX(0);
    }
}
