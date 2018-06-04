package com.fanwe.lib.dialoger.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

import com.fanwe.lib.dialoger.Dialoger;

public abstract class BaseAnimatorCreater implements Dialoger.AnimatorCreater
{
    @Override
    public final Animator createAnimator(final boolean show, final View view)
    {
        final Animator animator = onCreateAnimator(show, view);
        if (animator != null)
        {
            animator.addListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    super.onAnimationEnd(animation);
                    animation.removeListener(this);
                    BaseAnimatorCreater.this.onAnimationEnd(show, view);
                }
            });
        }
        return animator;
    }

    protected abstract Animator onCreateAnimator(boolean show, View view);

    protected abstract void onAnimationEnd(boolean show, View view);
}
