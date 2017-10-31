
package bzu.skyn.travehelper;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import java.util.List;

import bzu.skyn.travehelper.tools.FastToast;
import bzu.skyn.travehelper.util.GaodeSearchAdapter;

public class AMapActivity extends Activity implements View.OnClickListener, GeocodeSearch.OnGeocodeSearchListener {

    private MapView mMapView = null;
    private Button btnTrafficCondition, btnSatellite;
    private EditText etInputSearch;
    private RelativeLayout searchListFrame;
    private ListView lvSearchList;
    private ImageButton btnSearchGo;
    private boolean isShowTraCon = true, isShowSatellite = true;
    private AMap aMap;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private GeocodeSearch geocoderSearch;
    private GeocodeQuery query;
    private RegeocodeQuery query1;
    private String myCity = "010";
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            //定位成功
            FastToast.showToast(AMapActivity.this,"当前所在城市："+aMapLocation.getCity());
            myCity = aMapLocation.getCity();
        }
    };
    public AMapLocationClientOption mLocationOption = null;
    Marker marker =null;

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
        btnSearchGo = (ImageButton)findViewById(R.id.btn_search_go);
        btnSearchGo.setOnClickListener(this);

        searchListFrame = (RelativeLayout) findViewById(R.id.search_list_frame);
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
    }

    private void inquireList() {
        if(marker!=null){
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
        myLocationStyle.myLocationIcon(bdf.fromResource(R.drawable.positioning));
        myLocationStyle.anchor((float) 0.53, (float) 0.82);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setTrafficEnabled(true);//显示实时路况图层，aMap是地图控制器对象。
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        //mUiSettings.setCompassEnabled(true);//指南针用于向 App 端用户展示地图方向
        mUiSettings.setScaleControlsEnabled(true);//控制比例尺控件是否显示
        mUiSettings.isZoomGesturesEnabled();
        getMyLocation();
    }

    // 初始化数据
    private void initData() {
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    private void goSearch() {
        // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
        query = new GeocodeQuery(etInputSearch.getText().toString(), myCity);
        geocoderSearch.getFromLocationNameAsyn(query);
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
            case  R.id.btn_search_go:
                inquireList();
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
                    //FastToast.showToast(AMapActivity.this, "你点击了" + ga.getBuilding() + "，经纬度：" + ga.getLatLonPoint());
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
}