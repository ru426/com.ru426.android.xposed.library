package com.ru426.android.xposed.library;

import java.util.Locale;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.XModuleResources;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class ModuleBase {	
	protected static XSharedPreferences xSharedPreferences;
	protected static XModuleResources xModuleResources;
	protected static Context mContext;	
	protected static Configuration mConfiguration = new Configuration();
	protected static boolean isRegistered;
	protected boolean isDebugMode;
	private boolean isDebugMode() {
		return isDebugMode;
	}
	private void setDebugMode(boolean isDebugMode) {
		this.isDebugMode = isDebugMode;
	}
	public void initResources(XModuleResources resources) {
		xModuleResources = resources;
	}
	public void initZygote(XSharedPreferences prefs, boolean isDebug) {
		xSharedPreferences = prefs;
		setDebugMode(isDebug);
	}
	public void init(XSharedPreferences prefs, ClassLoader classLoader, boolean isDebug) {
		xSharedPreferences = prefs;
		setDebugMode(isDebug);
	}
	protected void xHookMethod(Class<?> cls, String methodName, Object[] callback, boolean doHook) {
		if(doHook){
			try {
				XposedHelpers.findAndHookMethod(cls, methodName, callback);
			} catch (NoSuchMethodError e) {
				XposedBridge.log(e);
			}			
		}
	}
	protected void onConfigrationChange(Configuration newConfig){
		if (newConfig == null) {
			mConfiguration.locale = Locale.getDefault();			
		} else {
			mConfiguration = newConfig;
		}
	}
	protected String xGetString(int resId) {
		if(xModuleResources == null | resId < 0) return null;		
		return xModuleResources.getString(resId);
	}
	protected Object xGetValue(XSharedPreferences prefs, String key, Object defValue){
		for(String _key : prefs.getAll().keySet()){
			if(_key != null 
					&& _key.contains(getClass().getPackage().getName())
					&& _key.startsWith(xModuleResources.getString(R.string.ru_mod_preferences_header))
					&& _key.endsWith(key)){
				try{
					boolean value = (Boolean) defValue;
					return prefs.getBoolean(_key, value);
				}catch(ClassCastException e){
					try{
						float value = (Float) defValue;
						return prefs.getFloat(_key, value);
					}catch(ClassCastException e1){
						try{
							int value = (Integer) defValue;
							return prefs.getInt(_key, value);
						}catch(ClassCastException e2){
							try{
								long value = (Long) defValue;
								return prefs.getLong(_key, value);
							}catch(ClassCastException e3){											
								try{
									String value = (String) defValue;
									return prefs.getString(_key, value);
								}catch(ClassCastException e4){
									try{
										@SuppressWarnings("unchecked")
										Set<String> value = (Set<String>) defValue;
										return prefs.getStringSet(_key, value);
									}catch(ClassCastException e5){
										XposedBridge.log(e5);
									}
								}
							}
						}
					}
				}
			}
		}
		return defValue;
	}
	private BroadcastReceiver xModuleReceiver = new BroadcastReceiver() {		
		@Override
		public void onReceive(Context context, Intent intent) {
			xOnReceive(context, intent);
		}
	};
	protected void xRegisterReceiver(Context context, IntentFilter filter){
		if(context != null && !isRegistered){
			context.registerReceiver(xModuleReceiver, filter);
			isRegistered = true;			
		}
	}
	protected void xUnRegisterReceiver(Context context){
		if(context != null && isRegistered){
			context.unregisterReceiver(xModuleReceiver);
			isRegistered = false;
		}
	}
	protected void xOnReceive(Context context, Intent intent){		
	}
	protected void xLog(String log){
		if(isDebugMode()) XposedBridge.log(log);
	}
}
