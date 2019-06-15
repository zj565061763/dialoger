package com.sd.lib.dialoger.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.View;

import com.sd.lib.dialoger.Dialoger;

public class CombineCreator extends BaseAnimatorCreator
{
    private final Dialoger.AnimatorCreator[] mCreators;

    public CombineCreator(Dialoger.AnimatorCreator... creators)
    {
        if (creators == null || creators.length <= 0)
            throw new IllegalArgumentException("creators is null or empty");

        for (Dialoger.AnimatorCreator item : creators)
        {
            if (item == null)
                throw new NullPointerException("creators array contains null item");
        }

        mCreators = creators;
    }

    protected final Dialoger.AnimatorCreator[] getCreators()
    {
        return mCreators;
    }

    private Animator getAnimator(boolean show, View view)
    {
        final Dialoger.AnimatorCreator[] creators = getCreators();
        final AnimatorSet animatorSet = new AnimatorSet();

        Animator mLast = null;
        for (int i = 0; i < creators.length; i++)
        {
            final Animator animator = creators[i].createAnimator(show, view);
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
    protected Animator onCreateAnimator(boolean show, View view)
    {
        return getAnimator(show, view);
    }
}
