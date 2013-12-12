package com.ru426.android.xposed.library.util;

import java.lang.reflect.Method;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Build;

public class XModUtil {	
	public static int getAccentColor(Context context) {
		int i = android.R.color.holo_blue_dark;
		if (context == null)
			return i;
		try{
			i = context.getResources().getColor(context.getResources().getIdentifier("semc_theme_accent_color", "color", "com.sonyericsson.uxp"));
		}catch(android.content.res.Resources.NotFoundException e){
			e.printStackTrace();
			return i;
		}
		switch(i){
		case -1:
			i = android.R.color.darker_gray;
			break;
		}
		return i;
	}
	
	public static void collapseStatusBar(Context context){
		if(context != null){
			try {
	            Object service = context.getSystemService("statusbar"); 
	            Class<?> clazz = Class.forName("android.app.StatusBarManager"); 
	            Method method = null;
	            if (Build.VERSION_CODES.JELLY_BEAN_MR1 <= Build.VERSION.SDK_INT) {
	                method = clazz.getMethod("collapsePanels");
	            } else if (Build.VERSION_CODES.ICE_CREAM_SANDWICH <= Build.VERSION.SDK_INT
	                    && Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
	                method = clazz.getMethod("collapse");
	            }
	            method.invoke(service); 
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
		}
	}
	
	public static void expandStatusBar(Context context){
		if(context != null){
			try {
	            Object service = context.getSystemService("statusbar"); 
	            Class<?> clazz = Class.forName("android.app.StatusBarManager"); 
	            Method method = null;
	            if (Build.VERSION_CODES.JELLY_BEAN_MR1 <= Build.VERSION.SDK_INT) {
	                method = clazz.getMethod("expandNotificationsPanel");
	            } else if (Build.VERSION_CODES.ICE_CREAM_SANDWICH <= Build.VERSION.SDK_INT
	                    && Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
	                method = clazz.getMethod("expand");
	            }
	            method.invoke(service); 
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
		}
	}
	
	public static List<ResolveInfo> getIsInstalled(Context context, String packageName, String activityName){
		try{
	        Intent intent = new Intent();
	        intent.setClassName(packageName, packageName + activityName);
	        intent.setAction(Intent.ACTION_MAIN);
	        intent.addCategory(Intent.CATEGORY_LAUNCHER);
	        PackageManager packageManager = context.getPackageManager();
	        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
	        //when uninstalled or frozen : 0; when installed : 1;
	        if(resolveInfos.size() > 0)
	        	return resolveInfos;
		}catch(Exception e){
			e.printStackTrace();
		}
        return null;
    }
	
	public static boolean isGXModSystemUI(Context context){
		Context mContext = null;
		int id = 0;
		try {
			mContext = context.createPackageContext("com.android.systemui", Context.CONTEXT_RESTRICTED);
			id = mContext.getResources().getIdentifier("custom_SystemUI0_3", "bool", mContext.getPackageName());
		} catch (NameNotFoundException e) {
			return false;
		}
		return id > 0;
 	}
}
