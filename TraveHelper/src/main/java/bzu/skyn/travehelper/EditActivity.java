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

public class EditActivity extends Activity {
	private EditText editTitle, editContent;
	private Button change, quit;
	private Diary diary;
	private Intent intent;
	private DiaryDao diaryDao;
	private Integer id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_diary);
		
		init();
		getBundle();
		change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				diary = new Diary(editTitle.getText().toString(), editContent
						.getText().toString(), DataTool.getCurrentTime());
				diaryDao = new DiaryDao(EditActivity.this);
				
				AlertDialog.Builder builder = new AlertDialog.Builder(
						EditActivity.this);
				builder.setTitle("提示")
						.setMessage("确定要修改这篇日志吗？")
						.setPositiveButton("确定", new Dialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								diary.setId(id);
								diaryDao.update(diary);

								intent = new Intent();
								intent.setClass(EditActivity.this,
										DiaryActivity.class);
								startActivity(intent);
								finish();
								Toast.makeText(EditActivity.this, "修改成功！",
										Toast.LENGTH_SHORT).show();

							}
						})
						.setNegativeButton("取消", new Dialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								intent = new Intent();
								intent.setClass(EditActivity.this,
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
				intent.setClass(EditActivity.this, DiaryActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void getBundle() {
		// TODO Auto-generated method stub
		Bundle bundle = this.getIntent().getExtras();
		id = bundle.getInt("id");
		String title = bundle.getString("title");
		String content = bundle.getString("content");
		editTitle.setText(title);
		editContent.setText(content);
	}

	// 查找控件
		public void init() {
			editTitle = (EditText) this.findViewById(R.id.editTitle);
			editContent = (EditText) this.findViewById(R.id.editContent);
			change = (Button) this.findViewById(R.id.edit);
			quit = (Button) this.findViewById(R.id.quit);
		}
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			switch (item.getItemId()) {
			case R.id.editback:
				intent=new Intent();
				intent.setClass(EditActivity.this, MainActivity.class);			
				startActivity(intent);
				this.finish();
				break;
			case R.id.editlist:
				intent=new Intent();
				intent.setClass(EditActivity.this, DiaryActivity.class);			
				startActivity(intent);
				finish();
				break;

			case R.id.editquit:
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
		getMenuInflater().inflate(R.menu.edit, menu);
		return true;
	}

}
