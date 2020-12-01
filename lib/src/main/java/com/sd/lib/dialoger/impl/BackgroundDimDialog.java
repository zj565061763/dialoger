package com.sd.lib.dialoger.impl;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.sd.lib.dialoger.R;
import com.sd.lib.systemui.statusbar.FStatusBarUtils;

class BackgroundDimDialog extends Dialog
{
    public BackgroundDimDialog(Context context)
    {
        super(context, R.style.lib_dialoger_default);
        final int color = context.getResources().getColor(R.color.lib_dialoger_background_dim);
        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        final View view = new View(context);
        view.setBackgroundColor(color);
        view.setLayoutParams(params);
        setContentView(view);

        setCancelable(false);
        getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getAttributes().height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getAttributes().horizontalMargin = 0;
        getWindow().getAttributes().verticalMargin = 0;

        FStatusBarUtils.setTransparent(this);
    }
}
