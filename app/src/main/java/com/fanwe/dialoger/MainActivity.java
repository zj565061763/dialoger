package com.fanwe.dialoger;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.fanwe.lib.dialoger.Dialoger;

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
            case R.id.btn_target:
                new PositionDialoger(this, v).show();
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
