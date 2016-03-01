package com.stockroompro.tool.color;

final class ColorEvaluator {
    private int currentValue;
    private int startA;
    private int startR;
    private int startG;
    private int startB;
    private int endA;
    private int endR;
    private int endG;
    private int endB;

    public ColorEvaluator(final int startValue, final int endValue) {
        initStart(startValue);
        initEnd(endValue);
        evaluate(0);
    }

    private void initStart(int startValue) {
        startA = (startValue >> 24) & 0xff;
        startR = (startValue >> 16) & 0xff;
        startG = (startValue >> 8) & 0xff;
        startB = startValue & 0xff;
    }

    private void initEnd(int endValue) {
        endA = (endValue >> 24) & 0xff;
        endR = (endValue >> 16) & 0xff;
        endG = (endValue >> 8) & 0xff;
        endB = endValue & 0xff;
    }

    public final void setStartValue(final int startValue) {
        initStart(startValue);
        evaluate(0);
    }

    public final void setEndValue(final int endValue) {
        initEnd(endValue);
        evaluate(100);
    }

    public final void evaluate(float fraction) {
        currentValue = (startA + (int) (fraction * (endA - startA))) << 24 |
                (startR + (int) (fraction * (endR - startR))) << 16 |
                (startG + (int) (fraction * (endG - startG))) << 8 |
                (startB + (int) (fraction * (endB - startB)));
    }

    public final int getCurrentValue() {
        return currentValue;
    }

}