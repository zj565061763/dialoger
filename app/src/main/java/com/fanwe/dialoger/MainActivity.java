package com.fanwe.dialoger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
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
            mDialogView = new DialogView(this);
        return mDialogView;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
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
                getDialogView().setGravity(Gravity.CENTER);
                getDialogView().getDialoger().show();
                break;
        }
    }
}
