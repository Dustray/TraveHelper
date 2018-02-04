package bzu.skyn.travehelper;

import java.io.IOException;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import bzu.skyn.travehelper.entity.AttractionEntity;
import bzu.skyn.travehelper.search.SceneryScrollView;
import bzu.skyn.travehelper.R;
import bzu.skyn.travehelper.tools.FastToast;
import bzu.skyn.travehelper.tools.JsonTools;
import bzu.skyn.travehelper.util.AttractionAdapter;
import bzu.skyn.travehelper.webservice.WebServiceConnection;

import android.accounts.NetworkErrorException;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.show.api.ShowApiRequest;

public class SearchActivity extends Activity implements OnGestureListener,
		OnTouchListener {

	private Button query;
	private EditText sname;
	private TextView traveltext;
	private ViewFlipper viewFlipper;
	private boolean showNext = true;
	private boolean isRun = true;
	private int currentPage = 0;
	private final int SHOW_NEXT = 0011;
	private static final int FLING_MIN_DISTANCE = 50;
	private static final int FLING_MIN_VELOCITY = 0;
	private GestureDetector mGestureDetector;
	private TextView date_TextView;
	private ListView lvAttraction;
	private View layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);

		LayoutInflater factory= this.getLayoutInflater();
		layout = factory.inflate(R.layout.list_head_search, null);
		init();
		
		date_TextView.setText(getDate());
		mGestureDetector = new GestureDetector(this);
		viewFlipper.setOnTouchListener(this);
		viewFlipper.setLongClickable(true);
		viewFlipper.setOnClickListener(clickListener);
		displayRatio_selelct(currentPage);

		SceneryScrollView myScrollView = (SceneryScrollView) layout.findViewById(R.id.scenery_vf_scrollview);
		myScrollView.setOnTouchListener(onTouchListener);
		myScrollView.setGestureDetector(mGestureDetector);
		lvAttraction = (ListView)findViewById(R.id.lv_attractions);

//		List<AttractionEntity> attList = new ArrayList<AttractionEntity>();
//		View headerView = View.inflate(SearchActivity.this, R.layout.list_head_search, null);
//		lvAttraction.addHeaderView(headerView);
//		AttractionAdapter adapter = new AttractionAdapter(SearchActivity.this,R.layout.item_attractions,attList);
//		lvAttraction.setAdapter(adapter);
		getAttractions();
		thread.start();



	}

	protected Handler mHandler1 =  new Handler();
	private void getAttractions(){
		//以下代码为纯java实现，并未依赖第三方框架,具体传入参数请参看接口描述详情页.


					new Thread(){
						//在新线程中发送网络请求
						public void run() {
							String appid="48846";//要替换成自己的
							String secret="6296793c56db40dfbbccf0353babe62c";//要替换成自己的
							SharedPreferences preference = getSharedPreferences("myWeather", MODE_PRIVATE);
							//city = readSharpPreference();
							String cityid = preference.getString("cityid", "283");
							String proid = preference.getString("proid", "22");
//							final String res=new ShowApiRequest( "http://route.showapi.com/268-1", appid, secret)
//									.addTextPara("keyword", "")
//									.addTextPara("proId", proid)
//									.addTextPara("cityId", cityid)
//									.addTextPara("areaId", "")
//									.addTextPara("page", "")
//									.post();
							String res="";
							WebServiceConnection wsc = new WebServiceConnection();
							try {
								Map<String,String> para = new HashMap<>();
								para.put("cityid",cityid);
								para.put("proid",proid);
								res=wsc.getRemoteInfo(para,"getAttractionJson");
							} catch (Exception e) {
								e.printStackTrace();
							}
							System.out.println(res);
							//把返回内容通过handler对象更新到界面
							final String finalRes = res;
							mHandler1.post(new Thread(){
								public void run() {
									//traveltext.setText(res+"  "+new Date());

									List<AttractionEntity> attList = null;
									try {
										attList = JsonTools.jsonAttraction(finalRes);
									} catch (NetworkErrorException e) {
										e.printStackTrace();
										Message msg = new Message();
										msg.what = 1;
										errorHandler.sendMessage(msg);
										return;
									}
									//View headerView = View.inflate(SearchActivity.this, R.layout.list_head_search, null);
									//lvAttraction.addHeaderView(headerView);
									//Toast.makeText(SearchActivity.this,":"+attList.get(0).getName(), Toast.LENGTH_LONG).show();
									AttractionAdapter adapter = new AttractionAdapter(SearchActivity.this,R.layout.item_attractions,attList);
									lvAttraction.setAdapter(adapter);
								}
							});
						}
					}.start();
		}
	protected Handler errorHandler =  new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case 1:
					FastToast.showToast(SearchActivity.this,"网络错误，请查看网络");
					break;
			}

		}
	};
	private void init() {		
		query = (Button) layout.findViewById(R.id.button1);
		query.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = sname.getText().toString();

				if (name == null || name.equals("")) {

					//sname.setText("请输入景点信息");

				} else if (name != null) {
					Toast.makeText(SearchActivity.this, "查询出错", Toast.LENGTH_LONG).show();
					getAttractions();
//					String infor = scenceList(name);
//					if (infor != null) {
//						//traveltext.setText(infor);
//
//					}else{
//						Toast.makeText(SearchActivity.this, "查询出错", Toast.LENGTH_LONG).show();
//					}
				}
			}
		});
		sname = (EditText) layout.findViewById(R.id.editText1);
		traveltext = (TextView) layout.findViewById(R.id.traveltext);
		viewFlipper = (ViewFlipper) layout.findViewById(R.id.mViewFliper_vf);
		date_TextView = (TextView) layout.findViewById(R.id.home_date_tv);
	}
	

	private void displayRatio_selelct(int id) {
		int[] ratioId = { R.id.scenery_img01, R.id.scenery_img02,
				R.id.scenery_img03, R.id.scenery_img04 };
		ImageView img = (ImageView) layout.findViewById(ratioId[id]);
		img.setSelected(true);
	}

	private void displayRatio_normal(int id) {
		int[] ratioId = { R.id.scenery_img01, R.id.scenery_img02,
				R.id.scenery_img03, R.id.scenery_img04 };
		ImageView img = (ImageView) layout.findViewById(ratioId[id]);
		img.setSelected(false);
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		}
	};
	private OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			return mGestureDetector.onTouchEvent(event);
		}
	};
	Thread thread = new Thread() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (isRun) {
				try {
					Thread.sleep(1000 * 5);
					Message msg = new Message();
					msg.what = SHOW_NEXT;
					mHandler.sendMessage(msg);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	};
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case SHOW_NEXT:
				if (showNext) {
					showNextView();
				} else {
					showPreviousView();
				}
				break;

			default:
				break;
			}
		}

	};

	private String getDate() {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		int w = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		String mDate =  weekDays[w];
		return mDate;
	}

	private void showNextView() {

		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_left_in));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_left_out));
		viewFlipper.showNext();
		currentPage++;
		if (currentPage == viewFlipper.getChildCount()) {
			displayRatio_normal(currentPage - 1);
			currentPage = 0;
			displayRatio_selelct(currentPage);
		} else {
			displayRatio_selelct(currentPage);
			displayRatio_normal(currentPage - 1);
		}
		//Log.e("currentPage", currentPage + "");

	}

	private void showPreviousView() {
		displayRatio_selelct(currentPage);
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_right_in));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_right_out));
		viewFlipper.showPrevious();
		currentPage--;
		if (currentPage == -1) {
			displayRatio_normal(currentPage + 1);
			currentPage = viewFlipper.getChildCount() - 1;
			displayRatio_selelct(currentPage);
		} else {
			displayRatio_selelct(currentPage);
			displayRatio_normal(currentPage + 1);
		}
		//Log.e("currentPage", currentPage + "");
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		Log.e("view", "onFling");
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			Log.e("fling", "left");
			showNextView();
			showNext = true;
			// return true;
		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			Log.e("fling", "right");
			showPreviousView();
			showNext = false;
			// return true;
		}
		return false;
	}

	public String scenceList(String sname) {		
		String wsdlUrl = "http://10.61.15.242:8080/Scenery/ScenerydaoPort";
		String namespace = "http://dao.skyn.bzu/";
		String webMethod = "getScenery";
		String soapAction = namespace + webMethod;

		SoapObject request = new SoapObject(namespace, webMethod);
		request.addProperty("arg0", sname);

		SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelop.bodyOut = request;
		envelop.setOutputSoapObject(request);

		HttpTransportSE se = new HttpTransportSE(wsdlUrl);
		try {
			se.call(soapAction, envelop);
			if (envelop.getResponse() != null) {
				request = (SoapObject) envelop.getResponse();
				try{
					String sintro = request.getProperty("sintro").toString();
					return sintro;
				}catch (Exception e) {
					// TODO: handle exception
					return "暂无此景点信息！";
				}
				

			}else{
				return "暂无此景点信息！";
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			isRun = false;
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		finish();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getAttractions();
	}
}
