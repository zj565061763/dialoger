package com.fanwe.dialoger;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

import com.fanwe.lib.dialoger.Dialoger;
import com.fanwe.lib.dialoger.TargetDialoger;
import com.fanwe.lib.dialoger.animator.AlphaCreater;
import com.fanwe.lib.dialoger.animator.CombineCreater;
import com.fanwe.lib.dialoger.animator.ScaleXYCreater;
import com.fanwe.lib.dialoger.animator.SlideBottomTopCreater;
import com.fanwe.lib.dialoger.animator.SlideLeftRightCreater;
import com.fanwe.lib.dialoger.animator.SlideRightLeftCreater;
import com.fanwe.lib.dialoger.animator.SlideTopBottomCreater;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    public static final String TAG = MainActivity.class.getSimpleName();

    private TestDialoger mDialoger;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDialoger = new TestDialoger(this);

        // 设置窗口关闭监听
        mDialoger.setOnDismissListener(new Dialoger.OnDismissListener()
        {
            @Override
            public void onDismiss(Dialoger dialoger)
            {
                Log.i(TAG, "onDismiss:" + dialoger);
            }
        });

        // 设置窗口显示监听
        mDialoger.setOnShowListener(new Dialoger.OnShowListener()
        {
            @Override
            public void onShow(Dialoger dialoger)
            {
                Log.i(TAG, "onShow:" + dialoger);
            }
        });

        // 设置按返回键是否可以关闭窗口，默认true
        mDialoger.setCancelable(true);

        // 设置触摸到非内容区域是否关闭窗口，默认false
        mDialoger.setCanceledOnTouchOutside(true);

        // 设置窗口背景颜色，默认#66000000
        mDialoger.setBackgroundColor(Color.parseColor("#66000000"));
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_top_center:
                // 设置动画创建对象，此处为：向下滑入，向上滑出
                mDialoger.setAnimatorCreater(new SlideBottomTopCreater());

                // 显示在目标view的底部
                mDialoger.target().showPosition(v, TargetDialoger.Position.BottomOutsideCenter);
                break;
            case R.id.btn_center:
                // 设置动画创建对象，通过CombineCreater可以组合多个creater对象
                mDialoger.setAnimatorCreater(new CombineCreater(new AlphaCreater(), new ScaleXYCreater()));

                // 设置重力属性
                mDialoger.setGravity(Gravity.CENTER);

                // 显示窗口
                mDialoger.show();
                break;
            case R.id.btn_bottom_center:
                // 设置动画创建对象，此处为：向上滑入，向下滑出
                mDialoger.setAnimatorCreater(new SlideTopBottomCreater());

                // 显示在目标view的顶部
                mDialoger.target().showPosition(v, TargetDialoger.Position.TopOutsideCenter);
                break;
            case R.id.btn_left_center:
                // 设置动画创建对象，此处为：向右滑入，向左滑出
                mDialoger.setAnimatorCreater(new SlideRightLeftCreater());

                // 显示在目标view的右边
                mDialoger.target().showPosition(v, TargetDialoger.Position.RightOutsideTop);
                break;
            case R.id.btn_right_center:
                // 设置动画创建对象，此处为：向左滑入，向右滑出
                mDialoger.setAnimatorCreater(new SlideLeftRightCreater());

                // 显示在目标view的左边
                mDialoger.target().showPosition(v, TargetDialoger.Position.LeftOutsideTop);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        /**
         * 传递按键事件给窗口
         */
        if (mDialoger.onKeyDown(keyCode, event))
            return true;
        return super.onKeyDown(keyCode, event);
    }
}
