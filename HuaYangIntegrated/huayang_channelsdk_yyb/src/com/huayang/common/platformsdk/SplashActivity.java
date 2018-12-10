package com.huayang.common.platformsdk;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.fqwl.hycommonsdk.util.ChannelConfigUtil;
import com.tencent.qqdownloader.pay.api.IQQDownloaderApi;
import com.tencent.qqdownloader.pay.api.QQDownloaderApiFactory;
import com.tencent.tmgp.lqzgzs.R;
import com.tencent.ysdk.api.YSDKApi;


public class SplashActivity extends Activity {

    //TODO 选择java还是cpp
//    public static final String LANG_CPP = "cpp";
    public static final String LANG_JAVA = "java";

    public static String LANG = LANG_JAVA;// 开发语言 java cpp

	private static String LOG_TAG = "fq";
    private static Activity mActivity = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e(LOG_TAG,"onCreate");
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.splash_layout);

        // TODO GAME 游戏需自行检测自身是否重复, 检测到重复的Activity则要把自己finish掉
        // 注意：游戏需要加上去重判断以及finish重复的实例的逻辑，否则可能发生重复拉起游戏的问题。
        if (null != mActivity && !mActivity.equals(this)) {
            Log.d(LOG_TAG,"Warning!Reduplicate game activity was detected.Activity will finish immediately.");
            // TODO GAME 处理游戏被拉起的情况
            YSDKApi.handleIntent(this.getIntent());
            this.finish();
            return;
        }else{

            // TODO GAME YSDK初始化
            YSDKApi.onCreate(this);

            // TODO GAME 处理游戏被拉起的情况
            YSDKApi.handleIntent(this.getIntent());
        }

        mActivity = this;

//        ModuleManager.LANG = LANG;
        //如果是需要鉴权的游戏，需要处理鉴权流程
        boolean flagIsAuthGame=false;
        if (flagIsAuthGame) {
            //TODO GAME 鉴权游戏需要处理鉴权请求
            IQQDownloaderApi api = QQDownloaderApiFactory.createApi(this);
            api.auth(new IQQDownloaderApi.AuthListener() {
                @Override
                public void onAuthSuceed() {
                    //TODO　GAME 执行正常进入游戏逻辑
                    Log.i("YYB SDK Auth", "鉴权成功");
                    enterGame();
                }

                @Override
                public void onAuthFailed(int i) {
                    Log.e("YYB SDK Auth", "鉴权失败");
                }
            });
        } else {
            enterGame();
        }


	}

    private void enterGame() {
        new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
        	
    		Class<?> channelsdk;
			try {
				channelsdk = Class.forName(ChannelConfigUtil.getMainActivityName(SplashActivity.this));
				 Intent intent = new Intent();
		            intent.setClass(SplashActivity.this, channelsdk);
		            SplashActivity.this.startActivity(intent);
		            SplashActivity.this.finish();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
        }
    }, 3000);
//        SplashActivity.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });

    }
    // TODO GAME 在onNewIntent中需要调用handleIntent将平台带来的数据交给YSDK处理
    @Override
    protected void onNewIntent(Intent intent) {
        Log.e(LOG_TAG,"onNewIntent");
        super.onNewIntent(intent);
        YSDKApi.handleIntent(intent);
    }

}

