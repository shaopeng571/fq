package com.tomato.fqsdk.publiclib.fonttype;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FQHanHeiTextView extends TextView {
	public FQHanHeiTextView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public FQHanHeiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public FQHanHeiTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("font/fq_hanhei.ttf", context);
        setTypeface(customFont);
    }

}
