# About
封装了dialog的逻辑，可以让某个View具有dialog的属性

# Gradle
`implementation 'com.fanwe.android:dialoger:1.0.0-rc1'`

# 简单demo
1. 创建一个Dialoger
```java
public class TestDialoger extends FDialoger
{
    public TestDialoger(Activity activity)
    {
        super(activity);
        setDebug(true);
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
                /**
                 * 关闭窗口
                 */
                dismiss();
            }
        });
    }
}
```

2. 让View像dialog一样显示
```java
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

        // 设置窗口关闭监听
        mDialoger.setOnDismissListener(new Dialoger.OnDismissListener()
        {
            @Override
            public void onDismiss(Dialoger dialoger)
            {
                Log.i(TAG, "onDismiss:" + dialoger);
            }
        });

        // 设置窗口显示监听
        mDialoger.setOnShowListener(new Dialoger.OnShowListener()
        {
            @Override
            public void onShow(Dialoger dialoger)
            {
                Log.i(TAG, "onShow:" + dialoger);
            }
        });

        // 设置按返回键是否可以关闭窗口，默认true
        mDialoger.setCancelable(true);

        // 设置触摸到非内容区域是否关闭窗口，默认false
        mDialoger.setCanceledOnTouchOutside(true);

        // 设置窗口背景颜色，默认#66000000
        mDialoger.setBackgroundColor(Color.parseColor("#66000000"));
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_top_center:
                // 设置动画创建对象，此处为：向下滑入，向上滑出
                mDialoger.setAnimatorCreater(new SlideBottomTopCreater());

                // 显示在目标view的底部
                mDialoger.target().showPosition(v, TargetDialoger.Position.BottomOutsideCenter);
                break;
            case R.id.btn_center:
                // 设置动画创建对象，通过CombineCreater可以组合多个creater对象
                mDialoger.setAnimatorCreater(new CombineCreater(new AlphaCreater(), new ScaleXYCreater()));

                // 设置重力属性
                mDialoger.setGravity(Gravity.CENTER);

                // 显示窗口
                mDialoger.show();
                break;
            case R.id.btn_bottom_center:
                // 设置动画创建对象，此处为：向上滑入，向下滑出
                mDialoger.setAnimatorCreater(new SlideTopBottomCreater());

                // 显示在目标view的顶部
                mDialoger.target().showPosition(v, TargetDialoger.Position.TopOutsideCenter);
                break;
            case R.id.btn_left_center:
                // 设置动画创建对象，此处为：向右滑入，向左滑出
                mDialoger.setAnimatorCreater(new SlideRightLeftCreater());

                // 显示在目标view的右边
                mDialoger.target().showPosition(v, TargetDialoger.Position.RightOutsideCenter);
                break;
            case R.id.btn_right_center:
                // 设置动画创建对象，此处为：向左滑入，向右滑出
                mDialoger.setAnimatorCreater(new SlideLeftRightCreater());

                // 显示在目标view的左边
                mDialoger.target().showPosition(v, TargetDialoger.Position.LeftOutsideCenter);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        /**
         * 传递按键事件给窗口
         */
        if (mDialoger.onKeyDown(keyCode, event))
            return true;
        return super.onKeyDown(keyCode, event);
    }
}
```

# Dialoger接口
```java
public interface Dialoger
{
    /**
     * 设置是否调试模式
     *
     * @param debug
     */
    void setDebug(boolean debug);

    Context getContext();

    /**
     * 返回窗口的内容view
     *
     * @return
     */
    View getContentView();

    /**
     * 设置窗口的内容view布局id
     *
     * @param layoutId
     */
    void setContentView(int layoutId);

    /**
     * 设置窗口的内容view
     *
     * @param view
     */
    void setContentView(View view);

    /**
     * 设置窗口背景颜色
     *
     * @param color
     */
    void setBackgroundColor(int color);

    /**
     * 根据id查找view
     *
     * @param id
     * @param <T>
     * @return
     */
    <T extends View> T findViewById(int id);

    /**
     * 设置按返回键是否可以关闭窗口，默认true
     * <br>
     * 此方法需要传递按键事件后才有效{@link #onKeyDown(int, KeyEvent)}
     *
     * @param cancel
     */
    void setCancelable(boolean cancel);

    /**
     * 设置触摸到非内容view区域是否关闭窗口，默认false
     *
     * @param cancel
     */
    void setCanceledOnTouchOutside(boolean cancel);

    /**
     * 设置窗口内容view动画创建对象
     *
     * @param creater
     */
    void setAnimatorCreater(AnimatorCreater creater);

    /**
     * 设置窗口关闭监听
     *
     * @param listener
     */
    void setOnDismissListener(OnDismissListener listener);

    /**
     * 设置窗口显示监听
     *
     * @param listener
     */
    void setOnShowListener(OnShowListener listener);

    /**
     * 添加生命周期回调
     *
     * @param callback
     */
    void addLifecycleCallback(LifecycleCallback callback);

    /**
     * 移除生命周期回调
     *
     * @param callback
     */
    void removeLifecycleCallback(LifecycleCallback callback);

    /**
     * 设置重力属性{@link android.view.Gravity}
     *
     * @param gravity
     */
    void setGravity(int gravity);

    /**
     * 设置左边间距
     *
     * @param padding
     */
    void paddingLeft(int padding);

    /**
     * 设置顶部间距
     *
     * @param padding
     */
    void paddingTop(int padding);

    /**
     * 设置右边间距
     *
     * @param padding
     */
    void paddingRight(int padding);

    /**
     * 设置底部间距
     *
     * @param padding
     */
    void paddingBottom(int padding);

    /**
     * 设置上下左右间距
     *
     * @param paddings
     */
    void paddings(int paddings);

    /**
     * 显示窗口
     */
    void show();

    /**
     * 窗口是否正在显示
     *
     * @return
     */
    boolean isShowing();

    /**
     * 关闭窗口
     */
    void dismiss();

    /**
     * 延迟多久后关闭dialog
     *
     * @param delay （毫秒）
     */
    void startDismissRunnable(long delay);

    /**
     * 停止延迟关闭任务
     */
    void stopDismissRunnable();

    /**
     * Activity需要调用此方法，如果此方法返回true的话，Activity那边的重写方法也要返回true
     */
    boolean onKeyDown(int keyCode, KeyEvent event);

    /**
     * 返回TargetDialoger对象
     *
     * @return
     */
    TargetDialoger target();

    /**
     * 关闭监听
     */
    interface OnDismissListener
    {
        /**
         * 消失后回调
         *
         * @param dialoger
         */
        void onDismiss(Dialoger dialoger);
    }

    /**
     * 显示监听
     */
    interface OnShowListener
    {
        /**
         * 显示后回调
         *
         * @param dialoger
         */
        void onShow(Dialoger dialoger);
    }

    /**
     * 动画创建接口
     */
    interface AnimatorCreater
    {
        /**
         * 创建动画
         * <br>
         * 注意：隐藏动画不能设置为无限循环，否则窗口将不能被移除
         *
         * @param show
         * @param view
         * @return
         */
        Animator createAnimator(boolean show, View view);
    }

    interface LifecycleCallback
    {
        /**
         * 窗口显示之前回调
         *
         * @param dialoger
         */
        void onStart(Dialoger dialoger);

        /**
         * 窗口关闭之前回调
         *
         * @param dialoger
         */
        void onStop(Dialoger dialoger);
    }
}
```