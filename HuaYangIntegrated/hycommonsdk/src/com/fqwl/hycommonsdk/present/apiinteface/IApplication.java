package com.fqwl.hycommonsdk.present.apiinteface;

import android.app.Application;
import android.content.Context;

public interface IApplication extends SdkApi{

	void initGamesApi(Application context);
	void initPluginInAppcation(Application application, Context context);
}
