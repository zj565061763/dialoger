package com.sd.lib.dialoger.animator;

import android.animation.Animator;
import android.view.View;

import com.sd.lib.dialoger.Dialoger;

public final class EmptyCreator implements Dialoger.AnimatorCreator
{
    @Override
    public Animator createAnimator(boolean show, View view)
    {
        return null;
    }
}
