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
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.fanwe.lib.dialoger.Dialoger;
import com.fanwe.lib.dialoger.R;
import com.fanwe.lib.dialoger.TargetDialoger;
import com.fanwe.lib.dialoger.animator.AlphaCreater;
import com.fanwe.lib.dialoger.animator.SlideBottomTopCreater;
import com.fanwe.lib.dialoger.animator.SlideLeftRightCreater;
import com.fanwe.lib.dialoger.animator.SlideRightLeftCreater;
import com.fanwe.lib.dialoger.animator.SlideTopBottomCreater;
import com.fanwe.lib.dialoger.utils.VisibilityAnimatorHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FDialoger implements Dialoger
{
    private final Activity mActivity;
    private final View mDialogerView;
    private final LinearLayout mContainerView;
    private final View mBackgroundView;

    private View mContentView;
    private boolean mCancelable = true;
    private boolean mCanceledOnTouchOutside = true;

    private int mGravity = Gravity.NO_GRAVITY;

    private OnDismissListener mOnDismissListener;
    private OnShowListener mOnShowListener;
    private List<LifecycleCallback> mLifecycleCallbacks;

    private boolean mLockDialoger;

    private VisibilityAnimatorHandler mAnimatorHandler;
    private AnimatorCreater mAnimatorCreater;
    private boolean mTryStartShowAnimator;

    private boolean mIsAnimatorCreaterModifiedInternal;

    private boolean mIsDebug;

    public FDialoger(Activity activity)
    {
        if (activity == null)
            throw new NullPointerException("activity is null");

        mActivity = activity;

        final InternalDialogerView dialogerView = new InternalDialogerView(activity);
        mDialogerView = dialogerView;
        mContainerView = dialogerView.mContainerView;
        mBackgroundView = dialogerView.mBackgroundView;

        final int defaultPadding = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.1f);
        setPadding(defaultPadding, 0, defaultPadding, 0);

        setGravity(Gravity.NO_GRAVITY);
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
        final View old = mContentView;
        if (old != view)
        {
            mContentView = view;

            if (old != null)
                mContainerView.removeView(old);

            if (view != null)
            {
                final ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                final ViewGroup.LayoutParams params = view.getLayoutParams();
                if (params != null)
                {
                    p.width = params.width;
                    p.height = params.height;
                }

                mContainerView.addView(view, p);
                onContentViewAdded(view);

                if (mIsDebug)
                    Log.i(Dialoger.class.getSimpleName(), "contentView:" + view);
            }
        }
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
    public void setAnimatorCreater(AnimatorCreater creater)
    {
        mAnimatorCreater = creater;
        mIsAnimatorCreaterModifiedInternal = false;
    }

    @Override
    public AnimatorCreater getAnimatorCreater()
    {
        return mAnimatorCreater;
    }

    @Override
    public void setGravity(int gravity)
    {
        mContainerView.setGravity(gravity);
    }

    @Override
    public int getGravity()
    {
        return mGravity;
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom)
    {
        mContainerView.setPadding(left, top, right, bottom);
    }

    @Override
    public int getPaddingLeft()
    {
        return mContainerView.getPaddingLeft();
    }

    @Override
    public int getPaddingTop()
    {
        return mContainerView.getPaddingTop();
    }

    @Override
    public int getPaddingRight()
    {
        return mContainerView.getPaddingRight();
    }

    @Override
    public int getPaddingBottom()
    {
        return mContainerView.getPaddingBottom();
    }

    @Override
    public void show()
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

        if (mIsDebug)
            Log.i(Dialoger.class.getSimpleName(), "try show");

        mLockDialoger = false;
        getDialog().show();
    }

    @Override
    public boolean isShowing()
    {
        return getDialog().isShowing();
    }

    @Override
    public void dismiss()
    {
        if (isShowing())
        {
            if (mLockDialoger)
                return;

            if (mIsDebug)
                Log.i(Dialoger.class.getSimpleName(), "try dismiss");

            mLockDialoger = true;

            mTryStartShowAnimator = false;
            getAnimatorHandler().setHideAnimator(createAnimator(false));
            if (getAnimatorHandler().startHideAnimator())
                return;

            removeDialogerView(false);
        }
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
    public boolean onTouchEvent(MotionEvent event)
    {
        return false;
    }

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

    private void setDefaultConfigBeforeShow()
    {
        if (mGravity == Gravity.NO_GRAVITY)
            setGravity(Gravity.CENTER);

        if (mAnimatorCreater == null)
        {
            switch (mGravity)
            {
                case Gravity.CENTER:
                    setAnimatorCreater(new AlphaCreater());
                    mIsAnimatorCreaterModifiedInternal = true;
                    break;
                case Gravity.LEFT:
                case Gravity.LEFT | Gravity.CENTER:
                    setAnimatorCreater(new SlideRightLeftCreater());
                    mIsAnimatorCreaterModifiedInternal = true;
                    break;
                case Gravity.TOP:
                case Gravity.TOP | Gravity.CENTER:
                    setAnimatorCreater(new SlideBottomTopCreater());
                    mIsAnimatorCreaterModifiedInternal = true;
                    break;
                case Gravity.RIGHT:
                case Gravity.RIGHT | Gravity.CENTER:
                    setAnimatorCreater(new SlideLeftRightCreater());
                    mIsAnimatorCreaterModifiedInternal = true;
                    break;
                case Gravity.BOTTOM:
                case Gravity.BOTTOM | Gravity.CENTER:
                    setAnimatorCreater(new SlideTopBottomCreater());
                    mIsAnimatorCreaterModifiedInternal = true;
                    break;
            }
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

        final Animator animatorBackground = (mBackgroundView.getBackground() == null) ? null : new AlphaCreater()
        {
            @Override
            protected void onAnimationStart(boolean show, View view)
            {
                super.onAnimationStart(show, view);
                view.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onAnimationEnd(boolean show, View view)
            {
                super.onAnimationEnd(show, view);
                if (!show)
                    view.setVisibility(View.INVISIBLE);
            }
        }.createAnimator(show, mBackgroundView);

        final Animator animatorContent = (mAnimatorCreater == null || mContentView == null) ?
                null : mAnimatorCreater.createAnimator(show, mContentView);

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
        getDialog().dismiss();
    }

    /**
     * dialog显示之前回调
     */
    protected void onStart()
    {
        if (mIsDebug)
            Log.i(Dialoger.class.getSimpleName(), "onStart");
    }

    /**
     * dialog关闭之前回调
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
        public void setVisibility(int visibility)
        {
            if (visibility == GONE || visibility == INVISIBLE)
                throw new IllegalArgumentException("you can not hide dialoger");
            super.setVisibility(visibility);
        }

        private boolean isViewUnder(View view, int x, int y)
        {
            if (view == null)
                return false;

            return x >= view.getLeft() && x < view.getRight()
                    && y >= view.getTop() && y < view.getBottom();
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev)
        {
            if (mLockDialoger)
                return true;
            return super.onInterceptTouchEvent(ev);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
            if (mLockDialoger)
                return false;

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

            if (FDialoger.this.onTouchEvent(event))
                return true;

            super.onTouchEvent(event);
            return true;
        }

        @Override
        protected void onAttachedToWindow()
        {
            super.onAttachedToWindow();
            if (mIsDebug)
                Log.i(Dialoger.class.getSimpleName(), "onAttachedToWindow");
        }

        @Override
        protected void onDetachedFromWindow()
        {
            super.onDetachedFromWindow();
            if (mIsDebug)
                Log.i(Dialoger.class.getSimpleName(), "onDetachedFromWindow");

            mLockDialoger = true;
            mTryStartShowAnimator = false;
            stopDismissRunnable();

            getAnimatorHandler().cancelShowAnimator();
            if (!mRemoveByHideAnimator)
                getAnimatorHandler().cancelHideAnimator();
            mRemoveByHideAnimator = false;

            if (mIsAnimatorCreaterModifiedInternal)
                setAnimatorCreater(null);
        }
    }

    private final class InernalContainerView extends LinearLayout
    {
        public InernalContainerView(Context context)
        {
            super(context);
        }

        @Override
        public void setGravity(int gravity)
        {
            if (mGravity != gravity)
            {
                mGravity = gravity;
                super.setGravity(gravity);
            }
        }

        @Override
        public void setPadding(int left, int top, int right, int bottom)
        {
            if (left != getPaddingLeft() || top != getPaddingTop()
                    || right != getPaddingRight() || bottom != getPaddingBottom())
            {
                super.setPadding(left, top, right, bottom);
            }
        }

        @Override
        public void setVisibility(int visibility)
        {
            if (visibility == GONE || visibility == INVISIBLE)
                throw new IllegalArgumentException("you can not hide container");
            super.setVisibility(visibility);
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
            {
                // 外部直接移除内容view的话，关闭窗口
                dismiss();
            }
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

    private Dialog mDialog;

    private Dialog getDialog()
    {
        if (mDialog == null)
        {
            mDialog = new Dialog(mActivity, R.style.lib_dialoger_dialoger)
            {
                private void setDefaultParams()
                {
                    final int targetWidth = ViewGroup.LayoutParams.MATCH_PARENT;
                    final int targetHeight = mActivity.getResources().getDisplayMetrics().heightPixels - getActivityStatusBarHeight(mActivity);

                    final WindowManager.LayoutParams params = getWindow().getAttributes();
                    if (params.width != targetWidth || params.height != targetHeight
                            || params.horizontalMargin != 0 || params.verticalMargin != 0)
                    {
                        params.width = targetWidth;
                        params.height = targetHeight;
                        params.horizontalMargin = 0;
                        params.verticalMargin = 0;
                        getWindow().setAttributes(params);
                    }

                    final View view = getWindow().getDecorView();
                    if (view.getPaddingLeft() != 0 || view.getPaddingTop() != 0
                            || view.getPaddingRight() != 0 || view.getPaddingBottom() != 0)
                    {
                        view.setPadding(0, 0, 0, 0);
                    }
                }

                @Override
                protected void onStart()
                {
                    super.onStart();
                    setDefaultParams();

                    FDialoger.this.onStart();
                    if (mLifecycleCallbacks != null)
                    {
                        for (LifecycleCallback item : mLifecycleCallbacks)
                        {
                            item.onStart(FDialoger.this);
                        }
                    }

                    setDefaultConfigBeforeShow();
                    mTryStartShowAnimator = true;
                }

                @Override
                protected void onStop()
                {
                    super.onStop();

                    FDialoger.this.onStop();
                    if (mLifecycleCallbacks != null)
                    {
                        for (LifecycleCallback item : mLifecycleCallbacks)
                        {
                            item.onStop(FDialoger.this);
                        }
                    }
                }

                @Override
                public boolean onKeyDown(int keyCode, KeyEvent event)
                {
                    if (mLockDialoger)
                        return false;

                    return FDialoger.this.onKeyDown(keyCode, event);
                }
            };
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setCancelable(false);
            mDialog.setOnShowListener(new DialogInterface.OnShowListener()
            {
                @Override
                public void onShow(DialogInterface dialog)
                {
                    if (mOnShowListener != null)
                        mOnShowListener.onShow(FDialoger.this);
                }
            });
            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
            {
                @Override
                public void onDismiss(DialogInterface dialog)
                {
                    if (mOnDismissListener != null)
                        mOnDismissListener.onDismiss(FDialoger.this);
                }
            });

            mDialog.setContentView(mDialogerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }
        return mDialog;
    }

    private static int getActivityStatusBarHeight(Activity activity)
    {
        final boolean isStatusBarVisible = ((activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == 0);
        if (isStatusBarVisible)
        {
            return getStatusBarHeight(activity);
        } else
        {
            return 0;
        }
    }

    private static int getStatusBarHeight(Context context)
    {
        int result = 0;

        final int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            result = context.getResources().getDimensionPixelSize(resourceId);

        return result;
    }
}
