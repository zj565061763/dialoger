package com.fanwe.dialoger;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.fanwe.lib.dialoger.FDialoger;

public class TestDialoger extends FDialoger
{
    public TestDialoger(Activity activity)
    {
        super(activity);
        /**
         * 设置窗口内容
         */
        setContentView(R.layout.dialog_view);
    }

    @Override
    protected void onContentViewAdded(View contentView)
    {
        super.onContentViewAdded(contentView);
        contentView.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
