package com.sd.dialoger;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.sd.lib.dialoger.Dialoger;
import com.sd.lib.dialoger.animator.AlphaCreator;
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
         * 设置窗口内容view动画创建对象，此处设置为透明度变化，可以实现AnimatorCreator接口来实现自定义动画
         *
         * 默认规则:
         * Gravity.CENTER:                      AlphaCreator 透明度
         *
         * Gravity.LEFT:                        SlideRightLeftCreator 向右滑入，向左滑出
         * Gravity.LEFT | Gravity.CENTER:       SlideRightLeftCreator
         *
         * Gravity.TOP:                         SlideBottomTopCreator 向下滑入，向上滑出
         * Gravity.TOP | Gravity.CENTER:        SlideBottomTopCreator
         *
         * Gravity.RIGHT:                       SlideLeftRightCreator 向左滑入，向右滑出
         * case Gravity.RIGHT | Gravity.CENTER: SlideLeftRightCreator
         *
         * Gravity.BOTTOM:                      SlideTopBottomCreator 向上滑入，向下滑出
         * Gravity.BOTTOM | Gravity.CENTER:     SlideTopBottomCreator
         *
         */
        dialoger.setAnimatorCreator(new AlphaCreator());

        /**
         * 设置动画时长
         */
        dialoger.setAnimatorDuration(2000);

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
