package com.tomato.fqsdk.publiclib.fonttype;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FQZhunHeiTextView extends TextView {
	public FQZhunHeiTextView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public FQZhunHeiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public FQZhunHeiTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("font/fq_zhunhei.ttf", context);
        setTypeface(customFont);
    }

}
