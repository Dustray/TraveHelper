package bzu.skyn.travehelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ShowActivity extends Activity {
	private TextView titText,contentText,createtimeText;	
	private Integer id;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_diary);
		
		init();
		getBundle();
	}

	private void init() {
		// 查找控件
		titText=(TextView) this.findViewById(R.id.titletext);
		contentText=(TextView) this.findViewById(R.id.contenttext);
		createtimeText=(TextView) this.findViewById(R.id.createtimetext);
	}
	private void getBundle() {
		// TODO Auto-generated method stub
		Bundle bundle = this.getIntent().getExtras();
		id = bundle.getInt("id");
		String title = bundle.getString("title");
		String content = bundle.getString("content");
		String createtime=bundle.getString("createtime");
		titText.setText(title);
		
		createtimeText.setText(createtime);
		contentText.setText(content);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.showback:
			intent=new Intent();
			intent.setClass(ShowActivity.this, MainActivity.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.showbacklist:
			intent=new Intent();
			intent.setClass(ShowActivity.this, DiaryActivity.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.showquit:
			finish();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show, menu);
		return true;
	}

}
