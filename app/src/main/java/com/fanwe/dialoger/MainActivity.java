package com.fanwe.dialoger;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.fanwe.lib.dialoger.Dialoger;
import com.fanwe.lib.dialoger.impl.FDialoger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showSimpleDemo();
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
         * 设置触摸到非内容区域是否关闭窗口，默认false
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
         * 设置重力属性
         */
        dialoger.setGravity(Gravity.CENTER);

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
            case R.id.btn_target:
                new PositionDialoger(this, v).show();
                break;
        }
    }
}
