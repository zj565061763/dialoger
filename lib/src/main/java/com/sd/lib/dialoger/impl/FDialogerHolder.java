package com.sd.lib.dialoger.impl;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

class FDialogerHolder
{
    private static final Map<Activity, Collection<FDialoger>> MAP_ACTIVITY_DIALOG = new HashMap<>();

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
    }
}
