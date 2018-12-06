package com.fqwl.hycommonsdk.present.apiinteface;

import android.app.Activity;
import android.content.Intent;


/**
 * activity生命周期
 * @author yzj
 *
 */
public interface ActivityCycle extends SdkApi{

	public void onStart(Activity activity);
	
	public void onRestart(Activity activity);
	
	public void onResume(Activity activity);
	
	public void onPause(Activity activity);
	
	public void onStop(Activity activity);
	
//	public void onDestroy(Activity activity);
	
	public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data);

	public void onNewIntent(Activity activity, Intent intent);

	

	

	

	

	

}
