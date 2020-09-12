package com.sd.lib.dialoger.impl;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class FDialogerHolder
{
    private static final Map<Activity, Set<FDialoger>> MAP_ACTIVITY_DIALOG = new HashMap<>();
    private static final Map<Activity, ActivityConfig> MAP_ACTIVITY_CONFIG = new HashMap<>();

    public synchronized static void addDialoger(FDialoger dialoger)
    {
        final Activity activity = dialoger.getOwnerActivity();

        Set<FDialoger> holder = MAP_ACTIVITY_DIALOG.get(activity);
        if (holder == null)
        {
            holder = new HashSet<>();
            MAP_ACTIVITY_DIALOG.put(activity, holder);
            addActivityConfig(activity);
        }

        holder.add(dialoger);
    }

    public synchronized static void removeDialoger(FDialoger dialoger)
    {
        final Activity activity = dialoger.getOwnerActivity();

        final Set<FDialoger> holder = MAP_ACTIVITY_DIALOG.get(activity);
        if (holder == null)
            return;

        holder.remove(dialoger);
        if (holder.isEmpty())
        {
            MAP_ACTIVITY_DIALOG.remove(activity);
            removeActivityConfig(activity);
        }
    }

    public synchronized static List<FDialoger> get(Activity activity)
    {
        final Set<FDialoger> holder = MAP_ACTIVITY_DIALOG.get(activity);
        if (holder == null)
            return null;

        return new ArrayList<>(holder);
    }

    public synchronized static void remove(Activity activity)
    {
        MAP_ACTIVITY_DIALOG.remove(activity);
        removeActivityConfig(activity);
    }

    private static void addActivityConfig(Activity activity)
    {
        final ActivityConfig config = new ActivityConfig();
        config.mSystemUiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility();
        MAP_ACTIVITY_CONFIG.put(activity, config);
    }

    private static void removeActivityConfig(Activity activity)
    {
        final ActivityConfig config = MAP_ACTIVITY_CONFIG.remove(activity);
        if (config != null)
        {
            if (activity.getWindow().getDecorView().getSystemUiVisibility() != config.mSystemUiVisibility)
                activity.getWindow().getDecorView().setSystemUiVisibility(config.mSystemUiVisibility);
        }
    }

    private static class ActivityConfig
    {
        public int mSystemUiVisibility;
    }
}
