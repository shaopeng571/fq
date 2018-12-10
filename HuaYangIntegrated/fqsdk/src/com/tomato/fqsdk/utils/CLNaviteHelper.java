package com.tomato.fqsdk.utils;

public class CLNaviteHelper {
	private String datakey="js3";
	private static CLNaviteHelper clNaviteHelper;
	private CLNaviteHelper(){
		
	}
	public static CLNaviteHelper getInstance(){
		if (clNaviteHelper==null) {
			clNaviteHelper=new CLNaviteHelper();
		}
		return clNaviteHelper;
	}
//	static {
//        System.loadLibrary("hjsdk");
//    }
//	//΢  
	public String GetWXAppId(){
			return FindResHelper.RStringStr("hj_wxappid");
	}
//	public static String GetWXMchId(){
//		return FindResHelper.RStringStr("hj_wxmchid");
//	}
//	public static native String getapi(Context context);
//	public static native String getWXApiKey(Context context);
//	public static native String getKey(Context context);
//	public static native String getDataKey(Context context);
//	public static native String getAliPartner(Context context);
//	public static native String getAliSeller(Context context);
//	public static native String getAliRsaPrivate(Context context);
//	public static native String getAliRsaPublic(Context context);
	public String getSdkKey(){
		StringBuilder sb=new StringBuilder();
		String str=FindResHelper.RStringStr("register_name_digits");
		sb.append(str.charAt(getGBS2(10, 10))).append(str.charAt(16)).append(str.charAt(getGBS3(23, 22))).append("@*").append(str.charAt(22)).append(str.charAt(16)).append(str.charAt(36));
		return sb.toString();
	}
//	public String getDataKey(){
//		return "FB5CCB43EB70A93795CBBD1F5FDB0FF1";
////		StringBuilder sb=new StringBuilder();
////		sb.append(getKey1()).append(getKey2()).append(datakey);
////		return sb.toString();
//	}
	private StringBuilder getKey1(){
		StringBuilder sb=new StringBuilder();
		String str=FindResHelper.RStringStr("register_name_digits");
		sb.append(str.charAt(23)).append(str.charAt(19));
		return sb;
	};
	private StringBuffer getKey2() {  
        StringBuffer sb = new StringBuffer();  
        sb.append(getGBS(12, 10)+1);  
        return sb;  
    }  
	private int getGBS(int x, int y){  
        for(int i = 1; i<= x * y; i++){  
            if(i % x == 0 && i % y == 0)  
                return i;  
        }  
  
        return x * y;  
    }  
	private int getGBS2(int x, int y){  
		  for(int i = 6; i<= x + y; i++){  
	            if(i % x == 5&& i % y == 5)  
	                return i;  
	        }  

	        return x / y;  
    }  
	private int getGBS3(int x, int y){  
        for(int i = 6; i<= x + y; i++){  
            if(i % x == 7&& i % y == 8)  
                return i;  
        }  

        return x * y+3;  
    }  
	//lh61js3
//	public static native String getWXAppId();
//	public static native String getWXMchId();
//	public static native String getWXApiKey();
//	//֧        ʱ   øģ 
//	public static String GetAliPartner(){
//		if (HJNaviteHelper.getAliPartner().startsWith("88021", 2)) {
//			return getAliPartner();
//		}else {
//			return null;
//		}
//	}
//	
//	
//	public static String GetAliSeller(){
//		if (HJNaviteHelper.getAliSeller().startsWith("anzh", 2)) {
//			return getAliSeller();
//		}else {
//			return null;
//		}
//	}
//	
//	
//	public static String GetAliRsaPrivate(){
//		if (HJNaviteHelper.getAliRsaPrivate().startsWith("ICdQIBADANB", 2)) {
//			return getAliRsaPrivate();
//		}else {
//			return null;
//		}
//	}

	// ü Sdk
//	public static String getUserDoMain(){
//		return "http://open.huanjia.cc";
//	};
//	public static native String getUserDoMain();
	public static String getAgreement(){
		return "file:///android_asset/html/hj_useragreement.html";
		
	};
	public static String getRememberPwd(){
		return "123456789012345";
	};
//	public static native byte[] savePWD(String pwd);
//	public static native byte[] getPWD(String pwd);
//	public static native String EnPwd(String pwd);
	
}
