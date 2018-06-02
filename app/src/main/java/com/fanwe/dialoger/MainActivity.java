package com.fanwe.dialoger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.fanwe.lib.dialoger.Dialoger;
import com.fanwe.lib.dialoger.animator.AlphaCreater;
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
         * 设置dialog关闭监听
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
         * 设置按返回键是否可以关闭窗口，默认true
         */
        mDialoger.setCancelable(true);
        /**
         * 设置触摸到非内容区域是否关闭窗口，默认-true
         */
        mDialoger.setCanceledOnTouchOutside(true);
        /**
         * 设置窗口view的动画创建对象，此处为透明度变化
         */
        mDialoger.setDialogAnimatorCreater(new AlphaCreater());
        /**
         * 设置内容view的动画创建对象，此处为顶部滑入顶部滑出
         */
        mDialoger.setContentAnimatorCreater(new SlideTopTopCreater());
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_show:
                /**
                 * 设置显示在顶部，左右居中
                 */
                mDialoger.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                /**
                 * 显示dialog
                 */
                mDialoger.show();
                /**
                 * 延迟2000毫秒后关闭
                 */
                mDialoger.startDismissRunnable(2000);
                break;
        }
    }
}
