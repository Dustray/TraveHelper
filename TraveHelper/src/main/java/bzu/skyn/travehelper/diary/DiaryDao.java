package bzu.skyn.travehelper.diary;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DiaryDao {
	private DBOpenHelper dbOpenHelper;
	private SQLiteDatabase database;
	private Integer id;
	
	public DiaryDao(Context context){
		dbOpenHelper=new DBOpenHelper(context);
	}
	/*保存日志*/
	public void save(Diary diary){
		database=dbOpenHelper.getWritableDatabase();
		String sql = "insert into diary(title,content,createtime) values(?,?,?)";
		database.execSQL(
				sql,
				new String[] { diary.getTitle(), diary.getContent(),
						diary.getCreatetime() });
		database.close();
	}
	/*更新日志*/
	public void update(Diary diary){
		database=dbOpenHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		   values.put("title", diary.getTitle());
		   values.put("content", diary.getContent());
		   values.put("createtime", diary.getCreatetime());
		   database.update("diary", values, "id=?", new String[]{diary.getId().toString()});
	}
	/*删除日志*/
	public void delete(Integer id){
		database=dbOpenHelper.getReadableDatabase();
		database.delete("diary", "id=?", new String[]{id.toString()});
	}
	/*查找id*/
	public List<Integer> findId() {
		List<Integer> ids = new ArrayList<Integer>();
		SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
		String sql = "select id from diary";
		Cursor cursor = database.rawQuery(sql, null);
		while (cursor.moveToNext()) {

			id = cursor.getInt(cursor.getColumnIndex("id"));
			ids.add(id);
		}
		cursor.close();
		return ids;
	}
	/*根据id查找日志*/
	public Diary find (Integer id ){
		Diary diary=null;
		   database=dbOpenHelper.getReadableDatabase();
		   Cursor cursor=database.query("diary", null, "id=?", new String[]{id.toString()},null, null, null);
		   if(cursor!=null){
			   if(cursor.moveToFirst()){
				   String title=cursor.getString(cursor.getColumnIndex("title"));
				   String content=cursor.getString(cursor.getColumnIndex("content"));
				   String createtime=cursor.getString(cursor.getColumnIndex("createtime"));
				   diary=new Diary(title,content,createtime);
				   diary.setId(id);
			   }
		   }
		   cursor.close();
		   return diary;
	}
	/*查找所有日志*/
	/*public Cursor getAllDiaries(){
	   database=dbOpenHelper.getReadableDatabase();
	   Cursor cursor=db.query("diary", null, null, null, null, null, null);
	   return cursor;
	}*/
	public List<Diary> getAllDiaries(){
		List<Diary> diaries=new ArrayList<Diary>();
		database=dbOpenHelper.getReadableDatabase();
		String sql="select * from diary ";
		Cursor cursor=database.rawQuery(sql, null);
		while(cursor.moveToNext()){
			String title=cursor.getString(cursor.getColumnIndex("title"));
			String content=cursor.getString(cursor.getColumnIndex("content"));
			String createtime=cursor.getString(cursor.getColumnIndex("createtime"));
			Diary diary=new Diary(title,content,createtime);
			diaries.add(diary);
		}
		return diaries;		
	} 
	


	/*记录日志总数*/
	public long count() {
		long count=0;
		database=dbOpenHelper.getReadableDatabase();
		   Cursor cursor=database.query("diary", new String[]{"count(*)"}, null, null, null, null, null);
		   if(cursor.moveToFirst()){
			   count=cursor.getLong(0);
		   }
		   cursor.close();
		   return count;
	}

}
