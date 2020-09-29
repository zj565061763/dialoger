package com.sd.dialoger;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.sd.lib.dialoger.impl.FDialoger;

public class TestDialoger extends FDialoger
{
    public TestDialoger(Activity activity)
    {
        super(activity);
        setDebug(true);
        setPadding(0, 0, 0, 0);
        setBackgroundDim(false);

        setContentView(R.layout.dialoger_test);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
                /**
                 * 关闭窗口
                 */
                dismiss();
            }
        });
    }
}
