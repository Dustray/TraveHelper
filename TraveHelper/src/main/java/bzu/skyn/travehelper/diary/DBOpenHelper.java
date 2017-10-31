package bzu.skyn.travehelper.diary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	private static final String DIARY_NAME="Diary.db";
	private static final int DIARY_VERSION=1;

	public DBOpenHelper(Context context) {
		super(context, DIARY_NAME, null, DIARY_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//创建表diary
		db.execSQL("create table diary(id integer primary key autoincrement,title varchar(50),content varchar(1000),createtime)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
