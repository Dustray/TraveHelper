
package bzu.skyn.travehelper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.List;

import bzu.skyn.travehelper.tools.FastToast;
import bzu.skyn.travehelper.util.GaodeSearchAdapter;
import bzu.skyn.travehelper.util.GaodeSearchPoiAdapter;

public class AMapActivity extends Activity implements View.OnClickListener, GeocodeSearch.OnGeocodeSearchListener, PoiSearch.OnPoiSearchListener {

    private MapView mMapView = null;
    private Button btnTrafficCondition, btnSatellite, btnNaviStep, btnNaviDrive, btnNaviBike;
    private EditText etInputSearch;
    private RelativeLayout searchListFrame;
    private LinearLayout letsgoFrame;
    private ListView lvSearchList;
    private ImageButton btnSearchGo;
    private boolean isShowTraCon = true, isShowSatellite = true;
    private AMap aMap;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private GeocodeSearch geocoderSearch;
    private GeocodeQuery query;
    private RegeocodeQuery query1;
    private PoiSearch.Query query2;
    private String myCity = "010";
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            //定位成功
            //FastToast.showToast(AMapActivity.this, "当前所在城市：" + aMapLocation.getCity());
            myCity = aMapLocation.getCity();
            SharedPreferences preference = getSharedPreferences("myLocation", MODE_PRIVATE);
            //city = readSharpPreference();
            SharedPreferences.Editor editor = preference.edit();
            editor.putString("latitude", ""+aMapLocation.getLatitude());
            editor.putString("longitude", ""+aMapLocation.getLongitude());
            editor.commit();
            //移动到定位点
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息

                    //取出经纬度
                    LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    //然后可以移动到定位点,使用animateCamera就有动画效果
                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
//                    Log.e("AmapError", "location Error, ErrCode:"
//                            + amapLocation.getErrorCode() + ", errInfo:"
//                            + amapLocation.getErrorInfo());
                }
            }
        }
    };
    public AMapLocationClientOption mLocationOption = null;
    Marker marker = null;
    NaviLatLng desNaviLatLng;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        initView();
        initMap();
        initData();
    }

    private void initView() {
        btnTrafficCondition = (Button) findViewById(R.id.btn_traffic_condition);
        btnTrafficCondition.setOnClickListener(this);
        btnSatellite = (Button) findViewById(R.id.btn_satellite_mode);
        btnSatellite.setOnClickListener(this);
        btnSearchGo = (ImageButton) findViewById(R.id.btn_search_go);
        btnSearchGo.setOnClickListener(this);

        searchListFrame = (RelativeLayout) findViewById(R.id.search_list_frame);
        letsgoFrame = (LinearLayout) findViewById(R.id.lets_go_frame);
        etInputSearch = (EditText) findViewById(R.id.et_input_search);
        etInputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                inquireList();
            }
        });
        lvSearchList = (ListView) findViewById(R.id.lv_search_list);

        btnNaviStep = (Button) findViewById(R.id.btn_navi_step);
        btnNaviStep.setOnClickListener(this);
        btnNaviDrive = (Button) findViewById(R.id.btn_navi_drive);
        btnNaviDrive.setOnClickListener(this);
        btnNaviBike = (Button) findViewById(R.id.btn_navi_bike);
        btnNaviBike.setOnClickListener(this);
    }

    private void inquireList() {
        if (marker != null) {
            marker.destroy();
            marker = null;
        }
        if (etInputSearch.getText().toString().trim().equals("")) {
            searchListFrame.setVisibility(View.GONE);
        } else {
            //这条给onGeocodeSearched
            if (searchListFrame.getVisibility() == View.GONE)
                searchListFrame.setVisibility(View.VISIBLE);
            goSearch();
        }
    }

    // 初始化视图
    private void initMap() {
        aMap = mMapView.getMap();
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        BitmapDescriptorFactory bdf = new BitmapDescriptorFactory();
        myLocationStyle.myLocationIcon(bdf.fromResource(R.drawable.direction));
        myLocationStyle.anchor((float) 0.5, (float) 0.59);
        myLocationStyle.strokeColor(Color.argb(200, 3, 169, 244));
        myLocationStyle.radiusFillColor(Color.argb(70, 3, 169, 244));
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setTrafficEnabled(true);//显示实时路况图层，aMap是地图控制器对象。

        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        //mUiSettings.setCompassEnabled(true);//指南针用于向 App 端用户展示地图方向
        mUiSettings.setScaleControlsEnabled(true);//控制比例尺控件是否显示
        mUiSettings.isZoomGesturesEnabled();
        mUiSettings.setCompassEnabled(true);//设置指南针

        getMyLocation();
    }

    // 初始化数据
    private void initData() {
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        //设置缓存经纬度
        SharedPreferences preference = getSharedPreferences("myLocation", MODE_PRIVATE);
        double latitude = Double.parseDouble(preference.getString("latitude", "39.908692"));
        double longitude = Double.parseDouble(preference.getString("longitude", "116.397477"));
        LatLng latLng = new LatLng(latitude,longitude);
        //然后可以移动到定位点,使用animateCamera就有动画效果
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
    }


    private void goSearch() {
        // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
        // TODO: 2017/10/31 0031 改变是否Poi 
        //query = new GeocodeQuery(etInputSearch.getText().toString(), myCity);
        //geocoderSearch.getFromLocationNameAsyn(query);

        query2 = new PoiSearch.Query(etInputSearch.getText().toString(), "", myCity);
        //keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query2.setPageSize(20);// 设置每页最多返回多少条poiitem
        query2.setPageNum(1);//设置查询页码

        PoiSearch poiSearch = new PoiSearch(this, query2);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    private void goSearchWithlatLon(double li, double lo) {
        query1 = new RegeocodeQuery(new LatLonPoint(li, lo), 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_traffic_condition:
                if (isShowTraCon) {
                    aMap.setTrafficEnabled(false);//显示实时路况图层，aMap是地图控制器对象。
                    btnTrafficCondition.setBackgroundResource(R.color.switch_bg_close);
                    btnTrafficCondition.setTextColor(this.getResources().getColor(R.color.switch_tx_close));
                    FastToast.showToast(AMapActivity.this, "已关闭实时路况");

                    isShowTraCon = false;
                } else {
                    aMap.setTrafficEnabled(true);//显示实时路况图层，aMap是地图控制器对象。
                    btnTrafficCondition.setBackgroundResource(R.color.switch_bg_open);
                    btnTrafficCondition.setTextColor(this.getResources().getColor(R.color.switch_tx_open));

                    FastToast.showToast(AMapActivity.this, "已开启实时路况");
                    isShowTraCon = true;
                }
                break;
            case R.id.btn_satellite_mode:
                if (isShowSatellite) {
                    aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
                    btnSatellite.setBackgroundResource(R.color.switch_bg_close);
                    btnSatellite.setTextColor(this.getResources().getColor(R.color.switch_tx_close));
                    btnSatellite.setText("普通");
                    FastToast.showToast(AMapActivity.this, "已开启卫星视图");
                    isShowSatellite = false;

                } else {
                    aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                    btnSatellite.setBackgroundResource(R.color.switch_bg_open);
                    btnSatellite.setTextColor(this.getResources().getColor(R.color.switch_tx_open));
                    btnSatellite.setText("卫星");
                    FastToast.showToast(AMapActivity.this, "已开启普通视图");

                    isShowSatellite = true;
                }
                break;
            case R.id.btn_search_go:
                inquireList();
                break;
            case R.id.btn_navi_step:
                if (desNaviLatLng == null) {
                    FastToast.showToast(AMapActivity.this, "请选择目的地");
                    return;
                }
                Intent intent = new Intent(this, NaviActivity.class);
                Bundle bundle = new Bundle();
                double a[] = {aMap.getMyLocation().getLatitude(), aMap.getMyLocation().getLongitude(), desNaviLatLng.getLatitude(), desNaviLatLng.getLongitude()};
                bundle.putDoubleArray("position", a);
                bundle.putInt("navistate", NaviActivity.NAVI_WALK);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btn_navi_drive:
                if (desNaviLatLng == null) {
                    FastToast.showToast(AMapActivity.this, "请选择目的地");
                    return;
                }
                Intent intent2 = new Intent(this, NaviActivity.class);
                Bundle bundle2 = new Bundle();
                double a2[] = {aMap.getMyLocation().getLatitude(), aMap.getMyLocation().getLongitude(), desNaviLatLng.getLatitude(), desNaviLatLng.getLongitude()};
                bundle2.putDoubleArray("position", a2);
                bundle2.putInt("navistate", NaviActivity.NAVI_DRIVAE);
                intent2.putExtras(bundle2);
                startActivity(intent2);
                break;
            case R.id.btn_navi_bike:
                if (desNaviLatLng == null) {
                    FastToast.showToast(AMapActivity.this, "请选择目的地");
                    return;
                }
                Intent intent3 = new Intent(this, NaviActivity.class);
                Bundle bundle3 = new Bundle();
                double a3[] = {aMap.getMyLocation().getLatitude(), aMap.getMyLocation().getLongitude(), desNaviLatLng.getLatitude(), desNaviLatLng.getLongitude()};
                bundle3.putDoubleArray("position", a3);
                bundle3.putInt("navistate", NaviActivity.NAVI_BIKE);
                intent3.putExtras(bundle3);
                startActivity(intent3);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        FastToast.showToast(AMapActivity.this, "所在城市：" + regeocodeResult.getRegeocodeAddress().getCity());
        //myCity = regeocodeResult.getRegeocodeAddress().getCity();
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        if (i == 1000) {

            final List<GeocodeAddress> resultList = geocodeResult.getGeocodeAddressList();
            //FastToast.showToast(AMapActivity.this, "点击了" + resultList.size());
            GaodeSearchAdapter adapter = new GaodeSearchAdapter(this, R.layout.item_search_location, resultList);
            lvSearchList.setAdapter(adapter);
            lvSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    GeocodeAddress ga = resultList.get(i);
                    FastToast.showToast(AMapActivity.this, "你点击了" + ga.getBuilding() + "，经纬度：" + ga.getLatLonPoint());
                    searchListFrame.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(AMapActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    showOnMap(ga);
                }
            });
        } else {
            FastToast.showToast(AMapActivity.this, "查询出错，请检查网络，错误代码：" + i);
        }
    }

    private void showOnMap(GeocodeAddress ga) {
        LatLng latLng = new LatLng(ga.getLatLonPoint().getLatitude(), ga.getLatLonPoint().getLongitude());
        MarkerOptions markerO = new MarkerOptions();
        markerO.position(latLng);
        markerO.title(ga.getFormatAddress()).snippet(ga.getLatLonPoint().toString());

        marker = aMap.addMarker(markerO);
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                new LatLng(ga.getLatLonPoint().getLatitude(), ga.getLatLonPoint().getLongitude()), 15, 0, 0));
        aMap.animateCamera(mCameraUpdate);
// 绑定 Marker 被点击事件

    }

    private void showOnMapPoi(PoiItem ga) {
        //Toast.makeText(AMapActivity.this,"sssasasaas", Toast.LENGTH_LONG).show();
        LatLng latLng = new LatLng(ga.getLatLonPoint().getLatitude(), ga.getLatLonPoint().getLongitude());
        MarkerOptions markerO = new MarkerOptions();
        markerO.position(latLng);
        markerO.title(ga.getTitle()).snippet(ga.getLatLonPoint().toString());
        marker = aMap.addMarker(markerO);
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                new LatLng(ga.getLatLonPoint().getLatitude(), ga.getLatLonPoint().getLongitude()), 15, 0, 0));
        aMap.animateCamera(mCameraUpdate);
    }

    public void hiddenSearchList(View v) {
        searchListFrame.setVisibility(View.GONE);
    }

    public void getMyLocation() {
        //声明AMapLocationClient类对象
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //获取一次定位结果：该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        // 如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (i == 1000) {

            final List<PoiItem> resultList = poiResult.getPois();
            //FastToast.showToast(AMapActivity.this, "点击了" + resultList.size());
            GaodeSearchPoiAdapter adapter = new GaodeSearchPoiAdapter(this, R.layout.item_search_location, resultList);
            lvSearchList.setAdapter(adapter);
            lvSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    PoiItem ga = resultList.get(i);
                    //FastToast.showToast(AMapActivity.this, "你点击了" + ga.getBuilding() + "，经纬度：" + ga.getLatLonPoint());
                    searchListFrame.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(AMapActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    //设置目的地经纬度
                    desNaviLatLng = new NaviLatLng(ga.getLatLonPoint().getLatitude(), ga.getLatLonPoint().getLongitude());
                    if(desNaviLatLng==null)
                        Toast.makeText(AMapActivity.this,"请设置目的地", Toast.LENGTH_LONG).show();
                    letsgoFrame.setVisibility(View.VISIBLE);
                    showOnMapPoi(ga);
                }
            });
        } else {
            FastToast.showToast(AMapActivity.this, "查询出错，请检查网络，错误代码：" + i);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onBackPressed() {

        if (searchListFrame.getVisibility() == View.VISIBLE) {
            searchListFrame.setVisibility(View.GONE);
        } else if(marker!=null){
            letsgoFrame.setVisibility(View.GONE);
            marker.remove();
            marker.destroy();
            marker=null;
        }else {
            super.onBackPressed();
        }
    }
}