package com.tobber.riding.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * 判断当前网络是否可用
 * @author Administrator
 *
 */
public class NetworkDetector {  
	   
	/**
	 * 返回boolean值,true表示可用,false不可用
	 * @param act
	 * @return
	 */
    public static boolean detect(Activity act) {  
        
       ConnectivityManager manager = (ConnectivityManager) act  
              .getApplicationContext().getSystemService(  
                     Context.CONNECTIVITY_SERVICE);  
        
       if (manager == null) {  
           return false;  
       }  
        
       NetworkInfo networkinfo = manager.getActiveNetworkInfo();  
        
       if (networkinfo == null || !networkinfo.isAvailable()) {  
           return false;  
       }  
   
       return true;  
    }  
} 
