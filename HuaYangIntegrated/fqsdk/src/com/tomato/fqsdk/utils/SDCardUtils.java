/*
 * Copyright (C) 2013 Peng fei Pan <sky@xiaopan.me>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tomato.fqsdk.utils;

import android.os.Environment;

import java.io.File;

/**
 * SD        
 */
public class SDCardUtils {
    /**
     *   ȡSD    ״̬
     */
    public static String getState() {
        return Environment.getExternalStorageState();
    }


    /**
     * SD   Ƿ    
     *
     * @return ֻ е SD   Ѿ   װ    ׼     ˲ŷ   true
     */
    public static boolean isAvailable() {
        return getState().equals(Environment.MEDIA_MOUNTED)&& Environment.getExternalStorageDirectory().canWrite();
    }


    /**
     *   ȡSD   ĸ Ŀ¼
     *
     * @return null        SD  
     */
    public static File getRootDirectory() {
        return isAvailable() ? Environment.getExternalStorageDirectory() : null;
    }


    /**
     *   ȡSD   ĸ ·  
     *
     * @return null        SD  
     */
    public static String getRootPath() {
        File rootDirectory = getRootDirectory();
        return rootDirectory != null ? rootDirectory.getPath() : null;
    }
    /**
     *  ȡsd  ·  
     * @return Stringpath
     */
    public static String getSDPath(){

        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                                         .equals(Environment.MEDIA_MOUNTED);   // ж sd   Ƿ    
        if   (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//  ȡ  Ŀ¼
        }
        return sdDir.toString();

    }
}
