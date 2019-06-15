package com.sd.lib.dialoger.animator;

/**
 * 缩放xy
 */
public class ScaleXYCreator extends CombineCreator
{
    public ScaleXYCreator()
    {
        super(new ScaleXCreator(), new ScaleYCreator());
    }
}
