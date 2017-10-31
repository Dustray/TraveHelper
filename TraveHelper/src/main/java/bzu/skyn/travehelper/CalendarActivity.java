package bzu.skyn.travehelper;

import java.util.Calendar;
import java.util.Date;

import bzu.skyn.travehelper.calendar.CalendarAdapter;
import bzu.skyn.travehelper.calendar.CalendarGridView;
import bzu.skyn.travehelper.tools.NumberCalendar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.LinearLayout.LayoutParams;

public class CalendarActivity extends Activity implements OnTouchListener {

	//判断手势
		private static final int SWIPE_MIN_DISTANCE = 120;
		private static final int SWIPE_MAX_OFF_PATH = 250;
		private static final int SWIPE_THRESHOLD_VELOCITY = 200;

		//滑动
		private Animation slideLeftIn;
		private Animation slideLeftOut;
		private Animation slideRightIn;
		private Animation slideRightOut;
		private ViewFlipper viewFlipper;
		GestureDetector gesture = null;
		private Intent intent;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			return gesture.onTouchEvent(event);
		}

		AnimationListener animationListener=new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				//当滑动完成后调用
				CreateGirdView();
			}
		};

		class GestureListener extends SimpleOnGestureListener {
			@Override
			public boolean onFling(MotionEvent m1, MotionEvent m2, float velocityX,float velocityY) {
				try {
					if (Math.abs(m1.getY() - m2.getY()) > SWIPE_MAX_OFF_PATH)
						return false;
					// 从右滑向左边
					if (m1.getX() - m2.getX() > SWIPE_MIN_DISTANCE	&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
						viewFlipper.setInAnimation(slideLeftIn);
						viewFlipper.setOutAnimation(slideLeftOut);
						viewFlipper.showNext();
						setNextViewItem();
						//CreateGirdView();
						return true;
					//从左滑向右边
					} else if (m2.getX() - m1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
						viewFlipper.setInAnimation(slideRightIn);
						viewFlipper.setOutAnimation(slideRightOut);
						viewFlipper.showPrevious();
						setPrevViewItem();
						//CreateGirdView(); 
						return true;

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				//得到当前选中的是第几个单元格
				int pos = gridview2.pointToPosition((int) e.getX(), (int) e.getY());
				LinearLayout txtDay = (LinearLayout) gridview2.findViewById(pos + 5000);
				if (txtDay != null) {
					if (txtDay.getTag() != null) {
						Date date = (Date) txtDay.getTag();
						seleCalendar.setTime(date);

						gAdapter.setSelectedDate(seleCalendar);
						gAdapter.notifyDataSetChanged();

						gAdapter1.setSelectedDate(seleCalendar);
						gAdapter1.notifyDataSetChanged();

						gAdapter3.setSelectedDate(seleCalendar);
						gAdapter3.notifyDataSetChanged();
					}
				}

				Log.i("TEST", "onSingleTapUp -  pos=" + pos);

				return false;
			}
		}
		
		private Context context = CalendarActivity.this;
		private GridView gridview;
		private GridView gridview1;// 上一个月
		private GridView gridview2;// 当前月
		private GridView gridview3;// 下一个月
		
		boolean checkSele = false;// 是否是选择事件发生
		private Calendar nowCalendar = Calendar.getInstance();// 当前显示的日历
		private Calendar seleCalendar = Calendar.getInstance(); // 选择的日历
		private Calendar todayCalendar = Calendar.getInstance(); // 今日
		private CalendarAdapter gAdapter;
		private CalendarAdapter gAdapter1;
		private CalendarAdapter gAdapter3;
		// 顶部按钮
		private Button button = null;
		private RelativeLayout mainLayout;

		//
		private int nowViewMonth = 0; // 当前视图月
		private int nowViewYear = 0; // 当前视图年
		private int firstDay = Calendar.MONDAY;

		private static final int mainLayoutID = 88; // 设置主布局
		private static final int titleLayoutID = 77; // title布局
		private static final int caltitleLayoutID = 66; // title布局
		private static final int calLayoutID = 55; // 日历布局
		//底部菜单文字
		String[] menu_toolbar_name_array;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(generateContentView());
			UpdateStartDateForMonth();
			
			
			slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
			slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
			slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
			slideRightOut = AnimationUtils.loadAnimation(this,R.anim.slide_right_out);
			
			slideLeftIn.setAnimationListener(animationListener);
			slideLeftOut.setAnimationListener(animationListener);
			slideRightIn.setAnimationListener(animationListener);
			slideRightOut.setAnimationListener(animationListener);
			
			gesture = new GestureDetector(this, new GestureListener());
		}

		AlertDialog.OnKeyListener onKeyListener = new AlertDialog.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					CalendarActivity.this.finish();
				}
				return false;

			}

		};

		
		// 生成内容视图
		private View generateContentView() {
			// 创建一个垂直的线性布局（整体内容）
			viewFlipper = new ViewFlipper(this);
			viewFlipper.setId(calLayoutID);
			
			mainLayout = new RelativeLayout(this); // 创建一个整体垂直的线性布局
			RelativeLayout.LayoutParams params_main = new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			mainLayout.setLayoutParams(params_main);
			mainLayout.setId(mainLayoutID);
			mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);

			LinearLayout layTopButton = createLayout(LinearLayout.HORIZONTAL); // 生成顶部按钮布局

			generateTopButtons(layTopButton); // 生成顶部按钮 
			RelativeLayout.LayoutParams params_title = new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			params_title.topMargin = 5;
			layTopButton.setId(titleLayoutID);
			mainLayout.addView(layTopButton, params_title);

			nowCalendar = getCalendarStartDate();

			setTitleGirdView();
			RelativeLayout.LayoutParams params_cal_title = new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			params_cal_title.addRule(RelativeLayout.BELOW, titleLayoutID);
			mainLayout.addView(gridview, params_cal_title);

			CreateGirdView();

			RelativeLayout.LayoutParams params_cal = new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			params_cal.addRule(RelativeLayout.BELOW, caltitleLayoutID);

			mainLayout.addView(viewFlipper, params_cal);
			
			LinearLayout br = new LinearLayout(this);
			RelativeLayout.LayoutParams params_br = new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, 1);
			params_br.addRule(RelativeLayout.BELOW, calLayoutID);
			br.setBackgroundColor(getResources().getColor(R.color.calendar_background));
			mainLayout.addView(br, params_br);

			return mainLayout;

		}

		// 创建一个线性布局
		private LinearLayout createLayout(int direction) {
			LinearLayout lay = new LinearLayout(this);
			LayoutParams params = new LayoutParams(
					android.view.ViewGroup.LayoutParams.FILL_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.topMargin = 10;
			// 设置布局参数
			lay.setLayoutParams(params);
			lay.setOrientation(direction);// 设置方向
			lay.setGravity(Gravity.LEFT);
			return lay;
		}

		// 生成顶部按钮
		private void generateTopButtons(LinearLayout layTopButton) {
			// 创建一个当前月按钮（中间的按钮）
			button = new Button(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			lp.leftMargin = 20;
			button.setLayoutParams(lp);
			button.setTextSize(25);
			button.setBackgroundResource(Color.TRANSPARENT);
			// 设置当前月按钮的背景颜色为按钮默认颜色

			// 当前月的点击事件的监听
			button.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View arg0) {
					setToDayViewItem();
				}
			});

			layTopButton.setGravity(Gravity.CENTER_HORIZONTAL);
			layTopButton.addView(button);

		}

		private void setTitleGirdView() {

			gridview = setGirdView();
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			// params.topMargin = 5;
			gridview.setLayoutParams(params);
			gridview.setVerticalSpacing(0);// 垂直间隔
			gridview.setHorizontalSpacing(0);// 水平间隔
			TitleGridAdapter titleAdapter = new TitleGridAdapter(this);
			gridview.setAdapter(titleAdapter);// 设置菜单Adapter
			gridview.setId(caltitleLayoutID);
		}

		private void CreateGirdView() {
			// 临时
			Calendar calSelect1 = Calendar.getInstance(); 
			Calendar calSelect2 = Calendar.getInstance(); 
			Calendar calSelect3 = Calendar.getInstance(); 
			calSelect1.setTime(nowCalendar.getTime());
			calSelect2.setTime(nowCalendar.getTime());
			calSelect3.setTime(nowCalendar.getTime());

			gridview1 = new CalendarGridView(context);
			calSelect1.add(Calendar.MONTH, -1);
			gAdapter1 = new CalendarAdapter(this, calSelect1);
			gridview1.setAdapter(gAdapter1);// 设置菜单Adapter
			gridview1.setId(calLayoutID);

			gridview2 = new CalendarGridView(context);
			gAdapter = new CalendarAdapter(this, calSelect2);
			gridview2.setAdapter(gAdapter);// 设置菜单Adapter
			gridview2.setId(calLayoutID);

			gridview3 = new CalendarGridView(context);
			calSelect3.add(Calendar.MONTH, 1);
			gAdapter3 = new CalendarAdapter(this, calSelect3);
			gridview3.setAdapter(gAdapter3);// 设置菜单Adapter
			gridview3.setId(calLayoutID);

			gridview2.setOnTouchListener(this);
			gridview1.setOnTouchListener(this);
			gridview3.setOnTouchListener(this);

			if (viewFlipper.getChildCount() != 0) {
				viewFlipper.removeAllViews();
			}

			viewFlipper.addView(gridview2);
			viewFlipper.addView(gridview3);
			viewFlipper.addView(gridview1);

			String s = nowCalendar.get(Calendar.YEAR)
					+ "-"
					+ NumberCalendar.LeftPad_Tow_Zero(nowCalendar
							.get(Calendar.MONTH) + 1);

			button.setText(s);
		}

		private GridView setGirdView() {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			GridView gridView = new GridView(this);
			gridView.setLayoutParams(params);
			gridView.setNumColumns(7);// 设置每行列数
			gridView.setGravity(Gravity.CENTER_VERTICAL);// 位置居中
			gridView.setVerticalSpacing(1);// 垂直间隔
			gridView.setHorizontalSpacing(1);// 水平间隔
			gridView.setBackgroundColor(getResources().getColor(
					R.color.calendar_background));

			WindowManager windowManager = getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			int i = display.getWidth() / 7;
			int j = display.getWidth() - (i * 7);
			int x = j / 2;
			gridView.setPadding(x, 0, 0, 0);// 居中

			return gridView;
		}

		// 上一个月
		private void setPrevViewItem() {
			nowViewMonth--;// 当前选择月
			// 如果当前月为负数的话显示上一年
			if (nowViewMonth == -1) {
				nowViewMonth = 11;
				nowViewYear--;
			}
			nowCalendar.set(Calendar.DAY_OF_MONTH, 1); // 设置日为当月1日
			nowCalendar.set(Calendar.MONTH, nowViewMonth); // 设置月
			nowCalendar.set(Calendar.YEAR, nowViewYear); // 设置年

		}

		// 当月
		private void setToDayViewItem() {

			seleCalendar.setTimeInMillis(todayCalendar.getTimeInMillis());
			seleCalendar.setFirstDayOfWeek(firstDay);
			nowCalendar.setTimeInMillis(todayCalendar.getTimeInMillis());
			nowCalendar.setFirstDayOfWeek(firstDay);

		}

		// 下一个月
		private void setNextViewItem() {
			nowViewMonth++;
			if (nowViewMonth == 12) {
				nowViewMonth = 0;
				nowViewYear++;
			}
			nowCalendar.set(Calendar.DAY_OF_MONTH, 1);
			nowCalendar.set(Calendar.MONTH, nowViewMonth);
			nowCalendar.set(Calendar.YEAR, nowViewYear);

		}

		// 根据改变的日期更新日历
		// 填充日历控件用
		private void UpdateStartDateForMonth() {
			nowCalendar.set(Calendar.DATE, 1); // 设置成当月第一天
			nowViewMonth = nowCalendar.get(Calendar.MONTH);// 得到当前日历显示的月
			nowViewYear = nowCalendar.get(Calendar.YEAR);// 得到当前日历显示的年

			String s = nowCalendar.get(Calendar.YEAR)
					+ "-"
					+ NumberCalendar.LeftPad_Tow_Zero(nowCalendar
							.get(Calendar.MONTH) + 1);
			button.setText(s);

			// 星期一是2 星期天是1 填充剩余天数
			int iDay = 0;
			int firstDay = Calendar.MONDAY;
			int iStartDay = firstDay;
			if (iStartDay == Calendar.MONDAY) {
				iDay = nowCalendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
				if (iDay < 0)
					iDay = 6;
			}
			if (iStartDay == Calendar.SUNDAY) {
				iDay = nowCalendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
				if (iDay < 0)
					iDay = 6;
			}
			nowCalendar.add(Calendar.DAY_OF_WEEK, -iDay);

		}

		private Calendar getCalendarStartDate() {
			todayCalendar.setTimeInMillis(System.currentTimeMillis());
			todayCalendar.setFirstDayOfWeek(firstDay);

			if (seleCalendar.getTimeInMillis() == 0) {
				nowCalendar.setTimeInMillis(System.currentTimeMillis());
				nowCalendar.setFirstDayOfWeek(firstDay);
			} else {
				nowCalendar.setTimeInMillis(seleCalendar.getTimeInMillis());
				nowCalendar.setFirstDayOfWeek(firstDay);
			}

			return nowCalendar;
		}

		public class TitleGridAdapter extends BaseAdapter {

			int[] titles = new int[] { R.string.Sun, R.string.Mon, R.string.Tue,
					R.string.Wed, R.string.Thu, R.string.Fri, R.string.Sat };

			private Activity activity;

			// construct
			public TitleGridAdapter(Activity a) {
				activity = a;
			}

			@Override
			public int getCount() {
				return titles.length;
			}

			@Override
			public Object getItem(int position) {
				return titles[position];
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LinearLayout iv = new LinearLayout(activity);
				TextView txtDay = new TextView(activity);
				txtDay.setFocusable(false);
				txtDay.setBackgroundColor(Color.TRANSPARENT);
				iv.setOrientation(1);

				txtDay.setGravity(Gravity.CENTER);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

				int i = (Integer) getItem(position);

				txtDay.setTextColor(Color.WHITE);
				Resources res = getResources();

				if (i == R.string.Sat) {
					// 周六
					txtDay.setBackgroundColor(res.getColor(R.color.title_text_6));
				} else if (i == R.string.Sun) {
					// 周日
					txtDay.setBackgroundColor(res.getColor(R.color.title_text_7));
				} else {

				}

				txtDay.setText((Integer) getItem(position));

				iv.addView(txtDay, lp);

				return iv;
			}
		}
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// TODO Auto-generated method stub
			menu.add(0, 1, 1, "返回主菜单");
			menu.add(0, 2, 2, "退出");
			return super.onCreateOptionsMenu(menu);
		}
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			switch (item.getItemId()) {
			case 1:
				intent=new Intent();
				intent.setClass(CalendarActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
				break;
			case 2:
				finish();
			default:
				break;
			}
			return super.onOptionsItemSelected(item);
		}
}
