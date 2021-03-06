package com.example.showdialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	static{
		System.loadLibrary("ShowDialog");
	}
	
	/**
	 * 由c代码实现的安全支付的方法
	 * @param username 用户名
	 * @param password 密码
	 * @param money 金钱
	 * @return 200支付成功 404 用户名密码错误  250余额不足
	 */
	public native int safePay(String username,String password, float money);
	
	private EditText et_username;
	private EditText et_password;
	private EditText et_money;
	private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		et_money = (EditText) findViewById(R.id.et_money);
    }


    public void pay(View view){
		final String username = et_username.getText().toString().trim();
		final String password = et_password.getText().toString().trim();
		final String money = et_money.getText().toString().trim();
		if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)||TextUtils.isEmpty(money)){
			Toast.makeText(this, "数据输入错误", 0).show();
			return;
		}
		new Thread(){
			public void run() {
				final int result = safePay(username, password, Float.parseFloat(money));
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						switch (result) {
						case 200:
							Toast.makeText(MainActivity.this, "支付成功", 0).show();
							break;

						case 404:
							Toast.makeText(MainActivity.this, "用户名密码错误", 0).show();
							break;
						case 250:
							Toast.makeText(MainActivity.this, "余额不足", 0).show();
							break;
						}
						
					}
				});
			};
		}.start();
		
	}

	/**
	 * 显示对话框
	 * @param msg 对话框的消息 
	 */
	public void showDialog(final String msg){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(dialog!=null){
					dialog.dismiss();
					dialog = null;
				}
				 dialog = new ProgressDialog(MainActivity.this);
				dialog.setTitle("提醒");
				dialog.setMessage(msg);
				dialog.show();
				
			}
		});
	}
	
	/**
	 * 关闭对话框
	 */
	public void dismissDialog(){
		dialog.dismiss();
	}
    
}
