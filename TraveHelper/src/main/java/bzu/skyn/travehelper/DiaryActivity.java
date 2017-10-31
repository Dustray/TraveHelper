package bzu.skyn.travehelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import bzu.skyn.travehelper.diary.Diary;
import bzu.skyn.travehelper.diary.DiaryDao;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class DiaryActivity extends Activity {
	
	private ListView listView;
	private DiaryDao diaryDao;
	private Intent intent;
	private List<Map<String, Object>> data;
	private SimpleAdapter adapter;
	private TextView empty;
	private int count;
	private int id;
	private Integer did;
	private Diary diary;
	private static final int item1 = 0;
	private static final int item2 = 1;
	private static final int item3=2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diary);
		
		init();
		getData();
		setTitle("你总共写了"+count+"篇日志");
		//注册一个上下文LiatView
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			
			@Override
			public void onCreateContextMenu(ContextMenu menu, View view,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				menu.setHeaderTitle("请选择");
				menu.setHeaderIcon(android.R.drawable.arrow_down_float);
				menu.add(1, item1, 0, "编辑日志");
				menu.add(1, item2, 1, "查看日志");
				menu.add(1, item3, 2, "删除日志");
			}
		});
		listView.setEmptyView(empty);
	}
	
	/* 获取数据 */
	private void getData() {
		// 获取数据库中的数据
		diaryDao = new DiaryDao(DiaryActivity.this);
		count=(int) diaryDao.count();
		List<Diary> diaries = diaryDao.getAllDiaries();
		data = new ArrayList<Map<String, Object>>();
		for (Diary diary : diaries) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", diary.getId());
			item.put("title", diary.getTitle());
			item.put("createtime", diary.getCreatetime());
			
			data.add(item);
		}
		adapter = new SimpleAdapter(DiaryActivity.this, data, R.layout.list,
				new String[] { "title", "createtime" }, new int[] { R.id.title,
						R.id.createtime });
		listView.setAdapter(adapter);
	}

	/* 查找控件 */
	public void init() {
		listView = (ListView) this.findViewById(R.id.listView);
		empty = (TextView) this.findViewById(R.id.empty);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// 长按listview提示菜单项的事件处理
		super.onContextItemSelected(item);
		
		AdapterContextMenuInfo info=(AdapterContextMenuInfo) item.getMenuInfo();
		id=(int) info.id;
		Log.v("DiaryActivity", "id=" + id);
		did=diaryDao.findId().get(id);
		Log.v("DiaryActivity", "did=" + did);
		diary=diaryDao.find(did);
		switch (item.getItemId()) {
		case item1:
			intent=new Intent();
			Bundle bundle=new Bundle();
			bundle.putInt("id", did);
			bundle.putString("title", diary.getTitle());
			bundle.putString("content", diary.getContent());
			intent.setClass(DiaryActivity.this, EditActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
			break;

		case item2:
			intent=new Intent();
			intent.putExtra("id", did);
			intent.putExtra("title", diary.getTitle());
			intent.putExtra("content", diary.getContent());
			intent.putExtra("createtime", diary.getCreatetime());
		    intent.setClass(DiaryActivity.this, ShowActivity.class);
			startActivity(intent);
			finish();
			break;
		case item3:
			AlertDialog.Builder builder=new AlertDialog.Builder(DiaryActivity.this);
			builder.setTitle("提示").setMessage("确认要删除这篇日志吗？").setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					diaryDao.delete(did);
					getData();
					Toast.makeText(DiaryActivity.this, "成功删除日志", Toast.LENGTH_LONG).show();
				}
			}).setNegativeButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).create().show();
			break;
		default:
			break;
		}
		return false;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO 菜单项的事件处理
		switch (item.getItemId()) {
		case R.id.back:
			intent=new Intent();
			intent.setClass(DiaryActivity.this, MainActivity.class);			
			startActivity(intent);
			this.finish();
			break;
		case R.id.add_diary:
			intent=new Intent();
			intent.setClass(DiaryActivity.this, WriterActivity.class);			
			startActivity(intent);
			finish();
			break;

		case R.id.quit:
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
		getMenuInflater().inflate(R.menu.diary, menu);
		return true;
	}

}
