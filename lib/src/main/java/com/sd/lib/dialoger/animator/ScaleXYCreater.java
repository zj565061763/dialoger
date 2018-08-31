package com.sd.lib.dialoger.animator;

/**
 * 缩放xy
 */
public class ScaleXYCreater extends CombineCreater
{
    public ScaleXYCreater()
    {
        super(new ScaleXCreater(), new ScaleYCreater());
    }
}
