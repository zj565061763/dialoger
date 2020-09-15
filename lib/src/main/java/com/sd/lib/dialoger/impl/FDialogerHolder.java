package com.sd.lib.dialoger.impl;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class FDialogerHolder
{
    private static final Map<Activity, Collection<FDialoger>> MAP_ACTIVITY_DIALOG = new HashMap<>();
    private static final Map<Activity, ActivityConfig> MAP_ACTIVITY_CONFIG = new WeakHashMap<>();

    static synchronized void addDialoger(FDialoger dialoger)
    {
        final Activity activity = dialoger.getOwnerActivity();

        Collection<FDialoger> holder = MAP_ACTIVITY_DIALOG.get(activity);
        if (holder == null)
        {
            holder = new HashSet<>();
            MAP_ACTIVITY_DIALOG.put(activity, holder);
        }

        holder.add(dialoger);
    }

    static synchronized void removeDialoger(FDialoger dialoger)
    {
        final Activity activity = dialoger.getOwnerActivity();

        final Collection<FDialoger> holder = MAP_ACTIVITY_DIALOG.get(activity);
        if (holder == null)
            return;

        holder.remove(dialoger);
        if (holder.isEmpty())
        {
            MAP_ACTIVITY_DIALOG.remove(activity);
            restoreActivityConfig(activity);
        }
    }

    static synchronized List<FDialoger> get(Activity activity)
    {
        final Collection<FDialoger> holder = MAP_ACTIVITY_DIALOG.get(activity);
        if (holder == null)
            return null;

        return new ArrayList<>(holder);
    }

    static synchronized void remove(Activity activity)
    {
        MAP_ACTIVITY_DIALOG.remove(activity);
        MAP_ACTIVITY_CONFIG.remove(activity);
    }

    private static void restoreActivityConfig(Activity activity)
    {
        final ActivityConfig config = MAP_ACTIVITY_CONFIG.get(activity);
        if (config != null)
            config.restore();
    }

    /**
     * 返回activity对象对应的配置
     *
     * @param activity
     * @return
     */
    public static synchronized ActivityConfig getActivityConfig(Activity activity)
    {
        if (activity == null)
            return null;

        ActivityConfig config = MAP_ACTIVITY_CONFIG.get(activity);
        if (config == null)
        {
            config = new ActivityConfig(activity);
            if (!activity.isFinishing())
                MAP_ACTIVITY_CONFIG.put(activity, config);
        }
        return config;
    }

    public static final class ActivityConfig
    {
        private final WeakReference<Activity> mActivity;
        private Integer mSystemUiVisibility;

        private ActivityConfig(Activity activity)
        {
            mActivity = new WeakReference<>(activity);
        }

        public void save()
        {
            final Activity activity = mActivity.get();
            if (activity == null)
                return;

            mSystemUiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility();
        }

        private void restore()
        {
            final Activity activity = mActivity.get();
            if (activity == null)
                return;

            if (mSystemUiVisibility != null)
                activity.getWindow().getDecorView().setSystemUiVisibility(mSystemUiVisibility);
        }
    }
}
