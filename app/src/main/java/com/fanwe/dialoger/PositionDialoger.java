package com.fanwe.dialoger;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.fanwe.lib.dialoger.TargetDialoger;
import com.fanwe.lib.dialoger.impl.FDialoger;

public class PositionDialoger extends FDialoger implements View.OnClickListener
{
    private View mTargetView;

    private Button btn_left_top, btn_left_center, btn_left_bottom;
    private Button btn_right_top, btn_right_center, btn_right_bottom;

    private Button btn_top_left, btn_top_center, btn_top_right;
    private Button btn_bottom_left, btn_bottom_center, btn_bottom_right;

    public PositionDialoger(Activity activity, View targetView)
    {
        super(activity);
        setPadding(0, 0, 0, 0);
        setBackgroundColor(0);
        mTargetView = targetView;
        setContentView(R.layout.dialoger_position);
        btn_left_top = findViewById(R.id.btn_left_top);
        btn_left_center = findViewById(R.id.btn_left_center);
        btn_left_bottom = findViewById(R.id.btn_left_bottom);

        btn_right_top = findViewById(R.id.btn_right_top);
        btn_right_center = findViewById(R.id.btn_right_center);
        btn_right_bottom = findViewById(R.id.btn_right_bottom);

        btn_top_left = findViewById(R.id.btn_top_left);
        btn_top_center = findViewById(R.id.btn_top_center);
        btn_top_right = findViewById(R.id.btn_top_right);

        btn_bottom_left = findViewById(R.id.btn_bottom_left);
        btn_bottom_center = findViewById(R.id.btn_bottom_center);
        btn_bottom_right = findViewById(R.id.btn_bottom_right);

        btn_left_top.setOnClickListener(this);
        btn_left_center.setOnClickListener(this);
        btn_left_bottom.setOnClickListener(this);

        btn_right_top.setOnClickListener(this);
        btn_right_center.setOnClickListener(this);
        btn_right_bottom.setOnClickListener(this);

        btn_top_left.setOnClickListener(this);
        btn_top_center.setOnClickListener(this);
        btn_top_right.setOnClickListener(this);

        btn_bottom_left.setOnClickListener(this);
        btn_bottom_center.setOnClickListener(this);
        btn_bottom_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        dismiss();

        final TestDialoger dialoger = new TestDialoger((Activity) getContext());

        switch (v.getId())
        {
            case R.id.btn_left_top:
                dialoger.target().showPosition(mTargetView, TargetDialoger.Position.LeftOutsideTop);
                break;
            case R.id.btn_left_center:
                dialoger.target().showPosition(mTargetView, TargetDialoger.Position.LeftOutsideCenter);
                break;
            case R.id.btn_left_bottom:
                dialoger.target().showPosition(mTargetView, TargetDialoger.Position.LeftOutsideBottom);
                break;

            case R.id.btn_right_top:
                dialoger.target().showPosition(mTargetView, TargetDialoger.Position.RightOutsideTop);
                break;
            case R.id.btn_right_center:
                dialoger.target().showPosition(mTargetView, TargetDialoger.Position.RightOutsideCenter);
                break;
            case R.id.btn_right_bottom:
                dialoger.target().showPosition(mTargetView, TargetDialoger.Position.RightOutsideBottom);
                break;

            case R.id.btn_top_left:
                dialoger.target().showPosition(mTargetView, TargetDialoger.Position.TopOutsideLeft);
                break;
            case R.id.btn_top_center:
                dialoger.target().showPosition(mTargetView, TargetDialoger.Position.TopOutsideCenter);
                break;
            case R.id.btn_top_right:
                dialoger.target().showPosition(mTargetView, TargetDialoger.Position.TopOutsideRight);
                break;

            case R.id.btn_bottom_left:
                dialoger.target().showPosition(mTargetView, TargetDialoger.Position.BottomOutsideLeft);
                break;
            case R.id.btn_bottom_center:
                dialoger.target().showPosition(mTargetView, TargetDialoger.Position.BottomOutsideCenter);
                break;
            case R.id.btn_bottom_right:
                dialoger.target().showPosition(mTargetView, TargetDialoger.Position.BottomOutsideRight);
                break;
        }
    }
}
