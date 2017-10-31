package bzu.skyn.travehelper.calendar;

import bzu.skyn.travehelper.R;
import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.AbsListView.LayoutParams;

public class CalendarGridView extends GridView {

	private Context cContext;

	public CalendarGridView(Context context) {
		super(context);
		cContext = context;
		
		setGirdView();
	}

	private void setGirdView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		
		setLayoutParams(params);
		setNumColumns(7);// 设置每行列数
		setGravity(Gravity.CENTER_VERTICAL);// 位置居中
		setVerticalSpacing(2);// 垂直间隔
		setHorizontalSpacing(2);// 水平间隔
		setBackgroundColor(getResources().getColor(R.color.calendar_background));
	
		WindowManager windowManager = ((Activity)cContext).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int i = display.getWidth() / 7;
		int j = display.getWidth() - (i * 7);
		int x = j / 2;
		setPadding(x, 0, 0, 0);// 居中
	}
}
