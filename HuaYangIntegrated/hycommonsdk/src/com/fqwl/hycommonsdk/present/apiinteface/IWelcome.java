package com.fqwl.hycommonsdk.present.apiinteface;

import android.app.Activity;

/**
 * 闪屏接口
 * @author yzj
 *
 */
public interface IWelcome extends SdkApi{

	void initWelcomeActivity(Activity activity,HyGameCallBack callBack);
}
