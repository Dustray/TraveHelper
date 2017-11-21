package bzu.skyn.travehelper;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RadioGroup;
import android.widget.TabHost;

/**
 * 该类实现每个页面的导航 继承TabActivity内同时包含了天气界面
 * 
 * @author Administrator
 * 
 */
public class MainActivity extends TabActivity implements
		OnCheckedChangeListener {
	// 声明变量
	private RadioGroup mainTab;
	private TabHost tabHost;
	private Intent ihome;
	private Intent iseacher;
	private Intent imap;
	private Intent idiary;
	private Intent imore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getId();
		tabHost = getTabHost();
		mainTab.setOnCheckedChangeListener(MainActivity.this);
		ihome = new Intent(this, WeatherActivity.class);
		tabHost.addTab(tabHost
				.newTabSpec("ihome")
				.setIndicator(getResources().getString(R.string.weather),
						getResources().getDrawable(R.drawable.ic_icon_home))
				.setContent(ihome));
		iseacher = new Intent(this, SearchActivity.class);
		tabHost.addTab(tabHost
				.newTabSpec("iseacher")
				.setIndicator(getResources().getString(R.string.select),
						getResources().getDrawable(R.drawable.ic_icon_spot))
				.setContent(iseacher));

		imap = new Intent(this, AMapActivity.class);
		tabHost.addTab(tabHost
				.newTabSpec("imap")
				.setIndicator(getResources().getString(R.string.map),
						getResources().getDrawable(R.drawable.ic_icon_map))
				.setContent(imap));

		idiary = new Intent(this, TuijianActivity.class);
		tabHost.addTab(tabHost
				.newTabSpec("idiary")
				.setIndicator(getResources().getString(R.string.tuijian),
						getResources().getDrawable(R.drawable.ic_icon_recommend))
				.setContent(idiary));

		imore = new Intent(this, ToolActivity.class);
		tabHost.addTab(tabHost
				.newTabSpec("imore")
				.setIndicator(getResources().getString(R.string.tool),
						getResources().getDrawable(R.drawable.ic_icon_more))
				.setContent(imore));

	}

	protected void getId() {
		mainTab = (RadioGroup) this.findViewById(R.id.main_tab);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radio_button0:
			this.tabHost.setCurrentTabByTag("ihome");
			break;
		case R.id.radio_button1:
			this.tabHost.setCurrentTabByTag("iseacher");
			break;
		case R.id.radio_button2:
			this.tabHost.setCurrentTabByTag("imap");
			break;
		case R.id.radio_button3:
			this.tabHost.setCurrentTabByTag("idiary");
			break;
		case R.id.radio_button4:
			this.tabHost.setCurrentTabByTag("imore");
			break;
		}

	}
	
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){
			AlertDialog.Builder builder=new Builder(MainActivity.this);
			builder.setMessage("确认要退出吗？");
			builder.setTitle("提示");
			builder.setPositiveButton("确认", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					MainActivity.this.finish();		
				}
			});
			builder.setNegativeButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					
				}
			});
			builder.create().show();
			return false;
		}
		return false;
	}*/
}
