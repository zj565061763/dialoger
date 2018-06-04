/*
 * Copyright (C) 2017 zhengjun, fanwe (http://www.fanwe.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fanwe.lib.dialoger.impl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.fanwe.lib.dialoger.Dialoger;
import com.fanwe.lib.dialoger.TargetDialoger;
import com.fanwe.lib.dialoger.animator.AlphaCreater;
import com.fanwe.lib.dialoger.utils.VisibilityAnimatorHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FDialoger implements Dialoger
{
    private final Activity mActivity;
    private final ViewGroup mDialogerParent;
    private final View mDialogerView;
    private final LinearLayout mContainerView;
    private final View mBackgroundView;

    private View mContentView;
    private boolean mCancelable = true;
    private boolean mCanceledOnTouchOutside = false;

    private int mGravity = Gravity.NO_GRAVITY;

    private OnDismissListener mOnDismissListener;
    private OnShowListener mOnShowListener;
    private List<LifecycleCallback> mLifecycleCallbacks;

    private boolean mIsAttached;

    private VisibilityAnimatorHandler mAnimatorHandler;
    private AnimatorCreater mAnimatorCreater;
    private boolean mTryStartShowAnimator;

    private boolean mIsDebug;

    public FDialoger(Activity activity)
    {
        if (activity == null)
            throw new NullPointerException("activity is null");

        mActivity = activity;
        mDialogerParent = activity.findViewById(android.R.id.content);

        final InternalDialogerView dialogerView = new InternalDialogerView(activity);
        mDialogerView = dialogerView;
        mContainerView = dialogerView.mContainerView;
        mBackgroundView = dialogerView.mBackgroundView;

        final int defaultHorizontalPadding = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.1f);
        paddingLeft(defaultHorizontalPadding);
        paddingRight(defaultHorizontalPadding);

        setAnimatorCreater(new AlphaCreater());
        setBackgroundColor(Color.parseColor("#66000000"));
    }

    @Override
    public void setDebug(boolean debug)
    {
        mIsDebug = debug;
    }

    @Override
    public Context getContext()
    {
        return mActivity;
    }

    @Override
    public View getContentView()
    {
        return mContentView;
    }

    @Override
    public void setContentView(int layoutId)
    {
        final View view = LayoutInflater.from(mActivity).inflate(layoutId, mContainerView, false);
        setContentView(view);
    }

    @Override
    public void setContentView(View view)
    {
        setDialogerView(view);
    }

    private void setDialogerView(View view)
    {
        if (mContentView == view)
            return;

        final ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        final ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null)
        {
            p.width = params.width;
            p.height = params.height;
        }

        final View old = mContainerView;
        mContentView = view;

        mContainerView.removeView(old);
        mContainerView.addView(view, p);

        onContentViewAdded(view);

        if (mIsDebug)
            Log.i(Dialoger.class.getSimpleName(), "contentView:" + view);
    }

    protected void onContentViewAdded(View contentView)
    {
    }

    @Override
    public void setBackgroundColor(int color)
    {
        if (color <= 0)
            mBackgroundView.setBackgroundDrawable(null);
        else
            mBackgroundView.setBackgroundColor(color);
    }

    @Override
    public <T extends View> T findViewById(int id)
    {
        if (mContentView == null)
            return null;

        return mContentView.findViewById(id);
    }

    @Override
    public void setCancelable(boolean cancel)
    {
        mCancelable = cancel;
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel)
    {
        if (cancel && !mCancelable)
            mCancelable = true;

        mCanceledOnTouchOutside = cancel;
    }

    @Override
    public void setAnimatorCreater(AnimatorCreater creater)
    {
        mAnimatorCreater = creater;
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener)
    {
        mOnDismissListener = listener;
    }

    @Override
    public void setOnShowListener(OnShowListener listener)
    {
        mOnShowListener = listener;
    }

    @Override
    public void addLifecycleCallback(LifecycleCallback callback)
    {
        if (callback == null)
            return;

        if (mLifecycleCallbacks == null)
            mLifecycleCallbacks = new CopyOnWriteArrayList<>();

        if (mLifecycleCallbacks.contains(callback))
            return;

        mLifecycleCallbacks.add(callback);
    }

    @Override
    public void removeLifecycleCallback(LifecycleCallback callback)
    {
        if (callback == null || mLifecycleCallbacks == null)
            return;

        mLifecycleCallbacks.remove(callback);

        if (mLifecycleCallbacks.isEmpty())
            mLifecycleCallbacks = null;
    }

    @Override
    public void setGravity(int gravity)
    {
        mGravity = gravity;
        mContainerView.setGravity(gravity);
    }

    @Override
    public void paddingLeft(int padding)
    {
        final View view = mContainerView;
        if (view.getPaddingLeft() == padding)
            return;

        view.setPadding(padding, view.getPaddingTop(),
                view.getPaddingRight(), view.getPaddingBottom());
    }

    @Override
    public void paddingTop(int padding)
    {
        final View view = mContainerView;
        if (view.getPaddingTop() == padding)
            return;

        view.setPadding(view.getPaddingLeft(), padding,
                view.getPaddingRight(), view.getPaddingBottom());
    }

    @Override
    public void paddingRight(int padding)
    {
        final View view = mContainerView;
        if (view.getPaddingRight() == padding)
            return;

        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(),
                padding, view.getPaddingBottom());
    }

    @Override
    public void paddingBottom(int padding)
    {
        final View view = mContainerView;
        if (view.getPaddingBottom() == padding)
            return;

        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(),
                view.getPaddingRight(), padding);
    }

    @Override
    public void paddings(int paddings)
    {
        final View view = mContainerView;
        if (view.getPaddingLeft() != paddings || view.getPaddingTop() != paddings
                || view.getPaddingRight() != paddings || view.getPaddingBottom() != paddings)
        {
            mContainerView.setPadding(paddings, paddings, paddings, paddings);
        }
    }

    @Override
    public void show()
    {
        attach(true);
    }

    @Override
    public boolean isShowing()
    {
        return mDialogerView.getParent() == mDialogerParent;
    }

    @Override
    public void dismiss()
    {
        attach(false);
    }

    @Override
    public void startDismissRunnable(long delay)
    {
        stopDismissRunnable();
        getDialogerHandler().postDelayed(mDismissRunnable, delay);
    }

    @Override
    public void stopDismissRunnable()
    {
        getDialogerHandler().removeCallbacks(mDismissRunnable);
    }

    private Handler mDialogerHandler;

    private Handler getDialogerHandler()
    {
        if (mDialogerHandler == null)
            mDialogerHandler = new Handler(Looper.getMainLooper());
        return mDialogerHandler;
    }

    private final Runnable mDismissRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            dismiss();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (isShowing() && keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (mCancelable)
                dismiss();
            return true;
        }
        return false;
    }

    private void attach(boolean attach)
    {
        if (mIsAttached == attach)
            return;

        if (attach)
        {
            if (isShowing())
            {
                if (getAnimatorHandler().isHideAnimatorStarted())
                {
                    if (mIsDebug)
                        Log.i(Dialoger.class.getSimpleName(), "cancel HideAnimator before show");

                    getAnimatorHandler().cancelHideAnimator();
                } else
                {
                    return;
                }
            }

            mIsAttached = true;

            if (mGravity == Gravity.NO_GRAVITY)
                setGravity(Gravity.CENTER);

            if (mIsDebug)
                Log.i(Dialoger.class.getSimpleName(), "try show");

            mDialogerParent.addView(mDialogerView);
        } else
        {
            if (mActivity.isFinishing())
                return;

            if (!isShowing())
                return;

            mIsAttached = false;

            mTryStartShowAnimator = false;
            getAnimatorHandler().setHideAnimator(createAnimator(false));
            if (getAnimatorHandler().startHideAnimator())
                return;

            removeDialogerView(false);
        }
    }

    private boolean mRemoveByHideAnimator;

    private VisibilityAnimatorHandler getAnimatorHandler()
    {
        if (mAnimatorHandler == null)
        {
            mAnimatorHandler = new VisibilityAnimatorHandler();
            mAnimatorHandler.setShowAnimatorListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationStart(Animator animation)
                {
                    super.onAnimationStart(animation);
                    if (mIsDebug)
                        Log.i(Dialoger.class.getSimpleName(), "show onAnimationStart ");
                }

                @Override
                public void onAnimationCancel(Animator animation)
                {
                    super.onAnimationCancel(animation);
                    if (mIsDebug)
                        Log.i(Dialoger.class.getSimpleName(), "show onAnimationCancel ");
                }

                @Override
                public void onAnimationEnd(Animator animation)
                {
                    super.onAnimationEnd(animation);
                    if (mIsDebug)
                        Log.i(Dialoger.class.getSimpleName(), "show onAnimationEnd ");
                }
            });
            mAnimatorHandler.setHideAnimatorListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationStart(Animator animation)
                {
                    super.onAnimationStart(animation);
                    if (mIsDebug)
                        Log.i(Dialoger.class.getSimpleName(), "dismiss onAnimationStart ");
                }

                @Override
                public void onAnimationCancel(Animator animation)
                {
                    super.onAnimationCancel(animation);
                    if (mIsDebug)
                        Log.i(Dialoger.class.getSimpleName(), "dismiss onAnimationCancel ");
                }

                @Override
                public void onAnimationEnd(Animator animation)
                {
                    super.onAnimationEnd(animation);
                    if (mIsDebug)
                        Log.i(Dialoger.class.getSimpleName(), "dismiss onAnimationEnd ");

                    removeDialogerView(true);
                }
            });
        }
        return mAnimatorHandler;
    }

    private Animator createAnimator(boolean show)
    {
        Animator animator = null;

        final Animator animatorBackground = (mBackgroundView.getBackground() != null) ?
                new AlphaCreater().createAnimator(show, mBackgroundView) : null;

        final Animator animatorContent = (mAnimatorCreater != null && mContentView != null) ?
                mAnimatorCreater.createAnimator(show, mContentView) : null;

        if (animatorBackground != null && animatorContent != null)
        {
            final AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(animatorBackground).with(animatorContent);
            animator = animatorSet;
        } else if (animatorBackground != null)
        {
            animator = animatorBackground;
        } else if (animatorContent != null)
        {
            animator = animatorContent;
        }

        if (mIsDebug)
            Log.i(Dialoger.class.getSimpleName(), "createAnimator " + (show ? "show" : "dismiss"));

        return animator;
    }

    private void removeDialogerView(boolean removeByHideAnimator)
    {
        if (mActivity.isFinishing())
            return;

        if (mIsDebug)
            Log.e(Dialoger.class.getSimpleName(), "removeDialogerView by hideAnimator:" + removeByHideAnimator);

        mRemoveByHideAnimator = removeByHideAnimator;

        final ViewParent parent = mDialogerView.getParent();
        if (parent instanceof ViewGroup)
        {
            ((ViewGroup) parent).removeView(mDialogerView);
        }
    }

    /**
     * dialog显示之后回调
     */
    protected void onStart()
    {
        if (mIsDebug)
            Log.i(Dialoger.class.getSimpleName(), "onStart");
    }

    /**
     * dialog关闭之后回调
     */
    protected void onStop()
    {
        if (mIsDebug)
            Log.i(Dialoger.class.getSimpleName(), "onStop");
    }

    private SimpleTargetDialoger mTargetDialoger;

    @Override
    public TargetDialoger target()
    {
        if (mTargetDialoger == null)
            mTargetDialoger = new SimpleTargetDialoger(this);
        return mTargetDialoger;
    }

    private final class InternalDialogerView extends FrameLayout
    {
        private final View mBackgroundView;
        private final LinearLayout mContainerView;

        public InternalDialogerView(Context context)
        {
            super(context);

            final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            mBackgroundView = new InternalBackgroundView(context);
            addView(mBackgroundView, params);

            mContainerView = new InernalContainerView(context);
            addView(mContainerView, params);
        }

        @Override
        public void onViewAdded(View child)
        {
            super.onViewAdded(child);
            if (child != mBackgroundView && child != mContainerView)
                throw new RuntimeException("you can not add view to dialoger view");
        }

        @Override
        public void onViewRemoved(View child)
        {
            super.onViewRemoved(child);
            if (child == mBackgroundView || child == mContainerView)
                throw new RuntimeException("you can not remove dialoger child");
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b)
        {
            super.onLayout(changed, l, t, r, b);
            if (changed)
                FDialoger.this.checkLayoutParams(this);

            if (mTryStartShowAnimator)
            {
                getAnimatorHandler().setShowAnimator(createAnimator(true));
                getAnimatorHandler().startShowAnimator();
                mTryStartShowAnimator = false;
            }
        }

        private boolean isViewUnder(View view, int x, int y)
        {
            if (view == null)
                return false;

            return x >= view.getLeft() && x < view.getRight()
                    && y >= view.getTop() && y < view.getBottom();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                if (!isViewUnder(mContentView, (int) event.getX(), (int) event.getY()))
                {
                    if (mCanceledOnTouchOutside)
                    {
                        dismiss();
                        return true;
                    }
                }
            }

            super.onTouchEvent(event);
            return true;
        }

        @Override
        protected void onAttachedToWindow()
        {
            super.onAttachedToWindow();
            if (mIsDebug)
                Log.i(Dialoger.class.getSimpleName(), "onAttachedToWindow");
            if (mDialogerView.getParent() != mDialogerParent)
                throw new RuntimeException("dialoger view can not be add to:" + mDialogerView.getParent());

            mTryStartShowAnimator = true;

            // notify
            onStart();
            if (mLifecycleCallbacks != null)
            {
                for (LifecycleCallback item : mLifecycleCallbacks)
                {
                    item.onStart(FDialoger.this);
                }
            }
            if (mOnShowListener != null)
            {
                getDialogerHandler().post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (mOnShowListener != null)
                            mOnShowListener.onShow(FDialoger.this);
                    }
                });
            }
        }

        @Override
        protected void onDetachedFromWindow()
        {
            super.onDetachedFromWindow();
            if (mIsDebug)
                Log.i(Dialoger.class.getSimpleName(), "onDetachedFromWindow");
            if (mIsAttached && !mActivity.isFinishing())
                throw new RuntimeException("you must call dismiss() method to remove dialoger");

            mTryStartShowAnimator = false;
            stopDismissRunnable();

            getAnimatorHandler().cancelShowAnimator();
            if (!mRemoveByHideAnimator)
                getAnimatorHandler().cancelHideAnimator();
            mRemoveByHideAnimator = false;

            // notify
            onStop();
            if (mLifecycleCallbacks != null)
            {
                for (LifecycleCallback item : mLifecycleCallbacks)
                {
                    item.onStop(FDialoger.this);
                }
            }
            if (mOnDismissListener != null)
            {
                getDialogerHandler().post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (mOnDismissListener != null)
                            mOnDismissListener.onDismiss(FDialoger.this);
                    }
                });
            }
        }
    }

    private final class InernalContainerView extends LinearLayout
    {
        public InernalContainerView(Context context)
        {
            super(context);
        }

        @Override
        public void onViewAdded(View child)
        {
            super.onViewAdded(child);
            if (child != mContentView)
                throw new RuntimeException("you can not add view to container");
        }

        @Override
        public void onViewRemoved(View child)
        {
            super.onViewRemoved(child);
            if (child == mContentView)
                throw new RuntimeException("you must call Dialoger.setContentView(null) instead");
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b)
        {
            super.onLayout(changed, l, t, r, b);
            if (changed)
                FDialoger.this.checkLayoutParams(this);
        }
    }

    private final class InternalBackgroundView extends View
    {
        public InternalBackgroundView(Context context)
        {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom)
        {
            super.onLayout(changed, left, top, right, bottom);
            if (changed)
                FDialoger.this.checkLayoutParams(this);
        }
    }

    private void checkLayoutParams(View view)
    {
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        if (params.width != ViewGroup.LayoutParams.MATCH_PARENT
                || params.height != ViewGroup.LayoutParams.MATCH_PARENT)
        {
            throw new RuntimeException("you can not change view's width or height");
        }

        if (params.leftMargin != 0 || params.rightMargin != 0
                || params.topMargin != 0 || params.bottomMargin != 0)
        {
            throw new RuntimeException("you can not set margin to view");
        }
    }
}
