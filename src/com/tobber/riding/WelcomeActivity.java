package com.tobber.riding;

import com.example.riding.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

public class WelcomeActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		AVOSCloud.initialize(this, "fuumy64kxlhydzmsmtenip4quh0wau1k6jw6o9mj0n92yftz", "cuqp59zxmqfqknppa4on79nlc5r9s8u4davfsyaj13skbo0c");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		AVObject testObject = new AVObject("TestObject");
		testObject.put("foo", "bar");
		testObject.saveInBackground();
		
    	Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					sleep(2000);
				} catch (Exception e) {
					// do nothing
				} finally {
					
					AVUser currentUser = AVUser.getCurrentUser();
					if (currentUser != null) {
						
						Intent intent = new Intent();
						intent.setClass(WelcomeActivity.this, MainActivity.class);
						startActivity(intent);
						finish();//调用这个Activity 的finish方法后，在主界面按手机 上的放回键就会直接退出程序
						
					} else {
					    //缓存用户对象为空时， 可打开用户注册界面…
						
						Intent intent = new Intent();
						intent.setClass(WelcomeActivity.this, LoginActivity.class);
						startActivity(intent);
						finish();//调用这个Activity 的finish方法后，在主界面按手机 上的放回键就会直接退出程序
					}
					
				}
			}
		};
		splashTread.start();
		
	}
	
}
