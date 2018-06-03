package com.fanwe.lib.dialoger.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.View;

import com.fanwe.lib.dialoger.Dialoger;

public class CombineCreater implements Dialoger.AnimatorCreater
{
    private final Dialoger.AnimatorCreater[] mCreaters;

    public CombineCreater(Dialoger.AnimatorCreater... creaters)
    {
        if (creaters == null || creaters.length <= 0)
            throw new IllegalArgumentException("creaters is null or empty");

        for (Dialoger.AnimatorCreater item : creaters)
        {
            if (item == null)
                throw new NullPointerException("creaters array contains null item");
        }

        mCreaters = creaters;
    }

    protected final Dialoger.AnimatorCreater[] getCreaters()
    {
        return mCreaters;
    }

    private Animator getAnimator(boolean show, View view)
    {
        final Dialoger.AnimatorCreater[] creaters = getCreaters();
        final AnimatorSet animatorSet = new AnimatorSet();

        Animator mLast = null;
        for (int i = 0; i < creaters.length; i++)
        {
            final Animator animator = creaters[i].createAnimator(show, view);
            if (animator == null)
                continue;

            if (mLast == null)
                animatorSet.play(animator);
            else
                animatorSet.play(mLast).with(animator);

            mLast = animator;
        }

        if (mLast == null)
            return null;

        return animatorSet;
    }

    @Override
    public Animator createAnimator(boolean show, View view)
    {
        final Animator animator = getAnimator(show, view);
        return animator == null ? null : animator;
    }
}
