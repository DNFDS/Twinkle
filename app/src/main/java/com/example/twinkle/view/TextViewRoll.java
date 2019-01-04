package com.example.twinkle.view;

import android.content.Context;
import android.util.AttributeSet;

public class TextViewRoll extends android.support.v7.widget.AppCompatTextView {
    public TextViewRoll(Context context) {
        super(context);
    }

    public TextViewRoll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewRoll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {//必须重写，且返回值是true，表示始终获取焦点
        return true;
    }
}