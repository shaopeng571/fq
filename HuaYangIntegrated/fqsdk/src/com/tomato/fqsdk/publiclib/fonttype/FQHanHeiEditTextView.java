package com.tomato.fqsdk.publiclib.fonttype;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class FQHanHeiEditTextView extends EditText {

	public FQHanHeiEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		applyCustomFont(context);
	}

	public FQHanHeiEditTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		applyCustomFont(context);
	}

	public FQHanHeiEditTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		applyCustomFont(context);
	}
    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("font/fq_hanhei.ttf", context);
        setTypeface(customFont);
    }
}
