package com.stockroompro.tool.color;

import java.util.ArrayList;

public final class AppPrimaryColor {
    private final ColorEvaluator colorEvaluator;
    private final ArrayList<ColorEvaluatorObserver> observers;

    private AppPrimaryColor() {
        colorEvaluator = new ColorEvaluator(BaseValue.START, BaseValue.END);
        observers = new ArrayList<ColorEvaluatorObserver>();
    }

    public static AppPrimaryColor getInstance() {
        return AppColorHolder.INSTANCE;
    }

    public final int getCurrentValue() {
        return colorEvaluator.getCurrentValue();
    }

    public final void setStartValue(final int value) {
        colorEvaluator.setStartValue(value);
    }

    public final void setEndValue(final int value) {
        colorEvaluator.setEndValue(value);
    }

    public void addObserver(final ColorEvaluatorObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    public void removeObserver(final ColorEvaluatorObserver observer) {
        if (observer != null) {
            observers.remove(observer);
        }
    }

    private void notifyObservers(final int eventType, final int value) {
        for (ColorEvaluatorObserver observer : observers) {
            notifyObserver(observer, eventType, value);
        }
    }

    private void notifyObserver(final ColorEvaluatorObserver observer, final int eventType, final int value) {
        if (observer != null) {
            switch (eventType) {
                case EventType.START:
                    observer.onChangeStartValue(value);
                    break;
                case EventType.END:
                    observer.onChangeEndValue(value);
                    break;
            }
        }
    }

    private interface BaseValue {
        int END = 0xFF444444;   //DARK GRAY
        int START = 0xFFFFFFFF; //WHITE
    }

    private interface EventType {
        int END = 0x1010;
        int START = 0x1011;
    }

    private static class AppColorHolder {
        private static final AppPrimaryColor INSTANCE = new AppPrimaryColor();
    }

}