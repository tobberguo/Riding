package com.tobber.riding;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.example.riding.R;
import com.tobber.riding.util.NetworkDetector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener {
	private Button titleBtRight;//跳过按钮
	private Button registerBtn;//注册
	private Button loginBtn;//登入
	private EditText userName;
	private EditText pwdEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		titleBtRight = (Button) findViewById(R.id.titleBtRight);
		registerBtn = (Button) findViewById(R.id.registerBtn);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		userName = (EditText) findViewById(R.id.userName);
		pwdEdit = (EditText) findViewById(R.id.pwdEdit);
		
		titleBtRight.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titleBtRight:
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				break;
				
			case R.id.registerBtn:
				startActivity((new Intent()).setClass(LoginActivity.this, RegisterActivity.class));
				break;
			case R.id.loginBtn:
				if(NetworkDetector.detect(LoginActivity.this)){
					
					AVUser.logInInBackground(userName.getText().toString().trim(), pwdEdit.getText().toString().trim(),new LogInCallback<AVUser>() {
						@Override
						public void done(AVUser user, AVException arg1) {
							if (user != null) {
								startActivity((new Intent()).setClass(LoginActivity.this, MainActivity.class));
					        } else {
					        	new AlertDialog.Builder(LoginActivity.this).setTitle("提示信息！").setMessage("用户名或密码错误")
								.setPositiveButton("确定", null).show();
					        }
						}
					});
					
				}else{
					new AlertDialog.Builder(LoginActivity.this).setTitle("提示信息！").setMessage("当前网络不可用，请检查网络")
					.setPositiveButton("确定", null).show();
				}
				
				break;
		}
	}

}
