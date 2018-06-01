package com.fanwe.dialoger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

import com.fanwe.lib.dialoger.Dialoger;
import com.fanwe.lib.dialoger.animator.AlphaCreater;
import com.fanwe.lib.dialoger.animator.ScaleXYCreater;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    public static final String TAG = MainActivity.class.getSimpleName();

    private DialogView mDialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public DialogView getDialogView()
    {
        if (mDialogView == null)
        {
            mDialogView = new DialogView(this);
            /**
             * 设置dialog要显示的内容
             */
            mDialogView.getDialoger().setContentView(R.layout.dialog_view);
            /**
             * 设置dialog关闭监听
             */
            mDialogView.getDialoger().setOnDismissListener(new Dialoger.OnDismissListener()
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
            mDialogView.getDialoger().setCancelable(true);
            /**
             * 设置触摸到非内容区域是否关闭窗口，默认-true
             */
            mDialogView.getDialoger().setCanceledOnTouchOutside(true);
            /**
             * 设置窗口view的动画创建对象，此处为透明度变化创建对象
             */
            mDialogView.getDialoger().setDialogAnimatorCreater(new AlphaCreater());
            /**
             * 设置内容view的动画创建对象，此处为缩放创建对象
             */
            mDialogView.getDialoger().setContentAnimatorCreater(new ScaleXYCreater());
        }
        return mDialogView;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // 传递事件
        if (getDialogView().getDialoger().onKeyDown(keyCode, event))
            return true;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_show:
                // 居中显示
                getDialogView().setGravity(Gravity.CENTER);
                // 显示dialog
                getDialogView().getDialoger().show();
                break;
        }
    }
}
