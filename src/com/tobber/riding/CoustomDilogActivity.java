package com.tobber.riding;

import com.example.riding.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;

public class CoustomDilogActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_dilog);
		 
		//设置显示位置
		WindowManager.LayoutParams lp = getWindow().getAttributes();  
		lp.gravity = Gravity.BOTTOM;  
		getWindow().setAttributes(lp);
	}

}
