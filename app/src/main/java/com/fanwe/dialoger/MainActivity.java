package com.fanwe.dialoger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

public class MainActivity extends AppCompatActivity
{
    private DialogView mDialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDialogView = new DialogView(this);
        mDialogView.getDialoger().show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (mDialogView.getDialoger().onKeyDown(keyCode, event))
            return true;
        return super.onKeyDown(keyCode, event);
    }
}
