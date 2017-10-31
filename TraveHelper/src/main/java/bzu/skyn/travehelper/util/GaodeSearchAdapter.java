package bzu.skyn.travehelper.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.geocoder.GeocodeAddress;

import java.util.List;

import bzu.skyn.travehelper.R;
import bzu.skyn.travehelper.entity.AttractionEntity;


/**
 * Created by Dustray on 2017/10/31 0031.
 */

public class GaodeSearchAdapter extends ArrayAdapter<GeocodeAddress> {
    private int resourceId;
    private Context mContext;
    public GaodeSearchAdapter(Context context, int textViewResourceId, List<GeocodeAddress> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        mContext = context;
    }


    @Override         //getView方法在每个子项被滚动到屏幕内的时候都会被调用，每次都将布局重新加载一边
    public View getView(int position, View convertView, ViewGroup parent) {//第一个参数表示位置，第二个参数表示缓存布局，第三个表示绑定的view对象
        View view;
        ViewHolderGaode viewHolder;                  //实例ViewHolder，当程序第一次运行，保存获取到的控件，提高效率
        if(convertView==null){
            viewHolder=new ViewHolderGaode();
            view = LayoutInflater.from(getContext()).inflate(//convertView为空代表布局没有被加载过，即getView方法没有被调用过，需要创建
                    resourceId, null);          // 得到子布局，非固定的，和子布局id有关
            viewHolder.aName = (TextView) view.findViewById(R.id.tv_search_local_name);
            viewHolder.aCity = (TextView) view.findViewById(R.id.tv_search_local_city);

            view.setTag(viewHolder);
        }else{
            view=convertView;           //convertView不为空代表布局被加载过，只需要将convertView的值取出即可
            viewHolder=(ViewHolderGaode) view.getTag();
        }

        GeocodeAddress att = getItem(position);


        viewHolder.aName .setText(att.getFormatAddress());
        String s = att.getProvince();
        if(!att.getCity().equals(""))s = s + "-" + att.getCity();
        if(!att.getDistrict().equals(""))s = s + "-" + att.getDistrict();
        if(!att.getTownship().equals(""))s = s + "-" + att.getTownship();
        if(!att.getNeighborhood().equals(""))s = s + "-" + att.getNeighborhood();
        viewHolder.aCity.setText(s);

        return view;

    }
}

class ViewHolderGaode{      //当布局加载过后，保存获取到的控件信息。

    TextView aName;
    TextView aCity;
}

