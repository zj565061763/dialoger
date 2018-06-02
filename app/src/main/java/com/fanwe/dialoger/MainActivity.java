package com.fanwe.dialoger;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.fanwe.lib.dialoger.Dialoger;
import com.fanwe.lib.dialoger.animator.SlideTopTopCreater;

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
        /**
         * 设置窗口关闭监听
         */
        mDialoger.setOnDismissListener(new Dialoger.OnDismissListener()
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
        mDialoger.setOnShowListener(new Dialoger.OnShowListener()
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
        mDialoger.setCancelable(true);
        /**
         * 设置触摸到非内容区域是否关闭窗口，默认-true
         */
        mDialoger.setCanceledOnTouchOutside(true);
        /**
         * 设置内容view的动画创建对象，此处为顶部滑入顶部滑出，默认为透明度变化
         */
        mDialoger.setAnimatorCreater(new SlideTopTopCreater());
        /**
         * 设置窗口背景颜色
         */
        mDialoger.setBackgroundColor(Color.parseColor("#66000000"));
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_show:
                /**
                 * 设置显示位置
                 */
                mDialoger.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                /**
                 * 显示窗口
                 */
                mDialoger.show();
                break;
        }
    }
}
