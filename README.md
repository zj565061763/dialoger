# About
封装了dialog的逻辑，可以让某个ViewGroup具有dialog的属性

# 简单demo
1. 创建一个ViewGroup
```java
public class DialogView extends LinearLayout
{
    private final Dialoger mDialoger;

    public DialogView(Activity activity)
    {
        super(activity);
        mDialoger = new FDialoger(activity, this);
    }

    public final Dialoger getDialoger()
    {
        return mDialoger;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        // 传递事件
        getDialoger().onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        super.onTouchEvent(event);
        // 传递事件
        return getDialoger().onTouchEvent(event);
    }
}
```

2. 让ViewGroup像dialog一样显示
```java
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
```

# Dialoger接口
```java
public interface Dialoger
{
    /**
     * 返回窗口view
     *
     * @return
     */
    View getDialogView();

    /**
     * 返回内容view
     *
     * @return
     */
    View getContentView();

    /**
     * 设置内容view布局id
     *
     * @param layoutId
     */
    void setContentView(int layoutId);

    /**
     * 设置内容view
     *
     * @param view
     */
    void setContentView(View view);

    /**
     * 设置按返回键是否可以关闭窗口，默认true
     *
     * @param cancel
     */
    void setCancelable(boolean cancel);

    /**
     * 设置触摸到非内容区域是否关闭窗口，默认-true
     *
     * @param cancel
     */
    void setCanceledOnTouchOutside(boolean cancel);

    /**
     * 设置窗口view的动画创建对象
     *
     * @param creater
     */
    void setDialogAnimatorCreater(AnimatorCreater creater);

    /**
     * 设置内容view的动画创建对象
     *
     * @param creater
     */
    void setContentAnimatorCreater(AnimatorCreater creater);

    /**
     * 设置窗口关闭监听
     *
     * @param listener
     */
    void setOnDismissListener(OnDismissListener listener);

    /**
     * 显示窗口
     */
    void show();

    /**
     * 关闭窗口
     */
    void dismiss();

    /**
     * 窗口是否正在显示
     *
     * @return
     */
    boolean isShowing();

    /**
     * 窗口view需要调用此方法
     */
    void onLayout(boolean changed, int left, int top, int right, int bottom);

    /**
     * 窗口view需要调用此方法
     */
    boolean onTouchEvent(MotionEvent event);

    /**
     * Activity需要调用此方法，如果此方法返回true的话，Activity那边的重写方法也要返回true
     */
    boolean onKeyDown(int keyCode, KeyEvent event);

    /**
     * 关闭监听
     */
    interface OnDismissListener
    {
        void onDismiss(Dialoger dialoger);
    }

    /**
     * 动画创建接口
     */
    interface AnimatorCreater
    {
        Animator createAnimator(boolean show, View view);
    }
}
```