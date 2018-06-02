package com.fanwe.lib.dialoger;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
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
import android.widget.LinearLayout;

import com.fanwe.lib.dialoger.animator.AlphaCreater;
import com.fanwe.lib.dialoger.utils.VisibilityAnimatorHandler;

public class FDialoger implements Dialoger
{
    private final Activity mActivity;
    private final ViewGroup mDialogerParent;
    private final InternalDialogerView mDialogerView;

    private View mContentView;
    private boolean mCancelable = true;
    private boolean mCanceledOnTouchOutside = true;

    private int mGravity = Gravity.NO_GRAVITY;

    private OnDismissListener mOnDismissListener;
    private OnShowListener mOnShowListener;

    private boolean mIsAttached;
    /**
     * true - try show
     * <br>
     * false - try dismiss
     * <br>
     * null - none
     */
    private Boolean mTryShow;

    private VisibilityAnimatorHandler mAnimatorHandler;
    private AnimatorCreater mDialogAnimatorCreater;
    private AnimatorCreater mContentAnimatorCreater;
    private boolean mTryStartShowAnimator;

    private boolean mIsDebug;

    public FDialoger(Activity activity)
    {
        if (activity == null)
            throw new NullPointerException("activity is null");

        mActivity = activity;
        mDialogerParent = activity.findViewById(android.R.id.content);
        mDialogerView = new InternalDialogerView(activity);

        setDialogAnimatorCreater(new AlphaCreater());

        final int defaultHorizontalPadding = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.1f);
        paddingLeft(defaultHorizontalPadding);
        paddingRight(defaultHorizontalPadding);
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
    public View getDialogerView()
    {
        return mDialogerView;
    }

    @Override
    public View getContentView()
    {
        return mContentView;
    }

    @Override
    public void setContentView(int layoutId)
    {
        final View view = LayoutInflater.from(mActivity).inflate(layoutId, mDialogerView, false);
        setContentView(view);
    }

    @Override
    public void setContentView(View view)
    {
        setDialogerView(view);
    }

    private void setDialogerView(View view)
    {
        mContentView = view;

        final ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        final ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null)
        {
            p.width = params.width;
            p.height = params.height;
        }

        mDialogerView.removeAllViews();
        mDialogerView.addView(view, p);

        onContentViewAdded(view);
    }

    @Override
    public <T extends View> T findViewById(int id)
    {
        if (mContentView == null)
            return null;
        return mContentView.findViewById(id);
    }

    protected void onContentViewAdded(View contentView)
    {
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
    public void setDialogAnimatorCreater(AnimatorCreater creater)
    {
        mDialogAnimatorCreater = creater;
    }

    @Override
    public void setContentAnimatorCreater(AnimatorCreater creater)
    {
        mContentAnimatorCreater = creater;
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
    public void setGravity(int gravity)
    {
        mGravity = gravity;
        mDialogerView.setGravity(gravity);
    }

    @Override
    public void paddingLeft(int padding)
    {
        final View view = mDialogerView;
        view.setPadding(padding, view.getPaddingTop(),
                view.getPaddingRight(), view.getPaddingBottom());
    }

    @Override
    public void paddingTop(int padding)
    {
        final View view = mDialogerView;
        view.setPadding(view.getPaddingLeft(), padding,
                view.getPaddingRight(), view.getPaddingBottom());
    }

    @Override
    public void paddingRight(int padding)
    {
        final View view = mDialogerView;
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(),
                padding, view.getPaddingBottom());
    }

    @Override
    public void paddingBottom(int padding)
    {
        final View view = mDialogerView;
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(),
                view.getPaddingRight(), padding);
    }

    @Override
    public void paddings(int paddings)
    {
        mDialogerView.setPadding(paddings, paddings, paddings, paddings);
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
            if (mTryShow != null && mTryShow == false)
                throw new RuntimeException("show() can not be called in onStop() method");

            if (mContentView == null)
                return;

            if (isShowing())
            {
                if (getAnimatorHandler().isHideAnimatorStarted())
                    getAnimatorHandler().cancelHideAnimator();
                else
                    return;
            }

            mIsAttached = true;

            if (mGravity == Gravity.NO_GRAVITY)
                setGravity(Gravity.CENTER);

            mTryShow = true;
            onStart();
            mDialogerParent.addView(mDialogerView);
        } else
        {
            if (mTryShow != null && mTryShow == true)
                throw new RuntimeException("dismiss() can not be called in onStart() method");

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

        final Animator dialogAnimator = mDialogAnimatorCreater != null ?
                mDialogAnimatorCreater.createAnimator(show, mDialogerView) : null;

        final Animator contentAnimator = (mContentAnimatorCreater != null && getContentView() != null) ?
                mContentAnimatorCreater.createAnimator(show, getContentView()) : null;

        if (dialogAnimator != null && contentAnimator != null)
        {
            final AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(dialogAnimator).with(contentAnimator);
            animator = animatorSet;
        } else if (dialogAnimator != null)
        {
            animator = dialogAnimator;
        } else if (contentAnimator != null)
        {
            animator = contentAnimator;
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
            mTryShow = false;
            onStop();
            ((ViewGroup) parent).removeView(mDialogerView);
        }
    }

    /**
     * dialog要显示之前的回调
     */
    protected void onStart()
    {
        if (mIsDebug)
            Log.i(Dialoger.class.getSimpleName(), "onStart");
    }

    /**
     * dialog要关闭之前的回调
     */
    protected void onStop()
    {
        if (mIsDebug)
            Log.i(Dialoger.class.getSimpleName(), "onStop");
    }

    private class InternalDialogerView extends LinearLayout
    {
        public InternalDialogerView(Context context)
        {
            super(context);
        }

        @Override
        public void onViewAdded(View child)
        {
            super.onViewAdded(child);
            if (getChildCount() > 1)
                throw new RuntimeException("dialoger view can only have one child");

            if (mIsDebug)
                Log.i(Dialoger.class.getSimpleName(), "contentView:" + child);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b)
        {
            super.onLayout(changed, l, t, r, b);
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

            mTryShow = null;
            mTryStartShowAnimator = true;
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
                throw new RuntimeException("you must call dismiss() method to remove dialoger view");

            mTryShow = null;
            mTryStartShowAnimator = false;
            stopDismissRunnable();

            getAnimatorHandler().cancelShowAnimator();
            if (!mRemoveByHideAnimator)
                getAnimatorHandler().cancelHideAnimator();

            mRemoveByHideAnimator = false;

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
}
