package com.tomato.fqsdk.clinterface;

import com.tomato.fqsdk.models.HyLoginResult;

public class HyInterface {
			public static interface OnLoginFinishedListener {
				public abstract void onLoginFinished(int resultCode,
						HyLoginResult paramUser);
			}

			public static abstract interface OnInitFinishedListener {
				public abstract void onInitFinish(int code, String desc);
			}
}
