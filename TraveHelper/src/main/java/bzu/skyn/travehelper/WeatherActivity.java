package bzu.skyn.travehelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ksoap2.serialization.SoapObject;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import bzu.skyn.travehelper.entity.AttractionEntity;
import bzu.skyn.travehelper.entity.CityEntity;
import bzu.skyn.travehelper.entity.ProvinceEntity;
import bzu.skyn.travehelper.entity.ProvinceListEntity;
import bzu.skyn.travehelper.entity.WeatherJsonEntity;
import bzu.skyn.travehelper.tools.FastToast;
import bzu.skyn.travehelper.tools.ImageTool;
import bzu.skyn.travehelper.tools.JsonTools;
import bzu.skyn.travehelper.tools.ListDataSave;
import bzu.skyn.travehelper.util.AttractionAdapter;
import bzu.skyn.travehelper.util.SoapUtil;

import android.annotation.SuppressLint;
import android.app.backup.SharedPreferencesBackupHelper;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.show.api.ShowApiRequest;

public class WeatherActivity extends Activity {
    //private TextView text;
    private Button city_btn;
    private static final int CITY = 0x11;
    private String city_str;
    private TextView city_text;
    private Spinner province_spinner;
    private Spinner city_spinner;
    private List<String> provinces;
    private List<String> citys;
    private SharedPreferences preference;
    private ImageTool imageTool;
    private String city;

    protected Handler mHandler1 = new Handler();
    protected Handler mHandler2 = new Handler();
    private List<CityEntity> cityList = new ArrayList<CityEntity>();
    private List<ProvinceEntity> provinceList = new ArrayList<ProvinceEntity>();
    private List<List<CityEntity>> provinceListList = new ArrayList<List<CityEntity>>();
    private OptionsPickerView pvOptions;
    private boolean getCityOver = false;
    private SQLiteDatabase db;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        LitePal.initialize(this);
        setContentView(R.layout.activity_weather);
        ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cwjManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            preference = getSharedPreferences("weather", MODE_PRIVATE);
            SharedPreferences preference2 = getSharedPreferences("myWeather", MODE_PRIVATE);
            //city = readSharpPreference();
            city = preference2.getString("city", "滨州");
            city_text = (TextView) findViewById(R.id.city);
            city_text.setText(city);
            //text = (TextView) findViewById(R.id.test);
            city_btn = (Button) findViewById(R.id.city_button);
            city_btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    switch (v.getId()) {
                        case R.id.city_button:

                            show_dialog(CITY);

                            break;

                        default:
                            break;
                    }
                }
            });
            //获取存在本地的weatherList
            ListDataSave lds = new ListDataSave(WeatherActivity.this, "weather");
            List<WeatherJsonEntity> s = lds.getDataList("WeatherList");
            if (s.size() != 0) {
                Log.e("logme", "sss" + lds.getDataList("WeatherList").toString());
                refresh(s);
            }

            getWeatherThread();
            db = Connector.getDatabase();
//            ListDataSave lds2 = new ListDataSave(WeatherActivity.this, "cityList");
//            List<ProvinceEntity> s1 = lds2.getProDataList("WeatherProList");
//            List<List<CityEntity>> s2 = lds2.getCityDataList("WeatherCityList");
//            Log.e("logme", "ssasasa######" + s1.size()  +"sss"+s2.size() );
//            if (s1.size() != 0 && s2.size() != 0) {
//                provinceList = s1;
//                provinceListList = s2;
//                getCityOver = true;
            if(DataSupport.count(ProvinceEntity.class)>0) {
                provinceList = DataSupport.findAll(ProvinceEntity.class);
                List<List<CityEntity>> cityListList = new ArrayList<List<CityEntity>>();
                for (int i = 0; i < provinceList.size(); i++) {
                    List<CityEntity> cityList = DataSupport.where("proId = ?", provinceList.get(i).getIds()).find(CityEntity.class);
                    provinceListList.add(cityList);
                }

                getCityOver = true;
            }else{

                FastToast.showToast(WeatherActivity.this, "正在更新城市信息,大约需要20秒");
                new Thread() {
                    //在新线程中发送网络请求
                    public void run() {
                        String appid = "48846";//要替换成自己的
                        String secret = "6296793c56db40dfbbccf0353babe62c";//要替换成自己的
                        final String res = new ShowApiRequest("http://route.showapi.com/268-2", appid, secret)
                                .post();

                        System.out.println(res);
                        //把返回内容通过handler对象更新到界面
                        mHandler1.post(new Thread() {
                            public void run() {
                                provinceList = JsonTools.jsonProvince(res);
                                th1.start();
                                //FastToast.showToast(WeatherActivity.this,"s"+provinceList.get(0).getName());
                            }
                        });
                    }
                }.start();

            }


        } else {
            Toast.makeText(WeatherActivity.this, "无网络连接，请确认是否连接网络！", Toast.LENGTH_LONG).show();
        }
    }

    private void getWeatherThread() {
        new Thread() {
            @Override
            public void run() {
                //Log.i("logme","InThread"+city);
                List<WeatherJsonEntity> wje = SoapUtil.getWeatherByCity(city);

                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = wje;
                mhandler.sendMessage(msg);
            }
        }.start();
    }
    //获取地级市1

    Thread th1 = new Thread() {
        //在新线程中发送网络请求
        public void run() {
            String appid = "48846";//要替换成自己的
            String secret = "6296793c56db40dfbbccf0353babe62c";//要替换成自己的
            int requestTag = 0;
            for (ProvinceEntity entity : provinceList) {
                final String res = new ShowApiRequest("http://route.showapi.com/268-3", appid, secret)
                        .addTextPara("proId", entity.getIds())
                        .post();
                cityList = JsonTools.jsonCity(res);
                provinceListList.add(cityList);

                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.i("logme", "SleepError" + e.toString());
                }

            }
            //把返回内容通过handler对象更新到界面
            mHandler2.post(new Thread() {
                public void run() {
                    FastToast.showToast(WeatherActivity.this, "更新城市信息成功");
//                    ListDataSave lds = new ListDataSave(WeatherActivity.this, "cityList");
//                    lds.setDataList("WeatherProList", provinceList);
//                    lds.setDataList("WeatherCityList", provinceListList);



                    DataSupport.saveAll(provinceList);
                    //DataSupport.saveAll(provinceListList);
                    for(List<CityEntity> sList :provinceListList){
                        DataSupport.saveAll(sList);
                    }
                    getCityOver = true;
                    //FastToast.showToast(WeatherActivity.this,"s"+cityList.size());
                }
            });
        }
    };

    public void showTast(String string) {
        Toast.makeText(WeatherActivity.this, string + "请确认是否连接网络！", Toast.LENGTH_SHORT).show();

    }

    public void show_dialog(int cityId) {

        if (getCityOver) {
            pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3, View v) {

                    //返回的分别是三个级别的选中位置
                    if (provinceListList.get(options1).size() == 0) {
                        FastToast.showToast(WeatherActivity.this, "此省信息为空，请切换其他城市");
                    } else {
                        String thisCity = provinceListList.get(options1).get(option2).getNames();
                        String thisProCity = provinceListList.get(options1).get(option2).getProName();
                        if (thisProCity.equals("北京")) {
                            thisCity = "北京";
                        } else if (thisProCity.equals("上海")) {
                            thisCity = "上海";
                        } else if (thisProCity.equals("重庆")) {
                            thisCity = "重庆";
                        } else if (thisProCity.equals("天津")) {
                            thisCity = "天津";
                        }
//                        if( thisProCity.equals("上海")||
//                                thisProCity.equals("重庆")||
//                                thisProCity.equals("天津")){
//                            if( thisCity.substring(thisCity.length() - 2,thisCity.length()).equals("新区")) {
//                                thisCity = thisCity.substring(0, thisCity.length() - 2);
//                            }else if( thisCity.substring(thisCity.length() - 1,thisCity.length()).equals("区")||
//                                    thisCity.substring(thisCity.length() - 1,thisCity.length()).equals("县")) {
//                                thisCity = thisCity.substring(0, thisCity.length() - 1);
//                            }
//                        }
                        FastToast.showToast(WeatherActivity.this, thisCity);
                        SharedPreferences preference1 = getSharedPreferences("myWeather", MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = preference1.edit();
                        editor1.putString("city", thisCity);
                        editor1.commit();
                        // Toast.makeText(WeatherActivity.this,"mycity1："+readSharpPreference() , Toast.LENGTH_LONG).show();
                        city = thisCity;
                        //readSharpPreference();
                        getWeatherThread();
                    }
                }
            }).setSubmitText("确定")//确定按钮文字
                    .setCancelText("取消")//取消按钮文字
                    .setTitleText("城市选择")//标题
                    .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                    .build();


            pvOptions.setPicker(provinceList, provinceListList);
            pvOptions.show();
        } else {
            FastToast.showToast(WeatherActivity.this, "正在更新城市信息，请稍后...");
        }
//        switch (cityId) {
//            case CITY:
//                // 取得city_layout.xml中的视图
//                final View view = LayoutInflater.from(this).inflate(
//                        R.layout.city_layout, null);
//
//                // 省份Spinner
//                province_spinner = (Spinner) view
//                        .findViewById(R.id.province_spinner);
//                // 城市Spinner
//                city_spinner = (Spinner) view.findViewById(R.id.city_spinner);
//
//                // 省份列表
//                provinces = SoapUtil.getProvinceList();
//
//                ArrayAdapter adapter = new ArrayAdapter(this,
//                        android.R.layout.simple_spinner_item, provinces);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//                province_spinner.setAdapter(adapter);
//                // 省份Spinner监听器
//                province_spinner
//                        .setOnItemSelectedListener(new OnItemSelectedListener() {
//
//                            @Override
//                            public void onItemSelected(AdapterView<?> arg0,
//                                                       View arg1, int position, long arg3) {
//
//                                citys = SoapUtil
//                                        .getCityListByProvince(provinces
//                                                .get(position));
//
//                                ArrayAdapter adapter1 = new ArrayAdapter(
//                                        WeatherActivity.this,
//                                        android.R.layout.simple_spinner_item, citys);
//                                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                city_spinner.setAdapter(adapter1);
//
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> arg0) {
//
//                            }
//                        });
//
//                // 城市Spinner监听器
//                city_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//                    @Override
//                    public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                               int position, long arg3) {
//                        city_str = citys.get(position);
//                        String[] citydata = city_str.split(" ");
//                        city = citydata[0];
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> arg0) {
//                    }
//                });
//
//                // 选择城市对话框
//                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//                dialog.setTitle("请选择所属城市");
//                dialog.setView(view);
//                dialog.setPositiveButton("确定",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                city_text.setText(city);
//                                writeSharpPreference(city);
//                                //@Dustray update &1Start
//                                //refresh(city);
//                                new Thread() {
//                                    @Override
//                                    public void run() {
//                                        List<WeatherJsonEntity> wje = SoapUtil.getWeatherByCity(city);
//                                        //Log.i("logme","InThread"+wje.getCity());
//                                        Message msg = Message.obtain();
//                                        msg.what = 1;
//                                        msg.obj = wje;
//                                        mhandler.sendMessage(msg);
//                                    }
//                                }.start();
//                                //@Dustray update &1end
//                            }
//                        });
//                dialog.setNegativeButton("取消",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                dialog.show();
//                break;
//            default:
//                break;
//        }
    }

    Handler mhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                super.handleMessage(msg);
                return;
            }
            switch (msg.what) {
                case 1:
                    ListDataSave lds = new ListDataSave(WeatherActivity.this, "weather");
                    lds.setDataList("WeatherList", (List<WeatherJsonEntity>) msg.obj);
                    refresh((List<WeatherJsonEntity>) msg.obj);
                    //FastToast.showToast(WeatherActivity.this,"更新天气成功");
                    break;
                case 2:
                    break;
            }
        }
    };

    protected void refresh(List<WeatherJsonEntity> detail) {
        imageTool = new ImageTool();
        //Log.i("logme", "ss22" + detail.get(0).getCity());

        // 取得<string>10月13日 中雨转小雨</string>中的数据
        String date = detail.get(0).getDate();
        // 将"10月13日 10:42:20"拆分成两个数组
        String[] date_array = date.split(" ");
        TextView today_text = (TextView) findViewById(R.id.today);
        today_text.setText(date_array[0]);

        // 取得<string>江苏 无锡</string>中的数据
        TextView city_text = (TextView) findViewById(R.id.city_text);
        city_text.setText(detail.get(0).getCity());
        //Toast.makeText(WeatherActivity.this,"更新天气成功"+detail.get(0).getCity(), Toast.LENGTH_LONG).show();
        this.city_text.setText(detail.get(0).getCity());

        // 取得温度

        TextView qiweng_text = (TextView) findViewById(R.id.qiweng);
        qiweng_text.setText(detail.get(0).getLowTem() + "℃/" + detail.get(0).getHighTem() + "℃");

        // 风向
        TextView shidu_text = (TextView) findViewById(R.id.shidu);
        shidu_text.setText(detail.get(0).getWindDirection());

        // 取得风速
        TextView fengli_text = (TextView) findViewById(R.id.fengli);
        fengli_text.setText(detail.get(0).getWindSpeed() + "km/h");


        TextView kongqi_text = (TextView) findViewById(R.id.kongqi);
        String wDay = detail.get(0).getWeatherDay(), wNight = detail.get(0).getWeatherNight();
        kongqi_text.setText(wDay.equals(wNight) ? wDay : wDay + "转" + wNight);

        TextView zhiwai_text = (TextView) findViewById(R.id.zhiwai);
        zhiwai_text.setText(detail.get(0).getUv());
        //设置降水概率
        String s = detail.get(0).getPrecip();
        if (s.equals("")) {
            s = "0%";
        } else {
            s = s + "%";
        }
        TextView jiangshui_text = (TextView) findViewById(R.id.jiangshui);
        jiangshui_text.setText(s);
        TextView xiaotieshi_text = (TextView) findViewById(R.id.xiaotieshi);
        xiaotieshi_text.setText(detail.get(0).getLifeIndex());
        // 设置当日图片
        ImageView image = (ImageView) findViewById(R.id.imageView1);

        int icon = imageTool.parseIcon(detail.get(0).getCodeDay() + ".png");
        // FastToast.showToast(WeatherActivity.this, "sss"+detail.getCodeDay());
        image.setImageResource(icon);

        // 取得第二天的天气情况
//            TextView tomorrow_date = (TextView) findViewById(R.id.tomorrow_date);
//            tomorrow_date.setText(detail.get(1).getDate());

        TextView tomorrow_qiweng = (TextView) findViewById(R.id.tomorrow_qiweng);
        tomorrow_qiweng.setText(detail.get(1).getLowTem() + "℃/" + detail.get(1).getHighTem() + "℃");

        TextView tomorrow_tianqi = (TextView) findViewById(R.id.tomorrow_tianqi);
        String wDay1 = detail.get(1).getWeatherDay(), wNight1 = detail.get(1).getWeatherNight();

        tomorrow_tianqi.setText(wDay1.equals(wNight1) ? wDay1 : wDay1 + "转" + wNight1);

        ImageView tomorrow_image = (ImageView) findViewById(R.id.tomorrow_image);
        int icon1 = imageTool.parseIcon(detail.get(1).getCodeDay() + ".png");
        tomorrow_image.setImageResource(icon1);

        // 取得第三天的天气情况

//            TextView afterday_date = (TextView) findViewById(R.id.afterday_date);
//            afterday_date.setText(date_str1[0]);

        TextView afterday_qiweng = (TextView) findViewById(R.id.afterday_qiweng);
        afterday_qiweng.setText(detail.get(2).getLowTem() + "℃/" + detail.get(2).getHighTem() + "℃");

        TextView afterday_tianqi = (TextView) findViewById(R.id.afterday_tianqi);
        String wDay2 = detail.get(2).getWeatherDay(), wNight2 = detail.get(2).getWeatherNight();

        afterday_tianqi.setText(wDay2.equals(wNight2) ? wDay2 : wDay2 + "转" + wNight2);

        ImageView afterday_image = (ImageView) findViewById(R.id.afterday_image);
        int icon2 = imageTool.parseIcon(detail.get(2).getCodeDay() + ".png");
        afterday_image.setImageResource(icon2);


    }


    public void writeSharpPreference(String string) {

        SharedPreferences.Editor editor = preference.edit();
        editor.putString("city", string);
        editor.commit();

    }

    public String readSharpPreference() {

        String city = preference.getString("city", "青岛");

        Log.i("logme", "city-----------------:" + city);
        //this.city = city;
        return city;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.weather, menu);
        return true;
    }

}
