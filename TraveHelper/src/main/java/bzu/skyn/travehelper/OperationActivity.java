package bzu.skyn.travehelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ShareCompat.IntentBuilder;
import android.view.Menu;
import android.view.MenuItem;

public class OperationActivity extends Activity {

	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_operation);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 菜单事件的处理事件
		switch (item.getItemId()) {
		case R.id.opback:
			intent=new Intent();
			intent.setClass(OperationActivity.this, MainActivity.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.optuichu:
			finish();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.operation, menu);
		return true;
	}

}
