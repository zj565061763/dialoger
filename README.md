# About
对dialog进行封装，提供更好用的api，可以让窗口显示在某个view的上下左右<br>
<br>
![](http://thumbsnap.com/i/qNJUXTJN.gif?0607)

# Gradle
[![](https://jitpack.io/v/zj565061763/dialoger.svg)](https://jitpack.io/#zj565061763/dialoger)

# 简单demo
```java
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
     * 设置窗口内容view动画创建对象，此处设置为透明度变化，可以实现AnimatorCreater接口来实现自定义动画
     *
     * 默认规则:
     * Gravity.CENTER:                      AlphaCreater 透明度
     *
     * Gravity.LEFT:                        SlideRightLeftCreater 向右滑入，向左滑出
     * Gravity.LEFT | Gravity.CENTER:       SlideRightLeftCreater
     *
     * Gravity.TOP:                         SlideBottomTopCreater 向下滑入，向上滑出
     * Gravity.TOP | Gravity.CENTER:        SlideBottomTopCreater
     *
     * Gravity.RIGHT:                       SlideLeftRightCreater 向左滑入，向右滑出
     * case Gravity.RIGHT | Gravity.CENTER: SlideLeftRightCreater
     *
     * Gravity.BOTTOM:                      SlideTopBottomCreater 向上滑入，向下滑出
     * Gravity.BOTTOM | Gravity.CENTER:     SlideTopBottomCreater
     *
     */
    dialoger.setAnimatorCreater(new AlphaCreater());

    /**
     * 显示窗口
     */
    dialoger.show();
}
```

# 窗口动画
可以给窗口设置动画创建对象来实现自己想要的动画效果，接口如下：
```java
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
     * @param show true-窗口显示，false-窗口隐藏
     * @param view 窗口内容view
     * @return
     */
    Animator createAnimator(boolean show, View view);
}
```

库中已经提供的实现类有：
* AlphaCreater 透明度
* ScaleXCreater 缩放宽度
* ScaleYCreater 缩放高度
* ScaleXYCreater 缩放宽高
* SlideBottomTopCreater 向下滑入，向上滑出，默认距离为内容view的高度
* SlideTopBottomCreater 向上滑入，向下滑出，默认距离为内容view的高度
* SlideLeftRightCreater 向左滑入，向右滑出，默认距离为内容view的宽度
* SlideRightLeftCreater 向右滑入，向左滑出，默认距离为内容view的宽度
* PivotCreater 为包装类，可以在动画开始的时候改变view的锚点
* CombineCreater 为组合类，可以同时组合多个不同的creater

# 显示在某个View周围

```java
/**
 * target()方法返回一个TargetDialoger对象，可以指定窗口要显示在某个view的什么位置
 */
dialoger.target().show(mTargetView, TargetDialoger.Position.LeftOutsideTop);
```
TargetDialoger接口：
```java
public interface TargetDialoger
{
    /**
     * x方向偏移量，大于0-向右；小于0-向左
     *
     * @param marginX
     * @return
     */
    TargetDialoger setMarginX(int marginX);

    /**
     * y方向偏移量，大于0-向下；小于0-向上
     *
     * @param marginY
     * @return
     */
    TargetDialoger setMarginY(int marginY);

    /**
     * 显示在目标view的某个位置
     *
     * @param target   目标view
     * @param position 显示的位置{@link Position}
     */
    void show(View target, Position position);

    enum Position
    {
        /**
         * 在target左边外侧
         */
        LeftOutside,
        /**
         * 在target的左边外侧靠顶部对齐
         */
        LeftOutsideTop,
        /**
         * 在target的左边外侧上下居中
         */
        LeftOutsideCenter,
        /**
         * 在target的左边外侧靠底部对齐
         */
        LeftOutsideBottom,

        /**
         * 在target的顶部外侧
         */
        TopOutside,
        /**
         * 在target的顶部外侧靠左对齐
         */
        TopOutsideLeft,
        /**
         * 在target的顶部外侧左右居中
         */
        TopOutsideCenter,
        /**
         * 在target的顶部外侧靠右对齐
         */
        TopOutsideRight,

        /**
         * 在target的右边外侧
         */
        RightOutside,
        /**
         * 在target的右边外侧靠顶部对齐
         */
        RightOutsideTop,
        /**
         * 在target的右边外侧上下居中
         */
        RightOutsideCenter,
        /**
         * 在target的右边外侧靠底部对齐
         */
        RightOutsideBottom,

        /**
         * 在target的底部外侧
         */
        BottomOutside,
        /**
         * 在target的底部外侧靠左对齐
         */
        BottomOutsideLeft,
        /**
         * 在target的底部外侧左右居中
         */
        BottomOutsideCenter,
        /**
         * 在target的底部外侧靠右对齐
         */
        BottomOutsideRight,
    }
}
```


# Dialoger接口
```java
public interface Dialoger
{
    /**
     * 设置调试模式，内部会输出日志，日志tag：Dialoger
     *
     * @param debug
     */
    void setDebug(boolean debug);

    Context getContext();

    Activity getOwnerActivity();

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
     * 设置触摸到非内容view区域是否关闭窗口，默认true
     *
     * @param cancel
     */
    void setCanceledOnTouchOutside(boolean cancel);

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
     * 设置窗口内容view动画创建对象
     *
     * @param creater
     */
    void setAnimatorCreater(AnimatorCreater creater);

    /**
     * 返回窗口内容view动画创建对象
     *
     * @return
     */
    AnimatorCreater getAnimatorCreater();

    /**
     * 设置重力属性{@link android.view.Gravity}
     *
     * @param gravity
     */
    void setGravity(int gravity);

    /**
     * 返回当前的重力属性
     *
     * @return
     */
    int getGravity();

    /**
     * 设置上下左右间距
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    void setPadding(int left, int top, int right, int bottom);

    /**
     * 左边边距
     *
     * @return
     */
    int getPaddingLeft();

    /**
     * 顶部边距
     *
     * @return
     */
    int getPaddingTop();

    /**
     * 右边边距
     *
     * @return
     */
    int getPaddingRight();

    /**
     * 底部边距
     *
     * @return
     */
    int getPaddingBottom();

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
     * 触摸事件回调
     *
     * @param event
     * @return
     */
    boolean onTouchEvent(MotionEvent event);

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
         * @param show true-窗口显示，false-窗口隐藏
         * @param view 窗口内容view
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