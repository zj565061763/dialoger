package com.sd.lib.dialoger;

import android.view.View;

public interface TargetDialoger
{
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
    void show(View target, Position position);

    enum Position
    {
        /**
         * 在target左边外侧
         */
        LeftOutside,
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
         * 在target的顶部外侧
         */
        TopOutside,
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
         * 在target的右边外侧
         */
        RightOutside,
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
         * 在target的底部外侧
         */
        BottomOutside,
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
