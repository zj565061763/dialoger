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
package com.fanwe.lib.dialoger;

import android.view.View;

public interface TargetDialoger
{
    /**
     * 设置是否设置paddding来限制边界，默认false
     *
     * @param padding
     * @return
     */
    TargetDialoger setPaddingToPosition(boolean padding);

    /**
     * x方向偏移量，大于0-向右；小于0-向左
     *
     * @param marginX
     * @return
     */
    TargetDialoger setMarginX(int marginX);

    /**
     * y方向偏移量，大于0-向下；小于0-向上
     *
     * @param marginY
     * @return
     */
    TargetDialoger setMarginY(int marginY);

    /**
     * 显示在目标view的某个位置
     *
     * @param target   目标view
     * @param position 显示的位置{@link Position}
     */
    void showPosition(View target, Position position);

    enum Position
    {
        /**
         * 在target的左边外侧靠顶部对齐
         */
        LeftOutsideTop,
        /**
         * 在target的左边外侧上下居中
         */
        LeftOutsideCenter,
        /**
         * 在target的左边外侧靠底部对齐
         */
        LeftOutsideBottom,

        /**
         * 在target的顶部外侧靠左对齐
         */
        TopOutsideLeft,
        /**
         * 在target的顶部外侧左右居中
         */
        TopOutsideCenter,
        /**
         * 在target的顶部外侧靠右对齐
         */
        TopOutsideRight,

        /**
         * 在target的右边外侧靠顶部对齐
         */
        RightOutsideTop,
        /**
         * 在target的右边外侧上下居中
         */
        RightOutsideCenter,
        /**
         * 在target的右边外侧靠底部对齐
         */
        RightOutsideBottom,

        /**
         * 在target的底部外侧靠左对齐
         */
        BottomOutsideLeft,
        /**
         * 在target的底部外侧左右居中
         */
        BottomOutsideCenter,
        /**
         * 在target的底部外侧靠右对齐
         */
        BottomOutsideRight,
    }
}
