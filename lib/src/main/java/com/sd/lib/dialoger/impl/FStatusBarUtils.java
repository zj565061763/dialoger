package com.sd.lib.dialoger.impl;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

class FStatusBarUtils extends FFlagUtils
{
    /**
     * 设置全透明
     *
     * @param activity
     */
    public static void setTransparent(Activity activity)
    {
        if (activity == null)
            return;

        setTransparent(activity.getWindow());
    }

    /**
     * 设置全透明
     *
     * @param dialog
     */
    public static void setTransparent(Dialog dialog)
    {
        if (dialog == null)
            return;

        setTransparent(dialog.getWindow());
    }

    /**
     * 设置亮度
     *
     * @param activity
     * @param dark
     */
    public static void setBrightness(Activity activity, boolean dark)
    {
        if (activity == null)
            return;

        setBrightness(activity.getWindow(), dark);
    }

    /**
     * 设置全透明
     *
     * @param window
     */
    static void setTransparent(Window window)
    {
        if (Build.VERSION.SDK_INT >= 21)
        {
            int flag = window.getDecorView().getSystemUiVisibility();
            flag = addFlag(flag, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.getDecorView().setSystemUiVisibility(flag);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            if (window.getStatusBarColor() != Color.TRANSPARENT)
                window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19)
        {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置亮度
     *
     * @param window
     * @param dark   true-暗色；false-亮色
     */
    static void setBrightness(Window window, boolean dark)
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            int flag = window.getDecorView().getSystemUiVisibility();
            if (dark)
            {
                flag = clearFlag(flag, View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else
            {
                flag = addFlag(flag, View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            window.getDecorView().setSystemUiVisibility(flag);
        }
    }

    /**
     * 内容是否延展到状态栏底部
     *
     * @param window
     * @return
     */
    public static boolean isContentExtension(Window window)
    {
        if (Build.VERSION.SDK_INT >= 21)
        {
            final int flags = window.getDecorView().getSystemUiVisibility();
            final boolean hasFullScreen = hasFlag(flags, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            return hasFullScreen;
        } else if (Build.VERSION.SDK_INT >= 19)
        {
            final int flags = window.getAttributes().flags;
            final boolean hasTranslucentStatus = hasFlag(flags, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            return hasTranslucentStatus;
        } else
        {
            return false;
        }
    }

    /**
     * Window的状态栏是否可见
     *
     * @param window
     * @return
     */
    public static boolean isBarVisible(Window window)
    {
        final int flags = window.getAttributes().flags;
        return !hasFlag(flags, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 状态栏高度
     *
     * @param context
     * @return
     */
    public static int getBarHeight(Context context)
    {
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resourceId == 0 ? 0 : resources.getDimensionPixelSize(resourceId);
    }

    @Deprecated
    public static int getStatusBarHeight(Context context)
    {
        return getBarHeight(context);
    }
}
