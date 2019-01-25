package com.sd.dialoger;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.sd.lib.dialoger.Dialoger;
import com.sd.lib.dialoger.animator.AlphaCreater;
import com.sd.lib.dialoger.impl.FDialoger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void showSimpleDemo()
    {
        final FDialoger dialoger = new FDialoger(this);

        /**
         * 设置调试模式，内部会输出日志，日志tag：Dialoger
         */
        dialoger.setDebug(true);
        /**
         * 设置窗口要显示的内容
         */
        dialoger.setContentView(new Button(this));

        /**
         * 设置窗口关闭监听
         */
        dialoger.setOnDismissListener(new Dialoger.OnDismissListener()
        {
            @Override
            public void onDismiss(Dialoger dialoger)
            {
                Log.i(TAG, "onDismiss:" + dialoger);
            }
        });

        /**
         * 设置窗口显示监听
         */
        dialoger.setOnShowListener(new Dialoger.OnShowListener()
        {
            @Override
            public void onShow(Dialoger dialoger)
            {
                Log.i(TAG, "onShow:" + dialoger);
            }
        });

        /**
         * 设置按返回键是否可以关闭窗口，默认true
         */
        dialoger.setCancelable(true);

        /**
         * 设置触摸到非内容区域是否关闭窗口，默认true
         */
        dialoger.setCanceledOnTouchOutside(true);

        /**
         * 设置窗口背景颜色，默认#66000000
         */
        dialoger.setBackgroundColor(Color.parseColor("#66000000"));

        /**
         * 设置上下左右间距
         */
        dialoger.setPadding(0, 0, 0, 0);

        /**
         * 设置重力属性，默认居中显示
         */
        dialoger.setGravity(Gravity.CENTER);

        /**
         * 设置窗口内容view动画创建对象，此处设置为透明度变化，可以实现AnimatorCreater接口来实现自定义动画
         *
         * 默认规则:
         * Gravity.CENTER:                      AlphaCreater 透明度
         *
         * Gravity.LEFT:                        SlideRightLeftCreater 向右滑入，向左滑出
         * Gravity.LEFT | Gravity.CENTER:       SlideRightLeftCreater
         *
         * Gravity.TOP:                         SlideBottomTopCreater 向下滑入，向上滑出
         * Gravity.TOP | Gravity.CENTER:        SlideBottomTopCreater
         *
         * Gravity.RIGHT:                       SlideLeftRightCreater 向左滑入，向右滑出
         * case Gravity.RIGHT | Gravity.CENTER: SlideLeftRightCreater
         *
         * Gravity.BOTTOM:                      SlideTopBottomCreater 向上滑入，向下滑出
         * Gravity.BOTTOM | Gravity.CENTER:     SlideTopBottomCreater
         *
         */
        dialoger.setAnimatorCreater(new AlphaCreater());

        /**
         * 显示窗口
         */
        dialoger.show();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_simple_demo:
                showSimpleDemo();
                break;
            case R.id.btn_target:
                new PositionDialoger(this, v).show();
                break;
        }
    }
}
