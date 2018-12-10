package com.tomato.fqsdk;

import android.app.Application;

public class HyApplicaion extends Application {  
  
    private static HyApplicaion sInstance;  
  
    @Override  
    public void onCreate() {  
        super.onCreate();  
        sInstance = this;  
//        UMConfigure.init(this,"5ba372cdb465f500c9000063"
//                ,"umeng",UMConfigure.DEVICE_TYPE_PHONE,"");//58edcfeb310c93091c000be2 5965ee00734be40b580001a0
////        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");//"wx0d0199b79d772e05", "e6c9d2721e28c69fd1b6c07fe35b8777");
//        PlatformConfig.setWeixin("wx0d0199b79d772e05", "e6c9d2721e28c69fd1b6c07fe35b8777");
//        Log.d("fq","weixin_appid:"+ "wx0d0199b79d772e05");
//        //豆瓣RENREN平台目前只能在服务器端配置
//        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad","http://sns.whalecloud.com");
//        PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
//        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
//        PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
//        PlatformConfig.setAlipay("2015111700822536");
//        PlatformConfig.setLaiwang("laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
//        PlatformConfig.setPinterest("1439206");
//        PlatformConfig.setKakao("e4f60e065048eb031e235c806b31c70f");
//        PlatformConfig.setDing("dingoalmlnohc0wggfedpk");
//        PlatformConfig.setVKontakte("5764965","5My6SNliAaLxEm3Lyd9J");
//        PlatformConfig.setDropbox("oz8v5apet3arcdy","h7p2pjbzkkxt02a");
    }  
  
    public static HyApplicaion getInstance() {  
        return sInstance;  
    }  
  
}  