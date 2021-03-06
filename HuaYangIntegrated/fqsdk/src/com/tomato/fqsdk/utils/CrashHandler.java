//package com.tomato.fqsdk.utils;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Environment;
//import android.os.Looper;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.io.Writer;
//import java.lang.reflect.Field;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// *   쳣 ռ 
// */
//public class CrashHandler implements Thread.UncaughtExceptionHandler {
////    private static final String TAG = "CrashHandler";
//    private Thread.UncaughtExceptionHandler mDefaultHandler;// ϵͳĬ ϵ UncaughtException      
//    private static CrashHandler INSTANCE = new CrashHandler();// CrashHandlerʵ  
//    private Context mContext;//      Context    
//    private Map<String, String> info = new HashMap<String, String>();//      洢 豸  Ϣ   쳣  Ϣ
//    @SuppressLint("SimpleDateFormat") private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//    ڸ ʽ      ,  Ϊ  ־ ļ     һ    
//
//    private String fileN = "clcrash";
//    private boolean isBlocked = false;// Ƿ     ϵͳĬ ϵ  쳣        trueΪϵͳ        ֻ   Լ     , falseΪϵͳ   Լ       (    3 룬toast   ˳  Ĳ   )
//
//    /**   ֻ֤  һ  CrashHandlerʵ   */
//    private CrashHandler() {
//
//    }
//
//    /**   ȡCrashHandlerʵ   ,    ģʽ */
//    public static CrashHandler getInstance() {
//        return INSTANCE;
//    }
//
//    /**
//     *   ʼ  
//     *
//     * @param context
//     */
//    public void init(Context context) {
//        mContext = context;
//        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();//   ȡϵͳĬ ϵ UncaughtException      
//        Thread.setDefaultUncaughtExceptionHandler(this);//    ø CrashHandlerΪ     Ĭ ϴ     
//    }
//
//    /**
//     *   UncaughtException    ʱ  ת     д ķ         
//     */
//    public void uncaughtException(Thread thread, Throwable ex) {
//        if (!handleException(ex) && mDefaultHandler != null) {
//            //     Զ    û д       ϵͳĬ ϵ  쳣            
//            mDefaultHandler.uncaughtException(thread, ex);
//        } else {
//            try {
//                Thread.sleep(3000);//         ˣ  ó          3     ˳     ֤ ļ    沢 ϴ         
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            //  ˳     
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(1);
//        }
//    }
//
//    /**
//     *  Զ        , ռ       Ϣ    ʹ  󱨸 Ȳ      ڴ    .
//     *
//     * @param ex
//     *             쳣  Ϣ
//     * @return true         ˸  쳣  Ϣ;   򷵻 false.
//     */
//    public boolean handleException(Throwable ex) {
//        if (ex == null)
//            return false;
//        new Thread() {
//            public void run() {
//                Looper.prepare();
////                Toast.makeText(mContext, " ܱ Ǹ,        Ӧ,     ˳ ", Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//        }.start();
//        //  ռ  豸      Ϣ
//        collectDeviceInfo(mContext);
//        //       ־ ļ 
//        saveCrashInfo2File(ex);
//        return isBlocked;
//    }
//
//    /**
//     *  ռ  豸      Ϣ
//     *
//     * @param context
//     */
//    public void collectDeviceInfo(Context context) {
//        try {
//            PackageManager pm = context.getPackageManager();//   ð       
//            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
//                    PackageManager.GET_ACTIVITIES);//  õ   Ӧ õ   Ϣ      Activity
//            if (pi != null) {
//                String versionName = pi.versionName == null ? "null"
//                        : pi.versionName;
//                String versionCode = pi.versionCode + "";
//                info.put("versionName", versionName);
//                info.put("versionCode", versionCode);
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        Field[] fields = Build.class.getDeclaredFields();//        
//        for (Field field : fields) {
//            try {
//                field.setAccessible(true);
//                info.put(field.getName(), field.get("").toString());
//                //Log.d(TAG, field.getName() + ":" + field.get(""));
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private String saveCrashInfo2File(Throwable ex) {
//        StringBuffer sb = new StringBuffer();
//        for (Map.Entry<String, String> entry : info.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            sb.append(key + "=" + value + "\r\n");
//        }
//        Writer writer = new StringWriter();
//        PrintWriter pw = new PrintWriter(writer);
//        ex.printStackTrace(pw);
//        Throwable cause = ex.getCause();
//        // ѭ   Ű    е  쳣  Ϣд  writer  
//        while (cause != null) {
//            cause.printStackTrace(pw);
//            cause = cause.getCause();
//        }
//        pw.close();//  ǵùر 
//        String result = writer.toString();
//        sb.append(result);
//        //      ļ 
//        long timetamp = System.currentTimeMillis();
//        String time = format.format(new Date());
//        String fileName = "crash-" + time + "-" + timetamp + ".log";
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//            try {
//            	  String dbDir = android.os.Environment.getExternalStorageDirectory()  
//                          .getAbsolutePath();  
//                  dbDir += "/databases";//    ݿ     Ŀ¼  
//                  String dbPath = dbDir + "/" + fileN;//    ݿ ·    
//                File dir = new File(dbPath);
//                //Log.i("CrashHandler", dir.toString());
//                if (!dir.exists())
//                    dir.mkdirs();
//                FileOutputStream fos = new FileOutputStream(new File(dir, fileName));
//                fos.write(sb.toString().getBytes());
//                fos.close();
//                return fileName;
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//}
