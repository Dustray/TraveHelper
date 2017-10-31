package bzu.skyn.travehelper;

import java.util.List;

import org.ksoap2.serialization.SoapObject;

import bzu.skyn.travehelper.entity.WeatherJsonEntity;
import bzu.skyn.travehelper.tools.ImageTool;
import bzu.skyn.travehelper.util.SoapUtil;

import android.annotation.SuppressLint;
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

    /**
     * Called when the activity is first created.
     */
    @Override
    //@SuppressLint("NewApi")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        //StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_weather);
        ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cwjManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            preference = getSharedPreferences("weather", MODE_PRIVATE);
            city = readSharpPreference();
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
            findViewById(R.id.content_today_layout).getBackground().setAlpha(120);
            findViewById(R.id.content_small_bg1).getBackground().setAlpha(120);
            findViewById(R.id.content_small_bg2).getBackground().setAlpha(120);
            //findViewById(R.id.content_small_bg3).getBackground().setAlpha(120);

            //@Dustray update &1Start
            //refresh(city);
            new Thread(){
                @Override
                public void run() {
                    WeatherJsonEntity wje = SoapUtil.getWeatherByCity(city);
                    Log.i("logme","InThread"+wje.getCity());
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = wje;
                    mhandler.sendMessage(msg);
                }
            }.start();
            //@Dustray update &1end
        } else {
            Toast.makeText(WeatherActivity.this, "无网络连接，请确认是否连接网络！", Toast.LENGTH_LONG).show();
        }
    }

    public void showTast(String string) {
        Toast.makeText(WeatherActivity.this, string + "请确认是否连接网络！", Toast.LENGTH_SHORT).show();

    }

    public void show_dialog(int cityId) {

        switch (cityId) {
            case CITY:
                // 取得city_layout.xml中的视图
                final View view = LayoutInflater.from(this).inflate(
                        R.layout.city_layout, null);

                // 省份Spinner
                province_spinner = (Spinner) view
                        .findViewById(R.id.province_spinner);
                // 城市Spinner
                city_spinner = (Spinner) view.findViewById(R.id.city_spinner);

                // 省份列表
                provinces = SoapUtil.getProvinceList();

                ArrayAdapter adapter = new ArrayAdapter(this,
                        android.R.layout.simple_spinner_item, provinces);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                province_spinner.setAdapter(adapter);
                // 省份Spinner监听器
                province_spinner
                        .setOnItemSelectedListener(new OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> arg0,
                                                       View arg1, int position, long arg3) {

                                citys = SoapUtil
                                        .getCityListByProvince(provinces
                                                .get(position));

                                ArrayAdapter adapter1 = new ArrayAdapter(
                                        WeatherActivity.this,
                                        android.R.layout.simple_spinner_item, citys);
                                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                city_spinner.setAdapter(adapter1);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {

                            }
                        });

                // 城市Spinner监听器
                city_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long arg3) {
                        city_str = citys.get(position);
                        String[] citydata = city_str.split(" ");
                        city = citydata[0];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

                // 选择城市对话框
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("请选择所属城市");
                dialog.setView(view);
                dialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                city_text.setText(city);
                                writeSharpPreference(city);
                                //@Dustray update &1Start
                                //refresh(city);
                                new Thread(){
                                    @Override
                                    public void run() {
                                        WeatherJsonEntity wje = SoapUtil.getWeatherByCity(city);
                                        Log.i("logme","InThread"+wje.getCity());
                                        Message msg = Message.obtain();
                                        msg.what = 1;
                                        msg.obj = wje;
                                        mhandler.sendMessage(msg);
                                    }
                                }.start();
                                //@Dustray update &1end
                            }
                        });
                dialog.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog.show();
                break;
            default:
                break;
        }
    }

    Handler mhandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0) {
                super.handleMessage(msg);
                return;
            }
            switch(msg.what){
                case 1:refresh((WeatherJsonEntity)msg.obj);break;
                case 2:break;
            }
        }
    };
    protected void refresh(WeatherJsonEntity detail) {
//        Object[] obj = {4,city};
//        SoapUtil soap = new SoapUtil(obj);
//        SoapObject detail = (SoapObject)soap.execute().get();
        imageTool = new ImageTool();
        Log.i("logme","ss22"+detail.getCity());

            // 取得<string>10月13日 中雨转小雨</string>中的数据
            String date = detail.getDate();
            // 将"10月13日 10:42:20"拆分成两个数组
            String[] date_array = date.split(" ");
            TextView today_text = (TextView) findViewById(R.id.today);
            today_text.setText(date_array[0]);

            // 取得<string>江苏 无锡</string>中的数据
            TextView city_text = (TextView) findViewById(R.id.city_text);
            city_text.setText(detail.getCity());
            this.city_text.setText(detail.getCity());
            TextView today_weather = (TextView) findViewById(R.id.today_weather);
            //today_weather.setText(date_array[1]);

            // 取得<string>15℃/21℃</string>中的数据

            TextView qiweng_text = (TextView) findViewById(R.id.qiweng);
            qiweng_text.setText(detail.getLowTem()+"℃/"+detail.getHighTem()+"℃");

            // 取得<string>今日天气实况：气温：20℃；风向/风力：东南风
            // 2级；湿度：79%</string>中的数据,并通过":"拆分成数组
            TextView shidu_text = (TextView) findViewById(R.id.shidu);
            shidu_text.setText( detail.getWindDirection());

            // 取得<string>东北风3-4级</string>中的数据
            TextView fengli_text = (TextView) findViewById(R.id.fengli);
            fengli_text.setText(detail.getWindSpeed()+"km/h");

            // 取得<string>空气质量：良；紫外线强度：最弱</string>中的数据,并通过";"拆分,再通过":"拆分,拆分两次,取得我们需要的数据

            TextView kongqi_text = (TextView) findViewById(R.id.kongqi);
            String wDay = detail.getWeatherDay(),wNight = detail.getWeatherNight();
            kongqi_text.setText(wDay.equals(wNight) ? wDay : wDay+"转"+wNight);

            TextView zhiwai_text = (TextView) findViewById(R.id.zhiwai);
            zhiwai_text.setText(detail.getUv());

            // 设置小贴士数据
            // <string>穿衣指数：较凉爽，建议着长袖衬衫加单裤等春秋过渡装。年老体弱者宜着针织长袖衬衫、马甲和长裤。感冒指数：虽然温度适宜但风力较大，仍较易发生感冒，体质较弱的朋友请注意适当防护。
            //运动指数：阴天，较适宜开展各种户内外运动。洗车指数：较不宜洗车，路面少量积水，如果执意擦洗汽车，要做好溅上泥水的心理准备。晾晒指数：天气阴沉，不利于水分的迅速蒸发，不太适宜晾晒。若需要晾晒，请尽量选择通风的地点。
            //旅游指数：阴天，风稍大，但温度适宜，总体来说还是好天气。这样的天气很适宜旅游，您可以尽情享受大自然的风光。路况指数：阴天，路面比较干燥，路况较好。舒适度指数：温度适宜，风力不大，您在这样的天气条件下，会感到比较清爽和舒适。
            //空气污染指数：气象条件有利于空气污染物稀释、扩散和清除，可在室外正常活动。紫外线指数：属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。</string>
            //String[] xiaotieshi = detail.getProperty(11).toString().split("\n");
            TextView xiaotieshi_text = (TextView) findViewById(R.id.xiaotieshi);
            //xiaotieshi_text.setText(xiaotieshi[0]);
            xiaotieshi_text.setText(detail.getLifeIndex());
            // 设置当日图片
            ImageView image = (ImageView) findViewById(R.id.imageView1);
        ///省略省略省略省略省略省略省略省略省略省略省略省略省略省略省略省略
            //int icon = imageTool.parseIcon(detail.getProperty(8).toString());
            //image.setImageResource(icon);
/*
            // 取得第二天的天气情况
            String[] date_str = detail.getProperty(13).toString().split(" ");
            TextView tomorrow_date = (TextView) findViewById(R.id.tomorrow_date);
            tomorrow_date.setText(date_str[0]);

            TextView tomorrow_qiweng = (TextView) findViewById(R.id.tomorrow_qiweng);
            tomorrow_qiweng.setText(detail.getProperty(12).toString());

            TextView tomorrow_tianqi = (TextView) findViewById(R.id.tomorrow_tianqi);
            tomorrow_tianqi.setText(date_str[1]);

            ImageView tomorrow_image = (ImageView) findViewById(R.id.tomorrow_image);
            int icon1 = imageTool.parseIcon(detail.getProperty(15).toString());
            tomorrow_image.setImageResource(icon1);

            // 取得第三天的天气情况
            String[] date_str1 = detail.getProperty(18).toString().split(" ");
            TextView afterday_date = (TextView) findViewById(R.id.afterday_date);
            afterday_date.setText(date_str1[0]);

            TextView afterday_qiweng = (TextView) findViewById(R.id.afterday_qiweng);
            afterday_qiweng.setText(detail.getProperty(17).toString());

            TextView afterday_tianqi = (TextView) findViewById(R.id.afterday_tianqi);
            afterday_tianqi.setText(date_str1[1]);

            ImageView afterday_image = (ImageView) findViewById(R.id.afterday_image);
            int icon2 = imageTool.parseIcon(detail.getProperty(20).toString());
            afterday_image.setImageResource(icon2);
*/


    }


    public void writeSharpPreference(String string) {

        SharedPreferences.Editor editor = preference.edit();
        editor.putString("city", string);
        editor.commit();

    }

    public String readSharpPreference() {

        String city = preference.getString("city", "binzhou");

        Log.i("logme","city:"+city);
        return city;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.weather, menu);
        return true;
    }

}
