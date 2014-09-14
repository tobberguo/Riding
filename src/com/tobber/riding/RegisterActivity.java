package com.tobber.riding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.example.riding.R;
import com.tobber.riding.util.NetworkDetector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity implements OnClickListener{
	
	private EditText emailEdit;
	private EditText userEdit;
	private EditText pwdEdit;
	private EditText confirmEdit;
	private Button returnLogin;
	private Button registerBtn;
	
	@Override
	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		/**
		 * 解决Android4.0以上版面 不能访问网络问题
		 */
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()       
        .detectDiskReads()       
        .detectDiskWrites()       
        .detectNetwork()         
        .penaltyLog()       
        .build());       
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()       
        .detectLeakedSqlLiteObjects()    
        .penaltyLog()       
        .penaltyDeath()       
        .build());
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		emailEdit = (EditText) findViewById(R.id.emailEdit);
		userEdit = (EditText) findViewById(R.id.userEdit);
		pwdEdit = (EditText) findViewById(R.id.pwdEdit);
		confirmEdit = (EditText) findViewById(R.id.confirmEdit);
		returnLogin = (Button) findViewById(R.id.returnLogin);
		registerBtn = (Button) findViewById(R.id.registerBtn);
		
		returnLogin.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.registerBtn: //用户注册
				if(NetworkDetector.detect(RegisterActivity.this)){
					
					if(emailEdit.getText().toString().trim()!=null && userEdit.getText().toString().trim()!=null
					&&pwdEdit.getText().toString().trim()!=null && confirmEdit.getText().toString().trim()!=null)
					{
						if(isEmail(emailEdit.getText().toString().trim())){
							if(6<=(pwdEdit.getText().toString().trim().length()) && (pwdEdit.getText().toString().trim().length())<=16){
								if(pwdEdit.getText().toString().trim().equals(confirmEdit.getText().toString().trim())){
									
									AVUser user = new AVUser();
									user.setUsername(emailEdit.getText().toString().trim());
									user.setPassword(pwdEdit.getText().toString().trim());
									user.setEmail(emailEdit.getText().toString().trim());
									// 其他属性可以像其他AVObject对象一样使用put方法添加
									user.put("name", userEdit.getText().toString().trim());
									user.signUpInBackground(new SignUpCallback() {
									    public void done(AVException e) {
									        if (e == null) {
									            // successfully
									        } else {
									        	new AlertDialog.Builder(RegisterActivity.this).setTitle("提示信息！").setMessage("该用户已存在")
												.setPositiveButton("确定", null).show();
									        }
									    }
									});
									
								}else{
									new AlertDialog.Builder(this).setTitle("提示信息！").setMessage("确认密码错误")
									.setPositiveButton("确定", null).show();
								}
								
							}else{
								new AlertDialog.Builder(this).setTitle("提示信息！").setMessage("请输入6-16位与数字或字母组成的密码")
								.setPositiveButton("确定", null).show();
							}
						}else{
							new AlertDialog.Builder(this).setTitle("提示信息！").setMessage("请输入正确的邮箱")
							.setPositiveButton("确定", null).show();
						}
					}else{
						new AlertDialog.Builder(this).setTitle("提示信息！").setMessage("注册信息不能为空，请填写完整")
						.setPositiveButton("确定", null).show();
					}
				}else{
					new AlertDialog.Builder(this).setTitle("提示信息！").setMessage("当前网络不可用，请检查网络")
					.setPositiveButton("确定", null).show();
				}
				break;
			case R.id.returnLogin:
				break;	
			
		}
	}
	
	/**
	 * 邮箱验证
	 * @param email
	 * @return
	 */
	private boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}
	
}
