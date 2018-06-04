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

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.fanwe.lib.dialoger.Dialoger;
import com.fanwe.lib.dialoger.TargetDialoger;
import com.fanwe.lib.updater.Updater;
import com.fanwe.lib.updater.ViewUpdater;
import com.fanwe.lib.updater.impl.OnPreDrawUpdater;
import com.fanwe.lib.viewtracker.FViewTracker;
import com.fanwe.lib.viewtracker.ViewTracker;

class SimpleTargetDialoger implements TargetDialoger
{
    private final Dialoger mDialoger;

    private final ViewUpdater mUpdater = new OnPreDrawUpdater();
    private final ViewTracker mTracker = new FViewTracker();
    private Position mPosition;

    private int mPaddingLeft;
    private int mPaddingTop;
    private int mPaddingRight;
    private int mPaddingBottom;

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
                if (mTracker.getSource() != null && mTracker.getTarget() != null)
                {
                    if (mUpdater.start())
                        mUpdater.notifyUpdatable();
                }
            }

            @Override
            public void onStop(Dialoger dialoger)
            {
                mUpdater.stop();
                mTracker.setSource(null).setTarget(null);
            }
        });
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
            public void onStateChanged(boolean started)
            {
                if (started)
                {
                    final View container = (View) mDialoger.getContentView().getParent();
                    mPaddingLeft = container.getPaddingLeft();
                    mPaddingTop = container.getPaddingTop();
                    mPaddingRight = container.getPaddingRight();
                    mPaddingBottom = container.getPaddingBottom();
                } else
                {
                    mDialoger.paddingLeft(mPaddingLeft);
                    mDialoger.paddingTop(mPaddingTop);
                    mDialoger.paddingRight(mPaddingRight);
                    mDialoger.paddingBottom(mPaddingBottom);
                }
            }
        });

        final Activity activity = (Activity) mDialoger.getContext();
        mUpdater.setView(activity.findViewById(android.R.id.content));
    }

    private void initTracker()
    {
        mTracker.setCallback(new ViewTracker.Callback()
        {
            @Override
            public void onUpdate(int x, int y, View source, View sourceParent, View target)
            {
                final int dx = x - source.getLeft();
                final int dy = y - source.getTop();
                source.offsetLeftAndRight(dx);
                source.offsetTopAndBottom(dy);

                Log.i(SimpleTargetDialoger.class.getSimpleName(), "onUpdate:" + source.getLeft() + "," + source.getTop() + " " + mPosition);

                switch (mPosition)
                {
                    case LeftOutsideTop:
                        mTracker.setPosition(ViewTracker.Position.LeftOutsideTop);
                        showLeftOfTarget(x, y, source, sourceParent, target);
                        break;
                    case LeftOutsideCenter:
                        mTracker.setPosition(ViewTracker.Position.LeftOutsideCenter);
                        showLeftOfTarget(x, y, source, sourceParent, target);
                        break;
                    case LeftOutsideBottom:
                        mTracker.setPosition(ViewTracker.Position.LeftOutsideBottom);
                        showLeftOfTarget(x, y, source, sourceParent, target);
                        break;

                    case TopOutsideLeft:
                        mTracker.setPosition(ViewTracker.Position.TopOutsideLeft);
                        showTopOfTarget(x, y, source, sourceParent, target);
                        break;
                    case TopOutsideCenter:
                        mTracker.setPosition(ViewTracker.Position.TopOutsideCenter);
                        showTopOfTarget(x, y, source, sourceParent, target);
                        break;
                    case TopOutsideRight:
                        mTracker.setPosition(ViewTracker.Position.TopOutsideRight);
                        showTopOfTarget(x, y, source, sourceParent, target);
                        break;

                    case RightOutsideTop:
                        mTracker.setPosition(ViewTracker.Position.RightOutsideTop);
                        showRightOfTarget(x, y, source, sourceParent, target);
                        break;
                    case RightOutsideCenter:
                        mTracker.setPosition(ViewTracker.Position.RightOutsideCenter);
                        showRightOfTarget(x, y, source, sourceParent, target);
                        break;
                    case RightOutsideBottom:
                        mTracker.setPosition(ViewTracker.Position.RightOutsideBottom);
                        showRightOfTarget(x, y, source, sourceParent, target);
                        break;

                    case BottomOutsideLeft:
                        mTracker.setPosition(ViewTracker.Position.BottomOutsideLeft);
                        showBottomOfTarget(x, y, source, sourceParent, target);
                        break;
                    case BottomOutsideCenter:
                        mTracker.setPosition(ViewTracker.Position.BottomOutsideCenter);
                        showBottomOfTarget(x, y, source, sourceParent, target);
                        break;
                    case BottomOutsideRight:
                        mTracker.setPosition(ViewTracker.Position.BottomOutsideRight);
                        showBottomOfTarget(x, y, source, sourceParent, target);
                        break;
                }
            }

            private void showLeftOfTarget(int x, int y, View source, View sourceParent, View target)
            {
                mDialoger.setGravity(Gravity.RIGHT);
                int padding = sourceParent.getWidth() - x - source.getWidth();
                mDialoger.paddingRight(padding);
            }

            private void showTopOfTarget(int x, int y, View source, View sourceParent, View target)
            {
                mDialoger.setGravity(Gravity.BOTTOM);
                int padding = sourceParent.getHeight() - y - source.getHeight();
                mDialoger.paddingBottom(padding);
            }

            private void showRightOfTarget(int x, int y, View source, View sourceParent, View target)
            {
                mDialoger.setGravity(Gravity.LEFT);
                int padding = x;
                mDialoger.paddingLeft(padding);
            }

            private void showBottomOfTarget(int x, int y, View source, View sourceParent, View target)
            {
                mDialoger.setGravity(Gravity.TOP);
                int padding = y;
                mDialoger.paddingTop(padding);
            }
        });
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

        mTracker.setSource(mDialoger.getContentView());
        mTracker.setTarget(target);

        mDialoger.show();
    }
}
