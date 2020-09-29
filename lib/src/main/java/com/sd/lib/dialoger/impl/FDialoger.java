package com.sd.lib.dialoger.impl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.sd.lib.dialoger.Dialoger;
import com.sd.lib.dialoger.R;
import com.sd.lib.dialoger.TargetDialoger;
import com.sd.lib.dialoger.animator.AlphaCreator;
import com.sd.lib.dialoger.animator.ObjectAnimatorCreator;
import com.sd.lib.dialoger.animator.SlideBottomTopCreator;
import com.sd.lib.dialoger.animator.SlideLeftRightCreator;
import com.sd.lib.dialoger.animator.SlideRightLeftCreator;
import com.sd.lib.dialoger.animator.SlideTopBottomCreator;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FDialoger implements Dialoger
{
    private final Activity mActivity;
    private final int mThemeResId;

    private final View mDialogerView;
    private final View mBackgroundView;
    private final LinearLayout mContainerView;
    private View mContentView;

    private boolean mCancelable = true;
    private boolean mCanceledOnTouchOutside = true;
    private int mGravity = Gravity.NO_GRAVITY;

    private State mState = State.Dismissed;

    private OnDismissListener mOnDismissListener;
    private OnShowListener mOnShowListener;
    private List<LifecycleCallback> mLifecycleCallbacks;

    private boolean mLockDialoger;
    private boolean mIsBackgroundDim;

    private FVisibilityAnimatorHandler mAnimatorHandler;
    private AnimatorCreator mAnimatorCreator;
    private AnimatorCreator mBackgroundViewAnimatorCreator;

    private boolean mTryStartShowAnimator;
    private boolean mIsAnimatorCreatorModifiedInternal;

    private boolean mIsDebug;

    public FDialoger(Activity activity)
    {
        this(activity, 0);
    }

    public FDialoger(Activity activity, int themeResId)
    {
        if (activity == null)
            throw new NullPointerException("activity is null");

        mActivity = activity;
        mThemeResId = themeResId != 0 ? themeResId : R.style.lib_dialoger_default;

        final InternalDialogerView dialogerView = new InternalDialogerView(activity);
        mDialogerView = dialogerView;
        mContainerView = dialogerView.mContainerView;
        mBackgroundView = dialogerView.mBackgroundView;

        final int defaultPadding = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.1f);
        setPadding(defaultPadding, 0, defaultPadding, 0);

        setBackgroundDim(true);
        setGravity(Gravity.CENTER);
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
    public Activity getOwnerActivity()
    {
        return mActivity;
    }

    @Override
    public Window getWindow()
    {
        return getDialog().getWindow();
    }

    @Override
    public Dialog dialog()
    {
        return getDialog();
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

    @Override
    public void setBackgroundDim(boolean backgroundDim)
    {
        mIsBackgroundDim = backgroundDim;
        if (backgroundDim)
        {
            final int color = mActivity.getResources().getColor(R.color.lib_dialoger_background_dim);
            mBackgroundView.setBackgroundColor(color);
        } else
        {
            mBackgroundView.setBackgroundColor(Color.TRANSPARENT);
        }
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
            }

            if (mIsDebug)
                Log.i(Dialoger.class.getSimpleName(), "onContentViewChanged:" + old + " , " + view);

            onContentViewChanged(old, view);
        }
    }

    protected void onContentViewChanged(View oldView, View contentView)
    {
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
        if (cancel)
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
    public void setAnimatorCreator(AnimatorCreator creator)
    {
        mAnimatorCreator = creator;
        mIsAnimatorCreatorModifiedInternal = false;
    }

    @Override
    public AnimatorCreator getAnimatorCreator()
    {
        return mAnimatorCreator;
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
    public boolean isShowing()
    {
        return mState == State.Shown;
    }

    @Override
    public void show()
    {
        if (Looper.myLooper() == Looper.getMainLooper())
        {
            mShowRunnable.run();
        } else
        {
            getDialogerHandler().removeCallbacks(mShowRunnable);
            getDialogerHandler().post(mShowRunnable);
        }
    }

    @Override
    public void dismiss()
    {
        if (Looper.myLooper() == Looper.getMainLooper())
        {
            mDismissRunnable.run();
        } else
        {
            getDialogerHandler().removeCallbacks(mDismissRunnable);
            getDialogerHandler().post(mDismissRunnable);
        }
    }

    private final Runnable mShowRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            final boolean isFinishing = mActivity.isFinishing();

            if (mIsDebug)
                Log.i(Dialoger.class.getSimpleName(), "try show isFinishing:" + isFinishing);

            if (isFinishing)
                return;

            if (mState.isShowPart())
                return;

            setState(State.TryShow);

            if (getAnimatorHandler().isHideAnimatorStarted())
            {
                if (mIsDebug)
                    Log.i(Dialoger.class.getSimpleName(), "cancel HideAnimator before show");

                getAnimatorHandler().cancelHideAnimator();
            }

            getDialog().show();
            setState(State.Shown);
        }
    };

    private final Runnable mDismissRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            final boolean isFinishing = mActivity.isFinishing();

            if (mIsDebug)
                Log.i(Dialoger.class.getSimpleName(), "try dismiss isFinishing:" + isFinishing);

            if (isFinishing)
            {
                if (getAnimatorHandler().isShowAnimatorStarted())
                    getAnimatorHandler().cancelShowAnimator();

                if (getAnimatorHandler().isHideAnimatorStarted())
                    getAnimatorHandler().cancelHideAnimator();

                setLockDialoger(true);
                removeDialogerView(false);
                return;
            }

            if (mState.isDismissPart())
                return;

            setState(State.TryDismiss);

            if (getAnimatorHandler().isShowAnimatorStarted())
            {
                if (mIsDebug)
                    Log.i(Dialoger.class.getSimpleName(), "cancel ShowAnimator before dismiss");

                getAnimatorHandler().cancelShowAnimator();
            }

            setLockDialoger(true);

            getAnimatorHandler().setHideAnimator(createAnimator(false));
            if (getAnimatorHandler().startHideAnimator())
            {
                // 等待动画结束后让窗口消失
            } else
            {
                removeDialogerView(false);
            }
        }
    };

    private void setLockDialoger(boolean lock)
    {
        if (mLockDialoger != lock)
        {
            mLockDialoger = lock;
            if (mIsDebug)
                Log.i(Dialoger.class.getSimpleName(), "setLockDialoger:" + lock);
        }
    }

    private void setTryStartShowAnimator(boolean tryShow)
    {
        if (mTryStartShowAnimator != tryShow)
        {
            mTryStartShowAnimator = tryShow;
            if (mIsDebug)
                Log.i(Dialoger.class.getSimpleName(), "setTryStartShowAnimator:" + tryShow);
        }
    }

    private void startShowAnimator()
    {
        if (mTryStartShowAnimator)
        {
            if (mIsDebug)
                Log.i(Dialoger.class.getSimpleName(), "startShowAnimator");

            setTryStartShowAnimator(false);
            getAnimatorHandler().setShowAnimator(createAnimator(true));
            getAnimatorHandler().startShowAnimator();
        }
    }

    private void setState(State state)
    {
        if (state == null)
            throw new IllegalArgumentException("state is null");

        if (mState != state)
        {
            if (mIsDebug)
                Log.e(Dialoger.class.getSimpleName(), "setState:" + state);

            mState = state;

            if (state.isDismissPart())
            {
                setTryStartShowAnimator(false);
            }
        }
    }

    @Override
    public void startDismissRunnable(long delay)
    {
        stopDismissRunnable();
        getDialogerHandler().postDelayed(mDelayedDismissRunnable, delay);
    }

    @Override
    public void stopDismissRunnable()
    {
        getDialogerHandler().removeCallbacks(mDelayedDismissRunnable);
    }

    private Handler mDialogerHandler;

    private Handler getDialogerHandler()
    {
        if (mDialogerHandler == null)
            mDialogerHandler = new Handler(Looper.getMainLooper());
        return mDialogerHandler;
    }

    private final Runnable mDelayedDismissRunnable = new Runnable()
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
        return false;
    }

    @Override
    public void onBackPressed()
    {
        if (mCancelable)
        {
            if (mIsDebug)
                Log.i(Dialoger.class.getSimpleName(), "onBackPressed try dismiss ");

            dismiss();
        }
    }

    private void setDefaultConfigBeforeShow()
    {
        if (mAnimatorCreator == null)
        {
            switch (mGravity)
            {
                case Gravity.CENTER:
                    setAnimatorCreator(new AlphaCreator());
                    mIsAnimatorCreatorModifiedInternal = true;
                    break;
                case Gravity.LEFT:
                case Gravity.LEFT | Gravity.CENTER:
                    setAnimatorCreator(new SlideRightLeftCreator());
                    mIsAnimatorCreatorModifiedInternal = true;
                    break;
                case Gravity.TOP:
                case Gravity.TOP | Gravity.CENTER:
                    setAnimatorCreator(new SlideBottomTopCreator());
                    mIsAnimatorCreatorModifiedInternal = true;
                    break;
                case Gravity.RIGHT:
                case Gravity.RIGHT | Gravity.CENTER:
                    setAnimatorCreator(new SlideLeftRightCreator());
                    mIsAnimatorCreatorModifiedInternal = true;
                    break;
                case Gravity.BOTTOM:
                case Gravity.BOTTOM | Gravity.CENTER:
                    setAnimatorCreator(new SlideTopBottomCreator());
                    mIsAnimatorCreatorModifiedInternal = true;
                    break;
            }
        }
    }

    private FVisibilityAnimatorHandler getAnimatorHandler()
    {
        if (mAnimatorHandler == null)
        {
            mAnimatorHandler = new FVisibilityAnimatorHandler();
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

        final Animator animatorBackground = mIsBackgroundDim ?
                getBackgroundViewAnimatorCreator().createAnimator(show, mBackgroundView) : null;

        final Animator animatorContent = (mAnimatorCreator == null || mContentView == null) ?
                null : mAnimatorCreator.createAnimator(show, mContentView);

        if (animatorBackground != null && animatorContent != null)
        {
            final long duration = getAnimatorDuration(animatorContent);
            if (duration < 0)
                throw new RuntimeException("Illegal duration:" + duration);
            animatorBackground.setDuration(duration);

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
            Log.i(Dialoger.class.getSimpleName(), "createAnimator " + (show ? "show" : "dismiss") + " animator " + (animator == null ? "null" : "not null"));

        return animator;
    }

    private AnimatorCreator getBackgroundViewAnimatorCreator()
    {
        if (mBackgroundViewAnimatorCreator == null)
        {
            mBackgroundViewAnimatorCreator = new ObjectAnimatorCreator()
            {
                @Override
                protected String getPropertyName()
                {
                    return View.ALPHA.getName();
                }

                @Override
                protected float getValueHidden(View view)
                {
                    return 0.0f;
                }

                @Override
                protected float getValueShown(View view)
                {
                    return 1.0f;
                }

                @Override
                protected float getValueCurrent(View view)
                {
                    return view.getAlpha();
                }

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
            };
        }
        return mBackgroundViewAnimatorCreator;
    }

    private void removeDialogerView(boolean removeByHideAnimator)
    {
        if (mIsDebug)
            Log.e(Dialoger.class.getSimpleName(), "removeDialogerView by hideAnimator:" + removeByHideAnimator);

        try
        {
            getDialog().dismiss();
        } catch (Exception e)
        {
            e.printStackTrace();
            if (mIsDebug)
                Log.e(Dialoger.class.getSimpleName(), "removeDialogerView error:" + e);
        } finally
        {
            setState(State.Dismissed);
        }
    }

    protected void onCreate(Bundle savedInstanceState)
    {
    }

    protected void onSaveInstanceState(Bundle bundle)
    {
    }

    protected void onStart()
    {
    }

    protected void onStop()
    {
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

            mBackgroundView = new InternalBackgroundView(context);
            addView(mBackgroundView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            mContainerView = new InternalContainerView(context);
            addView(mContainerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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
                    if (mCanceledOnTouchOutside && mCancelable)
                    {
                        if (mIsDebug)
                            Log.i(Dialoger.class.getSimpleName(), "touch outside try dismiss ");

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
        }
    }

    private final class InternalContainerView extends LinearLayout
    {
        public InternalContainerView(Context context)
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
            if (left < 0)
                left = getPaddingLeft();
            if (top < 0)
                top = getPaddingTop();
            if (right < 0)
                right = getPaddingRight();
            if (bottom < 0)
                bottom = getPaddingBottom();

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

            if (mIsDebug)
                Log.i(Dialoger.class.getSimpleName(), "onContentViewAdded:" + child);
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

            if (mIsDebug)
                Log.i(Dialoger.class.getSimpleName(), "onContentViewRemoved:" + child);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b)
        {
            super.onLayout(changed, l, t, r, b);
            if (changed)
                FDialoger.this.checkMatchLayoutParams(this);

            startShowAnimator();
        }

        @Override
        protected void onAttachedToWindow()
        {
            super.onAttachedToWindow();

            if (mState.isShowPart())
                setTryStartShowAnimator(true);

            if (getWidth() > 0 && getHeight() > 0)
                startShowAnimator();
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
                FDialoger.this.checkMatchLayoutParams(this);
        }
    }

    private void checkMatchLayoutParams(View view)
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
            mDialog = new InternalDialog(mActivity, mThemeResId);
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

    private final class InternalDialog extends Dialog
    {
        public InternalDialog(Context context, int themeResId)
        {
            super(context, themeResId);
            final WindowManager.LayoutParams params = getWindow().getAttributes();
            params.gravity = Gravity.TOP;
        }

        private void setDefaultParams()
        {
            final int targetWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            int targetHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

            if (Build.VERSION.SDK_INT >= 21)
            {
                final ViewGroup.LayoutParams layoutParams = getContentView().getLayoutParams();
                if (layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT)
                {
                    final int systemVisibility = getWindow().getDecorView().getSystemUiVisibility();
                    final int value = systemVisibility & View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                    if (value == View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
                    {
                        targetHeight = FDialoger.this.getContext().getResources().getDisplayMetrics().heightPixels;
                    }
                }
            }

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
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            FDialoger.this.onCreate(savedInstanceState);
        }

        @Override
        public Bundle onSaveInstanceState()
        {
            final Bundle bundle = super.onSaveInstanceState();
            FDialoger.this.onSaveInstanceState(bundle);
            return bundle;
        }

        @Override
        protected void onStart()
        {
            super.onStart();
            if (mIsDebug)
                Log.i(Dialoger.class.getSimpleName(), "onStart");

            getActivityLifecycleCallbacks().register(true);
            FDialogerHolder.addDialoger(FDialoger.this);
            setDefaultParams();

            FDialoger.this.onStart();
            if (mLifecycleCallbacks != null)
            {
                for (LifecycleCallback item : mLifecycleCallbacks)
                {
                    item.onStart(FDialoger.this);
                }
            }

            setLockDialoger(false);
            setDefaultConfigBeforeShow();
        }

        @Override
        protected void onStop()
        {
            super.onStop();
            if (mIsDebug)
                Log.i(Dialoger.class.getSimpleName(), "onStop");

            getActivityLifecycleCallbacks().register(false);
            FDialogerHolder.removeDialoger(FDialoger.this);

            stopDismissRunnable();

            FDialoger.this.onStop();
            if (mLifecycleCallbacks != null)
            {
                for (LifecycleCallback item : mLifecycleCallbacks)
                {
                    item.onStop(FDialoger.this);
                }
            }

            if (mIsAnimatorCreatorModifiedInternal)
                setAnimatorCreator(null);
        }

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event)
        {
            if (mLockDialoger)
                return false;

            if (FDialoger.this.onKeyDown(keyCode, event))
                return true;

            return super.onKeyDown(keyCode, event);
        }

        @Override
        public void onBackPressed()
        {
            FDialoger.this.onBackPressed();
        }
    }

    private InternalActivityLifecycleCallbacks mActivityLifecycleCallbacks;

    private InternalActivityLifecycleCallbacks getActivityLifecycleCallbacks()
    {
        if (mActivityLifecycleCallbacks == null)
            mActivityLifecycleCallbacks = new InternalActivityLifecycleCallbacks();
        return mActivityLifecycleCallbacks;
    }

    private final class InternalActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks
    {
        public void register(boolean register)
        {
            final Application application = mActivity.getApplication();
            application.unregisterActivityLifecycleCallbacks(this);
            if (register)
                application.registerActivityLifecycleCallbacks(this);
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState)
        {
        }

        @Override
        public void onActivityStarted(Activity activity)
        {
        }

        @Override
        public void onActivityResumed(Activity activity)
        {
        }

        @Override
        public void onActivityPaused(Activity activity)
        {
        }

        @Override
        public void onActivityStopped(Activity activity)
        {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState)
        {
        }

        @Override
        public void onActivityDestroyed(Activity activity)
        {
            if (activity == mActivity)
            {
                if (mIsDebug)
                    Log.e(Dialoger.class.getSimpleName(), "onActivityDestroyed");

                FDialogerHolder.remove(getOwnerActivity());
                dismiss();
            }
        }
    }

    /**
     * 关闭指定Activity的所有窗口
     *
     * @param activity
     */
    public static void dismissAll(Activity activity)
    {
        if (activity.isFinishing())
            return;

        final List<FDialoger> list = FDialogerHolder.get(activity);
        if (list == null || list.isEmpty())
            return;

        for (FDialoger item : list)
        {
            item.dismiss();
        }
    }

    private enum State
    {
        TryShow,
        Shown,

        TryDismiss,
        Dismissed;

        public boolean isShowPart()
        {
            return this == Shown || this == TryShow;
        }

        public boolean isDismissPart()
        {
            return this == Dismissed || this == TryDismiss;
        }
    }

    private static long getAnimatorDuration(Animator animator)
    {
        long duration = animator.getDuration();
        if (duration < 0)
        {
            if (animator instanceof AnimatorSet)
            {
                final List<Animator> list = ((AnimatorSet) animator).getChildAnimations();
                for (Animator item : list)
                {
                    final long durationItem = getAnimatorDuration(item);
                    if (durationItem > duration)
                        duration = durationItem;
                }
            }
        }
        return duration;
    }
}
