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

import android.view.View;

/**
 * 锚点参数持有者
 */
public class PivotHolder
{
    private final float[] mPivotXYOriginal = new float[2];

    public void setPivotXY(Position position, View view)
    {
        if (position == null || view == null)
            return;

        switch (position)
        {
            case TopLeft:
                setPivotXY(0.0f, 0.0f, view);
                break;
            case TopCenter:
                setPivotXY(view.getWidth() / 2, 0.0f, view);
                break;
            case TopRight:
                setPivotXY(view.getWidth(), 0.0f, view);
                break;

            case LeftCenter:
                setPivotXY(0.0f, view.getHeight() / 2, view);
                break;
            case Center:
                setPivotXY(view.getWidth() / 2, view.getHeight() / 2, view);
                break;
            case RightCenter:
                setPivotXY(view.getWidth(), view.getHeight() / 2, view);
                break;

            case BottomLeft:
                setPivotXY(0.0f, view.getHeight(), view);
                break;
            case BottomCenter:
                setPivotXY(view.getWidth() / 2, view.getHeight(), view);
                break;
            case BottomRight:
                setPivotXY(view.getWidth(), view.getHeight(), view);
                break;
        }
    }

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

    public enum Position
    {
        TopLeft,
        TopCenter,
        TopRight,

        LeftCenter,
        Center,
        RightCenter,

        BottomLeft,
        BottomCenter,
        BottomRight
    }
}
