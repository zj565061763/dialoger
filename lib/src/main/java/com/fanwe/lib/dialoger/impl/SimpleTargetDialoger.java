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

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.lib.dialoger.Dialoger;
import com.fanwe.lib.dialoger.TargetDialoger;
import com.fanwe.lib.dialoger.animator.PivotCreater;
import com.fanwe.lib.dialoger.animator.ScaleXYCreater;
import com.fanwe.lib.dialoger.utils.PivotHolder;
import com.fanwe.lib.updater.Updater;
import com.fanwe.lib.updater.ViewUpdater;
import com.fanwe.lib.updater.impl.OnGlobalLayoutChangeUpdater;
import com.fanwe.lib.viewtracker.FViewTracker;
import com.fanwe.lib.viewtracker.ViewTracker;

class SimpleTargetDialoger implements TargetDialoger
{
    private final Dialoger mDialoger;

    private final ViewUpdater mUpdater = new OnGlobalLayoutChangeUpdater();
    private final ViewTracker mTracker = new FViewTracker();
    private Position mPosition;

    private boolean mPaddingToPosition;
    private DialogerBackup mDialogerBackup;

    private boolean mIsAnimatorCreaterModified;
    private Dialoger.AnimatorCreater mModifyAnimatorCreater;

    public SimpleTargetDialoger(Dialoger dialoger)
    {
        if (dialoger == null)
            throw new NullPointerException("dialoger is null");

        mDialoger = dialoger;
        initUpdater();
        initTracker();

        dialoger.addLifecycleCallback(new Dialoger.LifecycleCallback()
        {
            @Override
            public void onStart(Dialoger dialoger)
            {
                if (mTracker.getSource() != null && mTracker.getTarget() != null && mPosition != null)
                {
                    switch (mPosition)
                    {
                        case LeftOutside:
                            mTracker.setPosition(ViewTracker.Position.Left);
                            setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), PivotHolder.Position.RightCenter));
                            break;
                        case LeftOutsideTop:
                            mTracker.setPosition(ViewTracker.Position.TopLeft);
                            setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), PivotHolder.Position.TopRight));
                            break;
                        case LeftOutsideCenter:
                            mTracker.setPosition(ViewTracker.Position.LeftCenter);
                            setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), PivotHolder.Position.RightCenter));
                            break;
                        case LeftOutsideBottom:
                            mTracker.setPosition(ViewTracker.Position.BottomLeft);
                            setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), PivotHolder.Position.BottomRight));
                            break;


                        case TopOutside:
                            mTracker.setPosition(ViewTracker.Position.Top);
                            setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), PivotHolder.Position.BottomCenter));
                            break;
                        case TopOutsideLeft:
                            mTracker.setPosition(ViewTracker.Position.TopLeft);
                            setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), PivotHolder.Position.BottomLeft));
                            break;
                        case TopOutsideCenter:
                            mTracker.setPosition(ViewTracker.Position.TopCenter);
                            setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), PivotHolder.Position.BottomCenter));
                            break;
                        case TopOutsideRight:
                            mTracker.setPosition(ViewTracker.Position.TopRight);
                            setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), PivotHolder.Position.BottomRight));
                            break;


                        case RightOutside:
                            mTracker.setPosition(ViewTracker.Position.Right);
                            setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), PivotHolder.Position.LeftCenter));
                            break;
                        case RightOutsideTop:
                            mTracker.setPosition(ViewTracker.Position.TopRight);
                            setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), PivotHolder.Position.TopLeft));
                            break;
                        case RightOutsideCenter:
                            mTracker.setPosition(ViewTracker.Position.RightCenter);
                            setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), PivotHolder.Position.LeftCenter));
                            break;
                        case RightOutsideBottom:
                            mTracker.setPosition(ViewTracker.Position.BottomRight);
                            setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), PivotHolder.Position.BottomLeft));
                            break;


                        case BottomOutside:
                            mTracker.setPosition(ViewTracker.Position.Bottom);
                            setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), PivotHolder.Position.TopCenter));
                            break;
                        case BottomOutsideLeft:
                            mTracker.setPosition(ViewTracker.Position.BottomLeft);
                            setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), PivotHolder.Position.TopLeft));
                            break;
                        case BottomOutsideCenter:
                            mTracker.setPosition(ViewTracker.Position.BottomCenter);
                            setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), PivotHolder.Position.TopCenter));
                            break;
                        case BottomOutsideRight:
                            mTracker.setPosition(ViewTracker.Position.BottomRight);
                            setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), PivotHolder.Position.TopRight));
                            break;
                    }

                    mUpdater.start();
                }
            }

            @Override
            public void onStop(Dialoger dialoger)
            {
                mUpdater.stop();
                mTracker.setSource(null).setTarget(null);
                mPosition = null;
                mIsAnimatorCreaterModified = false;
            }
        });
    }

    private void setDefaultAnimator(Dialoger.AnimatorCreater creater)
    {
        if (mDialoger.getAnimatorCreater() == null)
        {
            mDialoger.setAnimatorCreater(creater);

            mIsAnimatorCreaterModified = true;
            mModifyAnimatorCreater = creater;
        }
    }

    private DialogerBackup getDialogerBackup()
    {
        if (mDialogerBackup == null)
            mDialogerBackup = new DialogerBackup();
        return mDialogerBackup;
    }

    private void initUpdater()
    {
        mUpdater.setUpdatable(new Updater.Updatable()
        {
            @Override
            public void update()
            {
                mTracker.update();
            }
        });
        mUpdater.setOnStateChangeCallback(new Updater.OnStateChangeCallback()
        {
            @Override
            public void onStateChanged(boolean started, Updater updater)
            {
                if (started)
                {
                    if (mPaddingToPosition)
                        getDialogerBackup().backup(mDialoger);
                } else
                {
                    getDialogerBackup().restore(mDialoger);

                    if (mIsAnimatorCreaterModified && mModifyAnimatorCreater == mDialoger.getAnimatorCreater())
                        mDialoger.setAnimatorCreater(null);
                }
            }
        });
    }

    private void initTracker()
    {
        mTracker.setCallback(new ViewTracker.Callback()
        {
            @Override
            public boolean canUpdate(View source, View target)
            {
                return target != null && source != null
                        && source.getWidth() > 0 && source.getHeight() > 0;
            }

            @Override
            public void onUpdate(int x, int y, View source, View sourceParent, View target)
            {
                Log.i(SimpleTargetDialoger.class.getSimpleName(), x + "," + y);

                switch (mPosition)
                {
                    case LeftOutside:
                    case LeftOutsideTop:
                    case LeftOutsideCenter:
                    case LeftOutsideBottom:
                        x -= source.getWidth();
                        showLeftOfTarget(x, y, source, sourceParent, target);
                        break;

                    case TopOutside:
                    case TopOutsideLeft:
                    case TopOutsideCenter:
                    case TopOutsideRight:
                        y -= source.getHeight();
                        showTopOfTarget(x, y, source, sourceParent, target);
                        break;

                    case RightOutside:
                    case RightOutsideTop:
                    case RightOutsideCenter:
                    case RightOutsideBottom:
                        x += source.getWidth();
                        showRightOfTarget(x, y, source, sourceParent, target);
                        break;

                    case BottomOutside:
                    case BottomOutsideLeft:
                    case BottomOutsideCenter:
                    case BottomOutsideRight:
                        y += source.getHeight();
                        showBottomOfTarget(x, y, source, sourceParent, target);
                        break;
                }

                mDialoger.setGravity(Gravity.TOP | Gravity.LEFT);
                final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) source.getLayoutParams();
                if (params.leftMargin != x || params.topMargin != y)
                {
                    params.leftMargin = x;
                    params.topMargin = y;
                    source.setLayoutParams(params);
                }
            }

            private void showLeftOfTarget(int x, int y, View source, View sourceParent, View target)
            {
                if (mPaddingToPosition)
                {
                    final int padding = sourceParent.getWidth() - x - source.getWidth();
                    mDialoger.setPadding(mDialoger.getPaddingLeft(), mDialoger.getPaddingTop(),
                            padding, mDialoger.getPaddingBottom());
                }
            }

            private void showTopOfTarget(int x, int y, View source, View sourceParent, View target)
            {
                if (mPaddingToPosition)
                {
                    final int padding = sourceParent.getHeight() - y - source.getHeight();
                    mDialoger.setPadding(mDialoger.getPaddingLeft(), mDialoger.getPaddingTop(),
                            mDialoger.getPaddingRight(), padding);
                }
            }

            private void showRightOfTarget(int x, int y, View source, View sourceParent, View target)
            {
                if (mPaddingToPosition)
                {
                    final int padding = x;
                    mDialoger.setPadding(padding, mDialoger.getPaddingTop(),
                            mDialoger.getPaddingRight(), mDialoger.getPaddingBottom());
                }
            }

            private void showBottomOfTarget(int x, int y, View source, View sourceParent, View target)
            {
                if (mPaddingToPosition)
                {
                    final int padding = y;
                    mDialoger.setPadding(mDialoger.getPaddingLeft(), padding,
                            mDialoger.getPaddingRight(), mDialoger.getPaddingBottom());
                }
            }
        });
    }

    @Override
    public TargetDialoger setPaddingToPosition(boolean padding)
    {
        mPaddingToPosition = padding;
        return this;
    }

    @Override
    public TargetDialoger setMarginX(int marginX)
    {
        mTracker.setMarginX(marginX);
        return this;
    }

    @Override
    public TargetDialoger setMarginY(int marginY)
    {
        mTracker.setMarginY(marginY);
        return this;
    }

    @Override
    public void showPosition(View target, Position position)
    {
        if (position == null)
            throw new NullPointerException("position is null");

        mPosition = position;

        final View contentView = mDialoger.getContentView();
        mTracker.setSource(contentView);
        mTracker.setTarget(target);
        mUpdater.setView(contentView);

        mDialoger.show();
    }

    private static class DialogerBackup
    {
        private int mPaddingLeft;
        private int mPaddingTop;
        private int mPaddingRight;
        private int mPaddingBottom;
        private int mGravity;

        private boolean mHasBackup;

        public void backup(Dialoger dialoger)
        {
            mPaddingLeft = dialoger.getPaddingLeft();
            mPaddingTop = dialoger.getPaddingTop();
            mPaddingRight = dialoger.getPaddingRight();
            mPaddingBottom = dialoger.getPaddingBottom();
            mGravity = dialoger.getGravity();

            mHasBackup = true;
        }

        public void restore(Dialoger dialoger)
        {
            if (mHasBackup)
            {
                dialoger.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
                dialoger.setGravity(mGravity);
            }
            mHasBackup = false;
        }
    }
}
