package com.sd.lib.dialoger.impl;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Method;

class FNavigationBarUtils extends FFlagUtils
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
            flag = addFlag(flag, View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(flag);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            if (window.getNavigationBarColor() != Color.TRANSPARENT)
                window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19)
        {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
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
        if (Build.VERSION.SDK_INT >= 21)
        {
            int flag = window.getDecorView().getSystemUiVisibility();
            if (dark)
            {
                flag = clearFlag(flag, View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            } else
            {
                flag = addFlag(flag, View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            }
            window.getDecorView().setSystemUiVisibility(flag);
        }
    }

    /**
     * 内容是否延展到底部导航栏底部
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
            final boolean hasHideNavigation = hasFlag(flags, View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            return hasFullScreen && hasHideNavigation;
        } else if (Build.VERSION.SDK_INT >= 19)
        {
            final int flags = window.getAttributes().flags;
            final boolean hasTranslucentNavigation = hasFlag(flags, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            return hasTranslucentNavigation;
        } else
        {
            return false;
        }
    }

    /**
     * 底部导航栏是否可见
     *
     * @param context
     * @return
     */
    public static boolean isBarVisible(Context context)
    {
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId == 0)
            return !ViewConfiguration.get(context).hasPermanentMenuKey();

        boolean isVisible = resources.getBoolean(resourceId);

        final String navigationBarOverride = getNavigationBarOverride();
        if ("1".equals(navigationBarOverride))
        {
            isVisible = false;
        }

        return isVisible;
    }

    private static String getNavigationBarOverride()
    {
        String result = null;
        if (Build.VERSION.SDK_INT >= 19)
        {
            try
            {
                final Class clazz = Class.forName("android.os.SystemProperties");
                final Method method = clazz.getDeclaredMethod("get", String.class);
                method.setAccessible(true);
                result = (String) method.invoke(null, "qemu.hw.mainkeys");
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 底部导航栏高度
     *
     * @param context
     * @return
     */
    public static int getBarHeight(Context context)
    {
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resourceId == 0 ? 0 : resources.getDimensionPixelSize(resourceId);
    }
}
