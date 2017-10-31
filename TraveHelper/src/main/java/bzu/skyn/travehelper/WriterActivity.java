package bzu.skyn.travehelper;

import bzu.skyn.travehelper.diary.Diary;
import bzu.skyn.travehelper.diary.DiaryDao;
import bzu.skyn.travehelper.tools.DataTool;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WriterActivity extends Activity {
	private EditText titleText;
	private EditText contentText;
	private Diary diary;
	private Intent intent;
	private Button submit, quit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.writer_diary);
		
		init();
		setTitle("写日志");

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String title = titleText.getText().toString();
				String content = contentText.getText().toString();
				diary = new Diary(title, content, DataTool.getCurrentTime());
				AlertDialog.Builder builder = new AlertDialog.Builder(
						WriterActivity.this);
				builder.setTitle("提示")
						.setMessage("确定要保存这篇日志吗？")
						.setPositiveButton("确定", new Dialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								DiaryDao diaryDao = new DiaryDao(
										WriterActivity.this);
								diaryDao.save(diary);
								intent = new Intent();
								intent.setClass(WriterActivity.this,
										DiaryActivity.class);
								startActivity(intent);
								finish();
								Toast.makeText(WriterActivity.this,
										"成功保存一篇日志！", Toast.LENGTH_SHORT).show();

							}
						})
						.setNegativeButton("取消", new Dialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								intent = new Intent();
								intent.setClass(WriterActivity.this,
										DiaryActivity.class);
								startActivity(intent);
								finish();
							}
						}).create().show();
			}
		});
		quit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent();
				intent.setClass(WriterActivity.this, DiaryActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	/* 查找控件 */
	public void init() {
		titleText = (EditText) this.findViewById(R.id.title);
		contentText = (EditText) this.findViewById(R.id.content);
		submit = (Button) this.findViewById(R.id.submit);
		quit = (Button) this.findViewById(R.id.quit);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.writerback:
			intent=new Intent();
			intent.setClass(WriterActivity.this, MainActivity.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.writer_list:
			intent=new Intent();
			intent.setClass(WriterActivity.this, DiaryActivity.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.writer_quit:			
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.writer, menu);
		return true;
	}

}
