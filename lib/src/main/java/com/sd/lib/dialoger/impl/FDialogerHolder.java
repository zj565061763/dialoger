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

    public synchronized static void addDialoger(FDialoger dialoger)
    {
        final Activity activity = dialoger.getOwnerActivity();

        Set<FDialoger> holder = MAP_ACTIVITY_DIALOG.get(activity);
        if (holder == null)
        {
            holder = new HashSet<>();
            MAP_ACTIVITY_DIALOG.put(activity, holder);
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
            MAP_ACTIVITY_DIALOG.remove(activity);
    }

    public synchronized static List<FDialoger> get(Activity activity)
    {
        final Set<FDialoger> holder = MAP_ACTIVITY_DIALOG.get(activity);
        if (holder == null)
            return null;

        if (holder.isEmpty())
        {
            MAP_ACTIVITY_DIALOG.remove(activity);
            return null;
        }

        return new ArrayList<>(holder);
    }

    public synchronized static void remove(Activity activity)
    {
        MAP_ACTIVITY_DIALOG.remove(activity);
    }
}
