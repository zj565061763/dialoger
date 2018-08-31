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
import android.view.View;

import com.fanwe.lib.dialoger.Dialoger;
import com.fanwe.lib.dialoger.TargetDialoger;
import com.fanwe.lib.dialoger.animator.PivotCreater;
import com.fanwe.lib.dialoger.animator.ScaleXYCreater;
import com.sd.lib.viewtracker.FViewTracker;
import com.sd.lib.viewtracker.ViewTracker;
import com.sd.lib.viewupdater.ViewUpdater;
import com.sd.lib.viewupdater.impl.OnGlobalLayoutChangeUpdater;

class SimpleTargetDialoger implements TargetDialoger
{
    private final Dialoger mDialoger;

    private ViewUpdater mUpdater;
    private ViewTracker mTracker;

    private Position mPosition;
    private int mMarginX;
    private int mMarginY;

    private DialogerBackup mDialogerBackup;

    private Dialoger.AnimatorCreater mModifyAnimatorCreater;

    public SimpleTargetDialoger(Dialoger dialoger)
    {
        if (dialoger == null)
            throw new NullPointerException("dialoger is null");

        mDialoger = dialoger;
        dialoger.addLifecycleCallback(mLifecycleCallback);
    }

    private final Dialoger.LifecycleCallback mLifecycleCallback = new Dialoger.LifecycleCallback()
    {
        @Override
        public void onStart(Dialoger dialoger)
        {
            if (getTracker().getSource() != null && getTracker().getTarget() != null && mPosition != null)
            {
                switch (mPosition)
                {
                    case LeftOutside:
                        getTracker().setPosition(ViewTracker.Position.Left);
                        setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), 1.0f, 0.5f));
                        break;
                    case LeftOutsideTop:
                        getTracker().setPosition(ViewTracker.Position.TopLeft);
                        setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), 1.0f, 0.0f));
                        break;
                    case LeftOutsideCenter:
                        getTracker().setPosition(ViewTracker.Position.LeftCenter);
                        setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), 1.0f, 0.5f));
                        break;
                    case LeftOutsideBottom:
                        getTracker().setPosition(ViewTracker.Position.BottomLeft);
                        setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), 1.0f, 1.0f));
                        break;


                    case TopOutside:
                        getTracker().setPosition(ViewTracker.Position.Top);
                        setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), 0.5f, 1.0f));
                        break;
                    case TopOutsideLeft:
                        getTracker().setPosition(ViewTracker.Position.TopLeft);
                        setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), 0.0f, 1.0f));
                        break;
                    case TopOutsideCenter:
                        getTracker().setPosition(ViewTracker.Position.TopCenter);
                        setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), 0.5f, 1.0f));
                        break;
                    case TopOutsideRight:
                        getTracker().setPosition(ViewTracker.Position.TopRight);
                        setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), 1.0f, 1.0f));
                        break;


                    case RightOutside:
                        getTracker().setPosition(ViewTracker.Position.Right);
                        setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), 0.0f, 0.5f));
                        break;
                    case RightOutsideTop:
                        getTracker().setPosition(ViewTracker.Position.TopRight);
                        setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), 0.0f, 0.0f));
                        break;
                    case RightOutsideCenter:
                        getTracker().setPosition(ViewTracker.Position.RightCenter);
                        setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), 0.0f, 0.5f));
                        break;
                    case RightOutsideBottom:
                        getTracker().setPosition(ViewTracker.Position.BottomRight);
                        setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), 0.0f, 1.0f));
                        break;


                    case BottomOutside:
                        getTracker().setPosition(ViewTracker.Position.Bottom);
                        setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), 0.5f, 0.0f));
                        break;
                    case BottomOutsideLeft:
                        getTracker().setPosition(ViewTracker.Position.BottomLeft);
                        setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), 0.0f, 0.0f));
                        break;
                    case BottomOutsideCenter:
                        getTracker().setPosition(ViewTracker.Position.BottomCenter);
                        setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), 0.5f, 0.0f));
                        break;
                    case BottomOutsideRight:
                        getTracker().setPosition(ViewTracker.Position.BottomRight);
                        setDefaultAnimator(new PivotCreater(new ScaleXYCreater(), 1.0f, 0.0f));
                        break;
                }

                getUpdater().start();
            }
        }

        @Override
        public void onStop(Dialoger dialoger)
        {
            getUpdater().stop();
            getTracker().setSource(null).setTarget(null);

            mPosition = null;
            if (mModifyAnimatorCreater != null && mModifyAnimatorCreater == mDialoger.getAnimatorCreater())
            {
                mDialoger.setAnimatorCreater(null);
            }
        }
    };

    private void setDefaultAnimator(Dialoger.AnimatorCreater creater)
    {
        if (mDialoger.getAnimatorCreater() == null)
        {
            mDialoger.setAnimatorCreater(creater);
            mModifyAnimatorCreater = creater;
        }
    }

    private DialogerBackup getDialogerBackup()
    {
        if (mDialogerBackup == null)
            mDialogerBackup = new DialogerBackup();
        return mDialogerBackup;
    }

    private ViewUpdater getUpdater()
    {
        if (mUpdater == null)
        {
            mUpdater = new OnGlobalLayoutChangeUpdater();
            mUpdater.setUpdatable(new ViewUpdater.Updatable()
            {
                @Override
                public void update()
                {
                    getTracker().update();
                }
            });
            mUpdater.setOnStateChangeCallback(new ViewUpdater.OnStateChangeCallback()
            {
                @Override
                public void onStateChanged(boolean started, ViewUpdater updater)
                {
                    if (started)
                    {
                        getDialogerBackup().backup(mDialoger);
                    } else
                    {
                        getDialogerBackup().restore(mDialoger);
                    }
                }
            });
        }
        return mUpdater;
    }

    private ViewTracker getTracker()
    {
        if (mTracker == null)
        {
            mTracker = new FViewTracker();
            mTracker.setCallback(new ViewTracker.Callback()
            {
                @Override
                public boolean canUpdate(View source, View target)
                {
                    return target != null && source != null
                            && source.getWidth() > 0 && source.getHeight() > 0;
                }

                @Override
                public void onUpdate(int x, int y, View source, View target)
                {
                    x += mMarginX;
                    y += mMarginY;

                    switch (mPosition)
                    {
                        case LeftOutside:
                        case LeftOutsideTop:
                        case LeftOutsideCenter:
                        case LeftOutsideBottom:
                            x -= source.getWidth();
                            break;

                        case TopOutside:
                        case TopOutsideLeft:
                        case TopOutsideCenter:
                        case TopOutsideRight:
                            y -= source.getHeight();
                            break;

                        case RightOutside:
                        case RightOutsideTop:
                        case RightOutsideCenter:
                        case RightOutsideBottom:
                            x += source.getWidth();
                            break;


                        case BottomOutside:
                        case BottomOutsideLeft:
                        case BottomOutsideCenter:
                        case BottomOutsideRight:
                            y += source.getHeight();
                            break;
                    }


                    final View sourceParent = (View) source.getParent();

                    final int left = x;
                    final int top = y;
                    final int right = sourceParent.getWidth() - x - source.getWidth();
                    final int bottom = sourceParent.getHeight() - y - source.getHeight();

                    Log.i(SimpleTargetDialoger.class.getSimpleName(), left + "," + top + "," + right + "," + bottom);

                    mDialoger.setPadding(left, top, right, bottom);

                    source.offsetLeftAndRight(x - source.getLeft());
                    source.offsetTopAndBottom(y - source.getTop());
                }
            });
        }
        return mTracker;
    }

    @Override
    public TargetDialoger setMarginX(int margin)
    {
        mMarginX = margin;
        return this;
    }

    @Override
    public TargetDialoger setMarginY(int margin)
    {
        mMarginY = margin;
        return this;
    }

    @Override
    public void show(View target, Position position)
    {
        if (position == null)
            throw new NullPointerException("position is null");

        mPosition = position;

        final View contentView = mDialoger.getContentView();
        getTracker().setSource(contentView);
        getTracker().setTarget(target);
        getUpdater().setView(contentView);

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
