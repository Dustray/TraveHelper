package bzu.skyn.travehelper.map;

import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.net.Uri;

public class Suggestion extends SearchRecentSuggestionsProvider {

	public final static String AUTHORITY = "com.amap.cn.apis.poisearch.MySuggestionProvider";
	public final static int MODE = DATABASE_MODE_QUERIES;

	public Suggestion() {
		setupSuggestions(AUTHORITY, MODE);
	}
	
	 public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, 
	            String sortOrder) {
		  
		  Cursor cursor=super.query(uri, projection, selection, selectionArgs, sortOrder);
		 
		  return cursor;
	  }
}
