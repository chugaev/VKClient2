package com.anatoly.chugaev.VKAPP;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class BaselineLastLineTextView  extends TextView {

    public BaselineLastLineTextView(Context context) {
        super(context);
    }

    public BaselineLastLineTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaselineLastLineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getBaseline() {
        Layout layout = getLayout();
        if (layout == null) {
            return super.getBaseline();
        }
        int baselineOffset = super.getBaseline() - layout.getLineBaseline(0);
        return baselineOffset + layout.getLineBaseline(layout.getLineCount()-1);
    }
}
