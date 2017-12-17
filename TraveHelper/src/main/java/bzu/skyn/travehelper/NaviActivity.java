package bzu.skyn.travehelper;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;

import java.util.ArrayList;
import java.util.List;

public class NaviActivity extends BaseActivity {
    double[] myLocal;
    public final static int NAVI_WALK = 0;
    public final static int NAVI_BIKE = 1;
    public final static int NAVI_DRIVAE = 2;
    public final static int NAVI_DRIVAE_HIGH = 3;
    private static int naviState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        myLocal = bundle.getDoubleArray("position");
        naviState = bundle.getInt("navistate");


        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
        mAMapNaviView.setNaviMode(AMapNaviView.NORTH_UP_MODE);
       // AMapNaviViewOptions options = mAMapNaviView.getViewOptions();
        //options.setCarBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.direction));
        //options.setFourCornersBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.lane00));
       // mAMapNaviView.setViewOptions(options);

        //mAMapNaviView.setNaviMode(AMapNaviView.NORTH_UP_MODE);//正北模式
    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        switch (naviState) {
            case NAVI_WALK:

                mAMapNavi.calculateWalkRoute(new NaviLatLng(myLocal[0], myLocal[1]), new NaviLatLng(myLocal[2], myLocal[3]));
                break;
            case NAVI_DRIVAE:
                //驾车导航
                /**
                 * 方法:
                 *   int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute);
                 * 参数:
                 * @congestion 躲避拥堵
                 * @avoidhightspeed 不走高速
                 * @cost 避免收费
                 * @hightspeed 高速优先
                 * @multipleroute 多路径
                 *
                 * 说明:
                 *      以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
                 * 注意:
                 *      不走高速与高速优先不能同时为true
                 *      高速优先与避免收费不能同时为true
                 */
                int strategy = 0;
                try {
                    strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                List<NaviLatLng> sLists = new ArrayList<NaviLatLng>();
                List<NaviLatLng> eLists =  new ArrayList<NaviLatLng>();
                sLists.add(new NaviLatLng(myLocal[0], myLocal[1]));//start
                eLists.add(new NaviLatLng(myLocal[2], myLocal[3]));//end
                mAMapNavi.calculateDriveRoute(sLists, eLists, mWayPointList, strategy);
                break;
            case NAVI_BIKE:

                mAMapNavi.calculateRideRoute(new NaviLatLng(myLocal[0], myLocal[1]), new NaviLatLng(myLocal[2], myLocal[3]));

            break;
        }
    }

    @Override
    public void onCalculateRouteSuccess(int[] ids) {
        super.onCalculateRouteSuccess(ids);
        mAMapNavi.startNavi(NaviType.GPS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapNaviView.onDestroy();
    }
}
